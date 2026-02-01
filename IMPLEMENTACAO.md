# MyHome – O que foi implementado e alterado

Resumo do que foi feito no projeto MyHome ao longo do desenvolvimento, com base no documento de requisitos (PDF) e nos diagramas UML.

---

## 1. Estrutura inicial (alinhada aos diagramas UML)

### Usuários
- **Usuario** – Classe única de usuário (id, nome, email) com: `autenticar()`, `criarAnuncio()`, `editarAnuncio()`, `removerAnuncio()`, `buscarImoveis()`, `visualizarAnuncio()`, `favoritarAnuncio()`, `enviarMensagemVia(ChatMediator, texto)`.
- **Removido:** Anunciante e Cliente (unificação em uma única classe Usuario).

### Configuração (Singleton)
- **Configuracao** – `getInstancia()`, construtor privado, `carregarParametros()`, `getParametro(chave)`.
- **config.properties** – Arquivo de configuração (RF07) com taxas, limites e URLs.
- **Alterado:** `carregarParametros()` passou a carregar de arquivo `.properties` (raiz do projeto ou classpath).

### Validação (Chain of Responsibility)
- **ValidadorAnuncio** (abstrato) – `proximo` privado, `setProximo()`, `passarParaProximo()`, `validar(Anuncio)`.
- **ValidadorTitulo**, **ValidadorPreco**, **ValidadorImovel** – Validadores concretos em cadeia.

### Anúncio
- **Anuncio** – titulo, preco, imovel, estado; `submeter()`, `mudarEstado()`, `aceitar(Visitor)`, `adicionarObserver()`, `removerObserver()`, `notificar()`.
- **setEstado()** – Chama `notificar()` e registra no **LogMudancaEstado** (RF04 – log de mudança de status).
- **InteressadosAnuncio** – Classe separada para lista de interessados: `adicionar(Usuario)`, `getInteressados()`; Anuncio apenas delega.
- **LogMudancaEstado** – Singleton que registra cada mudança de estado (anúncio, estado anterior, estado novo, data/hora).

### Estados do anúncio (RF04)
- **RascunhoState** → Moderação.
- **ModeracaoState** → Ativo (aprovação) ou Suspenso (reprovação via `setEstado(new SuspensoState())`).
- **AtivoState** → Vendido ou Suspenso (retirada pelo usuário).
- **VendidoState** – Estado final.
- **SuspensoState** – Novo estado; `proximo()` volta para Rascunho.

### Imóveis
- **Imovel** (abstrato) – endereco, preco, `calcularValor()`, `aceitar(Visitor)`.
- **Terreno**, **Casa**, **Apartamento**, **SalaComercial**, **Galpao** – Subclasses com atributos e `calcularValor()` próprios.

### Fábricas (Factory Method)
- **ImovelFactory** (abstrata) – `criarImovel(): Imovel`.
- **CasaFactory**, **ApartamentoFactory**, **TerrenoFactory** – Implementações concretas.

### Prototype
- **ImovelPrototype** – Interface com `clone(): Imovel`.
- **TerrenoPadrao**, **CasaPadrao**, **ApartamentoPadrao** – Protótipos concretos.
- **ImovelPrototypeRegistry** – `registrar(tipo, prototipo)`, `getPrototype(tipo)`.

### Notificação (Observer + Adapter)
- **Observer** – Interface com `atualizar(Anuncio)`.
- **NotificacaoObserver** – Usa `ServicoNotificacaoExterno` (ex.: email).
- **NotificacaoInteressadosObserver** – Notifica usuários interessados quando o estado do anúncio muda; usa **Saida** (E2 – sem System.out em métodos de domínio).
- **ServicoNotificacaoExterno** – Interface `enviarMensagem(Anuncio)`.
- **EmailAdapter**, **SmsAdapter** – Implementações.

### Visitor
- **Visitor** – Interface com `visitar(Anuncio)` e `visitar(Imovel)`.
- **Anuncio** e **Imovel** – Método `aceitar(Visitor)` (estrutura do diagrama; uso opcional).

### Saída (E2)
- **Saida** – Interface `escrever(String mensagem)` para exibição de mensagens.
- **ConsoleSaida** – Implementação que usa `System.out.println`.
- Uso em **NotificacaoInteressadosObserver** e possivelmente em outros pontos para facilitar reuso e troca de canal.

---

## 2. Chat – RF08 (padrão Mediator) – implementado

- **ChatMediator** – Interface com `enviarMensagem(Usuario, texto)` e `getMensagens()`.
- **Conversa** – Mediador concreto: centraliza a comunicação entre dois Usuários (interessado e dono do anúncio).
- **Mensagem** – texto, remetente (Usuario), dataHora.
- **Usuario** – `enviarMensagemVia(ChatMediator, texto)` (colleague que delega ao mediator).
- **Main** – Opção de menu “Chat”: interessado inicia conversa sobre anúncio; dono do anúncio vê e responde conversas. Ao abrir conversa, o usuário interessado é registrado em **InteressadosAnuncio** do anúncio.

