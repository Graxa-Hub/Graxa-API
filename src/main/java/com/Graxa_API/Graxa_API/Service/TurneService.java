package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.TurneJaExistenteException;
import com.Graxa_API.Graxa_API.Exception.TurneNaoEncontradaException;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.Security.SecurityUtils;
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
    private final ImagemService imagemService;
    private final SecurityUtils securityUtils;  // ← novo

    public TurneService(TurneRepository turneRepository,
                        BandaRepository bandaRepository,
                        ImagemService imagemService,
                        SecurityUtils securityUtils) {  // ← novo
        this.turneRepository = turneRepository;
        this.bandaRepository = bandaRepository;
        this.imagemService = imagemService;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public ResponseEntity<ResponseTurneDto> criarTurne(RequestTurneDto dto, MultipartFile imagem) throws IOException {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();

        var banda = bandaRepository.findById(dto.bandaId())
                .orElseThrow(() -> new BandaNaoEncontradaException(dto.bandaId()));

        if (!banda.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Apenas o criador da banda pode criar turnês");
        }

        // Duplicada apenas se for do mesmo criador
        if (turneRepository.existsByNomeTurneAndCriadoPorId(dto.nomeTurne(), logado.getId())) {
            throw new TurneJaExistenteException(dto.nomeTurne());
        }

        ImagemEntity imagemSalva = imagemService.salvarImagem(imagem);

        TurneEntity turne = new TurneEntity();
        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());
        turne.setDescricao(dto.descricao());
        turne.setAtivo(true);
        turne.setNomeImagem(imagemSalva.getNomeArquivo());
        turne.setBanda(banda);
        turne.setCriadoPor(logado);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTurneDto.toResponse(turneRepository.save(turne)));
    }

    // Produtor só vê suas próprias turnês
    public Page<ResponseTurneDto> listarAtivas(Pageable pageable) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        return turneRepository.findByAtivoTrueAndCriadoPorId(logado.getId(), pageable)
                .map(ResponseTurneDto::toResponse);
    }

    public ResponseEntity<ResponseTurneDto> buscarPorId(Long id) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));

        if (!turne.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        return ResponseEntity.ok(ResponseTurneDto.toResponse(turne));
    }

    public ResponseEntity<ResponseTurneDto> buscarPorNome(String nome) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        TurneEntity turne = turneRepository.findByNomeTurneAndAtivoTrueAndCriadoPorId(nome, logado.getId())
                .orElseThrow(() -> new TurneNaoEncontradaException(nome));

        return ResponseEntity.ok(ResponseTurneDto.toResponse(turne));
    }

    public Page<ResponseTurneDto> buscarPorBanda(Long bandaId, Pageable pageable) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();

        var banda = bandaRepository.findById(bandaId)
                .orElseThrow(() -> new BandaNaoEncontradaException(bandaId));

        // Só vê turnês da banda se for o criador dela
        if (!banda.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        Page<TurneEntity> turnes = turneRepository.findByBandaIdAndAtivoTrue(banda.getId(), pageable);
        if (turnes.isEmpty()) throw new TurneNaoEncontradaException("Nenhuma turnê encontrada");
        return turnes.map(ResponseTurneDto::toResponse);
    }

    @Transactional
    public ResponseEntity<ResponseTurneDto> atualizarTurne(Long id, RequestTurneDto dto, MultipartFile imagem) throws IOException {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));

        // Só o criador pode atualizar
        if (!turne.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        var banda = bandaRepository.findById(dto.bandaId())
                .orElseThrow(() -> new BandaNaoEncontradaException(dto.bandaId()));

        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());
        turne.setDescricao(dto.descricao());
        turne.setBanda(banda);

        if (imagem != null && !imagem.isEmpty()) {
            turne.setNomeImagem(imagemService.salvarImagem(imagem).getNomeArquivo());
        }

        return ResponseEntity.ok(ResponseTurneDto.toResponse(turneRepository.save(turne)));
    }

    @Transactional
    public ResponseEntity<Void> deletarTurne(Long id) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));

        if (!turne.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        turne.setAtivo(false);
        turneRepository.save(turne);
        return ResponseEntity.noContent().build();
    }


}
