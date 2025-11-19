package com.Graxa_API.Graxa_API.Enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoUsuario {
    PRODUTOR_ESTRADA("produtorEstrada"),
    PRE_PRODUTOR("preProdutor"),
    PRODUTOR("produtor"),
    TECNICO_SOM("tecnicoSom"),
    TECNICO_LUZ("tecnicoLuz"),
    TECNICO_MONITOR("tecnicoMonitor"),
    TECNICO_PA("tecnicoPA"),
    ROAD("road"),
    ARTISTA("artista"),

    // Tipos de músicos mapeados
    GUITARRISTA("guitarrista"),
    BAIXISTA("baixista"),
    BATERISTA("baterista"),
    TECLADISTA("tecladista"),
    VIOLONISTA("violonista"),
    VOCALISTA("vocalista"),
    SAXOFONISTA("saxofonista"),
    TROMPETISTA("trompetista"),
    TROMBONISTA("trombonista"),
    PERCUSSIONISTA("percussionista"),
    VIOLINISTA("violinista"),
    CELISTA("celista"),
    CONTRABAIXISTA("contrabaixista"),
    FLAUTISTA("flautista"),
    CLARINETISTA("clarinetista"),
    OBOISTA("oboista"),
    FAGOTISTA("fagotista"),
    HARPISTA("harpista"),
    PIANISTA("pianista"),
    ACORDEONISTA("acordeonista"),
    GAITEIRO("gaiteiro"),
    BANDOLINISTA("bandolinista"),
    CAVAQUINISTA("cavaquinista"),
    UKULELISTA("ukulelista"),
    GUITARRA_RITMICA("guitarraRitmica"),
    GUITARRA_SOLO("guitarraSolo"),
    DJ("dj"),
    MC("mc"),
    REGENTE("regente"),
    MAESTRO("maestro"),
    ARRANJADOR("arranjador"),
    COMPOSITOR("compositor"),
    LETRISTA("letrista"),
    PRODUTOR_MUSICAL("produtorMusical"),
    ENGENHEIRO_SOM("engenheiroSom"),
    BACKING_VOCAL("backingVocal"),
    CORISTA("corista"),
    RAPPER("rapper"),
    VIOLISTA("violista"),
    TUBISTA("tubista"),
    SAX_BARITONO("saxBaritono"),
    SAX_TENOR("saxTenor"),
    SAX_ALTO("saxAlto"),
    SAX_SOPRANO("saxSoprano"),
    TROMPA("trompa"),
    EUPHONIUM("euphonium"),
    TIMPANISTA("timpanista"),
    MARIMBISTA("marimbista"),
    XYLOFONISTA("xilofonista"),
    VIBRAFONISTA("vibrafonista"),
    TRIANGULISTA("triangulista"),
    CANTOR_LIRICO("cantorLirico"),
    SOPRANO("soprano"),
    CONTRALTO("contralto"),
    TENOR("tenor"),
    BARITONO("baritono"),
    BAIXO("baixo"),
    // Adicione outros conforme necessário
    ;

    private final String value;

    TipoUsuario(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TipoUsuario fromValue(String value) {
        for (TipoUsuario tipo : TipoUsuario.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoUsuario: " + value);
    }
}