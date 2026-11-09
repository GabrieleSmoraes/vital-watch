# Vital Watch

O Vital Watch é uma API que recebe os batimentos enviados pelo relógio de idosos com problemas cardíacos que moram sozinhos e registra um alerta para a família quando uma medição sai do normal.

Projeto Final da disciplina de Backend (Engenharia de Software, SATC). **Etapa 1: arquitetura do backend.**

## Índice

- [Integrantes](#integrantes)
- [O problema](#o-problema)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Entidades](#entidades)
- [Rotas](#rotas)
- [Como executar](#como-executar)
- [Situação do projeto](#situação-do-projeto)
- [Documentação](#documentação)

## Integrantes

| Nome | GitHub |
|---|---|
| Gabriele Moraes | [@GabrieleSmoraes](https://github.com/GabrieleSmoraes) |
| Leticia Juvencio | [@LeticiaJuvencio](https://github.com/LeticiaJuvencio) |
| Camila da Rosa | [@camiladarosa789-ux](https://github.com/camiladarosa789-ux) |

## O problema

**Tema sorteado:** Saúde e Bem-Estar

Muitos idosos com problemas cardíacos, ou com outras condições que alteram os batimentos, moram sozinhos. Se um deles passa mal em casa, pode não conseguir pedir ajuda, e a família, que cuida à distância, só fica sabendo depois. Com o Vital Watch, o relógio envia os batimentos para a API, que registra um alerta quando a medição sai dos limites daquele idoso. A família consulta os alertas e registra o que foi feito. O sistema ajuda a acompanhar, mas não dá diagnóstico.

**Quem usa o sistema:**

| Quem | O que faz no sistema |
|---|---|
| Familiar ou cuidador | Cadastra o idoso e os limites dele, consulta os alertas e encerra cada um informando o que foi feito |
| Relógio do idoso (simulado) | Envia as medições de batimento |

**Regras de negócio.** O grupo criou estas quatro regras olhando para o problema: cada idoso tem um coração diferente, exercício acelera o batimento sem ser perigo, o relógio pode mandar a mesma medição duas vezes e a família precisa registrar o que fez com cada alerta.

| # | Regra | Onde está no código | Rota que usa |
|---|---|---|---|
| R1 | Cada idoso tem um limite mínimo e um máximo de batimentos (padrão: 60 a 100 por minuto). Uma medição fora desses limites gera um alerta. O cadastro é recusado se o mínimo não for menor que o máximo. | `IdosoService.criar` (limites padrão e mínimo menor que o máximo) e `MedicaoService.criar` (gera o alerta) | `POST /idosos` e `POST /medicoes` |
| R2 | Se a medição vier marcada como exercício, batimento alto só gera alerta acima de 220 menos a idade do idoso. O limite mínimo continua igual. | `MedicaoService.criar`, que calcula a idade no dia da medição | `POST /medicoes` |
| R3 | Uma medição repetida (mesmo idoso, mesma data e hora) é recusada com erro 400. | `MedicaoService.criar`, com a consulta `MedicaoRepository.existsByIdosoIdAndDataHoraMedicao` | `POST /medicoes` |
| R4 | Um alerta já encerrado não pode ser encerrado de novo (erro 400). Para encerrar, é preciso informar o que foi feito. | `AlertaService.encerrar` (alerta já encerrado) e `AlertaRequestDTO` (o que foi feito é obrigatório) | `PUT /alertas/{id}/encerrar` |

Quando R1, R3 ou R4 barram um pedido, a API devolve `400` com a regra explicada na mensagem. A R2 não barra nada: ela muda o limite usado para decidir se a medição gera alerta.

Os valores são de referência, e o projeto usa dados fictícios. Fontes: [MedlinePlus](https://medlineplus.gov/ency/article/003399.htm) (60 a 100 em repouso) e [Manual MSD](https://www.msdmanuals.com/home/multimedia/table/estimating-target-heart-rate) (220 menos a idade).

**Fora do escopo:** envio de SMS ou notificação no celular (a família consulta os alertas na API), integração com um relógio de verdade (as medições são enviadas pelo Swagger ou pelo Postman), login e diagnóstico médico.

## Tecnologias

| O quê | Qual |
|---|---|
| Linguagem | Java 25 |
| Framework | Spring Boot 3.5 (Web, Validation, Data JPA) |
| Banco de dados | PostgreSQL 16, em Docker |
| Migrations | Flyway |
| Documentação da API | springdoc-openapi (Swagger UI) |

## Arquitetura

A API é organizada em camadas. Uma requisição entra pelo Controller, passa pelo Service, que aplica as regras de negócio, e chega ao banco pelo Repository; a resposta faz o caminho de volta. O Mapper converte DTO em entidade e entidade em DTO.

**Mapa da estrutura.** Uma linha por entidade, todas as do modelo. Em cada célula, a classe e a situação: **pronta**, **implementada** (já tem lógica), **em parte** (só uma parte das rotas) ou **montada** (criada, ligada às outras, ainda sem lógica).

| Entidade | Migration | Model | Repository | DTOs | Mapper | Service | Controller |
|---|---|---|---|---|---|---|---|
| Idoso | `V1__criar_idosos.sql`, pronta | `Idoso`, pronta | `IdosoRepository`, pronto | `IdosoRequestDTO`, `IdosoResponseDTO`, prontos | `IdosoMapper`, implementado | `IdosoService`, implementado (R1) | `IdosoController`, implementado |
| Medicao | `V2__criar_medicoes.sql`, pronta | `Medicao`, pronta | `MedicaoRepository`, pronto | `MedicaoRequestDTO`, `MedicaoResponseDTO`, prontos | `MedicaoMapper`, implementado | `MedicaoService`, em parte: só o registro (R1, R2 e R3) | `MedicaoController`, em parte: só o `POST` |
| Alerta | `V3__criar_alertas.sql`, pronta | `Alerta`, pronta | `AlertaRepository`, pronto | `AlertaRequestDTO`, `AlertaResponseDTO`, prontos | `AlertaMapper`, implementado | `AlertaService`, implementado (R4) | `AlertaController`, implementado |

Também fazem parte do projeto, vindas do template: `ApiExceptionHandler`, `ErroDTO`, `CampoErroDTO`, `RecursoNaoEncontradoException`, `RegraDeNegocioException` e `OpenApiConfig`. O idoso e o alerta têm a sua exceção de "não encontrado": `IdosoNaoEncontradoException` e `AlertaNaoEncontradoException`.

Quem cria os alertas é o `MedicaoService`, no momento em que a medição chega: o cliente nunca cria um alerta direto. Por isso o `AlertaRequestDTO` não serve para criar alerta, e sim para encerrar: ele traz só o que a família fez.

Da medição, só o registro foi feito, porque é o que as regras precisam. As consultas de medições ficam para a próxima etapa.

## Entidades

| Entidade | O que representa | Como se relaciona |
|---|---|---|
| Idoso | Uma pessoa acompanhada pelo sistema, com os seus limites de batimento | Tem várias medições |
| Medicao | Um batimento enviado pelo relógio, com a data e a hora e se a pessoa estava em exercício | Pertence a um idoso; pode gerar um alerta |
| Alerta | O aviso de que uma medição saiu dos limites, que a família encerra dizendo o que fez | Vem de exatamente uma medição |

Atributos, tipos e o diagrama das tabelas estão em [docs/modelo-de-dominio.md](docs/modelo-de-dominio.md).

## Rotas

### As que já funcionam

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|---|---|---|---|---|---|
| `POST` | `/idosos` | Cadastra um idoso com os limites de batimento | R1 | `201` | `400` |
| `GET` | `/idosos` | Lista os idosos | Nenhuma | `200` | Nenhum |
| `GET` | `/idosos/{id}` | Busca um idoso pelo id | Nenhuma | `200` | `404` |
| `POST` | `/medicoes` | Registra uma medição e gera o alerta se ela sair dos limites | R1, R2 e R3 | `201` | `400`, `404` |
| `GET` | `/alertas` | Lista os alertas | Nenhuma | `200` | Nenhum |
| `GET` | `/alertas/{id}` | Busca um alerta pelo id | Nenhuma | `200` | `404` |
| `PUT` | `/alertas/{id}/encerrar` | Encerra o alerta com o que foi feito | R4 | `200` | `400`, `404` |

Exemplo de requisição: a Maria (60 a 100) mandou 130 batimentos em repouso.

```http
POST /medicoes
Content-Type: application/json

{
  "idosoId": 1,
  "batimentos": 130,
  "dataHoraMedicao": "2026-11-09T07:10:00",
  "emExercicio": false
}
```

Resposta, `201 Created`. Entra o id do idoso e ele volta completo; `gerouAlerta` diz que a medição passou do limite (R1):

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

### Erros

Todo erro sai no mesmo formato. Este é o `400` da regra R4, quando a família tenta encerrar um alerta que já foi encerrado:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R4: o alerta 1 já foi encerrado",
  "caminho": "/alertas/1/encerrar",
  "timestamp": "2026-11-09T09:16:14",
  "campos": []
}
```

Um campo inválido também devolve `400`, com a lista `campos` dizendo qual campo corrigir. Um idoso ou alerta que não existe devolve `404`.

### As planejadas

Estão no contrato, mas ainda não respondem.

| Recurso | O que vai fazer |
|---|---|
| `GET /medicoes` e `GET /medicoes/{id}` | Listar e buscar as medições, para a família ver o histórico |

O contrato completo, com todas as rotas e os DTOs, está em [docs/contrato-da-api.md](docs/contrato-da-api.md).

## Como executar

Pré-requisitos: JDK 25, Docker Desktop aberto e Git.

```bash
git clone https://github.com/GabrieleSmoraes/vital-watch.git
cd vital-watch

docker compose up -d          # sobe o PostgreSQL na porta 5437
./mvnw spring-boot:run        # Windows: .\mvnw.cmd spring-boot:run
```

Com a API no ar:

| Endereço | O que é |
|---|---|
| <http://localhost:8080/swagger-ui.html> | Swagger UI: as rotas, com **Try it out** para enviar requisições |
| <http://localhost:8080/v3/api-docs> | O contrato OpenAPI em JSON |

Para testar as rotas, use o Swagger UI ou o **Postman**. No Postman, importe todas as rotas de uma vez: **Import** → **Link** → `http://localhost:8080/v3/api-docs`.

Para ver as regras funcionando:

1. Cadastre a Maria com `POST /idosos`: `{"nome": "Maria Silva", "dataNascimento": "1950-03-15"}`. Sem limites, ela fica com 60 e 100 (R1).
2. Mande `POST /medicoes` com 130 batimentos e `emExercicio: false`: volta `gerouAlerta: true` (R1).
3. Mande 130 com `emExercicio: true` em outro horário: volta `gerouAlerta: false`, porque em exercício o limite da Maria, com 76 anos, é 220 − 76 = 144 (R2). Com 150, gera alerta.
4. Repita a medição do passo 2, com a mesma data e hora: `400` da R3.
5. Veja os alertas em `GET /alertas` e encerre um com `PUT /alertas/{id}/encerrar`, mandando `{"providencia": "Liguei para ela, estava bem"}`. Encerre o mesmo de novo: `400` da R4.
6. Para ver a R1 barrando: cadastre um idoso com `limiteMinimo` 100 e `limiteMaximo` 60.

Para parar: `Ctrl+C` na API e `docker compose stop` no banco. Para apagar o banco e rodar as migrations do zero: `docker compose down -v` e suba de novo.

## Situação do projeto

| Parte | Situação |
|---|---|
| Desenho | Completo: problema e regras neste README, modelo e contrato em `docs/` |
| Estrutura | As 3 entidades têm migration, entidade, Repository, DTOs, Mapper, Service e Controller |
| API e regras | Idosos com as 3 rotas da API simples; registro de medições com R1, R2 e R3; alertas listados, buscados e encerrados com R4. As 4 regras estão implementadas |

**Ainda não funciona:** `GET /medicoes` e `GET /medicoes/{id}`.

## Documentação

| Documento | O que tem |
|---|---|
| [Modelo de domínio](docs/modelo-de-dominio.md) | Entidades, relacionamentos, diagrama das tabelas, migrations |
| [Contrato da API](docs/contrato-da-api.md) | Todas as rotas, os DTOs, exemplos de JSON e erros |
| [Autoavaliação](docs/autoavaliacao.md) | Os critérios da revisão do código, marcados pelo grupo |
