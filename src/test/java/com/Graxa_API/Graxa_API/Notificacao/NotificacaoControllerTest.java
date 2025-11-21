package com.Graxa_API.Graxa_API.Notificacao;

import com.Graxa_API.Graxa_API.Controller.NotificacaoController;
import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import com.Graxa_API.Graxa_API.Service.NotificacaoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacaoController.class)
class NotificacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacaoService notificacaoService;

    // ✅ Caso positivo: criar notificação autenticado
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveCriarNotificacao() throws Exception {
        NotificacaoEntity notif = new NotificacaoEntity();
        notif.setMensagem("Você foi alocado no show X");
        notif.setTipo("ALOCACAO_SHOW");

        Mockito.when(notificacaoService.criarNotificacao(anyLong(), anyString(), anyString()))
                .thenReturn(notif);

        mockMvc.perform(post("/notificacoes")
                        .with(csrf()) // adiciona token CSRF
                        .param("colaboradorId", "1")
                        .param("mensagem", "Você foi alocado no show X")
                        .param("tipo", "ALOCACAO_SHOW")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Você foi alocado no show X"))
                .andExpect(jsonPath("$.tipo").value("ALOCACAO_SHOW"));
    }

    // ✅ Caso positivo: listar notificações autenticado
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveListarNotificacoesPorColaborador() throws Exception {
        NotificacaoEntity notif = new NotificacaoEntity();
        notif.setMensagem("Teste");

        Mockito.when(notificacaoService.listarPorColaborador(1L))
                .thenReturn(List.of(notif));

        mockMvc.perform(get("/notificacoes/colaborador/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensagem").value("Teste"));
    }

    // ✅ Caso positivo: listar notificações não lidas autenticado
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveListarNotificacoesNaoLidas() throws Exception {
        NotificacaoEntity notif = new NotificacaoEntity();
        notif.setMensagem("Não lida");
        notif.setLida(false);

        Mockito.when(notificacaoService.listarNaoLidas(1L))
                .thenReturn(List.of(notif));

        mockMvc.perform(get("/notificacoes/colaborador/1/nao-lidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensagem").value("Não lida"))
                .andExpect(jsonPath("$[0].lida").value(false));
    }

    // ✅ Caso positivo: marcar notificação como lida autenticado
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveMarcarNotificacaoComoLida() throws Exception {
        NotificacaoEntity notif = new NotificacaoEntity();
        notif.setMensagem("Teste");
        notif.setLida(true);

        Mockito.when(notificacaoService.marcarComoLida(5L))
                .thenReturn(notif);

        mockMvc.perform(put("/notificacoes/5/lida")
                        .with(csrf())) // adiciona token CSRF
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lida").value(true));
    }

    // ❌ Caso negativo: sem usuário autenticado → deve dar 401
    @Test
    void deveNegarAcessoSemUsuario() throws Exception {
        mockMvc.perform(get("/notificacoes/colaborador/1"))
                .andExpect(status().isUnauthorized());
    }

    // ❌ Caso negativo: sem CSRF em POST → deve dar 403
    @Test
    @WithMockUser(username = "gabriel", roles = {"USER"})
    void deveNegarAcessoSemCsrf() throws Exception {
        mockMvc.perform(post("/notificacoes")
                        .param("colaboradorId", "1")
                        .param("mensagem", "Teste")
                        .param("tipo", "ALOCACAO_SHOW")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
