# API Todo List

Uma API REST para gerenciamento de tarefas, desenvolvida em Java com Spring Boot, Spring Data JPA e PostgreSQL.

## Indice

- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Instalacao](#instalacao)
- [Uso](#uso)
- [Endpoints](#endpoints)
- [Banco de Dados](#banco-de-dados)
- [Desenvolvimento](#desenvolvimento)

## Funcionalidades

- Gestao de usuarios: cadastro e listagem de usuarios
- Gestao de tarefas: criacao, listagem e atualizacao de tarefas
- Autenticacao Basic Auth: rotas de tarefas protegidas por usuario e senha
- Senhas criptografadas: hash de senha com BCrypt
- Tarefas por usuario: cada usuario acessa apenas as suas tarefas
- Prioridade de tarefas: suporte para prioridade baixa, media e alta
- Validacao de dados: validacoes com Jakarta Bean Validation
- Persistencia com PostgreSQL: integracao via Spring Data JPA e Hibernate
- Docker Compose: banco PostgreSQL e Adminer prontos para desenvolvimento

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Hibernate
- Maven
- Lombok
- BCrypt
- Jakarta Bean Validation
- Docker Compose
- Adminer

## Estrutura do Projeto

```text
todolist/
├── src/
│   ├── main/
│   │   ├── java/br/com/isaacdev/todolist/
│   │   │   ├── filter/              # Filtro de autenticacao das tarefas
│   │   │   │   └── FIlterTaskAuth.java
│   │   │   ├── ping/                # Endpoint de health check simples
│   │   │   │   └── Ping.java
│   │   │   ├── task/                # Modulo de tarefas
│   │   │   │   ├── dto/
│   │   │   │   │   ├── CreateTaskDTO.java
│   │   │   │   │   └── UpdateTaskDTO.java
│   │   │   │   ├── TaskController.java
│   │   │   │   ├── TaskModel.java
│   │   │   │   ├── TaskPriority.java
│   │   │   │   └── TaskRepository.java
│   │   │   ├── user/                # Modulo de usuarios
│   │   │   │   ├── Dto/
│   │   │   │   │   ├── CreateUserDTO.java
│   │   │   │   │   └── UserResponseDTO.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── UserModel.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── utils/               # Utilitarios compartilhados
│   │   │   │   └── Utils.java
│   │   │   └── TodolistApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/br/com/isaacdev/todolist/
│           └── TodolistApplicationTests.java
├── docker-compose.yml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.MD
```

## Instalacao

### 1. Clone o repositorio

```bash
git clone git@github.com:IsaaczZj/task-manager-spring.git
cd task-manager-spring
```

### 2. Suba o banco de dados

```bash
docker compose up -d
```

O `docker-compose.yml` inicia:

- PostgreSQL em `localhost:5432`
- Adminer em `http://localhost:8082`

Credenciais do banco:

```env
POSTGRES_DB=todolist
POSTGRES_USER=todolist_user
POSTGRES_PASSWORD=todolist_pass
```

### 3. Configure a aplicacao

As configuracoes principais ficam em `src/main/resources/application.properties`:

```properties
server.port=8081
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:5432/todolist
spring.datasource.username=todolist_user
spring.datasource.password=todolist_pass
spring.jpa.hibernate.ddl-auto=update
```

Se o PostgreSQL estiver rodando localmente pelo Docker Compose, nenhuma alteracao adicional e necessaria.

### 4. Execute a API

```bash
./mvnw spring-boot:run
```

A API estara disponivel em:

```text
http://localhost:8081
```

## Uso

Fluxo basico:

1. Criar um usuario em `POST /users`
2. Usar as credenciais desse usuario com Basic Auth
3. Criar tarefas em `POST /tasks`
4. Listar tarefas em `GET /tasks`
5. Atualizar tarefas em `PATCH /tasks/{id}`

Exemplo de header para rotas de tarefas:

```http
Authorization: Basic base64(username:password)
```

Com `curl`, voce pode usar:

```bash
curl -u isaac:123456 http://localhost:8081/tasks
```

## Endpoints

### Health Check

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| GET | `/ping` | Retorna uma resposta simples para verificar se a API esta online |

Exemplo de resposta:

```json
{
  "message": "pong"
}
```

### Usuarios

| Metodo | Endpoint | Descricao | Autenticacao |
| --- | --- | --- | --- |
| POST | `/users` | Cria um novo usuario | Nao |
| GET | `/users` | Lista todos os usuarios | Nao |

Exemplo - Criar usuario:

```http
POST /users
Content-Type: application/json
```

```json
{
  "username": "isaac",
  "name": "Isaac Medeiros",
  "password": "123456"
}
```

Validacoes:

- `username`: obrigatorio
- `name`: obrigatorio
- `password`: obrigatoria, com no minimo 6 caracteres

### Tarefas

Todas as rotas de tarefas exigem Basic Auth.

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/tasks` | Cria uma nova tarefa para o usuario autenticado |
| GET | `/tasks` | Lista as tarefas do usuario autenticado |
| PATCH | `/tasks/{id}` | Atualiza uma tarefa do usuario autenticado |

Exemplo - Criar tarefa:

```http
POST /tasks
Content-Type: application/json
Authorization: Basic base64(username:password)
```

```json
{
  "description": "Finalizar documentacao do projeto",
  "title": "Criar README",
  "startAt": "2030-01-01T09:00:00",
  "endAt": "2030-01-01T11:00:00",
  "priority": "alta"
}
```

Exemplo - Listar tarefas:

```bash
curl -u isaac:123456 http://localhost:8081/tasks
```

Exemplo - Atualizar tarefa:

```http
PATCH /tasks/{id}
Content-Type: application/json
Authorization: Basic base64(username:password)
```

```json
{
  "description": "Atualizar README com instrucoes de uso",
  "priority": "media"
}
```

Validacoes de tarefas:

- `description`: obrigatoria na criacao
- `title`: obrigatorio na criacao e limitado a 50 caracteres
- `startAt`: obrigatoria na criacao e nao pode estar no passado
- `endAt`: obrigatoria na criacao, nao pode estar no passado e deve ser depois de `startAt`
- `priority`: opcional; valores aceitos: `baixa`, `media`, `alta`

## Banco de Dados

### Modelo de Usuario

```java
@Entity(name = "users")
public class UserModel {
    private UUID id;
    private String username;
    private String name;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Modelo de Tarefa

```java
@Entity(name = "tasks")
public class TaskModel {
    private UUID id;
    private String description;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private TaskPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID userId;
}
```

### Prioridades

```java
public enum TaskPriority {
    BAIXA,
    MEDIA,
    ALTA
}
```

## Autenticacao

As rotas iniciadas por `/tasks` passam pelo filtro `FIlterTaskAuth`.

Funcionamento:

1. A API le o header `Authorization`
2. Decodifica as credenciais Basic Auth
3. Busca o usuario pelo `username`
4. Verifica a senha com BCrypt
5. Adiciona o `userId` autenticado na requisicao
6. Permite que o controller liste, crie ou atualize apenas tarefas desse usuario

## Desenvolvimento

### Rodar os testes

```bash
./mvnw test
```

### Gerar o build

```bash
./mvnw clean package
```

### Acessar o Adminer

Abra:

```text
http://localhost:8082
```

Use:

```text
Sistema: PostgreSQL
Servidor: db
Usuario: todolist_user
Senha: todolist_pass
Banco de dados: todolist
```

## Observacoes

- O Hibernate esta configurado com `spring.jpa.hibernate.ddl-auto=update`, entao as tabelas sao atualizadas automaticamente em desenvolvimento.
- A aplicacao roda na porta `8081`.
- O PostgreSQL roda na porta `5432`.
- O Adminer roda na porta `8082`.
