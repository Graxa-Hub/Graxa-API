package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.TurneJaExistenteException;
import com.Graxa_API.Graxa_API.Exception.TurneNaoEncontradaException;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
public class TurneService {

    private final TurneRepository turneRepository;
    private final BandaRepository bandaRepository;
    private final ImagemService imagemService; // ✅ injetar também

    public TurneService(TurneRepository turneRepository,
                        BandaRepository bandaRepository,
                        ImagemService imagemService) {
        this.turneRepository = turneRepository;
        this.bandaRepository = bandaRepository;
        this.imagemService = imagemService;
    }

    @Transactional
    public ResponseEntity<ResponseTurneDto> criarTurne(RequestTurneDto dto, MultipartFile imagem) throws IOException {
        if (turneRepository.existsByNomeTurne(dto.nomeTurne())) {
            throw new TurneJaExistenteException(dto.nomeTurne());
        }

        var banda = bandaRepository.findById(dto.bandaId())
                .orElseThrow(() -> new BandaNaoEncontradaException(dto.bandaId()));

        // ✅ Salva a imagem e obtém o nome gerado (UID + extensão)
        ImagemEntity imagemSalva = imagemService.salvarImagem(imagem);
        String nomeImagem = imagemSalva.getNomeArquivo();

        TurneEntity turne = new TurneEntity();
        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());
        turne.setDescricao(dto.descricao());
        turne.setAtivo(true);
        turne.setNomeImagem(nomeImagem);
        turne.setBanda(banda);

        TurneEntity salvo = turneRepository.save(turne);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseTurneDto.toResponse(salvo));
    }

    public ResponseEntity<ResponseTurneDto> buscarPorId(Long id) {
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));
        return ResponseEntity.ok(ResponseTurneDto.toResponse(turne));
    }

    public Page<ResponseTurneDto> buscarPorBanda(Long bandaId, Pageable pageable) {
        var banda = bandaRepository.findById(bandaId)
                .orElseThrow(() -> new BandaNaoEncontradaException(bandaId));

        Page<TurneEntity> turnes = turneRepository.findByBandaIdAndAtivoTrue(banda.getId(), pageable);

        if (turnes.isEmpty()) {
            throw new TurneNaoEncontradaException("Nenhuma turnê encontrada para a banda " + banda.getNome());
        }

        return turnes.map(ResponseTurneDto::toResponse);
    }


    public ResponseEntity<ResponseTurneDto> buscarPorNome(String nome) {
        TurneEntity turne = turneRepository.findByNomeTurneAndAtivoTrue(nome)
                .orElseThrow(() -> new TurneNaoEncontradaException(nome));
        return ResponseEntity.ok(ResponseTurneDto.toResponse(turne));
    }

    @Transactional
    public ResponseEntity<ResponseTurneDto> atualizarTurne(Long id, RequestTurneDto dto, MultipartFile imagem) throws IOException {
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));

        var banda = bandaRepository.findById(dto.bandaId())
                .orElseThrow(() -> new BandaNaoEncontradaException(dto.bandaId()));

        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());
        turne.setDescricao(dto.descricao());
        turne.setBanda(banda);

        // ✅ Se imagem foi enviada, salva e atualiza nome
        if (imagem != null && !imagem.isEmpty()) {
            ImagemEntity imagemSalva = imagemService.salvarImagem(imagem);
            turne.setNomeImagem(imagemSalva.getNomeArquivo());
        }

        TurneEntity atualizado = turneRepository.save(turne);
        return ResponseEntity.ok(ResponseTurneDto.toResponse(atualizado));
    }

    @Transactional
    public ResponseEntity<Void> deletarTurne(Long id) {
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));

        turne.setAtivo(false);
        turneRepository.save(turne);
        return ResponseEntity.noContent().build();
    }

    public Page<ResponseTurneDto> listarAtivas(Pageable pageable) {
        Page<ResponseTurneDto> turnes = turneRepository.findAllByAtivoTrue(pageable)
                .map(ResponseTurneDto::toResponse);

        return turnes;
    }
}
