# MyHome

**MyHome** é uma plataforma digital de classificados imobiliários que conecta **proprietários**, **corretores**, **imobiliárias** e **potenciais compradores e locatários**. O sistema permite publicar anúncios de imóveis (venda, aluguel e temporada), realizar buscas avançadas, conversar por chat e concluir o fluxo de interesse até a confirmação de pagamento, utilizando **padrões de projeto** em Java.

Projeto desenvolvido na disciplina **Padrões de Projeto** (5º período), IFPB.

---

## Informações do Projeto

| Campo | Informação |
|-------|------------|
| **Disciplina** | Padrões de Projeto |
| **Período** | 5º |
| **Instituição** | IFPB |
| **Professor** | Alex Sandro da Cunha Rêgo |
| **Equipe** | Felipe Cartaxo, Leidiana Patrício e Matheus Barbosa |

---

## Descrição da Solução

O **MyHome** funciona como um **classificado imobiliário digital**: quem anuncia (proprietário, corretor ou imobiliária) cadastra imóveis e gerencia anúncios; quem busca (comprador ou locatário) filtra ofertas, visualiza detalhes, entra em contato e pode fechar negócio pela própria plataforma.

### Público e papéis

- **Proprietários, corretores e imobiliárias:** cadastram-se, entram no sistema por e-mail, criam anúncios (Casa, Apartamento, Terreno) para **venda**, **aluguel** ou **temporada**, com valores, área, descrição, suítes e bairro; recebem mensagens de chat e de confirmação de pagamento.
- **Compradores e locatários:** buscam imóveis com filtros (tipo, bairro, preço, área, quartos), visualizam anúncios, favoritam, conversam por chat com o anunciante e podem executar o pagamento (com confirmação pelo vendedor).

### Principais funcionalidades

- **Acesso ao sistema:** entrada por e-mail (cadastro automático se o usuário não existir); menu reduzido ao logar (mensagens do chat e confirmações de pagamento) e menu completo com todas as opções.
- **Anúncios:** criação de anúncios com tipo de oferta (venda/aluguel/temporada), valores, endereço, bairro (ex.: Jaguaribe, Castelo Branco, Bancários, Bessa, Tambaú, Altiplano), área, descrição e suítes; validação em cadeia (preço, título, imóvel) antes de publicar.
- **Busca avançada:** critérios opcionais e combináveis — o usuário escolhe quais usar (1=Tipo, 2=Bairro, 3=Preço, 4=Área, 5=Quartos) e informa apenas os valores desejados; filtros aplicados em conjunto (AND).
- **Visualização, favoritos e compra/aluguel:** a partir do anúncio ou da busca; escolha da forma de pagamento (cartão, PIX, boleto); geração de solicitação de pagamento aguardando confirmação do vendedor.
- **Chat:** conversas entre interessado e dono do anúncio, mediadas pela plataforma (padrão Mediator).
- **Confirmação de pagamento:** o vendedor vê as mensagens de pagamento pendentes e confirma o recebimento; ao confirmar, a compra é registrada e o anúncio é arquivado.
- **Auxiliares:** listagem de compras, log de mudanças de estado dos anúncios, configuração centralizada (`config.properties`), carregamento de dados iniciais por CSV (`dados/usuarios.csv`, `dados/anuncios.csv`).

O sistema utiliza **padrões de projeto** (State, Strategy, Observer, Adapter, Mediator, Factory Method, Chain of Responsibility, Singleton e filtros extensíveis na busca) para organizar o código e atender aos requisitos funcionais e não funcionais.

---

## Como Colocar o Projeto em Funcionamento

### Pré-requisitos

- **Java 11** ou superior (JDK).
- **Maven 3.x** (opcional; se não tiver Maven, os scripts usam `javac` e `java` diretamente no Windows).

### Execução

**Com Maven (Linux/Mac/Windows):**

```bash
# Na raiz do projeto
mvn compile exec:java
```

**Scripts na raiz do projeto:**

- **Linux/Mac:** `./run.sh` (usa Maven).
- **Windows:** `run.bat` (tenta Maven; se não encontrar, compila com `javac` e executa com `java`).

**Execução manual sem Maven (qualquer SO):**

```bash
# Compilar
javac -d bin -encoding UTF-8 -sourcepath src/main/java src/main/java/br/edu/ifpb/myhome/**/*.java src/main/java/br/edu/ifpb/myhome/Main.java
# (ou incluir todos os .java do projeto)

# Executar
java -cp bin br.edu.ifpb.myhome.Main
```

### Estrutura de dados (CSV)