---

## 3. Main e fluxo

- **Uma lista de usuários** – `List<Usuario> usuarios` (sem Anunciante/Cliente separados).
- **Mapa** – `Map<Anuncio, Usuario> donoDoAnuncio` para saber quem criou cada anúncio.
- **Menu** – 1) Cadastrar usuário, 2) Listar usuários, 3) Usuário: criar anúncio, 4) Usuário: buscar/visualizar/favoritar, 5) Chat, 6) Comprar imóvel, 7) Listar compras, 8) Consultar log de mudanças de estado, 0) Sair.
- **Chat** – Interessado escolhe anúncio e entra na conversa; dono escolhe uma conversa e responde. Mensagens exibidas com data/hora e nome do remetente.

---

## 4. Infraestrutura e build

- **pom.xml** – Projeto Maven para o VSCode/Java reconhecer o projeto; `maven-compiler-plugin` com `**/*.java`.
- **.vscode/launch.json** – Configuração para rodar a classe Main.
- **.vscode/settings.json** – Configurações do projeto Java/Maven.
- **.gitignore** – bin/, out/, *.class; opcionalmente .vscode/ removido para versionar configurações.
- **config.properties** – Arquivo de configuração na raiz (RF07).

---

## 5. Arquivos removidos

- **Anunciante.java**, **Cliente.java** – Unificação em Usuario.
- **ConfiguracaoSingleton.java** – Substituído por Configuracao.
- **EmailNotificacaoAdapter.java** – Substituído por EmailAdapter.

---

## 6. Resumo por requisito (documento base PDF)

| Requisito | Implementação |
|-----------|----------------|
| **RF01** – Criação de anúncios (tipos de imóvel) | Imovel + subclasses; Factory (CasaFactory, etc.); Validação em cadeia. |
| **RF02** – Instâncias padrão | Prototype (TerrenoPadrao, CasaPadrao, ApartamentoPadrao); ImovelPrototypeRegistry. |
| **RF03** – Publicação e moderação | submeter(); ValidadorAnuncio (Titulo, Preco, Imovel); estados Rascunho → Moderação → Ativo. |
| **RF04** – Ciclo de vida + notificação + log | Estados: Rascunho, Moderação, Ativo, Vendido, **Suspenso**; setEstado() chama notificar() e **LogMudancaEstado**. |
| **RF05** – Notificação (múltiplos canais) | Observer; EmailAdapter/SmsAdapter; NotificacaoInteressadosObserver (interessados); uso de Saida (E2). |
| **RF06** – Busca avançada (filtros) | buscarImoveis() em Usuario; filtros combináveis/extensíveis ainda podem ser ampliados (Strategy, etc.). |
| **RF07** – Configuração centralizada | Configuracao (Singleton); carregarParametros() a partir de **config.properties**. |
| **RF08** – Novo padrão (Chat) | **Chat** – implementado com padrão **Mediator** (ChatMediator, Conversa, Usuario.enviarMensagemVia). |
| **E1** – Povoar dados via CSV | **Implementado:** `CarregadorCSV`; no início do Main são carregados `dados/usuarios.csv` e `dados/anuncios.csv` quando existirem. |
| **E2** – Evitar System.out em métodos | Interface **Saida** + **ConsoleSaida**; Main usa apenas `saida.escrever()` e `saida.escreverSemQuebra()`; nenhum System.out no fluxo principal. |

---

## 7. Padrões de projeto utilizados

- **Singleton** – Configuracao, LogMudancaEstado.
- **Factory Method** – ImovelFactory + CasaFactory, ApartamentoFactory, TerrenoFactory.
- **Prototype** – ImovelPrototype + TerrenoPadrao, CasaPadrao, ApartamentoPadrao + ImovelPrototypeRegistry.
- **Chain of Responsibility** – ValidadorAnuncio + ValidadorTitulo, ValidadorPreco, ValidadorImovel.
- **Observer** – Observer + NotificacaoObserver, NotificacaoInteressadosObserver; Anuncio mantém lista e chama notificar() em setEstado().
- **Adapter** – ServicoNotificacaoExterno + EmailAdapter, SmsAdapter.
- **Visitor** – Visitor + aceitar() em Anuncio e Imovel.
- **Mediator** – ChatMediator + Conversa; Usuario como colleague (enviarMensagemVia).
- **Composição/delegação** – InteressadosAnuncio (lista e regras de interessados fora de Anuncio).

Este documento serve como referência do que foi implementado e alterado no MyHome em relação ao documento de base e aos diagramas.
