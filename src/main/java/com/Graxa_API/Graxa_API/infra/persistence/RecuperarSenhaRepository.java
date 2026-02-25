package com.Graxa_API.Graxa_API.infra.persistence;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import com.Graxa_API.Graxa_API.core.application.repository.IRecuperarSenhaRepository;

import java.util.Optional;

public class RecuperarSenhaRepository implements IRecuperarSenhaRepository {

    private final CredenciaisUsuarioRepository jpaRepository;

    public RecuperarSenhaRepository(CredenciaisUsuarioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<CredenciaisUsuarioEntity> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public void salvar(CredenciaisUsuarioEntity credencial) {
        jpaRepository.save(credencial);
    }
}