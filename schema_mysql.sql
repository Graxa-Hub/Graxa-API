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
-- ARTISTA  (UsuarioEntity @MappedSuperclass)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS artista_entity (
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  ativo     BOOLEAN NOT NULL DEFAULT TRUE,
  nome      VARCHAR(255),
  cpf       VARCHAR(14) UNIQUE,
  foto_nome VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
-- REPRESENTANTE  (UsuarioEntity @MappedSuperclass)
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
-- COLABORADOR  (funcionários / equipe de turnê)
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
  CONSTRAINT fk_banda_repr   FOREIGN KEY (representante_id) REFERENCES representante_entity(id),
  CONSTRAINT fk_banda_criador FOREIGN KEY (criador_id)      REFERENCES colaborador_entity(id)
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
-- ALOCACAO  (colaborador alocado num show)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS alocacao_entity (
  id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  show_id            BIGINT NOT NULL,
  colaborador_id     BIGINT NOT NULL,
  status             VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
  ativo              BOOLEAN NOT NULL DEFAULT TRUE,
  data_hora_criacao  DATETIME(6),
  data_hora_resposta DATETIME(6),
  CONSTRAINT fk_aloc_show   FOREIGN KEY (show_id)        REFERENCES show_entity(id)        ON DELETE CASCADE,
  CONSTRAINT fk_aloc_colab  FOREIGN KEY (colaborador_id) REFERENCES colaborador_entity(id) ON DELETE CASCADE
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
-- EXTRA_EVENTO  (observações e contatos do show)
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
  CONSTRAINT fk_notif_colab  FOREIGN KEY (colaborador_id) REFERENCES colaborador_entity(id) ON DELETE CASCADE,
  CONSTRAINT fk_notif_aloc   FOREIGN KEY (alocacao_id)    REFERENCES alocacao_entity(id)    ON DELETE SET NULL
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

-- ===========================================================
-- DADOS INICIAIS (seed)
-- ===========================================================

INSERT INTO colaborador_entity (ativo, cpf, foto_nome, nome, data_nascimento, tipo_usuario, endereco_id) VALUES
(TRUE, '64663646068', NULL, 'Carlos Menezes',    '1985-03-12', 'PRODUTOR_ESTRADA', NULL),
(TRUE, '23840363033', NULL, 'Fernanda Rocha',    '1990-07-25', 'PRODUTOR_ESTRADA', NULL),
(TRUE, '53987542082', NULL, 'João Almeida',      '1988-11-02', 'PRE_PRODUTOR',     NULL),
(TRUE, '05356247009', NULL, 'Mariana Silva',     '1992-04-18', 'PRE_PRODUTOR',     NULL),
(TRUE, '70686235007', NULL, 'Ricardo Santos',    '1980-09-10', 'PRODUTOR',         NULL),
(TRUE, '13420568000', NULL, 'Patrícia Gomes',    '1987-01-22', 'PRODUTOR',         NULL),
(TRUE, '42895137090', NULL, 'André Costa',       '1991-05-14', 'TECNICO_SOM',      NULL),
(TRUE, '89632541034', NULL, 'Luciana Pires',     '1989-08-30', 'TECNICO_SOM',      NULL),
(TRUE, '35914826075', NULL, 'Marcelo Oliveira',  '1984-02-19', 'TECNICO_LUZ',      NULL),
(TRUE, '61248593012', NULL, 'Tatiane Ribeiro',   '1993-06-07', 'TECNICO_LUZ',      NULL),
(TRUE, '92581436089', NULL, 'Rodrigo Mendes',    '1986-09-21', 'TECNICO_MONITOR',  NULL),
(TRUE, '25814963027', NULL, 'Aline Carvalho',    '1994-12-11', 'TECNICO_MONITOR',  NULL),
(TRUE, '58149632041', NULL, 'Felipe Duarte',     '1983-03-05', 'TECNICO_PA',       NULL),
(TRUE, '89432051068', NULL, 'Carolina Nunes',    '1995-08-17', 'TECNICO_PA',       NULL),
(TRUE, '14958236096', NULL, 'Daniel Moreira',    '1982-04-22', 'ROAD',             NULL),
(TRUE, '49582361053', NULL, 'Renata Rodrigues',  '1996-10-09', 'ROAD',             NULL),
(TRUE, '72361958000', NULL, 'Gustavo Barros',    '1987-06-13', 'ARTISTA',          NULL),
(TRUE, '06195823028', NULL, 'Natália Teixeira',  '1992-11-27', 'ARTISTA',          NULL),
(TRUE, '39582164085', NULL, 'Paulo Guitarra',    '1985-04-10', 'GUITARRISTA',      NULL),
(TRUE, '64219583019', NULL, 'Marina Guitarra',   '1990-09-21', 'GUITARRISTA',      NULL),
(TRUE, '95832164076', NULL, 'Roberto Baixo',     '1982-02-14', 'BAIXISTA',         NULL),
(TRUE, '21649583030', NULL, 'Juliana Baixo',     '1993-07-30', 'BAIXISTA',         NULL),
(TRUE, '58321649057', NULL, 'Thiago Batera',     '1987-05-19', 'BATERISTA',        NULL),
(TRUE, '83216495090', NULL, 'Camila Batera',     '1991-11-03', 'BATERISTA',        NULL),
(TRUE, '16495832046', NULL, 'Eduardo Teclado',   '1984-08-12', 'TECLADISTA',       NULL),
(TRUE, '49583216003', NULL, 'Larissa Teclado',   '1992-01-27', 'TECLADISTA',       NULL),
(TRUE, '73216495060', NULL, 'Marcelo Violão',    '1986-06-15', 'VIOLONISTA',       NULL),
(TRUE, '04958321087', NULL, 'Tatiane Violão',    '1994-03-08', 'VIOLONISTA',       NULL),
(TRUE, '32164958014', NULL, 'Felipe Vocal',      '1986-02-15', 'VOCALISTA',        NULL),
(TRUE, '64958321071', NULL, 'Carla Vocal',       '1991-09-28', 'VOCALISTA',        NULL),
(TRUE, '95832164025', NULL, 'Rafael Sax',        '1984-07-12', 'SAXOFONISTA',      NULL),
(TRUE, '21649583081', NULL, 'Juliana Sax',       '1993-03-05', 'SAXOFONISTA',      NULL),
(TRUE, '58321649006', NULL, 'Bruno Trompete',    '1982-11-20', 'TROMPETISTA',      NULL),
(TRUE, '83216495030', NULL, 'Tatiane Trompete',  '1990-04-14', 'TROMPETISTA',      NULL),
(TRUE, '16495832097', NULL, 'Marcelo Trombone',  '1985-06-09', 'TROMBONISTA',      NULL),
(TRUE, '49583216054', NULL, 'Renata Trombone',   '1992-12-01', 'TROMBONISTA',      NULL),
(TRUE, '73216495010', NULL, 'Diego Percussão',   '1987-08-17', 'PERCUSSIONISTA',   NULL),
(TRUE, '04958321036', NULL, 'Vanessa Percussão', '1994-02-22', 'PERCUSSIONISTA',   NULL),
(TRUE, '32164958065', NULL, 'Hugo Violino',      '1983-01-11', 'VIOLINISTA',       NULL),
(TRUE, '64958321020', NULL, 'Juliana Violino',   '1995-09-03', 'VIOLINISTA',       NULL),
(TRUE, '95832164084', NULL, 'Leonardo Cello',    '1984-03-22', 'CELISTA',          NULL),
(TRUE, '21649583049', NULL, 'Mariana Cello',     '1992-07-19', 'CELISTA',          NULL),
(TRUE, '58321649065', NULL, 'Roberto Contrabaixo','1981-05-11','CONTRABAIXISTA',   NULL),
(TRUE, '83216495006', NULL, 'Tatiane Contrabaixo','1993-09-25','CONTRABAIXISTA',   NULL),
(TRUE, '16495832003', NULL, 'Camila Flauta',     '1987-08-14', 'FLAUTISTA',        NULL),
(TRUE, '49583216062', NULL, 'Pedro Flauta',      '1994-02-03', 'FLAUTISTA',        NULL),
(TRUE, '73216495079', NULL, 'Daniel Clarinete',  '1985-10-09', 'CLARINETISTA',     NULL),
(TRUE, '04958321095', NULL, 'Larissa Clarinete', '1991-12-27', 'CLARINETISTA',     NULL),
(TRUE, '32164958022', NULL, 'Marcelo Oboé',      '1983-06-18', 'OBOISTA',          NULL),
(TRUE, '64958321080', NULL, 'Renata Oboé',       '1992-01-29', 'OBOISTA',          NULL),
(TRUE, '95832164033', NULL, 'Hugo Fagote',       '1986-04-07', 'FAGOTISTA',        NULL),
(TRUE, '21649583090', NULL, 'Juliana Fagote',    '1994-09-12', 'FAGOTISTA',        NULL),
(TRUE, '58321649014', NULL, 'Beatriz Harpa',     '1988-11-23', 'HARPISTA',         NULL),
(TRUE, '83216495049', NULL, 'Lucas Harpa',       '1995-05-16', 'HARPISTA',         NULL),
(TRUE, '16495832054', NULL, 'Eduardo Piano',     '1982-02-20', 'PIANISTA',         NULL),
(TRUE, '49583216011', NULL, 'Larissa Piano',     '1990-07-08', 'PIANISTA',         NULL),
(TRUE, '73216495028', NULL, 'Mateus Acordeon',   '1985-05-10', 'ACORDEONISTA',     NULL),
(TRUE, '04958321044', NULL, 'Carla Acordeon',    '1992-09-18', 'ACORDEONISTA',     NULL),
(TRUE, '32164958073', NULL, 'Rafael Gaita',      '1983-07-12', 'GAITEIRO',         NULL),
(TRUE, '64958321038', NULL, 'Juliana Gaita',     '1994-03-05', 'GAITEIRO',         NULL),
(TRUE, '95832164092', NULL, 'Bruno Bandolim',    '1982-11-20', 'BANDOLINISTA',     NULL),
(TRUE, '21649583057', NULL, 'Tatiane Bandolim',  '1990-04-14', 'BANDOLINISTA',     NULL),
(TRUE, '58321649073', NULL, 'Marcelo Cavaquinho','1985-06-09', 'CAVAQUINISTA',     NULL),
(TRUE, '83216495014', NULL, 'Renata Cavaquinho', '1992-12-01', 'CAVAQUINISTA',     NULL),
(TRUE, '16495832011', NULL, 'Diego Ukulele',     '1987-08-17', 'UKULELISTA',       NULL),
(TRUE, '49583216070', NULL, 'Vanessa Ukulele',   '1994-02-22', 'UKULELISTA',       NULL),
(TRUE, '73216495087', NULL, 'Paulo Base',        '1985-05-10', 'GUITARRA_RITMICA', NULL),
(TRUE, '04958321001', NULL, 'Carla Base',        '1992-09-18', 'GUITARRA_RITMICA', NULL),
(TRUE, '32164958030', NULL, 'Rafael Solo',       '1983-07-12', 'GUITARRA_SOLO',    NULL),
(TRUE, '64958321098', NULL, 'Juliana Solo',      '1994-03-05', 'GUITARRA_SOLO',    NULL),
(TRUE, '95832164041', NULL, 'Bruno DJ',          '1982-11-20', 'DJ',               NULL),
(TRUE, '21649583006', NULL, 'Tatiane DJ',        '1990-04-14', 'DJ',               NULL),
(TRUE, '58321649022', NULL, 'Marcelo MC',        '1985-06-09', 'MC',               NULL),
(TRUE, '83216495057', NULL, 'Renata MC',         '1992-12-01', 'MC',               NULL),
(TRUE, '16495832062', NULL, 'Diego Maestro',     '1987-08-17', 'MAESTRO',          NULL),
(TRUE, '49583216020', NULL, 'Vanessa Maestro',   '1994-02-22', 'MAESTRO',          NULL),
(TRUE, '73216495037', NULL, 'Paulo Regente',     '1985-05-10', 'REGENTE',          NULL),
(TRUE, '04958321052', NULL, 'Carla Regente',     '1992-09-18', 'REGENTE',          NULL),
(TRUE, '32164958081', NULL, 'Rafael Compositor', '1983-07-12', 'COMPOSITOR',       NULL),
(TRUE, '64958321046', NULL, 'Juliana Compositor','1994-03-05', 'COMPOSITOR',       NULL),
(TRUE, '95832164009', NULL, 'Bruno Arranjo',     '1982-11-20', 'ARRANJADOR',       NULL),
(TRUE, '21649583065', NULL, 'Tatiane Arranjo',   '1990-04-14', 'ARRANJADOR',       NULL),
(TRUE, '58321649081', NULL, 'Marcelo Prod. Musical','1985-06-09','PRODUTOR_MUSICAL',NULL),
(TRUE, '83216495022', NULL, 'Renata Prod. Musical', '1992-12-01','PRODUTOR_MUSICAL',NULL),
(TRUE, '16495832020', NULL, 'Diego Eng. Som',    '1987-08-17', 'ENGENHEIRO_SOM',   NULL),
(TRUE, '49583216089', NULL, 'Vanessa Eng. Som',  '1994-02-22', 'ENGENHEIRO_SOM',   NULL),
(TRUE, '73216495096', NULL, 'Hugo Letrista',     '1983-01-11', 'LETRISTA',         NULL),
(TRUE, '04958321010', NULL, 'Juliana Letrista',  '1995-09-03', 'LETRISTA',         NULL),
(TRUE, '32164958049', NULL, 'Felipe Arranjo Vocal','1986-02-15','BACKING_VOCAL',   NULL),
(TRUE, '47060193898', NULL, 'Gabriel Sousa',     '2000-05-15', 'PRODUTOR',         NULL);

INSERT INTO credenciais_usuario_entity
  (usuario_id, email, nome_usuario, senha, data_hora_ultimo_acesso, codigo_recuperacao, codigo_expira_em)
VALUES
  (1,  'carlos.menezes@email.com',    'carlosmenezes',       '$2a$10$senhaHash1',  CURRENT_TIMESTAMP, NULL, NULL),
  (2,  'fernanda.rocha@email.com',    'fernandarocha',       '$2a$10$senhaHash2',  CURRENT_TIMESTAMP, NULL, NULL),
  (3,  'joao.almeida@email.com',      'joaoalmeida',         '$2a$10$senhaHash3',  CURRENT_TIMESTAMP, NULL, NULL),
  (4,  'mariana.silva@email.com',     'marianasilva',        '$2a$10$senhaHash4',  CURRENT_TIMESTAMP, NULL, NULL),
  (5,  'ricardo.santos@email.com',    'ricardosantos',       '$2a$10$senhaHash5',  CURRENT_TIMESTAMP, NULL, NULL),
  (6,  'patricia.gomes@email.com',    'patriciagomes',       '$2a$10$senhaHash6',  CURRENT_TIMESTAMP, NULL, NULL),
  (7,  'andre.costa@email.com',       'andrecosta',          '$2a$10$senhaHash7',  CURRENT_TIMESTAMP, NULL, NULL),
  (8,  'luciana.pires@email.com',     'lucianapires',        '$2a$10$senhaHash8',  CURRENT_TIMESTAMP, NULL, NULL),
  (9,  'marcelo.oliveira@email.com',  'marcelooliveira',     '$2a$10$senhaHash9',  CURRENT_TIMESTAMP, NULL, NULL),
  (10, 'tatiane.ribeiro@email.com',   'tatianeribeiro',      '$2a$10$senhaHash10', CURRENT_TIMESTAMP, NULL, NULL),
  (11, 'rodrigo.mendes@email.com',    'rodrigomendes',       '$2a$10$senhaHash11', CURRENT_TIMESTAMP, NULL, NULL),
  (12, 'aline.carvalho@email.com',    'alinecarvalho',       '$2a$10$senhaHash12', CURRENT_TIMESTAMP, NULL, NULL),
  (13, 'felipe.duarte@email.com',     'felipeduarte',        '$2a$10$senhaHash13', CURRENT_TIMESTAMP, NULL, NULL),
  (14, 'carolina.nunes@email.com',    'carolinanunes',       '$2a$10$senhaHash14', CURRENT_TIMESTAMP, NULL, NULL),
  (15, 'daniel.moreira@email.com',    'danielmoreira',       '$2a$10$senhaHash15', CURRENT_TIMESTAMP, NULL, NULL),
  (16, 'renata.rodrigues@email.com',  'renatarodrigues',     '$2a$10$senhaHash16', CURRENT_TIMESTAMP, NULL, NULL),
  (17, 'gustavo.barros@email.com',    'gustavobarros',       '$2a$10$senhaHash17', CURRENT_TIMESTAMP, NULL, NULL),
  (18, 'natalia.teixeira@email.com',  'nataliateixeira',     '$2a$10$senhaHash18', CURRENT_TIMESTAMP, NULL, NULL),
  (19, 'paulo.guitarra@email.com',    'pauloguitarra',       '$2a$10$senhaHash19', CURRENT_TIMESTAMP, NULL, NULL),
  (20, 'marina.guitarra@email.com',   'marinaguitarra',      '$2a$10$senhaHash20', CURRENT_TIMESTAMP, NULL, NULL),
  (21, 'roberto.baixo@email.com',     'robertobaixo',        '$2a$10$senhaHash21', CURRENT_TIMESTAMP, NULL, NULL),
  (22, 'juliana.baixo@email.com',     'julianabaixo',        '$2a$10$senhaHash22', CURRENT_TIMESTAMP, NULL, NULL),
  (23, 'thiago.batera@email.com',     'thiagobatera',        '$2a$10$senhaHash23', CURRENT_TIMESTAMP, NULL, NULL),
  (24, 'camila.batera@email.com',     'camilabatera',        '$2a$10$senhaHash24', CURRENT_TIMESTAMP, NULL, NULL),
  (25, 'eduardo.teclado@email.com',   'eduardoteclado',      '$2a$10$senhaHash25', CURRENT_TIMESTAMP, NULL, NULL),
  (26, 'larissa.teclado@email.com',   'larissateclado',      '$2a$10$senhaHash26', CURRENT_TIMESTAMP, NULL, NULL),
  (27, 'marcelo.viola@email.com',     'marceloviola',        '$2a$10$senhaHash27', CURRENT_TIMESTAMP, NULL, NULL),
  (28, 'tatiane.viola@email.com',     'tatianeviola',        '$2a$10$senhaHash28', CURRENT_TIMESTAMP, NULL, NULL),
  (29, 'felipe.vocal@email.com',      'felipevocal',         '$2a$10$senhaHash29', CURRENT_TIMESTAMP, NULL, NULL),
  (30, 'carla.vocal@email.com',       'carlavocal',          '$2a$10$senhaHash30', CURRENT_TIMESTAMP, NULL, NULL),
  (31, 'rafael.sax@email.com',        'rafaelsax',           '$2a$10$senhaHash31', CURRENT_TIMESTAMP, NULL, NULL),
  (32, 'juliana.sax@email.com',       'julianasax',          '$2a$10$senhaHash32', CURRENT_TIMESTAMP, NULL, NULL),
  (33, 'bruno.trompete@email.com',    'brunotrompete',       '$2a$10$senhaHash33', CURRENT_TIMESTAMP, NULL, NULL),
  (34, 'tatiane.trompete@email.com',  'tatianetrompete',     '$2a$10$senhaHash34', CURRENT_TIMESTAMP, NULL, NULL),
  (35, 'marcelo.trombone@email.com',  'marcelotrombone',     '$2a$10$senhaHash35', CURRENT_TIMESTAMP, NULL, NULL),
  (36, 'renata.trombone@email.com',   'renatatrombone',      '$2a$10$senhaHash36', CURRENT_TIMESTAMP, NULL, NULL),
  (37, 'diego.percussao@email.com',   'diegopercussao',      '$2a$10$senhaHash37', CURRENT_TIMESTAMP, NULL, NULL),
  (38, 'vanessa.percussao@email.com', 'vanessapercussao',    '$2a$10$senhaHash38', CURRENT_TIMESTAMP, NULL, NULL),
  (39, 'hugo.violino@email.com',      'hugoviolino',         '$2a$10$senhaHash39', CURRENT_TIMESTAMP, NULL, NULL),
  (40, 'juliana.violino@email.com',   'julianaviolino',      '$2a$10$senhaHash40', CURRENT_TIMESTAMP, NULL, NULL),
  (41, 'leonardo.cello@email.com',    'leonardocello',       '$2a$10$senhaHash41', CURRENT_TIMESTAMP, NULL, NULL),
  (42, 'mariana.cello@email.com',     'marianacello',        '$2a$10$senhaHash42', CURRENT_TIMESTAMP, NULL, NULL),
  (43, 'roberto.contrabaixo@email.com','robertocontrabaixo', '$2a$10$senhaHash43', CURRENT_TIMESTAMP, NULL, NULL),
  (44, 'tatiane.contrabaixo@email.com','tatianecontrabaixo', '$2a$10$senhaHash44', CURRENT_TIMESTAMP, NULL, NULL),
  (45, 'camila.flauta@email.com',     'camilaflauta',        '$2a$10$senhaHash45', CURRENT_TIMESTAMP, NULL, NULL),
  (46, 'pedro.flauta@email.com',      'pedroflauta',         '$2a$10$senhaHash46', CURRENT_TIMESTAMP, NULL, NULL),
  (47, 'daniel.clarinete@email.com',  'danielclarinete',     '$2a$10$senhaHash47', CURRENT_TIMESTAMP, NULL, NULL),
  (48, 'larissa.clarinete@email.com', 'larissaclarinete',    '$2a$10$senhaHash48', CURRENT_TIMESTAMP, NULL, NULL),
  (49, 'marcelo.oboe@email.com',      'marcelooboe',         '$2a$10$senhaHash49', CURRENT_TIMESTAMP, NULL, NULL),
  (50, 'renata.oboe@email.com',       'renataoboe',          '$2a$10$senhaHash50', CURRENT_TIMESTAMP, NULL, NULL),
  (51, 'hugo.fagote@email.com',       'hugofagote',          '$2a$10$senhaHash51', CURRENT_TIMESTAMP, NULL, NULL),
  (52, 'juliana.fagote@email.com',    'julianafagote',       '$2a$10$senhaHash52', CURRENT_TIMESTAMP, NULL, NULL),
  (53, 'beatriz.harpa@email.com',     'beatrizharpa',        '$2a$10$senhaHash53', CURRENT_TIMESTAMP, NULL, NULL),
  (54, 'lucas.harpa@email.com',       'lucasharpa',          '$2a$10$senhaHash54', CURRENT_TIMESTAMP, NULL, NULL),
  (55, 'eduardo.piano@email.com',     'eduardopiano',        '$2a$10$senhaHash55', CURRENT_TIMESTAMP, NULL, NULL),
  (56, 'larissa.piano@email.com',     'larissapiano',        '$2a$10$senhaHash56', CURRENT_TIMESTAMP, NULL, NULL),
  (57, 'mateus.acordeon@email.com',   'mateusacordeon',      '$2a$10$senhaHash57', CURRENT_TIMESTAMP, NULL, NULL),
  (58, 'carla.acordeon@email.com',    'carlaacordeon',       '$2a$10$senhaHash58', CURRENT_TIMESTAMP, NULL, NULL),
  (59, 'rafael.gaita@email.com',      'rafaelgaita',         '$2a$10$senhaHash59', CURRENT_TIMESTAMP, NULL, NULL),
  (60, 'juliana.gaita@email.com',     'julianagaita',        '$2a$10$senhaHash60', CURRENT_TIMESTAMP, NULL, NULL),
  (61, 'bruno.bandolim@email.com',    'brunobandolim',       '$2a$10$senhaHash61', CURRENT_TIMESTAMP, NULL, NULL),
  (62, 'tatiane.bandolim@email.com',  'tatianebandolim',     '$2a$10$senhaHash62', CURRENT_TIMESTAMP, NULL, NULL),
  (63, 'marcelo.cavaquinho@email.com','marcelocavaquinho',   '$2a$10$senhaHash63', CURRENT_TIMESTAMP, NULL, NULL),
  (64, 'renata.cavaquinho@email.com', 'renatacavaquinho',    '$2a$10$senhaHash64', CURRENT_TIMESTAMP, NULL, NULL),
  (65, 'diego.ukulele@email.com',     'diegoukulele',        '$2a$10$senhaHash65', CURRENT_TIMESTAMP, NULL, NULL),
  (66, 'vanessa.ukulele@email.com',   'vanessaukulele',      '$2a$10$senhaHash66', CURRENT_TIMESTAMP, NULL, NULL),
  (67, 'paulo.base@email.com',        'paulobase',           '$2a$10$senhaHash67', CURRENT_TIMESTAMP, NULL, NULL),
  (68, 'carla.base@email.com',        'carlabase',           '$2a$10$senhaHash68', CURRENT_TIMESTAMP, NULL, NULL),
  (69, 'rafael.solo@email.com',       'rafaelsolo',          '$2a$10$senhaHash69', CURRENT_TIMESTAMP, NULL, NULL),
  (70, 'juliana.solo@email.com',      'julianasolo',         '$2a$10$senhaHash70', CURRENT_TIMESTAMP, NULL, NULL),
  (71, 'bruno.dj@email.com',          'brunodj',             '$2a$10$senhaHash71', CURRENT_TIMESTAMP, NULL, NULL),
  (72, 'tatiane.dj@email.com',        'tatianedj',           '$2a$10$senhaHash72', CURRENT_TIMESTAMP, NULL, NULL),
  (73, 'marcelo.mc@email.com',        'marcelomc',           '$2a$10$senhaHash73', CURRENT_TIMESTAMP, NULL, NULL),
  (74, 'renata.mc@email.com',         'renatamc',            '$2a$10$senhaHash74', CURRENT_TIMESTAMP, NULL, NULL),
  (75, 'diego.maestro@email.com',     'diegomaestro',        '$2a$10$senhaHash75', CURRENT_TIMESTAMP, NULL, NULL),
  (76, 'vanessa.maestro@email.com',   'vanessamaestro',      '$2a$10$senhaHash76', CURRENT_TIMESTAMP, NULL, NULL),
  (77, 'paulo.regente@email.com',     'pauloregente',        '$2a$10$senhaHash77', CURRENT_TIMESTAMP, NULL, NULL),
  (78, 'carla.regente@email.com',     'carlaregente',        '$2a$10$senhaHash78', CURRENT_TIMESTAMP, NULL, NULL),
  (79, 'rafael.compositor@email.com', 'rafaelcompositor',    '$2a$10$senhaHash79', CURRENT_TIMESTAMP, NULL, NULL),
  (80, 'juliana.compositor@email.com','julianacompositor',   '$2a$10$senhaHash80', CURRENT_TIMESTAMP, NULL, NULL),
  (81, 'bruno.arranjo@email.com',     'brunoarranjo',        '$2a$10$senhaHash81', CURRENT_TIMESTAMP, NULL, NULL),
  (82, 'tatiane.arranjo@email.com',   'tatianearranjo',      '$2a$10$senhaHash82', CURRENT_TIMESTAMP, NULL, NULL),
  (83, 'marcelo.produtor@email.com',  'marceloprodutor',     '$2a$10$senhaHash83', CURRENT_TIMESTAMP, NULL, NULL),
  (84, 'renata.produtor@email.com',   'renataprodutor',      '$2a$10$senhaHash84', CURRENT_TIMESTAMP, NULL, NULL),
  (85, 'diego.engenheiro@email.com',  'diegoengenheiro',     '$2a$10$senhaHash85', CURRENT_TIMESTAMP, NULL, NULL),
  (86, 'vanessa.engenheira@email.com','vanessaengenheira',   '$2a$10$senhaHash86', CURRENT_TIMESTAMP, NULL, NULL),
  (87, 'hugo.letrista@email.com',     'hugoletrista',        '$2a$10$senhaHash87', CURRENT_TIMESTAMP, NULL, NULL),
  (88, 'juliana.letrista@email.com',  'julianaletrista',     '$2a$10$senhaHash88', CURRENT_TIMESTAMP, NULL, NULL),
  (89, 'felipe.arranjo@email.com',    'felipearranjo',       '$2a$10$senhaHash89', CURRENT_TIMESTAMP, NULL, NULL),
  (90, 'gabriel.sousa@example.com',   'gabrielsousa',        '$2a$10$G.klFRRb/ilL2dBBTpw4..o3D0sGN0qppM.fsNLADhgcd82jnRwTi', '2025-12-01 11:46:16.782137', NULL, NULL);
