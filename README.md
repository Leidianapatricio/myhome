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

### `chat` (RF08 – Mediator)

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

### `moderacao` (RF03)

| Classe | Descrição |
|--------|-----------|
| `ServicoModeracao` | Valida anúncio contra regras dinâmicas (termos proibidos, preço mínimo, descrição/fotos mínimas). |
| `ResultadoModeracao` | Encapsula resultado da validação (aprovado ou lista de erros). |

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
| **Mediator** | `chat/` (RF08) | `Conversa` é o mediador; `Usuario` (interessado e dono) não se comunica diretamente, apenas via `ChatMediator` (envio de mensagens). |
| **Factory Method** | `factory/` | `ImovelFactory` define `criarImovel()`; `CasaFactory`, `ApartamentoFactory` e `TerrenoFactory` criam o tipo correspondente. |
| **Chain of Responsibility** | `validacao/` | `ValidadorAnuncio` encadeia validadores (Preço → Título → Imóvel); cada um pode repassar ao próximo ou rejeitar. |
| **Singleton** | `config/Configuracao` | Uma única instância de configuração; `getInstancia()` retorna o mesmo objeto; parâmetros carregados de `config.properties`. |
| **Prototype** | `prototype/` (RF02) | Cada tipo de imóvel (casa, apartamento, terreno) tem um protótipo com configuração padrão; `ImovelPrototypeRegistry.clonarPrototipo(tipo)` retorna uma cópia; novos tipos e configurações podem ser registrados no futuro. |
| **Template Method / Estratégia de filtro** | `busca/` | `FiltroBusca` define contrato `aceita(Anuncio)`; filtros concretos (Tipo, Bairro, Preço, Área, Quartos, Título) são combinados dinamicamente pelo `BuscaService` sem alterar o código de busca. |

---

## Requisitos do Projeto

Lista dos requisitos na ordem do enunciado (PDF) e onde/how são atendidos:

| ID | Requisito | Onde / Como foi resolvido |
|----|-----------|---------------------------|
| **RF01** | Criação de Anúncios | Cadastro de anúncios de diferentes tipos (Casa, Apartamento, Terreno); processo guiado; atributos obrigatórios: título, tipo do imóvel, preço; validação em cadeia (Chain of Responsibility). Menu: Cadastrar usuário, Listar usuários, Criar anúncio; entrada por e-mail. |
| **RF02** | Instâncias de anúncios padrão para certos tipos de imóveis | Prototype: `CasaPadrao`, `ApartamentoPadrao`, `TerrenoPadrao` com configuração padrão; `ImovelPrototypeRegistry.criarRegistroPadrao()` e `clonarPrototipo(tipo)`; criação inicia com clone do protótipo; campos com valor padrão entre colchetes (Enter mantém). |
| **RF03** | Publicação e moderação | Anúncios criados em **Rascunho**; opção **Submeter anúncio** aplica regras dinâmicas (termos proibidos, preço mínimo, descrição/fotos mínimas via `ServicoModeracao`); aprovado vai para **Ativo** (moderação automática). Busca/visualização exibem apenas anúncios **Ativos**. |
| **RF04** | Fases do ciclo de vida de um anúncio | Estados: Rascunho, Moderação, Ativo, Vendido/Alugado (Arquivado), Suspenso. Sempre que o anúncio mudar de estado, o anunciante é notificado (Observer) e um log registra a mudança (`LogMudancaEstado` em `Anuncio.setEstado`). Opção "Consultar log de mudanças de estado" no menu. |
| **RF05** | Notificação do usuário | Sistema notifica usuários sobre eventos (ex.: mudança de estado do anúncio). Solução flexível para alterar o canal de notificação (Observer + Adapter). Implementado: notificação via e-mail (`EmailAdapter`, `ServicoNotificacaoExterno`); `Anuncio` notifica observers ao mudar estado. |
| **RF06** | Busca avançada | Múltiplos critérios: tipo de imóvel, bairro (localização), faixa de preço, área, número de quartos, trecho do título, tipo de oferta (venda/aluguel/temporada), número mínimo de suítes. Filtros combinados dinamicamente (AND); filtros específicos por tipo de imóvel. `FiltroBusca` e implementações; `BuscaService.buscar(anuncios, filtros)`; novos filtros sem modificar código de busca. |
| **RF07** | Configuração centralizada | Taxas de comissão, limites de upload, termos impróprios, URLs de serviços externos carregados de fonte única e acessível globalmente. Singleton `Configuracao`; `config.properties`; `getInstancia().carregarParametros()`. |
| **RF08** | Novo padrão (Chat) | Requisito adicional resolvido com padrão **Mediator**: chat entre interessado e dono do anúncio. `Conversa` é o mediador; usuários enviam mensagens via `ChatMediator`; opção **Chat** no menu (interessado conversa sobre anúncio; dono vê e responde conversas). |
| **E1** | Povoar dados automaticamente a partir de CSV | `CarregadorCSV.carregarUsuarios(path)` e `carregarAnuncios(path, ...)`; `dados/usuarios.csv` e `dados/anuncios.csv`; parse por `;`; usuários e anúncios carregados ao iniciar a aplicação. |
| **E2** | Não usar System.out no domínio; fluxo de mensagens reutilizável | Interface `Saida` e `ConsoleSaida`; `Main` e observadores usam `Saida` para exibir mensagens; facilita troca por log ou UI sem alterar o domínio. |

