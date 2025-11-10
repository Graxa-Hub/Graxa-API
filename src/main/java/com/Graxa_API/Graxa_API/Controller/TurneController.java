package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Service.ImagemService;
import com.Graxa_API.Graxa_API.Service.TurneService;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/turnes")
@Tag(name = "Turnê", description = "Endpoints relacionados à gestão de turnês")
public class TurneController {

    private final TurneService turneService;
    private final ImagemService imagemService;

    public TurneController(TurneService turneService, ImagemService imagemService) {
        this.turneService = turneService;
        this.imagemService = imagemService;
    }

    @Operation(summary = "Cria uma nova turnê", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<ResponseTurneDto> criarTurne(
            @RequestPart("dados") @Valid RequestTurneDto dto,
            @RequestPart("imagem") MultipartFile imagem
    ) throws IOException {
        // Salva a imagem na pasta
        ImagemEntity imagemSalva = imagemService.salvarImagem(imagem).getBody();

        // Passa só o nome do arquivo para o TurneService
        return turneService.criarTurne(dto, imagemSalva.getNomeArquivo());
    }

    @Operation(summary = "Busca uma turnê pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTurneDto> buscarPorId(@PathVariable Long id) {
        return turneService.buscarPorId(id);
    }

    @Operation(summary = "Busca uma turnê pelo nome", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/nome/{nome}")
    public ResponseEntity<ResponseTurneDto> buscarPorNome(@PathVariable String nome) {
        return turneService.buscarPorNome(nome);
    }

    @Operation(summary = "Atualiza uma turnê existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ResponseTurneDto> atualizar(
            @PathVariable Long id,
            @RequestPart("dados") @Valid RequestTurneDto dto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) throws IOException {
        String nomeImagem = null;
        if (imagem != null && !imagem.isEmpty()) {
            ImagemEntity imagemSalva = imagemService.salvarImagem(imagem).getBody();
            nomeImagem = imagemSalva.getNomeArquivo();
        }
        return turneService.atualizarTurne(id, dto, nomeImagem);
    }

    @Operation(summary = "Deleta uma turnê pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return turneService.deletarTurne(id);
    }

    @Operation(summary = "Lista todas as turnês ativas", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseTurneDto>> listarAtivas() {
        return turneService.listarAtivas();
    }
}
