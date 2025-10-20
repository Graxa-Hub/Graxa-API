package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDate dataNascimento;

    @Column(unique = true)
    private String cpf;

    private TipoUsuario tipoUsuario;
    private Boolean ativo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TelefoneEntity> telefones;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private CredenciaisUsuarioEntity credenciais;

    @ManyToMany(mappedBy = "integrantes")
    private List<BandaEntity> bandas;

    @OneToMany(mappedBy = "responsavelAcao")
    private List<AcaoEntity> acoes;

    // 🔧 Construtor padrão
    public UsuarioEntity() {}

    // 🔧 Construtor com dados básicos (sem endereço)
    public UsuarioEntity(RequestUsuarioDto dto) {
        this.nome = dto.nome();
        this.dataNascimento = dto.dataNascimento();
        this.cpf = dto.cpf();
        this.tipoUsuario = dto.tipoUsuario();
        this.ativo = true; // padrão
    }

    // 🔍 Getters e Setters
    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public EnderecoEntity getEndereco() { return endereco; }
    public void setEndereco(EnderecoEntity endereco) { this.endereco = endereco; }

    public List<TelefoneEntity> getTelefones() { return telefones; }
    public void setTelefones(List<TelefoneEntity> telefones) { this.telefones = telefones; }

    public CredenciaisUsuarioEntity getCredenciais() { return credenciais; }
    public void setCredenciais(CredenciaisUsuarioEntity credenciais) { this.credenciais = credenciais; }

    public List<BandaEntity> getBandas() { return bandas; }
    public void setBandas(List<BandaEntity> bandas) { this.bandas = bandas; }

    public List<AcaoEntity> getAcoes() { return acoes; }
    public void setAcoes(List<AcaoEntity> acoes) { this.acoes = acoes; }
}
