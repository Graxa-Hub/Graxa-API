package com.Graxa_API.Graxa_API.Alocacao;

import com.Graxa_API.Graxa_API.Controller.AlocacaoController;
import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.Service.AlocacaoService;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.RequestAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.ResponseShowDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlocacaoController.class)
class AlocacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlocacaoService alocacaoService;

    private ResponseAlocacaoDto mockResponse(StatusAlocacao status) {
        // Simula dados do show
        ResponseShowDto showDto = new ResponseShowDto(
                1L,
                "Festival Graxa",
                LocalDateTime.parse("2025-11-01T18:00:00"),
                LocalDateTime.parse("2025-11-01T22:00:00"),
                "Show teste",
                null, // turnê
                null, // local
                null, // responsável
                List.of() // bandas
        );

        // Simula dados completos do colaborador
        ResponseUsuarioDto colaboradorDto = new ResponseUsuarioDto(
                5L,
                "Colaborador Teste",
                LocalDate.parse("1990-05-15"),
                "123.456.789-00",
                TipoUsuario.TECNICO_SOM,
                true
        );

        // Monta DTO completo
        return new ResponseAlocacaoDto(
                1L,
                showDto,
                colaboradorDto,
                status,
                true,
                LocalDateTime.parse("2025-11-01T17:00:00"), // dataHoraCriacao
                status != StatusAlocacao.PENDENTE ? LocalDateTime.parse("2025-11-01T17:30:00") : null // dataHoraResposta
        );
    }



    @Test
    @WithMockUser
    void deveCriarAlocacao() throws Exception {
        Mockito.when(alocacaoService.criarAlocacao(any(RequestAlocacaoDto.class)))
                .thenReturn(mockResponse(StatusAlocacao.PENDENTE));

        mockMvc.perform(post("/alocacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "showId": 1,
                                  "colaboradorId": 5
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("pendente"));
    }

    @Test
    @WithMockUser
    void deveResponderAlocacaoAceita() throws Exception {
        Mockito.when(alocacaoService.responderAlocacao(eq(1L), eq(true)))
                .thenReturn(mockResponse(StatusAlocacao.ACEITA));

        mockMvc.perform(put("/alocacoes/1/responder")
                        .param("aceita", "true")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("aceita"));
    }

    @Test
    @WithMockUser
    void deveResponderAlocacaoRecusada() throws Exception {
        Mockito.when(alocacaoService.responderAlocacao(eq(1L), eq(false)))
                .thenReturn(mockResponse(StatusAlocacao.RECUSADA));

        mockMvc.perform(put("/alocacoes/1/responder")
                        .param("aceita", "false")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("recusada"));
    }

    @Test
    @WithMockUser
    void deveListarPorShow() throws Exception {
        Mockito.when(alocacaoService.listarPorShow(1L))
                .thenReturn(List.of(mockResponse(StatusAlocacao.PENDENTE)));

        mockMvc.perform(get("/alocacoes/show/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("pendente"));
    }

    @Test
    void deveNegarAcessoSemUsuario() throws Exception {
        mockMvc.perform(get("/alocacoes/show/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void deveNegarCriacaoSemCsrf() throws Exception {
        mockMvc.perform(post("/alocacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "showId": 1,
                                  "colaboradorId": 5
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}
