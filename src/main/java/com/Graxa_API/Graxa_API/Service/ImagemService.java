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
import java.util.Arrays;
import java.util.List;
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
        // Pasta base normalizada
        Path pastaBase = Paths.get(pastaUpload).toAbsolutePath().normalize();

        // Caminho alvo normalizado
        Path caminho = pastaBase.resolve(nomeArquivo).normalize();

        // ✅ Verifica se o caminho está dentro da pasta base
        if (!caminho.startsWith(pastaBase)) {
            throw new SecurityException("Tentativa de path traversal detectada!");
        }

        // ✅ Whitelist de extensões permitidas
        List<String> extensoesPermitidas = Arrays.asList(".png", ".jpg", ".jpeg", ".webp");
        boolean permitido = extensoesPermitidas.stream().anyMatch(nomeArquivo::endsWith);
        if (!permitido) {
            throw new SecurityException("Extensão não permitida!");
        }

        // ✅ Verifica se o arquivo existe
        if (!Files.exists(caminho)) {
            throw new IOException("Imagem não encontrada");
        }

        return Files.readAllBytes(caminho);
    }

}
