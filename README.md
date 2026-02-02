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
- **Anúncios (RF02):** criação de anúncios a partir de **configuração padrão** por tipo de imóvel (casa, apartamento, terreno). Ex.: apartamento inicia com unidade habitacional em condomínio, 2 quartos, 60 m², 1 suíte, andar 1; casa com 80 m², 1 suíte; terreno com 200 m². O usuário pode manter os valores padrão (Enter) ou alterar. Tipo de oferta (venda/aluguel/temporada), valores, endereço, bairro; validação em cadeia (preço, título, imóvel) antes de publicar.
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

### `prototype` (RF02 – Prototype)

| Classe | Descrição |
|--------|-----------|
| `ImovelPrototype` | Interface do protótipo de imóvel (clone). |
| `CasaPadrao` | Protótipo de casa: 80 m², 1 suíte, "Casa residencial padrão". |
| `ApartamentoPadrao` | Protótipo de apartamento: unidade habitacional em condomínio, 2 quartos, 60 m², 1 suíte, andar 1. |
| `TerrenoPadrao` | Protótipo de terreno: 200 m², "Terreno padrão". |
| `ImovelPrototypeRegistry` | Registro de protótipos; `criarRegistroPadrao()` e `clonarPrototipo(tipo)`; novos tipos podem ser registrados no futuro. |

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
| **Prototype** | `prototype/` (RF02) | Cada tipo de imóvel (casa, apartamento, terreno) tem um protótipo com configuração padrão; `ImovelPrototypeRegistry.clonarPrototipo(tipo)` retorna uma cópia; novos tipos e configurações podem ser registrados no futuro. |
| **Template Method / Estratégia de filtro** | `busca/` | `FiltroBusca` define contrato `aceita(Anuncio)`; filtros concretos (Tipo, Bairro, Preço, Área, Quartos, Título) são combinados dinamicamente pelo `BuscaService` sem alterar o código de busca. |

---

## Requisitos do Projeto

Lista dos requisitos considerados no projeto e onde/how são atendidos:

| ID | Requisito | Onde / Como foi resolvido |
|----|-----------|---------------------------|
| **RF01** | Cadastro e gestão de usuários e anúncios | Menu principal: Cadastrar usuário (nome, e-mail), Listar usuários, Criar anúncio (com tipo de oferta, valores, imóvel, validação em cadeia). Entrada por e-mail; escolha de usuário quando não logado. |
| **RF02** | Instâncias de anúncios padrão para certos tipos de imóveis | Prototype: `CasaPadrao`, `ApartamentoPadrao`, `TerrenoPadrao` com configuração padrão; `ImovelPrototypeRegistry.criarRegistroPadrao()` e `clonarPrototipo(tipo)`; criação de anúncio inicia com clone do protótipo; campos com valor padrão entre colchetes (Enter mantém). |
| **RF03** | Publicação e moderação | Anúncios criados em **Rascunho**; opção **Submeter anúncio** aplica regras dinâmicas (termos proibidos, preço mínimo, descrição/fotos mínimas via `ServicoModeracao`); aprovado vai direto para **Ativo** (moderação automática). Busca/visualização exibem apenas anúncios **Ativos**. |
| **RF04** | Log de mudanças de estado do anúncio | `LogMudancaEstado` (singleton) registra cada mudança (anúncio, estado anterior, estado novo); chamado em `Anuncio.setEstado`; opção "Consultar log de mudanças de estado" no menu. |
| **RF05** | Chat e notificações | **Chat:** Mediator em `chat/` (`ChatMediator`, `Conversa`); usuários enviam mensagens via mediador. **Notificações:** Observer em `notificacao/` (`Observer`, `NotificacaoObserver`, `NotificacaoInteressadosObserver`); Adapter para e-mail/SMS (`ServicoNotificacaoExterno`, `EmailAdapter`); `Anuncio` notifica observers ao mudar estado. |
| **RF06** | Busca avançada (múltiplos critérios, combináveis, extensível) | Strategy em `busca/`: `FiltroBusca` e implementações (Tipo, Bairro, Preço, Área, Quartos, Título); `BuscaService.buscar(anuncios, filtros)` aplica AND; usuário escolhe critérios (1,2 ou 1,2,3 etc.); novos filtros sem alterar `BuscaService`. |
| **RF07** | Configuração centralizada | Singleton `Configuracao`; `getInstancia().carregarParametros()` lê `config.properties`; taxas, limites, termos, URLs centralizados. |
| **E1** | Carregar dados iniciais de CSV | `CarregadorCSV.carregarUsuarios(path)` e `carregarAnuncios(path, ...)`; `dados/usuarios.csv` e `dados/anuncios.csv`; parse por `;`, suporte a tipo de oferta, área, descrição, suítes, bairro. |
| **E2** | Abstração de saída (evitar System.out no domínio) | Interface `Saida` e `ConsoleSaida`; `Main` e observadores usam `Saida` para mensagens; facilita troca por log ou UI. |

---

## Especificação de Como Cada Requisito Foi Resolvido

