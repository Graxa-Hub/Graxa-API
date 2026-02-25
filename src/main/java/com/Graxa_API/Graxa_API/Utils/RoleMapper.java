package com.Graxa_API.Graxa_API.Utils;
import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
public class RoleMapper {
    public static String toRole(TipoUsuario tipoUsuario) {
        return switch (tipoUsuario) {
            // Produtores
            case PRODUTOR, PRODUTOR_ESTRADA, PRE_PRODUTOR, PRODUTOR_MUSICAL -> "ROLE_PRODUTOR";
            // Técnicos
            case TECNICO_SOM, TECNICO_LUZ, TECNICO_MONITOR, TECNICO_PA, ENGENHEIRO_SOM -> "ROLE_TECNICO";
            //Artistas e músicos
            case ARTISTA, GUITARRISTA, BAIXISTA, BATERISTA, TECLADISTA, VIOLONISTA, VOCALISTA, SAXOFONISTA, TROMPETISTA, TROMBONISTA, PERCUSSIONISTA, VIOLINISTA, CELISTA, CONTRABAIXISTA, FLAUTISTA, CLARINETISTA, OBOISTA, FAGOTISTA, HARPISTA, PIANISTA, ACORDEONISTA, GAITEIRO, BANDOLINISTA, CAVAQUINISTA, UKULELISTA, GUITARRA_RITMICA, GUITARRA_SOLO, DJ, MC, BACKING_VOCAL, CORISTA, RAPPER, VIOLISTA, TUBISTA, SAX_BARITONO, SAX_TENOR, SAX_ALTO, SAX_SOPRANO, TROMPA, EUPHONIUM, TIMPANISTA, MARIMBISTA, XYLOFONISTA, VIBRAFONISTA, TRIANGULISTA, CANTOR_LIRICO, SOPRANO, CONTRALTO, TENOR, BARITONO, BAIXO -> "ROLE_ARTISTA";
            // Outros papéis relacionados à produção musical
            case ROAD, REGENTE, MAESTRO, ARRANJADOR, COMPOSITOR, LETRISTA -> "ROLE_MUSICO";
            // Caso você queira ter um admin // (não está no enum, mas pode ser útil) //
            case ADMIN -> "ROLE_ADMIN"; // Fallback genérico default -> "ROLE_USER";
        };
    }
}
