package com.Graxa_API.Graxa_API.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genero {
    // Rock e derivados
    ROCK("rock"),
    ROCK_ALTERNATIVO("rock_alternativo"),
    HARD_ROCK("hard_rock"),
    PUNK_ROCK("punk_rock"),
    POP_ROCK("pop_rock"),
    INDIE_ROCK("indie_rock"),

    // Metal
    METAL("metal"),
    HEAVY_METAL("heavy_metal"),
    DEATH_METAL("death_metal"),
    BLACK_METAL("black_metal"),
    THRASH_METAL("thrash_metal"),
    METALCORE("metalcore"),

    // Pop
    POP("pop"),
    SYNTH_POP("synth_pop"),
    ELECTROPOP("electropop"),
    K_POP("k_pop"),

    // Eletrônica
    ELETRONICA("eletronica"),
    HOUSE("house"),
    TECHNO("techno"),
    TRANCE("trance"),
    DUBSTEP("dubstep"),
    DRUM_AND_BASS("drum_and_bass"),
    EDM("edm"),

    // Hip Hop e Rap
    HIP_HOP("hip_hop"),
    RAP("rap"),
    TRAP("trap"),
    DRILL("drill"),

    // Brasileiros
    MPB("mpb"),
    SAMBA("samba"),
    PAGODE("pagode"),
    BOSSA_NOVA("bossa_nova"),
    FORRO("forro"),
    SERTANEJO("sertanejo"),
    SERTANEJO_UNIVERSITARIO("sertanejo_universitario"),
    FUNK_CARIOCA("funk_carioca"),
    AXE("axe"),
    FORRO_ELETRONICO("forro_eletronico"),
    PISEIRO("piseiro"),
    ARROCHA("arrocha"),

    // Jazz e Blues
    JAZZ("jazz"),
    BLUES("blues"),
    SOUL("soul"),
    FUNK("funk"),
    RNB("rnb"),

    // Clássicos e Tradicionais
    CLASSICA("classica"),
    GOSPEL("gospel"),
    COUNTRY("country"),
    FOLK("folk"),

    // Reggae
    REGGAE("reggae"),
    REGGAETON("reggaeton"),
    SKA("ska"),

    // Outros
    ALTERNATIVO("alternativo"),
    INDIE("indie"),
    EXPERIMENTAL("experimental"),
    INSTRUMENTAL("instrumental"),
    ACUSTICO("acustico"),
    LO_FI("lo_fi"),
    AMBIENTE("ambiente"),
    NEW_WAVE("new_wave"),
    GRUNGE("grunge"),
    EMO("emo"),
    HARDCORE("hardcore"),
    POST_ROCK("post_rock"),
    SHOEGAZE("shoegaze"),
    WORLD_MUSIC("world_music"),
    LATIN("latin"),
    SALSA("salsa"),
    MERENGUE("merengue"),
    BACHATA("bachata"),
    FLAMENCO("flamenco"),
    TANGO("tango");

    private final String value;

    Genero(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Genero fromValue(String value) {
        for (Genero genero : Genero.values()) {
            if (genero.value.equalsIgnoreCase(value)) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Valor inválido para Genero: " + value);
    }
}