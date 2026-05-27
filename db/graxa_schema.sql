-- ===========================================================
-- Graxa-API — Schema MySQL
-- Gerado a partir das entities JPA (Hibernate JOINED inheritance)
-- ===========================================================

CREATE DATABASE IF NOT EXISTS graxa_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE graxa_db;

SET FOREIGN_KEY_CHECKS = 0;

-- ─────────────────────────────────────────────
-- ENDERECO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS endereco_entity (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  tipo_endereco VARCHAR(50),
  cep           VARCHAR(10),
  logradouro    VARCHAR(255),
  numero        INT,
  complemento   VARCHAR(255),
  bairro        VARCHAR(255),
  cidade        VARCHAR(255),
  estado        VARCHAR(100),
  pais          VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- ARTISTA
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS artista_entity (
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  ativo     BOOLEAN NOT NULL DEFAULT TRUE,
  nome      VARCHAR(255),
  cpf       VARCHAR(14) UNIQUE,
  foto_nome VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- REPRESENTANTE
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS representante_entity (
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  ativo     BOOLEAN NOT NULL DEFAULT TRUE,
  nome      VARCHAR(255),
  cpf       VARCHAR(14) UNIQUE,
  foto_nome VARCHAR(255),
  email     VARCHAR(255) UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- COLABORADOR
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS colaborador_entity (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  ativo           BOOLEAN NOT NULL DEFAULT TRUE,
  nome            VARCHAR(255),
  cpf             VARCHAR(14) UNIQUE,
  foto_nome       VARCHAR(255),
  data_nascimento DATE,
  tipo_usuario    VARCHAR(60),
  endereco_id     BIGINT,
  CONSTRAINT fk_colab_endereco FOREIGN KEY (endereco_id)
    REFERENCES endereco_entity(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- CREDENCIAIS DO USUÁRIO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS credenciais_usuario_entity (
  id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id              BIGINT NOT NULL UNIQUE,
  nome_usuario            VARCHAR(255),
  email                   VARCHAR(255) UNIQUE,
  senha                   VARCHAR(255),
  data_hora_ultimo_acesso DATETIME(6),
  codigo_recuperacao      VARCHAR(255),
  codigo_expira_em        DATETIME(6),
  lgpd_consentimento      BOOLEAN NOT NULL DEFAULT FALSE,
  data_consentimento_lgpd DATETIME(6),
  ip_consentimento        VARCHAR(45),
  CONSTRAINT fk_cred_usuario FOREIGN KEY (usuario_id)
    REFERENCES colaborador_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- TELEFONE
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS telefone_entity (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id      BIGINT NOT NULL,
  tipo_telefone   VARCHAR(20),
  numero_telefone VARCHAR(20),
  CONSTRAINT fk_tel_usuario FOREIGN KEY (usuario_id)
    REFERENCES colaborador_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- ACAO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS acao_entity (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id     BIGINT NOT NULL,
  tipo_acao      VARCHAR(100),
  data_hora_acao DATETIME(6),
  CONSTRAINT fk_acao_usuario FOREIGN KEY (usuario_id)
    REFERENCES colaborador_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- BANDA
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS banda_entity (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome             VARCHAR(255),
  descricao        TEXT,
  genero           VARCHAR(100),
  nome_foto        VARCHAR(255),
  ativo            BOOLEAN NOT NULL DEFAULT TRUE,
  representante_id BIGINT NOT NULL,
  criador_id       BIGINT NOT NULL,
  CONSTRAINT fk_banda_repr    FOREIGN KEY (representante_id) REFERENCES representante_entity(id),
  CONSTRAINT fk_banda_criador FOREIGN KEY (criador_id)       REFERENCES colaborador_entity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- BANDA_INTEGRANTE  (ManyToMany: banda ↔ artista)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS banda_integrante (
  banda_id   BIGINT NOT NULL,
  artista_id BIGINT NOT NULL,
  PRIMARY KEY (banda_id, artista_id),
  CONSTRAINT fk_bi_banda   FOREIGN KEY (banda_id)   REFERENCES banda_entity(id)   ON DELETE CASCADE,
  CONSTRAINT fk_bi_artista FOREIGN KEY (artista_id) REFERENCES artista_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- LOCAL DO EVENTO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS local_entity (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome        VARCHAR(255),
  capacidade  INT,
  endereco_id BIGINT,
  CONSTRAINT fk_local_endereco FOREIGN KEY (endereco_id)
    REFERENCES endereco_entity(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- TURNE
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS turne_entity (
  id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome_turne             VARCHAR(255),
  data_hora_inicio_turne DATETIME(6),
  data_hora_fim_turne    DATETIME(6),
  ativo                  BOOLEAN NOT NULL DEFAULT TRUE,
  nome_imagem            VARCHAR(255),
  descricao              TEXT,
  banda_id               BIGINT NOT NULL,
  criador_id             BIGINT NOT NULL,
  CONSTRAINT fk_turne_banda   FOREIGN KEY (banda_id)   REFERENCES banda_entity(id),
  CONSTRAINT fk_turne_criador FOREIGN KEY (criador_id) REFERENCES colaborador_entity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- EVENTO  (base da herança JOINED)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS evento_entity (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome_evento VARCHAR(255),
  data_inicio DATETIME(6),
  data_fim    DATETIME(6),
  descricao   TEXT,
  ativo       BOOLEAN NOT NULL DEFAULT TRUE,
  criador_id  BIGINT NOT NULL,
  CONSTRAINT fk_evento_criador FOREIGN KEY (criador_id)
    REFERENCES colaborador_entity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- SHOW  (herança JOINED de evento_entity)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS show_entity (
  id         BIGINT PRIMARY KEY,
  turne_id   BIGINT,
  local_id   BIGINT NOT NULL,
  usuario_id BIGINT NOT NULL,
  CONSTRAINT fk_show_evento      FOREIGN KEY (id)         REFERENCES evento_entity(id)      ON DELETE CASCADE,
  CONSTRAINT fk_show_turne       FOREIGN KEY (turne_id)   REFERENCES turne_entity(id)       ON DELETE SET NULL,
  CONSTRAINT fk_show_local       FOREIGN KEY (local_id)   REFERENCES local_entity(id),
  CONSTRAINT fk_show_responsavel FOREIGN KEY (usuario_id) REFERENCES colaborador_entity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- VIAGEM  (herança JOINED de evento_entity)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS viagem_entity (
  id          BIGINT PRIMARY KEY,
  tipo_viagem VARCHAR(50),
  show_id     BIGINT NOT NULL,
  CONSTRAINT fk_viagem_evento FOREIGN KEY (id)      REFERENCES evento_entity(id) ON DELETE CASCADE,
  CONSTRAINT fk_viagem_show   FOREIGN KEY (show_id) REFERENCES show_entity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- SHOW_BANDA  (ManyToMany: show ↔ banda)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS show_banda (
  show_id  BIGINT NOT NULL,
  banda_id BIGINT NOT NULL,
  PRIMARY KEY (show_id, banda_id),
  CONSTRAINT fk_sb_show  FOREIGN KEY (show_id)  REFERENCES show_entity(id)  ON DELETE CASCADE,
  CONSTRAINT fk_sb_banda FOREIGN KEY (banda_id) REFERENCES banda_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- AGENDA DO EVENTO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS agenda_evento (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  show_id          BIGINT NOT NULL,
  titulo           VARCHAR(255),
  descricao        TEXT,
  data_hora_inicio DATETIME(6),
  data_hora_fim    DATETIME(6),
  ordem            INT,
  tipo             VARCHAR(50),
  origem           VARCHAR(255),
  destino          VARCHAR(255),
  CONSTRAINT fk_agenda_show FOREIGN KEY (show_id)
    REFERENCES show_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- ALOCACAO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS alocacao_entity (
  id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  show_id            BIGINT NOT NULL,
  colaborador_id     BIGINT NOT NULL,
  status             VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
  ativo              BOOLEAN NOT NULL DEFAULT TRUE,
  data_hora_criacao  DATETIME(6),
  data_hora_resposta DATETIME(6),
  CONSTRAINT fk_aloc_show  FOREIGN KEY (show_id)        REFERENCES show_entity(id)        ON DELETE CASCADE,
  CONSTRAINT fk_aloc_colab FOREIGN KEY (colaborador_id) REFERENCES colaborador_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- HOTEL_EVENTO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS hotel_evento (
  id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
  show_id                BIGINT NOT NULL,
  colaborador_id         BIGINT NOT NULL,
  nome_hotel             VARCHAR(255),
  endereco               VARCHAR(255),
  latitude               DOUBLE,
  longitude              DOUBLE,
  distancia_palco_km     DOUBLE,
  distancia_aeroporto_km DOUBLE,
  checkin                DATETIME(6),
  checkout               DATETIME(6),
  CONSTRAINT fk_hotel_show  FOREIGN KEY (show_id)        REFERENCES show_entity(id)        ON DELETE CASCADE,
  CONSTRAINT fk_hotel_colab FOREIGN KEY (colaborador_id) REFERENCES colaborador_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- TRANSPORTE_EVENTO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS transporte_evento (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  show_id        BIGINT NOT NULL,
  colaborador_id BIGINT NOT NULL,
  tipo           VARCHAR(50),
  saida          DATETIME(6),
  destino        VARCHAR(255),
  motorista      VARCHAR(255),
  observacao     TEXT,
  CONSTRAINT fk_transp_show  FOREIGN KEY (show_id)        REFERENCES show_entity(id)        ON DELETE CASCADE,
  CONSTRAINT fk_transp_colab FOREIGN KEY (colaborador_id) REFERENCES colaborador_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- VOO_EVENTO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS voo_evento (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  show_id        BIGINT NOT NULL,
  colaborador_id BIGINT NOT NULL,
  cia_aerea      VARCHAR(255),
  codigo_voo     VARCHAR(50),
  origem         VARCHAR(255),
  destino        VARCHAR(255),
  partida        DATETIME(6),
  chegada        DATETIME(6),
  CONSTRAINT fk_voo_show  FOREIGN KEY (show_id)        REFERENCES show_entity(id)        ON DELETE CASCADE,
  CONSTRAINT fk_voo_colab FOREIGN KEY (colaborador_id) REFERENCES colaborador_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- EXTRA_EVENTO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS extra_evento_entity (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  show_id  BIGINT NOT NULL UNIQUE,
  obs      TEXT,
  contatos TEXT,
  CONSTRAINT fk_extra_show FOREIGN KEY (show_id)
    REFERENCES show_entity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- NOTIFICACAO
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS notificacao_entity (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  colaborador_id BIGINT NOT NULL,
  alocacao_id    BIGINT,
  mensagem       VARCHAR(500) NOT NULL,
  tipo           VARCHAR(50)  NOT NULL,
  lida           BOOLEAN NOT NULL DEFAULT FALSE,
  data_criacao   DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT fk_notif_colab FOREIGN KEY (colaborador_id) REFERENCES colaborador_entity(id) ON DELETE CASCADE,
  CONSTRAINT fk_notif_aloc  FOREIGN KEY (alocacao_id)    REFERENCES alocacao_entity(id)    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- AUDIT_LOG
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS audit_log (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  action     VARCHAR(255)  NOT NULL,
  entity     VARCHAR(255)  NOT NULL,
  entity_id  VARCHAR(255),
  usuario_id BIGINT,
  ip_address VARCHAR(100)  NOT NULL,
  timestamp  DATETIME(6)   NOT NULL,
  details    VARCHAR(4000),
  CONSTRAINT fk_audit_usuario FOREIGN KEY (usuario_id)
    REFERENCES colaborador_entity(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
