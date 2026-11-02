![CI](https://github.com/GabrielRoOl/back_controle_cadeira/actions/workflows/ci.yml/badge.svg)

# API de controle de empréstimo de cadeiras de rodas

API REST para registrar o empréstimo e a devolução de cadeiras de rodas em clínicas e hospitais. Cada empréstimo guarda paciente, destino, sala, data de entrega e data de devolução, e a API impede emprestar uma cadeira que ainda não foi devolvida.

O front-end em React está em [front_controle_cadeira](https://github.com/GabrielRoOl/front_controle_cadeira).

## Stack

- Java 17 e Spring Boot 3.5
- Spring Web, Bean Validation e Spring Data JPA (Hibernate)
- Spring Security com JWT (`java-jwt`), senhas com BCrypt e perfis `ADMIN` e `USER`
- PostgreSQL (H2 em memória nos testes)
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, MockMvc e `@DataJpaTest`
- Docker e Docker Compose
- GitHub Actions rodando `mvn test` a cada push

## Como rodar

Com Docker, sem instalar Java nem PostgreSQL:

```bash
docker compose up --build
```

A API sobe em `http://localhost:8080` com um PostgreSQL 16 ao lado. A documentação interativa fica em `http://localhost:8080/swagger-ui.html`.

O segredo do JWT vem da variável `JWT_SECRET`. Sem ela, o compose usa um valor de desenvolvimento.

Sem Docker, com um PostgreSQL local, defina `DB_CADEIRA_URL`, `DB_USERNAME`, `DB_PASSWORD` e `JWT_SECRET` e rode `./mvnw spring-boot:run`.

## Autenticação

As rotas `/api/**` exigem um token JWT de um usuário `ADMIN`.

```bash
# cria o usuário
curl -X POST localhost:8080/auth/register -H 'Content-Type: application/json' \
  -d '{"login":"admin","password":"admin123","confirmPassword":"admin123","role":"ADMIN"}'

# faz login e recebe {"token": "..."}
curl -X POST localhost:8080/auth/login -H 'Content-Type: application/json' \
  -d '{"login":"admin","password":"admin123"}'
```

Envie o token no cabeçalho `Authorization: Bearer <token>`.

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/register` | Cria usuário (`ADMIN` ou `USER`) |
| POST | `/auth/login` | Retorna o token JWT |
| GET | `/api/cadeira` | Lista todos os empréstimos |
| GET | `/api/cadeira/{id}` | Detalha um empréstimo |
| POST | `/api/cadeira` | Registra um empréstimo |
| PUT | `/api/cadeira/devolucao/{id}` | Registra a devolução pelo id do empréstimo |
| PUT | `/api/cadeira/devolucao/ECadeira/{cadeira}` | Registra a devolução pela identificação da cadeira (`CADEIRA_01` a `CADEIRA_08`) |

Exemplo de empréstimo:

```bash
curl -X POST localhost:8080/api/cadeira -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nomePaciente":"Paciente Teste","destino":"Clínica Teste","numeroClinica":101,"cadeira":"CADEIRA_01"}'
```

```json
{
  "nomePaciente": "Paciente Teste",
  "destino": "Clínica Teste",
  "numeroClinica": 101,
  "dataEntrega": "02-11-2026 15:02:37",
  "cadeira": "CADEIRA_01",
  "devolvida": false
}
```

### Regras e erros

| Situação | Resposta |
|---|---|
| Emprestar uma cadeira que ainda não foi devolvida | `409 Conflict`, "Cadeira em uso" |
| Devolver um empréstimo já devolvido, ou uma cadeira sem empréstimo em aberto | `409 Conflict`, "Cadeira já foi devolvida" |
| Id inexistente | `404 Not Found` |
| Campo obrigatório vazio | `400 Bad Request` com a lista de erros |
| Cadeira inválida na rota de devolução | `400 Bad Request` |
| Sem token ou sem perfil `ADMIN` | `403 Forbidden` |

## Testes

```bash
./mvnw test
```

Os testes não precisam de banco externo: usam H2 em memória.

- `CadeiraServiceTest`: regras de empréstimo e devolução, com o repositório mockado pelo Mockito
- `CadeiraRepositoryTest`: as consultas JPQL que decidem se uma cadeira está em uso (`@DataJpaTest`)
- `CadeiraControllerTest`: rotas e segurança com MockMvc
- `TokenServiceTest`: geração e validação do JWT

## Licença

MIT License

Copyright (c) 2025 [Gabriel Rodrigues de Oliveira](https://github.com/GabrielRoOl)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
