package com.Graxa_API.Graxa_API.Entity.Usuario;

import com.Graxa_API.Graxa_API.Entity.*;
import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ColaboradorEntity extends UsuarioEntity{
    public ColaboradorEntity() {}

    private LocalDate dataNascimento;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    private TipoUsuario tipoUsuario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TelefoneEntity> telefones;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private CredenciaisUsuarioEntity credenciais;

    @OneToMany(mappedBy = "responsavelAcao")
    private List<AcaoEntity> acoes = new ArrayList<>();

    // Relacionamento inverso: um colaborador pode ter várias notificações
    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificacaoEntity> notificacoes = new ArrayList<>();




    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }


    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }


    public EnderecoEntity getEndereco() { return endereco; }
    public void setEndereco(EnderecoEntity endereco) { this.endereco = endereco; }

    public List<TelefoneEntity> getTelefones() { return telefones; }
    public void setTelefones(List<TelefoneEntity> telefones) { this.telefones = telefones; }

    public CredenciaisUsuarioEntity getCredenciais() { return credenciais; }
    public void setCredenciais(CredenciaisUsuarioEntity credenciais) { this.credenciais = credenciais; }

    public List<AcaoEntity> getAcoes() { return acoes; }
    public void setAcoes(List<AcaoEntity> acoes) { this.acoes = acoes; }

    public List<NotificacaoEntity> getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(List<NotificacaoEntity> notificacoes) {
        this.notificacoes = notificacoes;
    }
}
