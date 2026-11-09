CREATE TABLE medicoes (
    id                BIGSERIAL PRIMARY KEY,
    idoso_id          BIGINT NOT NULL REFERENCES idosos (id),
    batimentos        INTEGER NOT NULL,
    data_hora_medicao TIMESTAMP NOT NULL,
    em_exercicio      BOOLEAN NOT NULL
);
