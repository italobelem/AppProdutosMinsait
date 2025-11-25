🛒 AppProdutos - API de E-commerce

API RESTful para gestão de e-commerce. O sistema gerencia desde o cadastro de produtos e controle transacional de estoque até o fluxo completo de carrinho de compras e checkout, com segurança via JWT e documentação interativa.

🚀 Tecnologias & Ferramentas

    Linguagem: Java 21

    Framework: Spring Boot 3.3

    Segurança: Spring Security + JWT (Auth0)

    Banco de Dados: H2 (In-memory) para desenvolvimento / PostgreSQL ready.

    Versionamento de Banco: Flyway Migrations

    Documentação: SpringDoc OpenAPI (Swagger UI)

    Testes: JUnit 5 & Mockito

    Mapeamento: JPA / Hibernate

    Outros: Lombok, Bean Validation, Maven.

⚙️ Arquitetura & Funcionalidades

O projeto segue uma arquitetura em camadas (Controller, Service, Repository) com uso intensivo de DTOs (Records) para entrada e saída de dados, garantindo desacoplamento da camada de persistência.

🔐 1. Segurança e Controle de Acesso (RBAC)

Autenticação Stateless via Token JWT. O sistema possui três perfis de acesso:

    ADMIN: Acesso total (Gestão de catálogo, usuários e ajustes de estoque).

    SELLER: Gestão de produtos e reposição de estoque.

    CUSTOMER: Experiência de compra (Carrinho, Pedidos e Histórico).

📦 2. Catálogo e Estoque Transacional

    CRUD completo de Produtos e Categorias.

    Auditoria de Estoque: Nenhuma quantidade é alterada diretamente. Tudo é feito via Transações (Entrada, Venda, Ajuste, Devolução/Estorno), permitindo rastreabilidade total.

    Bloqueio de venda sem saldo.

🛒 3. Carrinho de Compras Inteligente

    Carrinho persistente por usuário.

    Price Snapshot: O sistema congela o preço do produto no momento da adição ao carrinho. Se o preço da loja mudar depois, o cliente paga o preço acordado na adição.

💳 4. Checkout e Pedidos

    Transformação de Carrinho em Pedido.

    Baixa automática de estoque no momento da venda.

    Cancelamento com Estorno: Se um pedido é cancelado, os itens retornam automaticamente para o estoque disponível.

🛡️ 5. Tratamento de Erros Global

    Respostas de erro padronizadas (ErrorResponseDto) para exceções de negócio e validações (@Valid), evitando que o cliente receba "stack traces" genéricas.

    Envelopamento de respostas de sucesso (ApiResponseDto) para feedback claro.

🏗️ Estrutura do Banco de Dados (Migrations)

O banco é gerenciado pelo Flyway, garantindo integridade e versionamento do schema.
Versão	Descrição
V1	Tabelas iniciais (Users, Produtos, Estoque Simples)
V2	Tabela de Categorias e relacionamentos
V3	Tabela de Transações de Estoque (Histórico)
V4	Tabelas de Carrinho e Itens do Carrinho
V5	Tabelas de Pedidos e Itens do Pedido

▶️ Como Rodar o Projeto

Pré-requisitos

    JDK 21 instalado.

    Maven instalado.

Passo a Passo

    Clone o repositório:
    Bash

git clone https://github.com/seu-usuario/AppProdutos.git
cd AppProdutos

Compile e Execute:
Bash

    mvn spring-boot:run

    Acesse a Documentação Interativa: O projeto subirá na porta 8080. Acesse o Swagger para testar os endpoints: 👉 http://localhost:8080/swagger-ui.html

🧪 Guia de Testes (Postman / Swagger)

Como o projeto utiliza banco H2 em memória, os dados são resetados a cada reinício. Siga este fluxo para popular e testar a API:

    Criar Administrador:

        POST /auth/register → {"login": "admin@loja.com", "password": "123", "role": "ADMIN"}

    Login (Obter Token):

        POST /auth/login → Copie o token gerado.

        No Swagger, clique no cadeado "Authorize" e insira o token.

    Criar Dados Base (Como Admin):

        POST /v1/categories → Crie uma categoria.

        POST /v1/produtos/criar → Crie um produto vinculado à categoria.

        POST /v1/inventory/add → Adicione saldo ao produto.

    Simular Compra (Como Cliente):

        Registre e logue um usuário com role CUSTOMER.

        POST /v1/cart/add → Adicione itens.

        POST /v1/orders/checkout → Finalize o pedido.

✅ Testes Unitários

O projeto possui cobertura de testes unitários para as regras de negócio críticas (Cálculo de Checkout, Estorno de Estoque, Validações de Saldo).

Para rodar os testes:
Bash

mvn test
