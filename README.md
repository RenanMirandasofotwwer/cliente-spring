# Client API

API REST de clientes usando Java 21, Spring Boot, Maven, Spring Data JPA e H2.

## Executar

```shell
mvn spring-boot:run
```

O banco H2 em memória é criado automaticamente e recebe dez clientes de exemplo ao iniciar a aplicação. O console H2 fica disponível em `/h2-console` (JDBC URL: `jdbc:h2:mem:clientdb`, usuário `sa`, sem senha).

## Endpoints

| Método | Caminho | Descrição |
|---|---|---|
| `GET` | `/clients?page=0&size=12&sort=id,asc` | Lista clientes paginados |
| `GET` | `/clients/{id}` | Busca um cliente |
| `POST` | `/clients` | Cria um cliente (retorna `201 Created`) |
| `PUT` | `/clients/{id}` | Atualiza os dados de um cliente |
| `DELETE` | `/clients/{id}` | Remove um cliente (retorna `204 No Content`) |

O corpo de criação e atualização usa os campos `name`, `cpf`, `income`, `birthDate` (data/hora ISO-8601) e `children`. Campos obrigatórios, renda não negativa e quantidade de filhos não negativa são validados.

## Testes

```shell
mvn test
```

Os testes de integração usam um banco H2 isolado.