---

## Especificação de Como Cada Requisito Foi Resolvido

### RF01 – Criação de Anúncios

- **Onde:** `Main` (menu e fluxos), `usuario/Usuario`, `anuncio/Anuncio`, `imovel/`, `validacao/` (Chain of Responsibility).
- **Como:** O sistema permite o cadastro de anúncios de diferentes tipos de imóveis (Casa, Apartamento, Terreno). O processo de criação é guiado: título, tipo do imóvel e preço são obrigatórios; cada tipo possui características específicas (apartamento: andar, elevador; casa: quintal). Opção **Cadastrar usuário** (nome, e-mail), **Listar usuários**, **Criar anúncio** (tipo de oferta venda/aluguel/temporada, valores, imóvel obtido por RF02, validação em cadeia Preço → Título → Imóvel). Entrada por e-mail; escolha de usuário quando não logado. Flexível para adicionar novos tipos de imóveis (factory, prototype).

### RF02 – Instâncias de anúncios padrão para certos tipos de imóveis

- **Onde:** `prototype/ImovelPrototype`, `CasaPadrao`, `ApartamentoPadrao`, `TerrenoPadrao`, `ImovelPrototypeRegistry`; uso em `Main` na criação de anúncio.
- **Como:** Certos tipos de anúncios, quando criados, iniciam com configuração padrão. Apartamento: unidade habitacional em condomínio, 2 quartos, 60 m², 1 suíte, andar 1, sem elevador. Casa: 80 m², 1 suíte, descrição "Casa residencial padrão", sem quintal. Terreno: 200 m², descrição "Terreno padrão". O sistema obtém uma cópia via `ImovelPrototypeRegistry.criarRegistroPadrao()` e `clonarPrototipo("casa"|"apartamento"|"terreno")`. Campos exibidos com valor padrão entre colchetes (Enter mantém). Novas configurações podem ser registradas no futuro.

### RF03 – Publicação e moderação

- **Onde:** `estado/EstadoAnuncio`, `RascunhoState`, `AtivoState`, `ArquivadoState`; `moderacao/ServicoModeracao`, `ResultadoModeracao`; `config.properties` (regras dinâmicas: `termos.improprios`, `preco.minimo`, `moderacao.descricao.minimo`, `moderacao.fotos.minimo`); `Anuncio.quantidadeFotos`; uso em `Main` (opção 11).
- **Como:** O anunciante submete o anúncio; todos passam por etapa de moderação antes de se tornarem públicos. Regras dinâmicas: título e descrição não podem conter termos proibidos; preço condizente (mínimo configurável); ao menos uma foto ou quantidade mínima de texto na descrição. Moderação automática: opção **Submeter anúncio** valida; aprovado vai para **Ativo**; reprovado permanece em Rascunho com erros exibidos. Busca/visualização exibem apenas anúncios **Ativos**.

### RF04 – Fases do ciclo de vida de um anúncio

- **Onde:** `estado/EstadoAnuncio` (RascunhoState, ModeracaoState, AtivoState, ArquivadoState, SuspensoState), `anuncio/LogMudancaEstado`; `Anuncio` delega ao estado atual e chama log e notificação ao mudar.
- **Como:** Cada anúncio tem ciclo de vida: **Rascunho** (inicial) → **Moderação** (em revisão) → **Ativo** (aprovado e visível) → **Vendido/Alugado** (Arquivado) ou **Suspenso** (reprovado ou retirado; volta para Rascunho). Sempre que o anúncio mudar de estado, o anunciante é notificado automaticamente (Observer) e um mecanismo de **Log** retém a informação da mudança (`LogMudancaEstado.getInstancia().registrar(anuncio, estadoAnterior, estadoNovo)` chamado em `Anuncio.setEstado`). Opção **Consultar log de mudanças de estado** no menu.

