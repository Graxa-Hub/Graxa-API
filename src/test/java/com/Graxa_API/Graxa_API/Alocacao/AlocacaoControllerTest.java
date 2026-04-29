package com.Graxa_API.Graxa_API.Alocacao;

import com.Graxa_API.Graxa_API.Controller.AlocacaoController;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.Service.AlocacaoService;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.RequestAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.ResponseEnderecoDto;
import com.Graxa_API.Graxa_API.dto.LocalDto.ResponseLocalDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.ResponseResumoShowDto;
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
import com.Graxa_API.Graxa_API.Enums.TipoEndereco;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;

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

    @MockitoBean
    private CredenciaisUsuarioRepository credenciaisUsuarioRepository;

    private ResponseAlocacaoDto mockResponse(StatusAlocacao status) {

        ResponseEnderecoDto enderecoDto = new ResponseEnderecoDto(
                99L,
                TipoEndereco.LOCAL,
                "00000-000",
                "Rua Teste",
                "Centro",
                123,
                "Apto 12",
                "São Paulo",
                "SP",
                "Brasil"
        );



        ResponseLocalDto localDto = new ResponseLocalDto(
                10L,
                "Auditório Central",
                enderecoDto,
                300
        );

        ResponseResumoShowDto showDto = new ResponseResumoShowDto(
                1L,
                "Festival Graxa",
                LocalDateTime.parse("2025-11-01T18:00:00"),
                LocalDateTime.parse("2025-11-01T22:00:00"),
                "Show teste",
                localDto
        );

        ResponseUsuarioDto colaboradorDto = new ResponseUsuarioDto(
                5L,
                "Colaborador Teste",
                LocalDate.parse("1990-05-15"),
                "123.456.789-00",
                TipoUsuario.TECNICO_SOM,
                true,
                "foto123.png"
        );

        return new ResponseAlocacaoDto(
                1L,
                showDto,
                colaboradorDto,
                status,
                true,
                LocalDateTime.parse("2025-11-01T17:00:00"),
                status != StatusAlocacao.PENDENTE ? LocalDateTime.parse("2025-11-01T17:30:00") : null
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

        Mockito.when(alocacaoService.responderAlocacao(eq(1L), eq(StatusAlocacao.ACEITO)))
                .thenReturn(mockResponse(StatusAlocacao.ACEITO));

        mockMvc.perform(put("/alocacoes/1/responder")
                        .param("status", "ACEITO")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("aceito"));
    }

    @Test
    @WithMockUser
    void deveResponderAlocacaoRecusada() throws Exception {

        Mockito.when(alocacaoService.responderAlocacao(eq(1L), eq(StatusAlocacao.RECUSADO)))
                .thenReturn(mockResponse(StatusAlocacao.RECUSADO));

        mockMvc.perform(put("/alocacoes/1/responder")
                        .param("status", "RECUSADO")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("recusado"));
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
