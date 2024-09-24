-- PAGINAÇÃO
SELECT * FROM employee;

EXPLAIN ANALYZE SELECT * FROM employee LIMIT 20;

SELECT * FROM employee ORDER BY id DESC;

-- CRIPTOGRAFIA DE DADOS

CREATE EXTENSION PGCRYPTO;

CREATE TABLE OB_USERS(
                         ID SERIAL PRIMARY KEY,
                         EMAIL VARCHAR NOT NULL UNIQUE,
                         PASSW VARCHAR NOT NULL
)

SELECT * FROM OB_USERS;

-- Salva registros sem criptografia dos dados
-- INSERT INTO OB_USERS(EMAIL, PASSW) VALUES
--                                        ('user1@company.com', 'admin'),
--                                        ('user2@company.com', '123');

-- Salva registros com criptografia dos dados, utilizando a function pgp_sym_encrypt
INSERT INTO OB_USERS(EMAIL, PASSW) VALUES
                                       ('user3@company.com', pgp_sym_encrypt('admin', 'secret')),
                                       ('user4@company.com', pgp_sym_encrypt('123', 'secret'));

-- OBS: SE A COLUNA CRIPTOGRAFADA TIVER DADOS NÃO CRIPTOGRAFADOS IRÁ GERAR
-- O SEGUINTE ERROR: Wrong key or corrupt data. SQL state: 39000

SELECT ID, EMAIL, pgp_sym_decrypt(PASSW::BYTEA, 'secret') AS PASSW
FROM OB_USERS;

-- PARA CRIPTOGRAFAR: pgp_sym_encrypt
-- PARA DESCRIPTOGRAFAR: pgp_sym_decrypt(arg::bytea, 'secret')


