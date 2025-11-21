package com.Graxa_API.Graxa_API.Notificacao;

import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Repository.NotificacaoRepository;
import com.Graxa_API.Graxa_API.Service.NotificacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacaoServiceTest {

    private NotificacaoRepository notificacaoRepository;
    private ColaboradorRepository colaboradorRepository;
    private NotificacaoService notificacaoService;

    @BeforeEach
    void setUp() {
        notificacaoRepository = mock(NotificacaoRepository.class);
        colaboradorRepository = mock(ColaboradorRepository.class);
        notificacaoService = new NotificacaoService(notificacaoRepository, colaboradorRepository);
    }

    @Test
    void deveCriarNotificacaoQuandoColaboradorExiste() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(1L);

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaborador));

        NotificacaoEntity result = notificacaoService.criarNotificacao(1L, "Mensagem teste", "TIPO_TESTE");

        // Captura o objeto salvo
        ArgumentCaptor<NotificacaoEntity> captor = ArgumentCaptor.forClass(NotificacaoEntity.class);
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

        assertEquals("Colaborador não encontrado", ex.getMessage());
        verifyNoInteractions(notificacaoRepository);
    }

    @Test
    void deveListarNotificacoesPorColaborador() {
        NotificacaoEntity notif = new NotificacaoEntity();
        notif.setMensagem("Teste");

        when(notificacaoRepository.findByColaboradorId(1L)).thenReturn(List.of(notif));

        List<NotificacaoEntity> result = notificacaoService.listarPorColaborador(1L);

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
        when(notificacaoRepository.save(any(NotificacaoEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        NotificacaoEntity result = notificacaoService.marcarComoLida(10L);

        assertTrue(result.isLida());
        verify(notificacaoRepository).save(result);
    }

    @Test
    void deveLancarExcecaoQuandoNotificacaoNaoExiste() {
        when(notificacaoRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notificacaoService.marcarComoLida(123L));

        assertEquals("Notificação não encontrada", ex.getMessage());
    }
}