### RF05 – Notificação do usuário

- **Onde:** `notificacao/Observer`, `NotificacaoObserver`, `NotificacaoInteressadosObserver`; `ServicoNotificacaoExterno`, `EmailAdapter`, `SmsAdapter`; `Anuncio` mantém lista de observers e chama `notificar()` ao mudar estado.
- **Como:** O sistema notifica usuários sobre eventos (ex.: mudança de estado do anúncio). Solução flexível para alterar o canal de notificação (Adapter para Email, SMS, etc.). Uma opção implementada na prática: **Email** via `EmailAdapter` que implementa `ServicoNotificacaoExterno`. Observer: ao mudar estado do anúncio, todos os observers são notificados; `NotificacaoObserver` usa o serviço externo (e-mail); `NotificacaoInteressadosObserver` usa `Saida` para exibir.

### RF06 – Busca avançada

- **Onde:** `busca/BuscaService`, `busca/FiltroBusca` e implementações (`FiltroTipoImovel`, `FiltroBairro`, `FiltroPreco`, `FiltroArea`, `FiltroQuartos`, `FiltroTitulo`, `FiltroTipoOferta`, `FiltroSuites`).
- **Como:** Usuários podem buscar imóveis aplicando múltiplos critérios (faixa de preço, localização/bairro, área, número de quartos). Filtros combinados dinamicamente (AND). Filtros específicos por tipo de imóvel (ex.: quartos para apartamento). Novos filtros podem ser adicionados sem modificar o código de busca principal (novas classes que implementam `FiltroBusca`). Usuário escolhe critérios (1=Tipo, 2=Bairro, 3=Preço, 4=Área, 5=Quartos); `BuscaService.buscar(anuncios, filtros)` aplica todos em AND.

### RF07 – Configuração centralizada

- **Onde:** `config/Configuracao`, arquivo `config.properties` (raiz do projeto ou `src/main/resources`).
- **Como:** O sistema carrega configurações (taxas de comissão padrão, limites de upload de fotos, termos impróprios nos textos dos anúncios, URLs de serviços externos) a partir de uma fonte única e acessível globalmente. `Configuracao` é Singleton; `getInstancia().carregarParametros()` lê o arquivo `.properties`. Informações carregadas de `config.properties` (ou equivalente).

### RF08 – Novo padrão (Chat)

- **Onde:** `chat/ChatMediator`, `Conversa`, `Mensagem`; `usuario/Usuario.enviarMensagemVia(mediator, texto)`; opção **Chat** no menu.
- **Como:** Requisito adicional resolvido com padrão **Mediator**, não usado para atender aos requisitos anteriores. Chat entre interessado e dono do anúncio: usuários não se comunicam diretamente; enviam mensagens via `Conversa` (mediador). Opção **Chat** no menu: "Interessado: conversar sobre um anúncio" e "Dono do anúncio: ver e responder conversas". `Conversa` centraliza as mensagens entre interessado e dono.

### E1 – Povoar dados automaticamente a partir de CSV

- **Onde:** `csv/CarregadorCSV`, arquivos `dados/usuarios.csv` e `dados/anuncios.csv`.
- **Como:** Dados são povoados automaticamente a partir de arquivos CSV ao iniciar a aplicação, evitando digitações iniciais para testar o sistema. No `Main`, se existirem os arquivos, são chamados `CarregadorCSV.carregarUsuarios(path)` e `CarregadorCSV.carregarAnuncios(path, ...)`. Parse por `;`; suporte a tipo de oferta, valores, área, descrição, suítes, bairro.

### E2 – Não usar System.out no domínio; fluxo de mensagens reutilizável

- **Onde:** `saida/Saida`, `saida/ConsoleSaida`; uso em `Main` e em `NotificacaoInteressadosObserver`.
- **Como:** Não há chamadas a `System.out.println()` dentro de métodos do domínio. O fluxo de mensagens é exibido de forma reutilizável: interface `Saida` define `escrever(String)` e `escreverSemQuebra(String)`; `ConsoleSaida` implementa para console. `Main` e observadores usam `Saida`, facilitando troca por log ou UI sem alterar o domínio.

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
    │   ├── moderacao/
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
