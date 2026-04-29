package com.Graxa_API.Graxa_API;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.RepresentanteEntity;
import com.Graxa_API.Graxa_API.Enums.Genero;
import com.Graxa_API.Graxa_API.Exception.BandaDuplicadaException;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.ArtistaRepository;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.RepresentanteRepository;
import com.Graxa_API.Graxa_API.Security.SecurityUtils;
import com.Graxa_API.Graxa_API.Service.BandaService;
import com.Graxa_API.Graxa_API.Service.ImagemService;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BandaServiceTest {

    private BandaRepository bandaRepository;
    private ArtistaRepository artistaRepository;
    private RepresentanteRepository representanteRepository;
    private ImagemService imagemService;
    private SecurityUtils securityUtils;
    private BandaService bandaService;

    private ColaboradorEntity logado;

    @BeforeEach
    void setUp() {
        bandaRepository = mock(BandaRepository.class);
        artistaRepository = mock(ArtistaRepository.class);
        representanteRepository = mock(RepresentanteRepository.class);
        imagemService = mock(ImagemService.class);
        securityUtils = mock(SecurityUtils.class);
        bandaService = new BandaService(bandaRepository, artistaRepository, representanteRepository, imagemService, securityUtils);

        logado = new ColaboradorEntity();
        logado.setId(1L);
        logado.setNome("Produtor Teste");
        when(securityUtils.getUsuarioLogado()).thenReturn(logado);
    }

    @Test
    void deveCriarBandaComSucesso() throws IOException {
        RequestBandaDto dto = new RequestBandaDto("Banda Graxa", "Descrição da banda", Genero.ROCK, 10L);

        RepresentanteEntity representante = new RepresentanteEntity();
        representante.setId(10L);
        representante.setNome("Representante Teste");

        when(bandaRepository.existsByNomeAndCriadoPorId(dto.nome(), logado.getId())).thenReturn(false);
        when(representanteRepository.findById(10L)).thenReturn(Optional.of(representante));

        ImagemEntity imagem = new ImagemEntity();
        imagem.setNomeArquivo("foto.jpg");
        when(imagemService.salvarImagem(any())).thenReturn(imagem);

        BandaEntity salva = new BandaEntity();
        salva.setId(1L);
        salva.setNome(dto.nome());
        salva.setDescricao(dto.descricao());
        salva.setGenero(dto.genero());
        salva.setRepresentante(representante);
        salva.setCriadoPor(logado);
        salva.setNomeFoto("foto.jpg");
        when(bandaRepository.save(any(BandaEntity.class))).thenReturn(salva);

        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "dados".getBytes());

        ResponseEntity<ResponseBandaDto> response = bandaService.criarBanda(dto, foto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Banda Graxa", response.getBody().nome());
        assertEquals("foto.jpg", response.getBody().nomeFoto());
        verify(bandaRepository).save(any(BandaEntity.class));
    }

    @Test
    void deveLancarExcecaoQuandoBandaDuplicada() {
        RequestBandaDto dto = new RequestBandaDto("Banda Graxa", "Descrição", Genero.ROCK, 10L);

        when(bandaRepository.existsByNomeAndCriadoPorId(dto.nome(), logado.getId())).thenReturn(true);

        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "dados".getBytes());

        assertThrows(BandaDuplicadaException.class, () -> bandaService.criarBanda(dto, foto));
        verify(bandaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoBandaNaoEncontradaPorId() {
        when(bandaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BandaNaoEncontradaException.class, () -> bandaService.getBandaPorId(99L));
    }

    @Test
    void deveLancarExcecaoQuandoRepresentanteNaoEncontrado() {
        RequestBandaDto dto = new RequestBandaDto("Banda Graxa", "Descrição", Genero.ROCK, 99L);

        when(bandaRepository.existsByNomeAndCriadoPorId(dto.nome(), logado.getId())).thenReturn(false);
        when(representanteRepository.findById(99L)).thenReturn(Optional.empty());

        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "dados".getBytes());

        assertThrows(UsuarioNaoEncontradoException.class, () -> bandaService.criarBanda(dto, foto));
        verify(bandaRepository, never()).save(any());
    }

    @Test
    void deveDeletarBanda() {
        BandaEntity banda = new BandaEntity();
        banda.setId(1L);
        banda.setNome("Banda Graxa");
        banda.setAtivo(true);
        banda.setCriadoPor(logado);

        when(bandaRepository.findById(1L)).thenReturn(Optional.of(banda));
        when(bandaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        bandaService.deletarBanda(1L);

        assertFalse(banda.getAtivo());
        verify(bandaRepository).save(banda);
    }

    @Test
    void deveLancarExcecaoAoAcessarBandaDeOutroProdutor() {
        ColaboradorEntity outroProduto = new ColaboradorEntity();
        outroProduto.setId(99L);

        BandaEntity banda = new BandaEntity();
        banda.setId(1L);
        banda.setCriadoPor(outroProduto);

        when(bandaRepository.findById(1L)).thenReturn(Optional.of(banda));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bandaService.getBandaPorId(1L));
        assertEquals("Acesso negado", ex.getMessage());
    }
}