- **`dados/usuarios.csv`**: `nome;email` (uma linha por usuário).
- **`dados/anuncios.csv`**: colunas `titulo;preco;tipo;endereco;idDono;tipoOferta;valorVenda;valorAluguel;valorTemporada;area;descricao;suites;bairro;extras...` (ver comentários no próprio arquivo para detalhes e exemplos de bairros: Jaguaribe, Castelo Branco, Bancários, Bessa, Tambaú, Altiplano).

Se os arquivos existirem, usuários e anúncios são carregados automaticamente ao iniciar a aplicação.

---

## Classes do Projeto

### Pacote principal

| Classe | Descrição |
|--------|-----------|
| `Main` | Ponto de entrada; menu principal, fluxos de usuário, busca, chat, pagamento e integração com os serviços. |

### `anuncio`

| Classe | Descrição |
|--------|-----------|
| `Anuncio` | Entidade anúncio; tipo de oferta (venda/aluguel/temporada), valores, imóvel, estado e observers. |
| `InteressadosAnuncio` | Gerencia lista de usuários interessados no anúncio. |
| `LogMudancaEstado` | Registra mudanças de estado do anúncio (RF04). |
| `TipoOferta` | Enum: VENDA, ALUGUEL, TEMPORADA. |

### `busca` (RF06 – Busca avançada)

| Classe | Descrição |
|--------|-----------|
| `BuscaService` | Serviço de busca; aplica lista de filtros (AND). |
| `FiltroBusca` | Interface dos filtros de busca. |
| `FiltroTipoImovel` | Filtro por tipo (casa, apartamento, terreno). |
| `FiltroBairro` | Filtro por bairro. |
| `FiltroPreco` | Filtro por faixa de preço. |
| `FiltroArea` | Filtro por faixa de área (m²). |
| `FiltroQuartos` | Filtro por número mínimo de quartos (apartamentos). |
| `FiltroTitulo` | Filtro por trecho do título. |

### `chat` (Mediator)

| Classe | Descrição |
|--------|-----------|
| `ChatMediator` | Interface do mediador de chat. |
| `Conversa` | Mediador concreto; centraliza mensagens entre interessado e dono do anúncio. |
| `Mensagem` | Modelo de uma mensagem (texto, remetente, data). |

### `compra`

| Classe | Descrição |
|--------|-----------|
| `Compra` | Registro de compra/aluguel concluído. |
| `SolicitacaoCompra` | Solicitação de pagamento aguardando confirmação do vendedor. |

### `config` (RF07)

| Classe | Descrição |
|--------|-----------|
| `Configuracao` | Singleton; configuração centralizada (config.properties). |

### `csv` (E1)

| Classe | Descrição |
|--------|-----------|
| `CarregadorCSV` | Carrega usuários e anúncios a partir de arquivos CSV. |

### `estado` (State)

| Classe | Descrição |
|--------|-----------|
| `EstadoAnuncio` | Interface dos estados do anúncio. |
| `RascunhoState` | Estado inicial. |
| `ModeracaoState` | Em moderação. |
| `AtivoState` | Ativo (disponível). |
| `ArquivadoState` | Arquivado (vendido/alugado). |
| `SuspensoState` | Suspenso. |
| `VendidoState` | Vendido. |

### `factory`

| Classe | Descrição |
|--------|-----------|
| `ImovelFactory` | Classe base abstrata da fábrica de imóveis. |
| `CasaFactory` | Fábrica de casas. |
| `ApartamentoFactory` | Fábrica de apartamentos. |
| `TerrenoFactory` | Fábrica de terrenos. |

### `imovel`

| Classe | Descrição |
|--------|-----------|
| `Imovel` | Classe base abstrata (endereço, bairro, área, descrição, suítes). |
| `Casa` | Casa (possui quintal). |
| `Apartamento` | Apartamento (quartos, andar, elevador). |
| `Terreno` | Terreno. |
| `Galpao` | Galpão. |
| `SalaComercial` | Sala comercial. |

### `notificacao` (Observer + Adapter)

| Classe | Descrição |
|--------|-----------|
| `Observer` | Interface do observador. |
| `NotificacaoObserver` | Observador que notifica via serviço externo (e-mail/SMS). |
| `NotificacaoInteressadosObserver` | Observador que notifica interessados; usa `Saida`. |
| `ServicoNotificacaoExterno` | Interface do serviço externo de notificação. |
| `EmailAdapter` | Adapter para envio por e-mail. |
| `EmailApi` | API fictícia de e-mail. |
| `Notificacao` | Modelo de notificação. |
| `SmsAdapter` | Adapter para SMS. |

### `pagamento` (Strategy)

