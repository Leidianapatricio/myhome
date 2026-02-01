# MyHome

Sistema de anúncios de imóveis em Java, desenvolvido como trabalho da disciplina de Padrões de Projeto.

---

## Dados da entrega

| Campo | Valor |
|-------|--------|
| **Disciplina** | [Nome da disciplina – ex.: Padrões de Projeto] |
| **Período** | [Ex.: 2024.1] |
| **Professor** | [Nome do professor] |
| **Equipe** | [Nomes dos integrantes] |

---

## Requisitos

- **JDK 11 ou superior** (não apenas JRE) — necessário para compilar e executar.

### Instalar o JDK (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install default-jdk
```

Ou uma versão específica (ex.: OpenJDK 17):

```bash
sudo apt install openjdk-17-jdk
```

### Verificar instalação

```bash
javac -version
java -version
```

---

## Como colocar em funcionamento

### No VSCode / Cursor

1. Abra a pasta do projeto no editor.
2. Aguarde o Maven carregar o projeto (`pom.xml`).
3. Abra `src/br/edu/ifpb/myhome/Main.java` e use **Run** acima do método `main`, ou **Run and Debug** → **Run Main**.

### No terminal (após instalar o JDK)

A partir da raiz do projeto:

```bash
# Com Maven (recomendado)
mvn compile exec:java -Dexec.mainClass="src.br.edu.ifpb.myhome.Main"

# Ou apenas compilar
mvn compile
```

Para executar a classe principal após compilar com Maven:

```bash
mvn exec:java -Dexec.mainClass="src.br.edu.ifpb.myhome.Main"
```

**E1 – Dados iniciais via CSV:** Na primeira execução, se existirem os arquivos `dados/usuarios.csv` e `dados/anuncios.csv`, usuários e anúncios serão carregados automaticamente. O formato está documentado em comentários no `CarregadorCSV` e nos próprios CSVs.

---

## Descrição da solução

O **MyHome** simula um sistema de anúncios de imóveis (estilo OLX): usuários podem cadastrar-se, criar anúncios (Casa, Apartamento, Terreno etc.), buscar e visualizar anúncios, favoritar, conversar por chat sobre um anúncio e realizar “compra” (o anúncio passa a estado Arquivado). Há moderação de anúncios (estados Rascunho → Moderação → Ativo), notificação por e-mail/SMS (Observer + Adapter), log de mudanças de estado e configuração centralizada em `config.properties`.

---

## Como cada requisito foi atendido

| Requisito | Atendimento |
|-----------|-------------|
| **RF01** – Criação de anúncios (tipos de imóvel) | Classes `Imovel`, `Casa`, `Apartamento`, `Terreno`, etc.; **Factory Method** (`ImovelFactory`, `CasaFactory`, `ApartamentoFactory`, `TerrenoFactory`); validação em cadeia no momento da criação. |
| **RF02** – Instâncias padrão | **Prototype**: `ImovelPrototype`, `TerrenoPadrao`, `CasaPadrao`, `ApartamentoPadrao`, `ImovelPrototypeRegistry`. |
| **RF03** – Publicação e moderação | `Anuncio.submeter()`; **Chain of Responsibility** (`ValidadorAnuncio` → ValidadorTitulo, ValidadorPreco, ValidadorImovel); estados Rascunho → Moderação → Ativo. |
| **RF04** – Ciclo de vida, notificação e log | Estados: Rascunho, Moderação, Ativo, Vendido, Suspenso, Arquivado; `setEstado()` notifica observers e registra em **LogMudancaEstado** (Singleton). Menu opção 8 consulta o log. |
| **RF05** – Notificação (múltiplos canais) | **Observer** + **Adapter**: `Observer`, `NotificacaoObserver`, `NotificacaoInteressadosObserver`; `ServicoNotificacaoExterno`, `EmailAdapter`, `SmsAdapter`. |
| **RF06** – Busca avançada | `Usuario.buscarImoveis()`; pacote `busca` com `FiltroBusca`, `FiltroPreco`, `FiltroTitulo`, `FiltroTipoImovel`, `FiltroArea`, `BuscaService` (filtros combináveis/extensíveis). |
| **RF07** – Configuração centralizada | **Singleton** `Configuracao`; `carregarParametros()` lê `config.properties` na raiz; chamado no início do `Main`. |
| **RF08** – Novo padrão (Chat) | **Mediator**: `ChatMediator`, `Conversa`, `Mensagem`; `Usuario.enviarMensagemVia(mediator, texto)`. Menu opção 5: chat entre interessado e dono do anúncio. |
| **E1** – Povoar dados via CSV | `CarregadorCSV.carregarUsuarios()` e `carregarAnuncios()`; no início do `Main` são carregados `dados/usuarios.csv` e `dados/anuncios.csv` quando existirem. |
| **E2** – Evitar System.out em métodos | Interface **Saida** e **ConsoleSaida**; o `Main` usa uma instância de `Saida` para todas as saídas (`escrever` / `escreverSemQuebra`). Nenhum `System.out` no fluxo principal. |

---

## Classes e padrões (onde se encaixam)

- **Singleton:** `Configuracao`, `LogMudancaEstado`
- **Factory Method:** `ImovelFactory`, `CasaFactory`, `ApartamentoFactory`, `TerrenoFactory`
- **Prototype:** `ImovelPrototype`, `TerrenoPadrao`, `CasaPadrao`, `ApartamentoPadrao`, `ImovelPrototypeRegistry`
- **Chain of Responsibility:** `ValidadorAnuncio`, `ValidadorTitulo`, `ValidadorPreco`, `ValidadorImovel`
- **Observer:** `Observer`, `NotificacaoObserver`, `NotificacaoInteressadosObserver`; `Anuncio` mantém lista e chama `notificar()` em `setEstado()`
- **Adapter:** `ServicoNotificacaoExterno`, `EmailAdapter`, `SmsAdapter`
- **Visitor:** `Visitor`; `Anuncio` e `Imovel` com `aceitar(Visitor)`
- **Mediator:** `ChatMediator`, `Conversa`; `Usuario` como colleague
- **State:** `EstadoAnuncio`, `RascunhoState`, `ModeracaoState`, `AtivoState`, `VendidoState`, `SuspensoState`, `ArquivadoState`
- **Composição:** `InteressadosAnuncio` (lista de interessados do anúncio)
- **E2 (abstração de saída):** `Saida`, `ConsoleSaida`

Detalhes adicionais estão em `IMPLEMENTACAO.md`.

---

## Menu principal

1. Cadastrar usuário  
2. Listar usuários  
3. Usuário: criar anúncio  
4. Usuário: buscar imóveis / visualizar / favoritar / **comprar** anúncio  
5. Chat (conversar sobre anúncio)  
6. Listar compras  
7. Consultar log de mudanças de estado  
0. Sair  

Dentro da opção 4 (anúncios), o usuário pode: **1** Buscar imóveis (por tipo), **2** Visualizar anúncio, **3** Favoritar anúncio, **4** Comprar imóvel (escolhe um anúncio ativo para comprar).  

---

## Estrutura do projeto

- `src/br/edu/ifpb/myhome/` — código fonte (Main, anuncio, busca, chat, compra, config, csv, estado, factory, imovel, notificacao, saida, usuario, validacao, visitor, prototype)
- `dados/` — `usuarios.csv`, `anuncios.csv` (E1)
- `config.properties` — configuração (RF07)
- `pom.xml` — projeto Maven

Pasta ou repositório do projeto devem estar disponíveis conforme orientação do professor para entrega e reprodução no ambiente de avaliação.
