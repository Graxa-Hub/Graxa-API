package com.Graxa_API.Graxa_API.Turne;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.TurneJaExistenteException;
import com.Graxa_API.Graxa_API.Exception.TurneNaoEncontradaException;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.Security.SecurityUtils;
import com.Graxa_API.Graxa_API.Service.ImagemService;
import com.Graxa_API.Graxa_API.Service.TurneService;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private SecurityUtils securityUtils;
    private TurneService turneService;

    private ColaboradorEntity logado;
    private BandaEntity banda;

    @BeforeEach
    void setup() {
        turneRepository = mock(TurneRepository.class);
        bandaRepository = mock(BandaRepository.class);
        imagemService = mock(ImagemService.class);
        securityUtils = mock(SecurityUtils.class);
        turneService = new TurneService(turneRepository, bandaRepository, imagemService, securityUtils);

        logado = new ColaboradorEntity();
        logado.setId(1L);
        logado.setNome("Produtor Teste");
        when(securityUtils.getUsuarioLogado()).thenReturn(logado);

        banda = new BandaEntity();
        banda.setId(1L);
        banda.setNome("Banda Teste");
        banda.setCriadoPor(logado);
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

        when(bandaRepository.findById(1L)).thenReturn(Optional.of(banda));
        when(turneRepository.existsByNomeTurneAndCriadoPorId(dto.nomeTurne(), logado.getId())).thenReturn(false);

        ImagemEntity imagemEntity = new ImagemEntity();
        imagemEntity.setNomeArquivo("uid123.jpg");
        when(imagemService.salvarImagem(any())).thenReturn(imagemEntity);

        TurneEntity salvo = new TurneEntity();
        salvo.setId(10L);
        salvo.setNomeTurne(dto.nomeTurne());
        salvo.setCriadoPor(logado);
        salvo.setBanda(banda);
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
                1L
        );

        when(bandaRepository.findById(1L)).thenReturn(Optional.of(banda));
        when(turneRepository.existsByNomeTurneAndCriadoPorId(dto.nomeTurne(), logado.getId())).thenReturn(true);

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
        turne.setCriadoPor(logado);
        turne.setBanda(banda);

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
        turne.setCriadoPor(logado);
        turne.setBanda(banda);

        when(turneRepository.findByAtivoTrueAndCriadoPorId(eq(logado.getId()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(turne)));

        Page<ResponseTurneDto> response = turneService.listarAtivas(Pageable.unpaged());

        assertEquals(1, response.getContent().size());
        assertEquals("Rock Brasil", response.getContent().get(0).nomeTurne());
    }

    @Test
    void deveDeletarTurne() {
        TurneEntity turne = new TurneEntity();
        turne.setId(1L);
        turne.setNomeTurne("Rock Brasil");
        turne.setAtivo(true);
        turne.setCriadoPor(logado);
        turne.setBanda(banda);

        when(turneRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(turne));
        when(turneRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        turneService.deletarTurne(1L);

        assertFalse(turne.isAtivo());
        verify(turneRepository).save(turne);
    }

    @Test
    void deveLancarExcecaoAoCriarTurneComBandaDeOutroProdutor() {
        ColaboradorEntity outroProduto = new ColaboradorEntity();
        outroProduto.setId(99L);

        BandaEntity bandaDeOutro = new BandaEntity();
        bandaDeOutro.setId(2L);
        bandaDeOutro.setCriadoPor(outroProduto);

        RequestTurneDto dto = new RequestTurneDto(
                "Turnê Proibida",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Acesso negado",
                2L
        );

        when(bandaRepository.findById(2L)).thenReturn(Optional.of(bandaDeOutro));

        MockMultipartFile mockFile = new MockMultipartFile("imagem", "file.jpg", "image/jpeg", "conteudo".getBytes());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> turneService.criarTurne(dto, mockFile));
        assertEquals("Apenas o criador da banda pode criar turnês", ex.getMessage());
        verify(turneRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoAoBuscarTurneDeOutroProdutor() {
        ColaboradorEntity outroProduto = new ColaboradorEntity();
        outroProduto.setId(99L);

        TurneEntity turneDeOutro = new TurneEntity();
        turneDeOutro.setId(5L);
        turneDeOutro.setAtivo(true);
        turneDeOutro.setCriadoPor(outroProduto);
        turneDeOutro.setBanda(banda);

        when(turneRepository.findByIdAndAtivoTrue(5L)).thenReturn(Optional.of(turneDeOutro));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> turneService.buscarPorId(5L));
        assertEquals("Acesso negado", ex.getMessage());
    }
}
