# Contrato da API

## Convenções

| Item | Valor |
|---|---|
| Endereço base | `http://localhost:8080` |
| Formato | JSON, na requisição e na resposta |
| Datas | `yyyy-MM-dd`; data com hora: `yyyy-MM-ddTHH:mm:ss` |
| Erros | Sempre no formato `ErroDTO` (ver [Erros](#erros)) |
| Documentação viva | `http://localhost:8080/swagger-ui.html` |

## Rotas

Coluna **Funciona**: `sim` para as rotas que já respondem nesta etapa: as três da API simples (idosos) e as que as regras de negócio usam.

### Idosos

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `POST` | `/idosos` | Cadastra um idoso. Sem limites, usa 60 e 100 (R1) | `IdosoRequestDTO` | `IdosoResponseDTO` | `201` | `400` | sim |
| `GET` | `/idosos` | Lista os idosos | Nenhuma | lista de `IdosoResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/idosos/{id}` | Busca um idoso | Nenhuma | `IdosoResponseDTO` | `200` | `404` | sim |

### Medições

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `POST` | `/medicoes` | Registra uma medição e gera o alerta se ela sair dos limites | `MedicaoRequestDTO` | `MedicaoResponseDTO` | `201` | `400`, `404` | sim |
| `GET` | `/medicoes` | Lista as medições | Nenhuma | lista de `MedicaoResponseDTO` | `200` | Nenhum | não |
| `GET` | `/medicoes/{id}` | Busca uma medição | Nenhuma | `MedicaoResponseDTO` | `200` | `404` | não |

### Alertas

Não existe `POST /alertas`: quem cria o alerta é a API, quando a medição sai dos limites.

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `GET` | `/alertas` | Lista os alertas | Nenhuma | lista de `AlertaResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/alertas/{id}` | Busca um alerta | Nenhuma | `AlertaResponseDTO` | `200` | `404` | sim |

### Rotas que não são CRUD

As que nascem das regras de negócio do README. Todas funcionam.

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|---|---|---|---|---|---|
| `POST` | `/medicoes` | Registra a medição; fora dos limites, grava um alerta junto | R1, R2 e R3 | `201` | `400`, `404` |
| `PUT` | `/alertas/{id}/encerrar` | Encerra o alerta com o que a família fez. Entrada: `AlertaRequestDTO`; saída: `AlertaResponseDTO` | R4 | `200` | `400`, `404` |

## DTOs

O que entra não é igual ao que sai: o cliente não envia `id` nem campos que a API decide.

| DTO | Direção | Campos | Validações |
|---|---|---|---|
| `IdosoRequestDTO` | entrada | `nome`, `dataNascimento`, `limiteMinimo`, `limiteMaximo` | `nome` obrigatório, até 100 caracteres; `dataNascimento` obrigatória e no passado; limites opcionais, maiores que zero |
| `IdosoResponseDTO` | saída | `id`, `nome`, `dataNascimento`, `limiteMinimo`, `limiteMaximo` | Nenhuma |
| `MedicaoRequestDTO` | entrada | `idosoId`, `batimentos`, `dataHoraMedicao`, `emExercicio` | todos obrigatórios; `batimentos` maior que zero; `dataHoraMedicao` não pode estar no futuro |
| `MedicaoResponseDTO` | saída | `id`, `idoso`, `batimentos`, `dataHoraMedicao`, `emExercicio`, `gerouAlerta` | Nenhuma. `gerouAlerta` é decidido pelas regras R1 e R2 |
| `AlertaRequestDTO` | entrada | `providencia` | obrigatória, até 255 caracteres (R4) |
| `AlertaResponseDTO` | saída | `id`, `medicao`, `tipo`, `limite`, `encerrado`, `providencia`, `dataHoraEncerramento` | Nenhuma. `tipo` e `limite` são decididos pelas regras R1 e R2 |

## Exemplos

### `POST /idosos` (funciona)

Requisição, sem os limites:

```json
{
  "nome": "Maria Silva",
  "dataNascimento": "1950-03-15"
}
```

Resposta, `201 Created`. A API usou os limites padrão da R1:

```json
{
  "id": 1,
  "nome": "Maria Silva",
  "dataNascimento": "1950-03-15",
  "limiteMinimo": 60,
  "limiteMaximo": 100
}
```

### `POST /medicoes` (funciona)

Entra só o id do idoso; ele volta completo.

Requisição:

```json
{
  "idosoId": 1,
  "batimentos": 130,
  "dataHoraMedicao": "2026-11-09T07:10:00",
  "emExercicio": false
}
```

Resposta, `201 Created`. 130 passou do limite de 100, então gerou alerta (R1):

```json
{
  "id": 2,
  "idoso": { "id": 1, "nome": "Maria Silva", "dataNascimento": "1950-03-15", "limiteMinimo": 60, "limiteMaximo": 100 },
  "batimentos": 130,
  "dataHoraMedicao": "2026-11-09T07:10:00",
  "emExercicio": false,
  "gerouAlerta": true
}
```

Com `"emExercicio": true`, os mesmos 130 não geram alerta: a Maria tem 76 anos e, em exercício, o limite alto dela é 220 − 76 = 144 (R2).

### `PUT /alertas/1/encerrar` (funciona)

Requisição:

```json
{
  "providencia": "Liguei para ela, estava bem e tinha subido a escada"
}
```

Resposta, `200 OK`:

```json
{
  "id": 1,
  "medicao": {
    "id": 2,
    "idoso": { "id": 1, "nome": "Maria Silva", "dataNascimento": "1950-03-15", "limiteMinimo": 60, "limiteMaximo": 100 },
    "batimentos": 130,
    "dataHoraMedicao": "2026-11-09T07:10:00",
    "emExercicio": false,
    "gerouAlerta": true
  },
  "tipo": "ALTO",
  "limite": 100,
  "encerrado": true,
  "providencia": "Liguei para ela, estava bem e tinha subido a escada",
  "dataHoraEncerramento": "2026-11-09T09:16:14"
}
```

## Erros

Todo erro sai no mesmo formato. Este é o `404` de `GET /idosos/999`:

```json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Idoso não encontrado: 999",
  "caminho": "/idosos/999",
  "timestamp": "2026-11-09T09:16:07",
  "campos": []
}
```

No `400` de validação, a lista `campos` traz um item por campo inválido: `{"campo": "nome", "mensagem": "Nome é obrigatório"}`.

Quando uma regra de negócio barra o pedido, o `400` vem com a regra na `mensagem` e `campos` vazio. Este é o de `POST /medicoes` com uma medição repetida:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R3: já existe uma medição deste idoso com a mesma data e hora",
  "caminho": "/medicoes",
  "timestamp": "2026-11-09T09:16:08",
  "campos": []
}
```

| Status | Quando acontece neste projeto |
|---|---|
| `400` | JSON ilegível ou campo inválido no DTO de entrada, inclusive encerrar um alerta sem dizer o que foi feito (R4) |
| `400` | Regra R1 (mínimo não é menor que o máximo), R3 (medição repetida) ou R4 (alerta já encerrado) violada. A `mensagem` explica a regra |
| `404` | Id de idoso ou de alerta que não existe |
