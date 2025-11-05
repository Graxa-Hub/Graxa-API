package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Service.ImagemService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


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
            ImagemEntity img = imagemService.salvarImagem(arquivo).getBody();
            return ResponseEntity.ok(img);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download/{nomeArquivo}")
    public ResponseEntity<byte[]> downloadImagem(@PathVariable String nomeArquivo) {
        try{
            byte[] dados = imagemService.baixarImagem(nomeArquivo);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment filename =/"+nomeArquivo+ "/")
            .contentType(MediaType.IMAGE_JPEG).body(dados);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
