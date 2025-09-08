
<h1>Plataforma de Cursos Online</h1>

Uma plataforma de cursos online desenvolvida em Java com Spring Boot, que permite a criação, gerenciamento e consumo de cursos e aulas em vídeo.
O projeto foi inspirado em uma ideia do vídeo da Fernanda Kipper sobre projetos back-end avançados.

## Tecnologias
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Docker](https://www.docker.com/)
- [PostgresSQL](https://www.postgresql.org/)
- [AWS(S3)](https://aws.amazon.com/pt/s3/)
- [Gumlet](https://docs.gumlet.com/docs/apis)
- [OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [JUnit5](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit-platform.html)
- [Mockito](https://site.mockito.org/)

## 🚀 Funcionalidades

- 👤 **Cadastro e autenticação de usuários** com papéis (`ROLE_ADMIN` e `ROLE_USER`).

- 🎓 **Gerenciamento de cursos**:
    - Admins podem criar, publicar e organizar cursos.
    - Usuários podem visualizar e se inscrever em cursos.

- 🎥 **Upload e streaming de vídeos**:
    - Upload dos vídeos para o **AWS S3** em buckets privados.
    - Integração com o **Gumlet** para streaming otimizado.
    - Uso de **URLs pré-assinadas** para garantir que apenas alunos matriculados tenham acesso às aulas.

- 📝 **Controle de matrículas** através da entidade `Enrollment`, relacionando usuários aos cursos.

- 📄 **Exceções personalizadas e tratamento global de erros**, garantindo respostas claras da API.

- 🔒 **Segurança** implementada com **Spring Security e JWT**.

## Como Executar

- Clonar repositório git
```
git clone git@github.com:igorbarret0/online-courses-platform.git
```

- Construir o projeto:
```
./mvnw clean package
```

- Subir o docker-compose
```
docker-compose up 
```

A API poderá ser acessada em [localhost:8180](http://localhost:8180).

## API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [Postman](https://www.postman.com/)


-- AUTH
```
POST /api/v1/auth - Realizar login
```

```
POST /api/v1/user - Realizar cadastro de usuário
```

```
POST /api/v1/courses - Cadastro de um curso
```

```
GET /api/v1/courses/user - Obter cursos em que um user está cadastrado
```

```
GET /api/v1/courses - Obter listas de todos os cursos disponíveis
```

```
POST /api/v1/uploads - Upload de vídeo para um curso
```

```
POST /api/v1/enrollments/{courseId} - Realizar inscrição em um curso
```




