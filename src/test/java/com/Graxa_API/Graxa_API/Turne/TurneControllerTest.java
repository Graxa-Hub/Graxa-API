package com.Graxa_API.Graxa_API.Turne;

import com.Graxa_API.Graxa_API.Controller.TurneController;
import com.Graxa_API.Graxa_API.Service.TurneService;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TurneController.class)
class TurneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TurneService turneService;

    private final String jsonTurne = """
        {
          "nomeTurne": "Rock Brasil",
          "dataHoraInicioTurne": "2025-11-10T20:00:00",
          "dataHoraFimTurne": "2025-12-20T23:00:00",
          "descricao": "Grande evento",
          "bandaId": 99
        }
        """;

    private ResponseTurneDto mockResponse() {
        return new ResponseTurneDto(
                1L,
                "Rock Brasil",
                LocalDateTime.parse("2025-11-10T20:00:00"),
                LocalDateTime.parse("2025-12-20T23:00:00"),
                "uid123.jpg",
                "Grande evento",
                99L,
                "Banda Teste"
        );
    }


    @Test
    @WithMockUser
    void deveCriarTurne() throws Exception {
        Mockito.when(turneService.criarTurne(any(), any()))
                .thenReturn(ResponseEntity.status(201).body(mockResponse()));

        MockMultipartFile dados = new MockMultipartFile("dados", "", "application/json", jsonTurne.getBytes());
        MockMultipartFile imagem = new MockMultipartFile("imagem", "file.jpg", "image/jpeg", "conteudo".getBytes());

        mockMvc.perform(multipart("/turnes")
                        .file(dados)
                        .file(imagem)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeTurne").value("Rock Brasil"))
                .andExpect(jsonPath("$.nomeImagem").value("uid123.jpg"));
    }

    @Test
    @WithMockUser
    void deveAtualizarTurne() throws Exception {
        Mockito.when(turneService.atualizarTurne(eq(1L), any(), any()))
                .thenReturn(ResponseEntity.ok(mockResponse()));

        MockMultipartFile dados = new MockMultipartFile("dados", "", "application/json", jsonTurne.getBytes());
        MockMultipartFile imagem = new MockMultipartFile("imagem", "file.jpg", "image/jpeg", "conteudo".getBytes());

        mockMvc.perform(multipart("/turnes/1")
                        .file(dados)
                        .file(imagem)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTurne").value("Rock Brasil"));
    }

    @Test
    @WithMockUser
    void deveBuscarPorId() throws Exception {
        Mockito.when(turneService.buscarPorId(1L)).thenReturn(ResponseEntity.ok(mockResponse()));

        mockMvc.perform(get("/turnes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTurne").value("Rock Brasil"));
    }

    @Test
    @WithMockUser
    void deveBuscarPorNome() throws Exception {
        Mockito.when(turneService.buscarPorNome("Rock Brasil")).thenReturn(ResponseEntity.ok(mockResponse()));

        mockMvc.perform(get("/turnes/nome/Rock Brasil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTurne").value("Rock Brasil"));
    }

    @Test
    @WithMockUser
    void deveListarAtivas() throws Exception {
        Page<ResponseTurneDto> page = new PageImpl<>(List.of(mockResponse()));
        Mockito.when(turneService.listarAtivas(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/turnes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeTurne").value("Rock Brasil"));
    }

    @Test
    @WithMockUser
    void deveDeletarTurne() throws Exception {
        Mockito.when(turneService.deletarTurne(1L)).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/turnes/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveNegarAcessoSemUsuario() throws Exception {
        mockMvc.perform(get("/turnes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void deveNegarCriacaoSemCsrf() throws Exception {
        MockMultipartFile dados = new MockMultipartFile("dados", "", "application/json", jsonTurne.getBytes());
        MockMultipartFile imagem = new MockMultipartFile("imagem", "file.jpg", "image/jpeg", "conteudo".getBytes());

        mockMvc.perform(multipart("/turnes")
                        .file(dados)
                        .file(imagem))
                .andExpect(status().isForbidden());
    }
}
