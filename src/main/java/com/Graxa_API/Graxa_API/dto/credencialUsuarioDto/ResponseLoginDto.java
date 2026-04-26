    package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

    import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
    import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;

    public record ResponseLoginDto(
            String token,
            String role,              // ← novo
            ResponseUsuarioDto usuario
    ) {
        public static ResponseLoginDto toResponse(CredenciaisUsuarioEntity credenciais, String token, String role) {
            return new ResponseLoginDto(
                    token,
                    role,
                    ResponseUsuarioDto.toResponse(credenciais.getUsuario())
            );
        }
    }
