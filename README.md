# MapShelf 🛒📱

> **Sistema Guia para Clientes em Mercados — Foco em Acessibilidade e Inclusão de Idosos**

O **MapShelf** é uma solução tecnológica em formato de aplicativo voltada para facilitar a locomoção, orientação espacial e localização de produtos dentro de supermercados. O projeto foi concebido a partir de dados demográficos do IBGE que apontam o crescimento do envelhecimento populacional no Brasil, contrastando com a falta de adaptação dos ambientes de consumo para as demandas específicas da terceira idade (problemas de legibilidade, trocas constantes de gôndolas e fadiga).

Este repositório contém a documentação técnica, modelagem de dados e os requisitos estruturados para o desenvolvimento da plataforma.

---

## 🚀 Funcionalidades Principais

- **Mapeamento de Gôndolas:** Orientação espacial precisa indicando corredores, fileiras e prateleiras.
- **Busca Simplificada de Produtos:** Interface adaptada com alta legibilidade para encontrar itens rapidamente.
- **Suporte Operacional:** Auxílio aos gestores na organização do layout do estabelecimento e controle de estoque associado à posição física.
- **Foco em Acessibilidade:** Design centrado no usuário, priorizando contraste, tamanho de fonte adequado e caminhos otimizados.

---

## 🛠️ Tecnologias Cobertas na Modelagem

- **Engenharia de Requisitos:** Casos de uso voltados para dois perfis principais: *Administrador* e *Cliente*.
- **Banco de Dados:** Modelo Entidade-Relacionamento (MER) estruturado para consistência física e lógica dos produtos, layouts e usuários.

---

## 📊 Modelagem do Sistema

### 1. Diagrama de Casos de Uso
O escopo de interações do sistema foi desenhado separando as permissões de gerenciamento de estoque e gôndolas das ações de consulta direta do consumidor final.

### 2. Dicionário de Dados (Estrutura do Banco)

O banco de dados é estruturado sobre três entidades fundamentais de controle:

* **Usuário:** Armazena credenciais e permissões (`ID`, `Nome`, `E-mail`, `Senha`, `Tipo de Usuário`).
* **Produto:** Contém os dados mercadológicos e físicos do produto (`ID`, `Nome`, `Marca`, `Tipo`, `Preço`, `Peso`, `Volume`, `Quantidade`, `Número da Gôndola`).
* **Layout:** Mapeia a arquitetura física do supermercado (`Número da Gôndola`, `Corredor`, `Fileira`, `Prateleira`).

---

## 👥 Integrantes do Grupo

- Eduardo Alves dos Santos
- Abraão
- Angelo Ribeiro
- Kaique Miranda
- Matheus Sebrian

---

## 📄 Licença

Este projeto foi desenvolvido como atividade acadêmica de extensão universitária voltada ao impacto social e melhoria da jornada de compra de públicos vulneráveis.
