# Metamorfose API

Sistema de Monitoramento de Plantas - API REST integrada com PL/SQL

## Sumário

- [Metamorfose API](#metamorfose-api)
  - [Sumário](#sumário)
  - [Sobre o Projeto](#sobre-o-projeto)
  - [Funcionalidades](#funcionalidades)
  - [Tecnologias Utilizadas](#tecnologias-utilizadas)
  - [Como Executar](#como-executar)
  - [Configuração](#configuração)
  - [Documentação da API](#documentação-da-api)
  - [Testes](#testes)
  - [Contribuição](#contribuição)
  - [Licença](#licença)

---

## Sobre o Projeto

A **Metamorfose API** é uma aplicação Java Spring Boot para monitoramento de plantas, integrando-se a procedures e funções PL/SQL em banco Oracle. Permite visualizar dashboards, monitorar status, registrar alertas e executar rotinas automáticas de backend.

---

## Funcionalidades

- Consultar dashboard de plantas (todas ou por usuário)
- Calcular índice de saúde de uma planta
- Obter status formatado de uma planta
- Registrar alertas críticos (para todas as plantas ou uma específica)
- Executar rotinas automáticas de backend (COMPLETO, ALERTAS, LIMPEZA, STATS)
- Processamento assíncrono de rotinas

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2
- Spring Web, JDBC, Validation, Cache, Async
- Oracle Database (ojdbc11)
- Swagger/OpenAPI ([springdoc-openapi](https://springdoc.org/))
- Maven

---

## Como Executar

1. **Pré-requisitos:**
   - Java 17+
   - Maven 3.9+
   - Banco Oracle

2. **Clone o repositório:**
   ```sh
   git clone https://github.com/seu-usuario/metamorfose-api.git
   cd metamorfose-api/metamorfose
   ```

3. **Configure o banco de dados:**
   - Edite `src/main/resources/application.yml` com as credenciais e URL do seu Oracle.

4. **Build e execução:**
   ```sh
   ./mvnw spring-boot:run
   ```
   ou
   ```sh
   mvn spring-boot:run
   ```

5. **Acesse a API:**
   - Base URL: [http://localhost:8080/api/v1](http://localhost:8080/api/v1)
   - Swagger UI: [http://localhost:8080/api/v1/swagger-ui.html](http://localhost:8080/api/v1/swagger-ui.html)

---

## Configuração

O arquivo [`src/main/resources/application.yml`](metamorfose/src/main/resources/application.yml) contém as configurações de banco, porta, logging, etc.

Exemplo:
```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:xe
    username: SYSTEM
    password: sua_senha
    driver-class-name: oracle.jdbc.OracleDriver
```

---

## Documentação da API

A documentação interativa está disponível via Swagger/OpenAPI:

- [http://localhost:8080/api/v1/swagger-ui.html](http://localhost:8080/api/v1/swagger-ui.html)

Principais endpoints:

- `GET /dashboard/plants` — Lista todas as plantas
- `GET /dashboard/plants/user/{userId}` — Lista plantas de um usuário
- `GET /dashboard/plants/{plantId}/health` — Índice de saúde da planta
- `GET /dashboard/plants/{plantId}/status` — Status formatado da planta
- `POST /monitoring/alerts` — Registrar alertas críticos para todas as plantas
- `POST /monitoring/alerts/{plantId}` — Registrar alertas para uma planta
- `POST /monitoring/process/{type}` — Executar rotina automática
- `POST /monitoring/process/{type}/async` — Executar rotina automática assíncrona

---

## Testes

Para rodar os testes automatizados:

```sh
mvn test
```

Os testes estão em [`src/test/java/com/metamorfose/metamorfose/MetamorfoseApplicationTests.java`](metamorfose/src/test/java/com/metamorfose/metamorfose/MetamorfoseApplicationTests.java).

---

## Contribuição

1. Fork este repositório
2. Crie uma branch: `git checkout -b minha-feature`
3. Commit suas alterações: `git commit -m 'feat: minha nova feature'`
4. Push para o branch: `git push origin minha-feature`
5. Abra um Pull Request

---

## Licença

Este projeto está licenciado sob a [MIT License](https://opensource.org/licenses/MIT).

---

**Equipe Metamorfose**  
[contato@metamorfose.io@gmail.com](mailto:contato@metamorfose.io@gmail.com)