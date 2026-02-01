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
- **Maven** (opcional, mas recomendado) — para compilar e rodar com um comando.

### Instalar o JDK

**Windows:**  
1. Baixe o JDK em [Adoptium](https://adoptium.net/) (recomendado) ou [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) — escolha o instalador **.msi** para Windows.  
2. Instale marcando a opção de **adicionar Java ao PATH** (se existir).  
3. Para rodar o projeto: dê **duplo clique em `run.bat`** (na pasta do projeto) ou abra o **CMD** nessa pasta e digite `run.bat`.  
   - O `run.bat` funciona **com ou sem Maven**: se o Maven estiver instalado, ele usa; senão, compila e executa só com o JDK.

**macOS:**
```bash
brew install openjdk@11
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install default-jdk
```

**Verificar instalação (qualquer sistema):**
```bash
javac -version
java -version
```

### Instalar o Maven (recomendado)

**Windows:** Baixe em [maven.apache.org](https://maven.apache.org/download.cgi), extraia e adicione o `bin` ao PATH.

**macOS:** `brew install maven`  

**Linux:** `sudo apt install maven`

---

## Como rodar em qualquer computador

**Importante:** O programa deve rodar **na pasta raiz do projeto** (onde estão `pom.xml`, `run.bat`, `dados/` e `config.properties`). O `run.bat` já entra nessa pasta automaticamente.

### Rodar no Windows

1. Instale o **JDK 11+** (Adoptium ou Oracle — instalador .msi).  
2. Copie ou clone a pasta do projeto para o computador.  
3. Entre na pasta do projeto e dê **duplo clique em `run.bat`**.  
   - Se o Maven estiver instalado, o script usa Maven; senão, compila e executa só com o JDK.  
   - A janela do CMD abre, o programa roda e, ao encerrar, mostra "Pressione qualquer tecla...".

Se preferir usar o terminal: abra o **CMD**, vá até a pasta do projeto (`cd caminho\para\myhome`) e digite `run.bat` ou `mvn compile exec:java`.

### Opção 1 – Com Maven (recomendado)

Na pasta raiz do projeto:

```bash
mvn compile exec:java
```

Ou use o script:  
- **Windows:** duplo clique em `run.bat` ou, no CMD na pasta do projeto, digite `run.bat`. Funciona com ou sem Maven (se não tiver Maven, usa só o JDK).  
- **Linux/Mac:** no terminal na pasta do projeto, `chmod +x run.sh` (só na primeira vez) e depois `./run.sh`.

### Opção 2 – Sem Maven (só JDK)

Se não tiver Maven, compile todos os `.java` dentro de `src/` com `javac` (saída em `bin/`) e execute:

```bash
java -cp bin br.edu.ifpb.myhome.Main
```

(O diretório de trabalho deve ser a pasta raiz do projeto.)

### Opção 3 – VSCode / Cursor

1. Abra a pasta do projeto no editor.
2. Aguarde o Maven carregar (`pom.xml`).
3. Abra `src/main/java/br/edu/ifpb/myhome/Main.java` e use **Run** ou **Run and Debug** → **Run Main**.

**Dados iniciais (E1):** Se existirem `dados/usuarios.csv` e `dados/anuncios.csv` na pasta raiz, eles são carregados automaticamente ao iniciar.

---

## Descrição da solução

O **MyHome** simula um sistema de anúncios de imóveis: usuários podem cadastrar-se, criar anúncios (Casa, Apartamento, Terreno etc.), buscar e visualizar anúncios, favoritar, conversar por chat sobre um anúncio e realizar “compra” (o anúncio passa a estado Arquivado). Há moderação de anúncios (estados Rascunho → Moderação → Ativo), notificação por e-mail/SMS (Observer + Adapter), log de mudanças de estado e configuração centralizada em `config.properties`.

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
- **Mediator:** `ChatMediator`, `Conversa`; `Usuario` como colleague
- **State:** `EstadoAnuncio`, `RascunhoState`, `ModeracaoState`, `AtivoState`, `VendidoState`, `SuspensoState`, `ArquivadoState`
- **Composição:** `InteressadosAnuncio` (lista de interessados do anúncio)
- **E2 (abstração de saída):** `Saida`, `ConsoleSaida`

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

Dentro da opção 4 (anúncios), o usuário pode: **1** Buscar imóveis (por tipo), **2** Visualizar anúncio, **3** Favoritar anúncio, **4** Comprar imóvel (escolhe um anúncio disponível para comprar).  

---

## Estrutura do projeto

- `src/main/java/br/edu/ifpb/myhome/` — código fonte (padrão Maven)
- `src/main/resources/` — recursos (ex.: config.properties)
- `dados/` — `usuarios.csv`, `anuncios.csv` (E1)
- `config.properties` — configuração (RF07)
- `pom.xml` — projeto Maven
- `run.sh` / `run.bat` — scripts para rodar em qualquer computador (a partir da pasta raiz do projeto)
