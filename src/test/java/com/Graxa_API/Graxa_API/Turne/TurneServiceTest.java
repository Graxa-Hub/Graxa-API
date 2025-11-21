package com.Graxa_API.Graxa_API.Turne;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.TurneJaExistenteException;
import com.Graxa_API.Graxa_API.Exception.TurneNaoEncontradaException;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.Service.ImagemService;
import com.Graxa_API.Graxa_API.Service.TurneService;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TurneServiceTest {

    private TurneRepository turneRepository;
    private BandaRepository bandaRepository;
    private ImagemService imagemService;
    private TurneService turneService;

    @BeforeEach
    void setup() {
        turneRepository = mock(TurneRepository.class);
        bandaRepository = mock(BandaRepository.class);
        imagemService = mock(ImagemService.class);
        turneService = new TurneService(turneRepository, bandaRepository, imagemService);
    }

    @Test
    void deveCriarTurneComSucesso() throws IOException {
        RequestTurneDto dto = new RequestTurneDto(
                "Rock Brasil",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Grande evento",
                1L
        );


        BandaEntity banda = new BandaEntity();
        banda.setId(1L);
        banda.setNome("Banda Teste");

        when(turneRepository.existsByNomeTurne(dto.nomeTurne())).thenReturn(false);
        when(bandaRepository.findById(1L)).thenReturn(Optional.of(banda));

        ImagemEntity imagemEntity = new ImagemEntity();
        imagemEntity.setNomeArquivo("uid123.jpg");
        when(imagemService.salvarImagem(any())).thenReturn(imagemEntity);


        TurneEntity salvo = new TurneEntity();
        salvo.setId(10L);
        salvo.setNomeTurne(dto.nomeTurne());
        when(turneRepository.save(any(TurneEntity.class))).thenReturn(salvo);

        MockMultipartFile mockFile = new MockMultipartFile("imagem", "file.jpg", "image/jpeg", "conteudo".getBytes());

        ResponseEntity<ResponseTurneDto> response = turneService.criarTurne(dto, mockFile);

        assertEquals(201, response.getStatusCode().value());

        assertEquals("Rock Brasil", response.getBody().nomeTurne());
        verify(turneRepository).save(any(TurneEntity.class));
        verify(imagemService).salvarImagem(any());
    }

    @Test
    void deveLancarExcecaoQuandoTurneJaExiste() {
        RequestTurneDto dto = new RequestTurneDto(
                "Rock Brasil",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Grande evento",
                99L
        );


        when(turneRepository.existsByNomeTurne(dto.nomeTurne())).thenReturn(true);

        MockMultipartFile mockFile = new MockMultipartFile("imagem", "file.jpg", "image/jpeg", "conteudo".getBytes());

        assertThrows(TurneJaExistenteException.class, () -> turneService.criarTurne(dto, mockFile));
    }

    @Test
    void deveLancarExcecaoQuandoBandaNaoEncontrada() {
        RequestTurneDto dto = new RequestTurneDto(
                "Rock Brasil",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Grande evento",
                99L
        );


        when(turneRepository.existsByNomeTurne(dto.nomeTurne())).thenReturn(false);
        when(bandaRepository.findById(99L)).thenReturn(Optional.empty());

        MockMultipartFile mockFile = new MockMultipartFile("imagem", "file.jpg", "image/jpeg", "conteudo".getBytes());

        assertThrows(BandaNaoEncontradaException.class, () -> turneService.criarTurne(dto, mockFile));
    }

    @Test
    void deveBuscarTurnePorId() {
        TurneEntity turne = new TurneEntity();
        turne.setId(1L);
        turne.setNomeTurne("Rock Brasil");
        turne.setAtivo(true);

        when(turneRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(turne));

        ResponseEntity<ResponseTurneDto> response = turneService.buscarPorId(1L);

        assertEquals(200, response.getStatusCode().value());

        assertEquals("Rock Brasil", response.getBody().nomeTurne());
    }

    @Test
    void deveLancarExcecaoQuandoTurneNaoEncontradaPorId() {
        when(turneRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());
        assertThrows(TurneNaoEncontradaException.class, () -> turneService.buscarPorId(1L));
    }

    @Test
    void deveListarTurnesAtivas() {
        TurneEntity turne = new TurneEntity();
        turne.setId(1L);
        turne.setNomeTurne("Rock Brasil");
        turne.setAtivo(true);

        when(turneRepository.findAllByAtivoTrue()).thenReturn(List.of(turne));

        ResponseEntity<List<ResponseTurneDto>> response = turneService.listarAtivas();

        assertEquals(200, response.getStatusCode().value());

        assertEquals(1, response.getBody().size());
        assertEquals("Rock Brasil", response.getBody().get(0).nomeTurne());
    }
}
