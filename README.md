#POC SAGA PATTERN

Esta é uma POC desenvolvida em cima do *pattern* **SAGA** utilizando a abordagem *coreografia*
- Técnologias Utilizada:
  - Spring Boot 
  - Spring Web
  - Spring Data JPA
  - RabbitMQ
  - Docker

### Passos

Para executar o projeto os seguintes passos devem ser seguidos

1. Alterar os campos `<dockerhub-username>` dentro do arquivo `docker-compose.yml` dentro da raiz do projeto.
2. Gerar uma imagem do `RabbitMQ` a partir do `Dockerfile` na raiz do projeto.
3. Gerar uma imagem das aplicações a partir dos `Dockerfile` dentro de cada projeto.

### Comandos

1. Para gerar a imagem deve ser executado o seguinte comando:

```shell
docker build -t <dockerhub-username>/<projeto> <path>
```
2. Para executar o `docker-compose.yml` deve ser executado o seguinte comando na raiz do projeto:

```shell
docker-compose up
```

Obs: Antes de executar o build da imagem das aplicações deve ser gerado o `.jar` de cada uma delas, isso pode ser feito com o comando `.mvnw package` na raiz de cada projeto

