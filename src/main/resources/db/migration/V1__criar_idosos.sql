CREATE TABLE idosos (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    limite_minimo   INTEGER NOT NULL,
    limite_maximo   INTEGER NOT NULL
);