| Classe | Descrição |
|--------|-----------|
| `FormaPagamento` | Interface Strategy das formas de pagamento. |
| `PagamentoCartao` | Pagamento com cartão. |
| `PagamentoPix` | Pagamento com PIX. |
| `PagamentoBoleto` | Pagamento com boleto. |
| `ServicoPagamento` | Contexto que usa `FormaPagamento`. |
| `PlanoAssinatura` | Enum de planos (Básico, Premium, Ilimitado). |

### `prototype`

| Classe | Descrição |
|--------|-----------|
| `ImovelPrototype` | Interface do protótipo de imóvel. |
| `CasaPadrao` | Protótipo de casa. |
| `ApartamentoPadrao` | Protótipo de apartamento. |
| `TerrenoPadrao` | Protótipo de terreno. |
| `ImovelPrototypeRegistry` | Registro de protótipos. |

### `saida` (E2)

| Classe | Descrição |
|--------|-----------|
| `Saida` | Interface de saída (evita System.out no domínio). |
| `ConsoleSaida` | Implementação para console. |

### `usuario`

| Classe | Descrição |
|--------|-----------|
| `Usuario` | Entidade usuário (Colleague no chat); nome, e-mail, métodos de anúncio, chat e favoritos. |

### `util`

| Classe | Descrição |
|--------|-----------|
| `FormatadorMoeda` | Formatação de valores em Real (R$). |

### `validacao` (Chain of Responsibility)

| Classe | Descrição |
|--------|-----------|
| `ValidadorAnuncio` | Classe base abstrata da cadeia de validação. |
| `ValidadorPreco` | Valida preço. |
| `ValidadorTitulo` | Valida título. |
| `ValidadorImovel` | Valida imóvel. |

---

## Padrões Utilizados e Onde

| Padrão | Onde | Descrição |
|--------|------|------------|
| **State** | `estado/` | Estados do anúncio (Rascunho, Moderação, Ativo, Arquivado, Suspenso, Vendido). `Anuncio` delega comportamento ao estado atual (`EstadoAnuncio`). |
| **Strategy** | `pagamento/` | Formas de pagamento (Cartão, PIX, Boleto) implementam `FormaPagamento`; `ServicoPagamento` usa a estratégia escolhida. |
| **Observer** | `anuncio/Anuncio` + `notificacao/` | `Anuncio` mantém lista de `Observer`; ao mudar estado, notifica. `NotificacaoObserver` e `NotificacaoInteressadosObserver` reagem às mudanças. |
| **Adapter** | `notificacao/` | `EmailAdapter` e `SmsAdapter` implementam `ServicoNotificacaoExterno`, adaptando APIs externas (e-mail/SMS) ao uso pelo sistema. |
| **Mediator** | `chat/` | `Conversa` é o mediador; `Usuario` (interessado e dono) não se comunica diretamente, apenas via `ChatMediator` (envio de mensagens). |
| **Factory Method** | `factory/` | `ImovelFactory` define `criarImovel()`; `CasaFactory`, `ApartamentoFactory` e `TerrenoFactory` criam o tipo correspondente. |
| **Chain of Responsibility** | `validacao/` | `ValidadorAnuncio` encadeia validadores (Preço → Título → Imóvel); cada um pode repassar ao próximo ou rejeitar. |
| **Singleton** | `config/Configuracao` | Uma única instância de configuração; `getInstancia()` retorna o mesmo objeto; parâmetros carregados de `config.properties`. |
| **Template Method / Estratégia de filtro** | `busca/` | `FiltroBusca` define contrato `aceita(Anuncio)`; filtros concretos (Tipo, Bairro, Preço, Área, Quartos, Título) são combinados dinamicamente pelo `BuscaService` sem alterar o código de busca. |

---

## Especificação de Como Cada Requisito Foi Resolvido

### RF04 – Log de mudanças de estado do anúncio

- **Onde:** `anuncio/LogMudancaEstado`, `estado/EstadoAnuncio` e suas implementações.
- **Como:** Ao mudar o estado do anúncio (`setEstado` em `Anuncio`), é chamado `LogMudancaEstado.getInstancia().registrar(anuncio, estadoAnterior, estadoNovo)`. O log mantém o histórico de transições (anúncio, estado anterior, estado novo). A opção **Consultar log de mudanças de estado** no menu principal exibe essas entradas.

### RF06 – Busca avançada