### RF01 – Cadastro e gestão de usuários e anúncios

- **Onde:** `Main` (menu e fluxos), `usuario/Usuario`, `anuncio/Anuncio`, `imovel/`, `validacao/` (Chain of Responsibility).
- **Como:** Opção **Cadastrar usuário** pede nome e e-mail e adiciona à lista. **Listar usuários** exibe todos. **Criar anúncio** exige usuário (logado ou escolhido na lista); pede título, tipo de oferta (venda/aluguel/temporada), valores, tipo de imóvel (Casa, Apartamento, Terreno); o imóvel é obtido por **RF02** (clone do protótipo) e o usuário pode manter padrões ou alterar (endereço, bairro, área, descrição, suítes, quartos/andar/elevador/quintal); validação em cadeia (Preço → Título → Imóvel) antes de publicar. **Entrar no sistema** por e-mail; se não existir usuário, pode cadastrar na hora.

### RF02 – Instâncias de anúncios padrão para certos tipos de imóveis

- **Onde:** `prototype/ImovelPrototype`, `CasaPadrao`, `ApartamentoPadrao`, `TerrenoPadrao`, `ImovelPrototypeRegistry`; uso em `Main` na criação de anúncio.
- **Como:** Ao criar um anúncio, o usuário escolhe o tipo (Casa, Apartamento, Terreno). O sistema obtém uma **cópia** da configuração padrão via `ImovelPrototypeRegistry.criarRegistroPadrao()` e `clonarPrototipo("casa"|"apartamento"|"terreno")`. **Apartamento** inicia com: unidade habitacional em condomínio, 2 quartos, 60 m², 1 suíte, andar 1, sem elevador. **Casa** inicia com: 80 m², 1 suíte, descrição "Casa residencial padrão", sem quintal. **Terreno** inicia com: 200 m², descrição "Terreno padrão". Os campos são exibidos com o valor padrão entre colchetes (ex.: Área (m²) [60]:); o usuário pode pressionar Enter para manter o padrão ou digitar outro valor. Novas configurações podem ser adicionadas no futuro registrando novos protótipos no registro.

### RF03 – Publicação e moderação

- **Onde:** `estado/EstadoAnuncio`, `RascunhoState`, `AtivoState`, `ArquivadoState`; `moderacao/ServicoModeracao`, `ResultadoModeracao`; `config.properties` (regras dinâmicas: `termos.improprios`, `preco.minimo`, `moderacao.descricao.minimo`, `moderacao.fotos.minimo`); `Anuncio.quantidadeFotos`; uso em `Main` (opção 11).
- **Como:** O anunciante cria o anúncio (estado **Rascunho**); na criação é informada a **quantidade de fotos**. Para publicar, usa a opção **11 - Submeter anúncio**: o sistema valida automaticamente (moderação automática): título e descrição não podem conter termos proibidos; preço deve ser condizente (mínimo configurável, por tipo de oferta); anúncio deve ter ao menos uma foto OU descrição com quantidade mínima de caracteres. Se aprovado, o anúncio vai direto para **Ativo** (publicado); se reprovado, permanece em Rascunho e os erros são exibidos. Busca de imóveis, visualização e favoritos exibem apenas anúncios **Ativos**. `Anuncio.setEstado(novoEstado)` dispara o log (RF04) e notificações (RF05). Ao confirmar pagamento pelo vendedor, o anúncio passa a `ArquivadoState`.

### RF04 – Log de mudanças de estado do anúncio

- **Onde:** `anuncio/LogMudancaEstado`, `estado/EstadoAnuncio` e suas implementações.
- **Como:** Ao mudar o estado do anúncio (`setEstado` em `Anuncio`), é chamado `LogMudancaEstado.getInstancia().registrar(anuncio, estadoAnterior, estadoNovo)`. O log mantém o histórico de transições (anúncio, estado anterior, estado novo). A opção **Consultar log de mudanças de estado** no menu principal exibe essas entradas.

### RF05 – Chat e notificações

- **Onde:** **Chat:** `chat/ChatMediator`, `Conversa`, `Mensagem`; `usuario/Usuario.enviarMensagemVia(mediator, texto)`. **Notificações:** `notificacao/Observer`, `NotificacaoObserver`, `NotificacaoInteressadosObserver`; `notificacao/ServicoNotificacaoExterno`, `EmailAdapter`; `Anuncio` mantém lista de observers e chama `notificar()` ao mudar estado.
- **Como:** **Chat:** Padrão Mediator — usuários não se comunicam diretamente; enviam mensagens via `Conversa` (mediador). Opção **Chat** no menu permite “Interessado: conversar sobre um anúncio” e “Dono do anúncio: ver e responder conversas”. **Notificações:** Padrão Observer — ao mudar estado do anúncio, todos os observers são notificados; `NotificacaoObserver` usa serviço externo (e-mail); `NotificacaoInteressadosObserver` usa `Saida` para exibir. Adapter adapta APIs externas (e-mail/SMS) à interface `ServicoNotificacaoExterno`.

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
