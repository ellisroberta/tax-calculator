# Tax Calculator

Este projeto é um microserviço simples para calcular o imposto sobre ganhos de capital baseado em operações do mercado financeiro.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Maven
- JUnit
- Mockito

## Como Rodar o Projeto

1. Compile e rode o projeto:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

2. Acesse o endpoint para calcular o imposto:
    ```bash
    curl -X POST http://localhost:8080/api/tax/calculate -H "Content-Type: application/json" -d '[{"type":"buy", "unitCost":10.00, "quantity":100}, {"type":"sell", "unitCost":15.00, "quantity":50}, {"type":"sell", "unitCost":15.00, "quantity":50}]'
    ```

## Testes

Para rodar os testes unitários, utilize o comando:
```bash
mvn test
