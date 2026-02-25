package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Exception.PathTraversalException;
import com.Graxa_API.Graxa_API.Service.ImagemService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/imagens")
public class ImagemController {
    private final ImagemService imagemService;

    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ImagemEntity> uploadImagem(@RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        try{
            ImagemEntity img = imagemService.salvarImagem(arquivo);
            return ResponseEntity.ok(img);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download/{nomeArquivo}")
    public ResponseEntity<Resource> downloadImagem(@PathVariable String nomeArquivo) throws IOException {
        Path pastaBase = Paths.get("uploads").toAbsolutePath().normalize();
        Path caminho = pastaBase.resolve(nomeArquivo).normalize();

        // Bloqueio de Path Traversal
        if (!caminho.startsWith(pastaBase)) {
            throw new PathTraversalException("Tentativa de Path Traversal detectada");
        }

        if (!Files.exists(caminho)) {
            return ResponseEntity.notFound().build();
        }


            Resource recurso = new UrlResource(caminho.toUri());
            if (!recurso.exists() || !recurso.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(caminho);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(recurso);


    }






}
