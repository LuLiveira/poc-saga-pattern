# POC SAGA PATTERN

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

### Fluxo

A idéia do projeto é representar um fluxo de compra onde temos 3 serviços, order-service, payment-service e storage-service.

1. A inicio do fluxo é a partir de um endpoint **GET** exposto pela aplicação order-service na rota `/orders`
2. O serviço irá persistir um objeto de compra em um `H2` e enviar uma mensagem para o RabbitMQ com o mesmo
3. A aplicação payment-service estara ouvindo a fila associada ao exchange da mensagem enviada pelo order-service
4. Ao receber a mensagem o payment-service fará uma logica simples para simular a validação de um pagamento
   1. Sucesso: O pagamento será persistido em um `H2` e a ordem sera gerada uma nova mensagem para o RabbitMQ.
      1. O storage-service estará ouvindo a fila associada ao exchange de sucesso e caso receba uma mensagem ira realizar uma lógica para verificar se o produto possui estoque.
          1. Sucesso: Em caso de sucesso o estoque é decrementado e o fluxo se encerra.
          2. Falha: O estoque não é alterado e uma mensagem nova para o RabbitMQ é gerada.
             1. O order-service e o payment-service estarão com filas ouvindo o exchange de falha do stora-service e ao receber uma mensagem irão apagar os objetos persistidos.
   2. Falha: O pagamento não é persistido e uma mensagem nova para o RabbitMQ é gerada.
       1. O order-service estará ouvindo a fila associada ao exchange de falha e caso receba uma mensagem ira alterar/remover a order persistida.

### Considerações

Caso algo não tenha ficado claro ou existam sugestões, dicas, etc... segue o meu [Linkedin](https://www.linkedin.com/in/lucas-oliveira1/)

**Não tome a estrutura/arquitura desse projeto como base pois os mesmos não foram o foco no momento do desenvolvimento**

