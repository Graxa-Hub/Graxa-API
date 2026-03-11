package com.Graxa_API.Graxa_API.infra.config;

import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import com.Graxa_API.Graxa_API.core.application.gateway.EmailGateway;
import com.Graxa_API.Graxa_API.core.application.RecuperarSenhaUseCase;
import com.Graxa_API.Graxa_API.infra.persistence.RecuperarSenhaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RecuperarSenhaBeanConfig {

    @Bean
    public RecuperarSenhaRepository recuperarSenhaRepository(
            CredenciaisUsuarioRepository jpaRepository
    ) {
        return new RecuperarSenhaRepository(jpaRepository);
    }

    @Bean
    public RecuperarSenhaUseCase recuperarSenhaUseCase(
            RecuperarSenhaRepository repository,
            EmailGateway emailGateway,
            PasswordEncoder passwordEncoder
    ) {
        return new RecuperarSenhaUseCase(repository, emailGateway, passwordEncoder);
    }
}