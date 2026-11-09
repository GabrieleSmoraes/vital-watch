CREATE TABLE alertas (
    id                     BIGSERIAL PRIMARY KEY,
    medicao_id             BIGINT NOT NULL UNIQUE REFERENCES medicoes (id),
    tipo                   VARCHAR(10) NOT NULL,
    limite                 INTEGER NOT NULL,
    providencia            VARCHAR(255),
    data_hora_encerramento TIMESTAMP
);
