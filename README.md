SoulAI — API RESTful de Microgestão e EngajamentoO SoulAI é um ecossistema desenvolvido para potencializar a produtividade e o bem-estar através da gamificação de tarefas diárias e rotinas. Esta API RESTful, desenvolvida com Spring Boot, gerencia o controle de usuários, avatares, inventários, lojas de acessórios e o sistema de missões diárias com cálculo automático de recompensas.
🌐 Deploy na Nuvem (Render)
A aplicação encontra-se publicada e ativa na plataforma Render:
URL Base da API: https://soulai-api.onrender.com
⚠️ Observação sobre o acesso via Navegador (Erro 404):
A URL raiz da API (https://soulai-api.onrender.com/) não possui uma rota pública mapeada, 
portanto abrir este link diretamente no navegador retornará uma página de erro 404 Not Found.
Isso é o comportamento esperado e não indica que o deploy esteja quebrado.

Para testar a API ativa, acesse uma rota válida especificada na tabela de endpoints, por exemplo:
GET Missões Ativas: https://soulai-api.onrender.com/missao/ativas
(Nota: Por utilizar o plano gratuito do Render, a primeira requisição pode levar até 1 minuto para responder devido ao ciclo de hibernação do container).

🛠️ Tecnologias Utilizadas
Linguagem:Java 17
Framework: Spring Boot 3.3.x (Web, Validation)
Banco de Dados: Oracle Autonomous Database (JDBC / Oracle Driver)
Gerenciador de Dependências: Apache Maven
Containers: Docker (Dockerfile multi-stage build)
Hospedagem / PaaS: Render
🚀 Como Executar o Projeto 
LocalmentePré-requisitosJava JDK 17 ou superior instaladoMaven instalado (ou utilizar o wrapper ./mvnw)
Conexão com a internet para acesso ao banco Oracle Cloud (detalhes em application.properties)
Passo a passo
Clone o repositório:git clone https://github.com/Lenahalawi07/entrega_final_java.git
cd entrega_final_java
Compile o projeto e baixe as dependências:./mvnw clean package -DskipTests
Execute a aplicação Spring Boot:./mvnw spring-boot:run
A aplicação estará disponível localmente em: http://localhost:8080📂 Arquivos de Entrega no Repositório/src: Código-fonte Java completo do projeto.Documentacao_SoulAI_Sprint4.pdf: Documentação técnica detalhada em formato PDF.SoulAi.zip: Arquivo compactado com o código do projeto Java.Dockerfile: Script de automação para construção do container no Render.
