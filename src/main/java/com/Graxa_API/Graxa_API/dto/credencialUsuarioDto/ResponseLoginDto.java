    package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

    import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
    import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;

    public record ResponseLoginDto(
            String token,
            ResponseUsuarioDto usuario
    ) {
        public static ResponseLoginDto toResponse(CredenciaisUsuarioEntity credenciais, String token) {
            ResponseUsuarioDto usuarioDto = ResponseUsuarioDto.toResponse(credenciais.getUsuario());
            return new ResponseLoginDto(token, usuarioDto);
        }
    }
