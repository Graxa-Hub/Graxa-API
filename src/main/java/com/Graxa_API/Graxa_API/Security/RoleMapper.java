package com.Graxa_API.Graxa_API.Security;

import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Mapeia TipoUsuario para grupos de roles do Spring Security.
 *
 * Grupos:
 *  ROLE_PRODUCAO  → quem organiza/gerencia o evento
 *  ROLE_TECNICO   → equipe técnica de som/luz/monitor
 *  ROLE_ARTISTA   → artistas e performers em geral
 *  ROLE_MUSICO    → músicos (subgrupo de ARTISTA, com acesso específico)
 */
public final class RoleMapper {

    private RoleMapper() {}

    private static final Set<TipoUsuario> PRODUCAO = Set.of(
            TipoUsuario.PRODUTOR,
            TipoUsuario.PRODUTOR_ESTRADA,
            TipoUsuario.PRE_PRODUTOR,
            TipoUsuario.ROAD
    );

    private static final Set<TipoUsuario> TECNICO = Set.of(
            TipoUsuario.TECNICO_SOM,
            TipoUsuario.TECNICO_LUZ,
            TipoUsuario.TECNICO_MONITOR,
            TipoUsuario.TECNICO_PA,
            TipoUsuario.ENGENHEIRO_SOM
    );

    private static final Set<TipoUsuario> MUSICO = Set.of(
            TipoUsuario.GUITARRISTA, TipoUsuario.BAIXISTA, TipoUsuario.BATERISTA,
            TipoUsuario.TECLADISTA, TipoUsuario.VIOLONISTA, TipoUsuario.VOCALISTA,
            TipoUsuario.SAXOFONISTA, TipoUsuario.TROMPETISTA, TipoUsuario.TROMBONISTA,
            TipoUsuario.PERCUSSIONISTA, TipoUsuario.VIOLINISTA, TipoUsuario.CELISTA,
            TipoUsuario.CONTRABAIXISTA, TipoUsuario.FLAUTISTA, TipoUsuario.CLARINETISTA,
            TipoUsuario.OBOISTA, TipoUsuario.FAGOTISTA, TipoUsuario.HARPISTA,
            TipoUsuario.PIANISTA, TipoUsuario.ACORDEONISTA, TipoUsuario.GAITEIRO,
            TipoUsuario.BANDOLINISTA, TipoUsuario.CAVAQUINISTA, TipoUsuario.UKULELISTA,
            TipoUsuario.GUITARRA_RITMICA, TipoUsuario.GUITARRA_SOLO, TipoUsuario.DJ,
            TipoUsuario.MC, TipoUsuario.BACKING_VOCAL, TipoUsuario.CORISTA,
            TipoUsuario.RAPPER, TipoUsuario.VIOLISTA, TipoUsuario.TUBISTA,
            TipoUsuario.SAX_BARITONO, TipoUsuario.SAX_TENOR, TipoUsuario.SAX_ALTO,
            TipoUsuario.SAX_SOPRANO, TipoUsuario.TROMPA, TipoUsuario.EUPHONIUM,
            TipoUsuario.TIMPANISTA, TipoUsuario.MARIMBISTA, TipoUsuario.XYLOFONISTA,
            TipoUsuario.VIBRAFONISTA, TipoUsuario.TRIANGULISTA,
            TipoUsuario.CANTOR_LIRICO, TipoUsuario.SOPRANO, TipoUsuario.CONTRALTO,
            TipoUsuario.TENOR, TipoUsuario.BARITONO, TipoUsuario.BAIXO,
            TipoUsuario.REGENTE, TipoUsuario.MAESTRO
    );

    private static final Set<TipoUsuario> ARTISTA = Set.of(
            TipoUsuario.ARTISTA,
            TipoUsuario.ARRANJADOR,
            TipoUsuario.COMPOSITOR,
            TipoUsuario.LETRISTA,
            TipoUsuario.PRODUTOR_MUSICAL
    );

    /**
     * Retorna as authorities para um TipoUsuario.
     * Músicos recebem ROLE_MUSICO + ROLE_ARTISTA (herança de acesso).
     */
    public static Collection<GrantedAuthority> toAuthorities(TipoUsuario tipo) {
        if (tipo == null) return List.of();

        if (PRODUCAO.contains(tipo))   return List.of(authority("ROLE_PRODUCAO"));
        if (TECNICO.contains(tipo))    return List.of(authority("ROLE_TECNICO"));
        if (MUSICO.contains(tipo))     return List.of(authority("ROLE_MUSICO"), authority("ROLE_ARTISTA"));
        if (ARTISTA.contains(tipo))    return List.of(authority("ROLE_ARTISTA"));

        return List.of();
    }

    /** Converte uma string de roles separada por vírgula de volta para authorities (uso no JWT filter). */
    public static List<GrantedAuthority> fromRolesString(String rolesCommaDelimited) {
        if (rolesCommaDelimited == null || rolesCommaDelimited.isBlank()) return List.of();
        return Arrays.stream(rolesCommaDelimited.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .map(a -> (GrantedAuthority) a)   // ← cast explícito
                .toList();
    }

    private static SimpleGrantedAuthority authority(String role) {
        return new SimpleGrantedAuthority(role);
    }
}