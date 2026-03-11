    package com.Graxa_API.Graxa_API.Entity;


    import jakarta.persistence.*;

    @Entity

    public class LocalEntity implements Identifiable{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nome;

        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "endereco_id")
        private EnderecoEntity endereco;

        private Integer capacidade;


        public LocalEntity() {}


        public Long getId() { return id; }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public EnderecoEntity getEndereco() { return endereco; }
        public void setEndereco(EnderecoEntity endereco) { this.endereco = endereco; }

        public Integer getCapacidade() { return capacidade; }
        public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }
    }
