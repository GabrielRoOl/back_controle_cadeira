![License](https://img.shields.io/badge/License-MIT-green)
![Java](https://img.shields.io/badge/Java-17-blue)

# 🏥 API de controle de empréstimos para cadeiras de rodas
API para gerenciamento de empréstimos, devoluções e rastreamento de cadeiras de rodas, destinada a centros clínicos, condominios, hospitais que realizam o controle de empréstimo desses equipamentos.


## 🔧 Funcionalidades
- Cadastro de pessoas e instituições no sistema.
- Controle de empréstimo e devolução por usuário (pacientes, funcionário, clínica, etc.).
- Histórico de movimentação (quem pegou, quando, status).

## 🌐 Objetivo

Desenvolver uma API robusta para gerenciar o ciclo de empréstimo e devolução de cadeiras de rodas, garantindo:

- Controle preciso da disponibilidade dos equipamentos em tempo real.
- Rastreabilidade de movimentações (quem retirou, quando e status atual).
- Eficiência operacional para instituições de saúde, reduzindo perdas e atrasos.
- Acessibilidade digital, permitindo integração com sistemas hospitalares ou aplicativos móveis.


**Exemplo de impacto**: <br>
"Facilitar o acesso de pacientes a cadeiras de rodas em hospitais, assegurando que o equipamento certo esteja disponível no momento certo, enquanto fornece dados para otimizar a alocação de recursos."



• [Tecnologias](#tecnologias) • [Pré-requisitos](#pré-requisitos) • [Rotas](#-rotas) • [Futuras Implementações](#️-futuras-implementações) • [Licença](#-licença)

## Tecnologias

- Java
- PostgreSQL    
- Spring Boot
- Intellij


## Pré-requisitos

- [Java JDK 17+](https://www.java.com/en/download/manual.jsp)
- [Spring boot](https://spring.io/projects/spring-boot)
- [PostgreSQL](https://www.postgresql.org/)

## 📍 Rotas

| Método | Rota                                       | Descrição                                                                                                                   |
|--------|--------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| GET    | `/api/cadeira`                             | Lista todos os registros [detalhes da resposta](#get-apicadeira)                                                            |
| GET    | `/api/cadeira/{id}`                        | Lista registro pelo ID [detalhes da resposta](#get-apicadeiraid)                                                            |
| POST   | `/api/cadeira`                             | Faz um novo registro [detalhes do corpo e resposta](#post-apicadeira)                                                       |
| PUT    | `/api/cadeira/devolucao/{id}`              | Registra devolução [detalhes da resposta](#put-apicadeiradevolucaoid)                                                       |
| PUT    | `api/cadeira/devolucao/ECadeira/{cadeira}` | Registra devolução da cadeira pela identificação da cadeira [detalhes da resposta](#put-apicadeiradevolucaoECadeiracadeira) |

### GET `/api/cadeira`
**Resposta**
```json
[
    {
        "id": 10,
        "nomePaciente": "João Silva",
        "destino": "Clínica Cardíaca",
        "numeroClinica": 101,
        "dataEntrega": "15-01-2024 13:00:00",
        "dataDevolucao": "15-01-2024 15:30:00",
        "cadeira": "CADEIRA_01",
        "devolvida": true
    }
]
```

### GET `/api/cadeira/{id}`
**Resposta**
```json
{
    "nomePaciente": "Gabriel Rodrigues",
    "destino": "Clínica do Gabriel",
    "numeroClinica": 101,
    "dataEntrega": "23-07-2025 14:55:00",
    "dataDevolucao": "23-07-2025 14:57:03",
    "cadeira": "CADEIRA_01",
    "devolvida": true
}
```

### POST `/api/cadeira`
**Corpo**
```json
{
    "nomePaciente": "João martins",
    "destino": "Nefrostar",
    "numeroClinica": 217,
    "cadeira": "CADEIRA_01"
}
```

**Resposta**
```json
{
    "nomePaciente": "João martins",
    "destino": "Nefrostar",
    "numeroClinica": 217,
    "dataEntrega": "27-07-2025 10:43:34",
    "cadeira": "CADEIRA_01",
    "devolvida": false
}
```

### PUT `/api/cadeira/devolucao/{id}`
**Response**
```json
{
    "nomePaciente": "João martins",
    "destino": "Nefrostar",
    "numeroClinica": 217,
    "dataEntrega": "27-07-2025 13:43:34",
    "dataDevolucao": "27-07-2025 10:45:31",
    "cadeira": "CADEIRA_01",
    "devolvida": true
}
```
### PUT `api/cadeira/devolucao/ECadeira/{cadeira}`
- O parametro ``{cadeira}`` é um Enum [clique aqui](https://github.com/GabrielRoOl/back_controle_cadeira/blob/main/src/main/java/br/com/cadeira/controle/vitrium/vitrium/entity/enums/ECadeira.java) para vê-lo 

| ECadeira   |
|------------|
| CADEIRA_01 |
| CADEIRA_02 |
| CADEIRA_03 |
| CADEIRA_04 |
| CADEIRA_05 |
| CADEIRA_06 |
| CADEIRA_07 |
| CADEIRA_08 |
````json
{
    "nomePaciente": "Gabriel Rodrigues de Oliveira",
    "destino": "Araia",
    "numeroClinica": 217,
    "dataEntrega": "09-08-2025 13:33:11",
    "dataDevolucao": "09-08-2025 10:33:17",
    "cadeira": "CADEIRA_01",
    "devolvida": true
}
````



## 🛠️ Futuras Implementações

- Cadastrar usuários
  - [ ] Autenticação de usuário 
- Cadastrar cadeiras
  - [ ] Registra modelo
  - [ ] Manutenção
- Testes Unitários
  - [ ] Todos os endpoints

# 📄 Licença

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

[Inicio](#-api-de-controle-de-empréstimos-para-cadeiras-de-rodas)
