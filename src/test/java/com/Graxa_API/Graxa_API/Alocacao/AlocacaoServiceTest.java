package com.Graxa_API.Graxa_API.Alocacao;

import com.Graxa_API.Graxa_API.Entity.AlocacaoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Entity.LocalEntity;
import com.Graxa_API.Graxa_API.Entity.EnderecoEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import com.Graxa_API.Graxa_API.Repository.AlocacaoRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Service.AlocacaoService;
import com.Graxa_API.Graxa_API.Service.NotificacaoService;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.RequestAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AlocacaoServiceTest {

    private AlocacaoRepository alocacaoRepository;
    private ShowRepository showRepository;
    private ColaboradorRepository colaboradorRepository;
    private NotificacaoService notificacaoService;
    private AlocacaoService alocacaoService;

    private ShowEntity show;
    private ColaboradorEntity colaborador;

    @BeforeEach
    void setUp() {
        alocacaoRepository = mock(AlocacaoRepository.class);
        showRepository = mock(ShowRepository.class);
        colaboradorRepository = mock(ColaboradorRepository.class);
        notificacaoService = mock(NotificacaoService.class);
        alocacaoService = new AlocacaoService(alocacaoRepository, showRepository, colaboradorRepository, notificacaoService);

        TurneEntity turne = new TurneEntity();
        turne.setId(99L);
        turne.setNomeTurne("Turnê Teste");

        EnderecoEntity enderecoLocal = new EnderecoEntity();
        enderecoLocal.setCep("08506-000");
        enderecoLocal.setLogradouro("Rua das Flores");
        enderecoLocal.setNumero(123);
        enderecoLocal.setBairro("Centro");
        enderecoLocal.setCidade("Ferraz de Vasconcelos");
        enderecoLocal.setEstado("SP");
        enderecoLocal.setPais("Brasil");

        LocalEntity local = new LocalEntity();
        local.setId(10L);
        local.setNome("Arena Graxa");
        local.setCapacidade(5000);
        local.setEndereco(enderecoLocal);

        ColaboradorEntity responsavel = new ColaboradorEntity();
        responsavel.setId(99L);
        responsavel.setNome("Produtor Responsável");

        show = new ShowEntity();
        show.setId(1L);
        show.setNomeEvento("Festival Graxa");
        show.setTurne(turne);
        show.setLocal(local);
        show.setResponsavelEvento(responsavel);

        colaborador = new ColaboradorEntity();
        colaborador.setId(5L);
        colaborador.setNome("Colaborador Teste");
        colaborador.setDataNascimento(LocalDate.of(1990, 5, 15));
    }

    @Test
    void deveCriarAlocacaoComStatusPendente() {
        RequestAlocacaoDto dto = new RequestAlocacaoDto(1L, 5L);

        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(colaboradorRepository.findById(5L)).thenReturn(Optional.of(colaborador));
        when(alocacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseAlocacaoDto response = alocacaoService.criarAlocacao(dto);

        assertThat(response.status()).isEqualTo(StatusAlocacao.PENDENTE);
        assertThat(response.show().nomeEvento()).isEqualTo("Festival Graxa");
        assertThat(response.colaborador().nome()).isEqualTo("Colaborador Teste");

        verify(notificacaoService).criarNotificacao(
                eq(5L),
                contains("Festival Graxa"),
                eq("ALOCACAO_SHOW"),
                nullable(Long.class)   // ⭐ CORREÇÃO: aceita null
        );
    }


    @Test
    void deveResponderAlocacaoComoAceita() {
        AlocacaoEntity alocacao = new AlocacaoEntity();
        alocacao.setId(1L);
        alocacao.setShow(show);
        alocacao.setColaborador(colaborador);
        alocacao.setStatus(StatusAlocacao.PENDENTE);
        alocacao.setDataHoraCriacao(LocalDateTime.now());

        when(alocacaoRepository.findById(1L)).thenReturn(Optional.of(alocacao));
        when(alocacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // usa o enum correto do service
        ResponseAlocacaoDto response = alocacaoService.responderAlocacao(1L, StatusAlocacao.ACEITO);

        assertThat(response.status()).isEqualTo(StatusAlocacao.ACEITO);
        assertThat(response.dataHoraResposta()).isNotNull();
    }

    @Test
    void deveResponderAlocacaoComoRecusada() {
        AlocacaoEntity alocacao = new AlocacaoEntity();
        alocacao.setId(1L);
        alocacao.setShow(show);
        alocacao.setColaborador(colaborador);
        alocacao.setStatus(StatusAlocacao.PENDENTE);

        when(alocacaoRepository.findById(1L)).thenReturn(Optional.of(alocacao));
        when(alocacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // usa o enum correto do service
        ResponseAlocacaoDto response = alocacaoService.responderAlocacao(1L, StatusAlocacao.RECUSADO);

        assertThat(response.status()).isEqualTo(StatusAlocacao.RECUSADO);
        assertThat(response.dataHoraResposta()).isNotNull();
    }

    @Test
    void deveListarAlocacoesPorShow() {
        AlocacaoEntity alocacao1 = new AlocacaoEntity();
        alocacao1.setId(1L);
        alocacao1.setShow(show);
        alocacao1.setColaborador(colaborador);
        alocacao1.setStatus(StatusAlocacao.PENDENTE);

        AlocacaoEntity alocacao2 = new AlocacaoEntity();
        alocacao2.setId(2L);
        alocacao2.setShow(show);
        alocacao2.setColaborador(colaborador);
        alocacao2.setStatus(StatusAlocacao.ACEITO);

        when(alocacaoRepository.findByShowId(1L)).thenReturn(List.of(alocacao1, alocacao2));

        List<ResponseAlocacaoDto> response = alocacaoService.listarPorShow(1L);

        assertThat(response).hasSize(2);
        assertThat(response.get(0).status()).isEqualTo(StatusAlocacao.PENDENTE);
        assertThat(response.get(1).status()).isEqualTo(StatusAlocacao.ACEITO);
    }
}
