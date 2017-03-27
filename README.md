# xy-inc | Backend Maker

Este projeto tem como objetivo fornecer uma interface amigável para criação rápida de serviços REST.

## Arquitetura da Solução
### Ambiente de Produção
Para um ambiente de produção, que deve ser horizontalmente escalável, a arquitetura da solução definida pode ser visualizada na figura a seguir.

![Arquitetura Produção](https://raw.githubusercontent.com/edussm/xy-inc/master/images/arquitetura-prod.png)

Todas as requisições passariam por um API Gateway/Balancer e seriam respondidas pelos nós de "Frontend", responsável por fornecer a interface gráfica de adminstração, ou pelos nós responsáveis por prover os serviços REST.

Os nós de "backend", por sua vez, se conectam a um cluster de bancos MongoDB, permitindo que a estrutura de persistência da solução também seja escalável.

É importante lembrar que, apesar de omitido na figura, existe a necessidade de implementar a integração com um serviço de autenticação.

### MVP (Minimum Viable Product)
Considerando que um MVP é uma solução rápida, muitas vezes para provar um conceito, a arquitetura implementada neste projeto, formalmente um MVP, pode ser visualizada na figura abaixo.

![Arquitetura MVP](https://raw.githubusercontent.com/edussm/xy-inc/master/images/arquitetura-mvp.png)

Observa-se que os clientes podem se comunicar diretamente com a aplicação, sem necessidade de instalar uma estrutura distribuída, inclusive para o banco de dados, que é carregado de forma embarcada, juntamente com o restante do software.

A implementação do MVP também conta com um mecanismo de login e controles de acesso para as APIs do sistema.

## Detalhes de Implementação
Neste tópico serão informados os softwares e frameworks, que foram utilizados e merecem destaque.
### Banco de Dados
* MongoDB versão 3.4.2.
### Backend
* Spring (Boot, MVC, Data);
* EmbedMongo e Flapdoodle (Instâncias e gestão do MongoDB embarcado);
* Maven.
### Frontend
* AngularJS;
* Bootstrap;

## Instalação/Execução
### Requisitos
* JDK 1.8
* Cliente git

### Procedimento de Instalação e Execução (Linux/OSx)
1. Baiar o código fonte:
        
        git clone https://github.com/edussm/xy-inc.git
2. Acessar a pasta do projeto:

        cd xy-inc
3. Compilar e efetuar testes unitários
    
        ./mvnw test
4. Executar
    
        ./mvnw spring-boot:run
ou
    
        ./mvnw clean package
        java -jar target/backend-maker-1.0.0-SNAPSHOT.jar
5. Acessar a interface no endereço: http://localhost:8080
        
        usuário: user
        senha: password

### Utilização
A interface gráfica permite que o usuário crie os modelos passando o nome do "Modelo" desejado e inserindo colunas dos tipos string, text, int, decimal e double.

A partir da criação do modelo, a API REST para este fica disponível.

Considerando que o usuário criou um Modelo com o seguinte formato:
* Nome: Produtos
* Colunas: nome(string), preco(double), marca (string) e quantidade (string).

Ficariam disponíveis os seguintes serviços:

| Endpoint | Descrição |
| ---- | --------------- |
| [GET /api/dynamic/Produtos](http://localhost:8080/api/dynamic/Produtos) | Busca todos os produtos cadastrados |
| [GET /api/dynamic/Produtos/{id}](http://localhost:8080/api/dynamic/Produtos/{id}) | Busca um produto pelo ID |
| [POST /api/dynamic/Produtos](http://localhost:8080/api/dynamic/Produtos) | Cria um Produto. O corpo da chamada deve ser um objeto JSON com os campos do modelo. |
| [PUT /api/dynamic/Produtos/{id}](http://localhost:8080/api/dynamic/Produtos/{id}) | Edita um produto pelo ID.  | O corpo da chamada deve ser um objeto JSON com os campos do modelo. |
| [DELTE /api/dynamic/Produtos/{id}](http://localhost:8080/api/dynamic/Produtos/{id}) | Remove um produto pelo ID. |

### Exemplos de Chamada do Serviço REST

#### GET

        curl -u user:password http://localhost:8080/api/dynamic/Produto
        curl -u user:password http://localhost:8080/api/dynamic/Produto/2a88bec3-1923-4ef7-97f1-2b8da0632f1a

#### POST

        curl -u user:password -H "Content-Type: application/json" -X POST -d '{"nome": "Arroz Agulhinha", "marca": "Marca X", "preco": 10.99, "quantidate": "5 kg", "categoria": "Cereais"}' http://localhost:8080/api/dynamic/Produto

#### PUT

        curl -u user:password -H "Content-Type: application/json" -X PUT -d '{"nome": "Arroz Agulhinha", "marca": "Marca ABC", "preco": 9.99, "quantidate": "5 kg", "categoria": "Cereais", "id": "39f08f9c-b170-437d-9bc8-1feb3b0c5dc4"}' http://localhost:8080/api/dynamic/Produto/39f08f9c-b170-437d-9bc8-1feb3b0c5dc4


#### DELETE

        curl -u user:password -X DELETE http://localhost:8080/api/dynamic/Produto/39f08f9c-b170-437d-9bc8-1feb3b0c5dc4

### Observações sobre os serviços REST
Os códigos de retorno do serviço criado são dados de acordo com as operações de sucesso ou erro ocorridas.
Os significados dos códigos de sucesso/erros são listados em https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html