package com.Graxa_API.Graxa_API.Notificacao;

import com.Graxa_API.Graxa_API.Controller.NotificacaoController;
import com.Graxa_API.Graxa_API.Service.NotificacaoWebSocketService;
import com.Graxa_API.Graxa_API.dto.NotificacaoDto.RequestNotificacaoDto;
import com.Graxa_API.Graxa_API.dto.NotificacaoDto.ResponseNotificacaoDto;
import com.Graxa_API.Graxa_API.Service.NotificacaoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacaoController.class)
class NotificacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacaoService notificacaoService;
    @MockitoBean
    private NotificacaoWebSocketService notificacaoWebSocketService;


    // -------------------------------------------------------------
    // ✅ Criar notificação autenticado (USANDO DTO NO BODY)
    // -------------------------------------------------------------
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveCriarNotificacao() throws Exception {

        ResponseNotificacaoDto response = new ResponseNotificacaoDto(
                1L,
                "Você foi alocado no show X",
                "ALOCACAO_SHOW",
                false,
                LocalDateTime.now(),
                null
        );

        Mockito.when(notificacaoService.criarNotificacao(any(RequestNotificacaoDto.class)))
                .thenReturn(response);

        String json = """
                {
                    "colaboradorId": 1,
                    "mensagem": "Você foi alocado no show X",
                    "tipo": "ALOCACAO_SHOW",
                    "alocacaoId": null
                }
                """;

        mockMvc.perform(post("/notificacoes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Você foi alocado no show X"))
                .andExpect(jsonPath("$.tipo").value("ALOCACAO_SHOW"));
    }

    // -------------------------------------------------------------
    // ✅ Listar por colaborador
    // -------------------------------------------------------------
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveListarNotificacoesPorColaborador() throws Exception {

        ResponseNotificacaoDto notif = new ResponseNotificacaoDto(
                2L,
                "Teste",
                "INFO",
                false,
                LocalDateTime.now(),
                null
        );

        Mockito.when(notificacaoService.listarPorColaborador(1L))
                .thenReturn(List.of(notif));

        mockMvc.perform(get("/notificacoes/colaborador/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensagem").value("Teste"));
    }

    // -------------------------------------------------------------
    // ✅ Listar não lidas
    // -------------------------------------------------------------
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveListarNotificacoesNaoLidas() throws Exception {

        ResponseNotificacaoDto notif = new ResponseNotificacaoDto(
                3L,
                "Não lida",
                "INFO",
                false,
                LocalDateTime.now(),
                null
        );

        Mockito.when(notificacaoService.listarNaoLidas(1L))
                .thenReturn(List.of(notif));

        mockMvc.perform(get("/notificacoes/colaborador/1/nao-lidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensagem").value("Não lida"))
                .andExpect(jsonPath("$[0].lida").value(false));
    }

    // -------------------------------------------------------------
    // ✅ Marcar como lida (agora usa DTO e retorna ResponseNotificacaoDto)
    // -------------------------------------------------------------
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveMarcarNotificacaoComoLida() throws Exception {

        ResponseNotificacaoDto notif = new ResponseNotificacaoDto(
                5L,
                "Teste",
                "INFO",
                true,
                LocalDateTime.now(),
                null
        );

        Mockito.when(notificacaoService.marcarComoLida(5L))
                .thenReturn(notif);

        mockMvc.perform(put("/notificacoes/5/lida").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lida").value(true));
    }

    // -------------------------------------------------------------
    // ❌ Sem usuário autenticado → deve retornar 401
    // -------------------------------------------------------------
    @Test
    void deveNegarAcessoSemUsuario() throws Exception {
        mockMvc.perform(get("/notificacoes/colaborador/1"))
                .andExpect(status().isUnauthorized());
    }

    // -------------------------------------------------------------
    // ❌ Sem CSRF em POST → deve retornar 403
    // -------------------------------------------------------------
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveNegarAcessoSemCsrf() throws Exception {

        String json = """
                {
                    "colaboradorId": 1,
                    "mensagem": "Teste",
                    "tipo": "INFO",
                    "alocacaoId": null
                }
                """;

        mockMvc.perform(post("/notificacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
