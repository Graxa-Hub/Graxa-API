package com.Graxa_API.Graxa_API.core.application.repository;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;

import java.util.Optional;

public interface IRecuperarSenhaRepository {
    Optional<CredenciaisUsuarioEntity> buscarPorEmail(String email);
    void salvar(CredenciaisUsuarioEntity credencial);
}