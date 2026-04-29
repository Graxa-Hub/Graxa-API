package com.Graxa_API.Graxa_API.Notificacao;

import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.AlocacaoRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Repository.NotificacaoRepository;
import com.Graxa_API.Graxa_API.Service.NotificacaoService;
import com.Graxa_API.Graxa_API.Service.NotificacaoWebSocketService;
import com.Graxa_API.Graxa_API.dto.NotificacaoDto.ResponseNotificacaoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacaoServiceTest {

    private NotificacaoRepository notificacaoRepository;
    private ColaboradorRepository colaboradorRepository;
    private AlocacaoRepository alocacaoRepository;
    private NotificacaoWebSocketService webSocketService;

    private NotificacaoService notificacaoService;

    @BeforeEach
    void setUp() {
        notificacaoRepository = mock(NotificacaoRepository.class);
        colaboradorRepository = mock(ColaboradorRepository.class);
        alocacaoRepository = mock(AlocacaoRepository.class);
        webSocketService = mock(NotificacaoWebSocketService.class);

        notificacaoService = new NotificacaoService(
                notificacaoRepository,
                colaboradorRepository,
                alocacaoRepository
        );

        try {
            var field = NotificacaoService.class.getDeclaredField("webSocketService");
            field.setAccessible(true);
            field.set(notificacaoService, webSocketService);
        } catch (Exception e) {
            fail("Erro ao injetar WebSocketService no teste: " + e.getMessage());
        }
    }

    @Test
    void deveCriarNotificacaoQuandoColaboradorExiste() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(1L);

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaborador));

        ArgumentCaptor<NotificacaoEntity> captor = ArgumentCaptor.forClass(NotificacaoEntity.class);
        when(notificacaoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        NotificacaoEntity result = notificacaoService.criarNotificacao(1L, "Mensagem teste", "TIPO_TESTE");

        verify(notificacaoRepository).save(captor.capture());
        NotificacaoEntity saved = captor.getValue();

        assertEquals("Mensagem teste", saved.getMensagem());
        assertEquals("TIPO_TESTE", saved.getTipo());
        assertEquals(colaborador, saved.getColaborador());
        assertFalse(saved.isLida());
        assertNotNull(saved.getDataCriacao());
    }

    @Test
    void deveLancarExcecaoQuandoColaboradorNaoExiste() {
        when(colaboradorRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notificacaoService.criarNotificacao(99L, "Mensagem", "TIPO"));

        assertEquals("Colaborador não encontrado com ID: 99", ex.getMessage());
        verifyNoInteractions(notificacaoRepository);
    }

    @Test
    void deveListarNotificacoesPorColaborador() {
        NotificacaoEntity notif = new NotificacaoEntity();
        notif.setMensagem("Teste");

        when(notificacaoRepository.findByColaboradorId(1L)).thenReturn(List.of(notif));

        List<NotificacaoEntity> result = notificacaoService.listarPorColaboradorEntity(1L);

        assertEquals(1, result.size());
        assertEquals("Teste", result.get(0).getMensagem());
    }

    @Test
    void deveMarcarNotificacaoComoLida() {
        NotificacaoEntity notif = new NotificacaoEntity();
        notif.setId(10L);
        notif.setMensagem("Teste");
        notif.setLida(false);

        when(notificacaoRepository.findById(10L)).thenReturn(Optional.of(notif));
        when(notificacaoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        NotificacaoEntity result = notificacaoService.marcarComoLidaEntity(10L);

        assertTrue(result.isLida());
        verify(notificacaoRepository).save(notif);
    }

    @Test
    void naoDeveSalvarNotificacaoQuandoColaboradorNaoExiste() {
        when(colaboradorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> notificacaoService.criarNotificacao(999L, "Mensagem", "TIPO"));

        verifyNoInteractions(notificacaoRepository);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaNotificacoes() {
        when(notificacaoRepository.findByColaboradorId(1L)).thenReturn(List.of());

        List<NotificacaoEntity> result = notificacaoService.listarPorColaboradorEntity(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void deveLancarExcecaoQuandoNotificacaoNaoEncontradaParaMarcarComoLida() {
        when(notificacaoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificacaoService.marcarComoLidaEntity(999L));
        verify(notificacaoRepository, never()).save(any());
    }
}