- **Onde:** `busca/BuscaService`, `busca/FiltroBusca` e implementações (`FiltroTipoImovel`, `FiltroBairro`, `FiltroPreco`, `FiltroArea`, `FiltroQuartos`, `FiltroTitulo`).
- **Como:** O usuário escolhe **quais** critérios usar (1=Tipo, 2=Bairro, 3=Preço, 4=Área, 5=Quartos), informando os números separados por vírgula (ex.: `1,2` para tipo e bairro). O sistema pergunta apenas os valores dos critérios selecionados. Uma lista de `FiltroBusca` é montada e passada a `BuscaService.buscar(anuncios, filtros)`, que aplica todos em **AND**. Novos filtros podem ser adicionados criando novas classes que implementam `FiltroBusca`, sem alterar o `BuscaService`. Filtros por tipo de imóvel e por quartos são específicos (apartamento, etc.). Bairros de exemplo: Jaguaribe, Castelo Branco, Bancários, Bessa, Tambaú, Altiplano.

### RF07 – Configuração centralizada

- **Onde:** `config/Configuracao`, arquivo `config.properties` (raiz do projeto ou `src/main/resources`).
- **Como:** `Configuracao` é um **Singleton**; `getInstancia()` retorna a única instância. No início da aplicação, `Configuracao.getInstancia().carregarParametros()` lê taxas, limites, termos e URLs do `config.properties`. O restante do sistema pode obter parâmetros via essa instância.

### E1 – Carregar dados iniciais de CSV

- **Onde:** `csv/CarregadorCSV`, arquivos `dados/usuarios.csv` e `dados/anuncios.csv`.
- **Como:** No `Main`, se existirem os arquivos, são chamados `CarregadorCSV.carregarUsuarios(path)` e `CarregadorCSV.carregarAnuncios(path, ...)`. O carregador lê linha a linha (ignorando comentários `#`), faz o parse por `;` e preenche listas de usuários e anúncios e o mapa dono-do-anúncio. O CSV de anúncios suporta tipo de oferta, valores (venda/aluguel/temporada), área, descrição, suítes e bairro.

### E2 – Abstração de saída (evitar System.out no domínio)

- **Onde:** `saida/Saida`, `saida/ConsoleSaida`, uso em `Main` e em `NotificacaoInteressadosObserver`.
- **Como:** A interface `Saida` define `escrever(String)` e `escreverSemQuebra(String)`. `ConsoleSaida` implementa com `System.out`. O `Main` e os observadores que precisam exibir mensagens recebem/referenciam `Saida`, permitindo trocar o canal de saída (console, log, UI) sem alterar o domínio.

### Funcionalidades adicionais implementadas

- **Entrada por e-mail:** Opção “Entrar no sistema” pede o e-mail; se o usuário existir, é logado; se não, pode cadastrar (nome + e-mail).
- **Menu reduzido:** Após entrar, é exibido um menu com mensagens do chat e mensagens de confirmação de pagamento; opção “Menu completo” para acessar todas as funções.
- **Criar anúncio / Buscar sem login obrigatório:** Se não houver usuário logado, o sistema pede que se escolha um usuário da lista para criar anúncio ou para buscar/visualizar/favoritar/comprar.
- **Pagamento e confirmação:** Ao comprar/alugar (na busca ou ao visualizar anúncio), o comprador escolhe forma de pagamento; é criada uma `SolicitacaoCompra`. O vendedor vê as mensagens em “Ver mensagens de confirmação de pagamento” e pode confirmar o recebimento; ao confirmar, a solicitação é removida, a compra é registrada e o anúncio passa a `ArquivadoState`.
- **Formatação de preços:** Valores exibidos em Real (R$) via `FormatadorMoeda.formatarReal()`.
- **Validação na criação de anúncio:** Cadeia de validadores (Preço, Título, Imóvel) em Chain of Responsibility antes de adicionar o anúncio.

---

## Estrutura de Diretórios

```
myhome/
├── README.md
├── pom.xml
├── config.properties
├── run.sh
├── run.bat
├── dados/
│   ├── usuarios.csv
│   └── anuncios.csv
└── src/main/
    ├── java/br/edu/ifpb/myhome/
    │   ├── Main.java
    │   ├── anuncio/
    │   ├── busca/
    │   ├── chat/
    │   ├── compra/
    │   ├── config/
    │   ├── csv/
    │   ├── estado/
    │   ├── factory/
    │   ├── imovel/
    │   ├── notificacao/
    │   ├── pagamento/
    │   ├── prototype/
    │   ├── saida/
    │   ├── usuario/
    │   ├── util/
    │   └── validacao/
    └── resources/
        └── config.properties
```

---

## Licença e Autoria

Projeto acadêmico desenvolvido no IFPB – Disciplina **Padrões de Projeto** (5º período), Professor **Alex Sandro da Cunha Rêgo**.  
**Equipe:** Felipe Cartaxo, Leidiana Patrício e Matheus Barbosa.
