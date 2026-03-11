package com.Graxa_API.Graxa_API.core.application;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.core.application.gateway.EmailGateway;
import com.Graxa_API.Graxa_API.core.application.repository.IRecuperarSenhaRepository;
import com.Graxa_API.Graxa_API.core.exception.*;
import com.Graxa_API.Graxa_API.core.recuperarSenha.valueObject.CodigoRecuperacao;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RecuperarSenhaUseCase {

    private final IRecuperarSenhaRepository repository;
    private final EmailGateway  emailGateway;
    private final PasswordEncoder passwordEncoder;

    public RecuperarSenhaUseCase(
            IRecuperarSenhaRepository repository,
            EmailGateway emailGateway,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.emailGateway = emailGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public void enviarCodigo(String email) {
        CredenciaisUsuarioEntity credencial = repository.buscarPorEmail(email)
                .orElseThrow(EmailNaoEncontradoException::new);

        CodigoRecuperacao codigo = CodigoRecuperacao.gerar();

        credencial.setCodigoRecuperacao(codigo.getValor());
        credencial.setCodigoExpiraEm(codigo.getExpiraEm());
        repository.salvar(credencial);

        emailGateway.enviar(
                credencial.getEmail(),
                "Código de Recuperação de Senha",
                "Seu código de recuperação é: " + codigo.getValor()
        );
    }

    public void validarCodigo(String email, String codigoInformado) {
        CredenciaisUsuarioEntity credencial = repository.buscarPorEmail(email)
                .orElseThrow(EmailNaoEncontradoException::new);

        if (credencial.getCodigoRecuperacao() == null) {
            throw new CodigoNaoSolicitadoException();
        }

        CodigoRecuperacao codigo = CodigoRecuperacao.de(
                credencial.getCodigoRecuperacao(),
                credencial.getCodigoExpiraEm()
        );

        if (!codigo.correspondeA(codigoInformado)) {
            throw new CodigoInvalidoException();
        }

        if (codigo.expirou()) {
            throw new CodigoExpiradoException();
        }
    }

    public void resetarSenha(String email, String novaSenha) {
        CredenciaisUsuarioEntity credencial = repository.buscarPorEmail(email)
                .orElseThrow(EmailNaoEncontradoException::new);

        credencial.setSenha(passwordEncoder.encode(novaSenha));
        credencial.setCodigoRecuperacao(null);
        credencial.setCodigoExpiraEm(null);
        repository.salvar(credencial);
    }
}