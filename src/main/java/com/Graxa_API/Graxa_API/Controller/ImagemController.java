package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Service.ImagemService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
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
    public ResponseEntity<byte[]> downloadImagem(@PathVariable String nomeArquivo) {
        try{
            byte[] dados = imagemService.baixarImagem(nomeArquivo);
            String contentType = Files.probeContentType(Paths.get("uploads/" + nomeArquivo));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(dados);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
