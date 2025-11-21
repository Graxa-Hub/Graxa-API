package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImagemService {

    @Value("${upload.dir}")
    private String pastaUpload;

    public ImagemEntity salvarImagem(MultipartFile arquivo) throws IOException {
        // Cria pasta se não existir
        Path pasta = Paths.get(pastaUpload);
        if (!Files.exists(pasta)) {
            Files.createDirectories(pasta);
        }

        // Gera UID único
        String uid = UUID.randomUUID().toString();

        // Preserva extensão (opcional)
        String extensao = Optional.ofNullable(arquivo.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")))
                .orElse("");

        String nomeFinal = uid + extensao;

        // Caminho final
        Path caminho = pasta.resolve(nomeFinal);

        // Salva fisicamente (substitui se já existir)
        Files.copy(arquivo.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

        // Cria entidade da imagem
        ImagemEntity arquivoGerado = new ImagemEntity();
        arquivoGerado.setNomeArquivo(nomeFinal);
        arquivoGerado.setTipo(arquivo.getContentType());
        arquivoGerado.setCaminho(caminho.toString());

        return arquivoGerado;
    }

    public byte[] baixarImagem(String nomeArquivo) throws IOException {
        Path caminho = Paths.get(pastaUpload).resolve(nomeArquivo);
        if (!Files.exists(caminho)) {
            throw new IOException("Imagem não encontrada");
        }
        return Files.readAllBytes(caminho);
    }
}
