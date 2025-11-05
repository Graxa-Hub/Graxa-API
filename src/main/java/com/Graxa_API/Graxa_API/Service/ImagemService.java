package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImagemService {
    @Value("${upload.dir}")
    private String pastaUpload;



    public ResponseEntity<ImagemEntity> salvarImagem(MultipartFile arquivo) throws IOException{
        Path pasta = Paths.get(pastaUpload);
        if(!Files.exists(pasta)){
            Files.createDirectories(pasta);
        }

        Path caminho = pasta.resolve(arquivo.getOriginalFilename());

        Files.copy(arquivo.getInputStream(), caminho);

        ImagemEntity arquivoGerado = new ImagemEntity();
        arquivoGerado.setNomeArquivo(arquivo.getOriginalFilename());
        arquivoGerado.setTipo(arquivo.getContentType());
        arquivoGerado.setCaminho(caminho.toString());

        return ResponseEntity.status(201).body(arquivoGerado);
    }

    public byte[] baixarImagem(String nomeArquivo) throws IOException{
        Path caminho = Paths.get(pastaUpload).resolve(nomeArquivo);
        if (!Files.exists(caminho)){
            throw new IOException("Imagem não encontrada");
        }
        return Files.readAllBytes(caminho);
    }
}
