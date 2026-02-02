# MyHome

Plataforma de classificados imobiliários em Java: anúncios (venda/aluguel/temporada), busca avançada, chat e confirmação de pagamento. Usa padrões de projeto (State, Observer, Factory, Prototype, Mediator, etc.).

**IFPB · Padrões de Projeto (5º período) · Prof. Alex Sandro da Cunha Rêgo**  
**Equipe:** Felipe Cartaxo, Leidiana Patrício e Matheus Barbosa

---

## Resumo da Solução

- **Anunciantes:** cadastro por e-mail, criação de anúncios (Casa, Apartamento, Terreno) com configuração padrão (RF02 – Prototype), tipo de oferta e valores; submissão com moderação automática (RF03); recebem notificações e mensagens de chat/pagamento.
- **Compradores/locatários:** busca avançada (tipo, bairro, preço, área, quartos – RF06), visualização, favoritos, chat, pagamento com confirmação pelo vendedor.
- **Ciclo do anúncio (RF04):** Rascunho → Submeter (validação: termos proibidos, preço mínimo, descrição/fotos mínimas) → Ativo; ao confirmar pagamento → Arquivado. Log de mudanças de estado e notificação (Observer) ao mudar estado.
- **Notificação (RF05):** canal flexível (Adapter); e-mail implementado. **Configuração (RF07):** Singleton `Configuracao`, `config.properties`. **Chat (RF08):** padrão Mediator (`Conversa`). **E1:** dados iniciais via CSV. **E2:** saída via interface `Saida` (sem System.out no domínio).

---

## Requisitos (onde foi resolvido)

| ID | Requisito | Onde |
|----|-----------|------|
| **RF01** | Criação de anúncios (tipos, processo guiado, título/tipo/preço obrigatórios) | `Main`, `usuario/`, `anuncio/`, `imovel/`, `validacao/` (Chain of Responsibility), `factory/` |
| **RF02** | Instâncias padrão (casa 80 m², apartamento 60 m² 2 quartos, terreno 200 m²) | Subclasses de Imovel (Casa, Apartamento, Terreno) são os protótipos – implementam `ImovelPrototype` e `clone()`; `prototype/ImovelPrototypeRegistry` registra instâncias padrão |
| **RF03** | Publicação e moderação (título e descrição não podem ter palavras de baixo calão/termos proibidos; preço mínimo; descrição/fotos mínimos) | `moderacao/ServicoModeracao`, `ResultadoModeracao`; `config.properties`; opção 11 no menu |
| **RF04** | Ciclo de vida (Rascunho, Moderação, Ativo, Arquivado, Suspenso); notificação e log ao mudar estado | `estado/` (State), `LogMudancaEstado`, `Anuncio.setEstado` → log + notificar() |
| **RF05** | Notificação (canal flexível; e-mail implementado) | `notificacao/` – Observer, `EmailAdapter`, `ServicoNotificacaoExterno` |
| **RF06** | Busca avançada (preço, localização, área, quartos; filtros combinados; extensível) | `busca/` – `BuscaService`, `FiltroBusca` e implementações (Tipo, Bairro, Preço, Área, Quartos, Título) |
| **RF07** | Configuração centralizada | `config/Configuracao` (Singleton), `config.properties` |
| **RF08** | Novo padrão: Chat | `chat/` – Mediator (`Conversa`, `ChatMediator`) |
| **E1** | Dados iniciais via CSV | `csv/CarregadorCSV`, `dados/usuarios.csv`, `dados/anuncios.csv` |
| **E2** | Sem System.out; saída reutilizável | `saida/Saida`, `ConsoleSaida`; uso em `Main` e observadores |

---

## Padrões utilizados

| Padrão | Onde |
|--------|------|
| **State** | `estado/` – ciclos de vida do anúncio |
| **Strategy** | `pagamento/` – formas de pagamento (Cartão, PIX, Boleto) |
| **Observer** | `anuncio/Anuncio` + `notificacao/` – notificação ao mudar estado |
| **Adapter** | `notificacao/` – e-mail/SMS |
| **Mediator** | `chat/` (RF08) – conversas entre interessado e dono |
| **Factory Method** | `factory/` – criação por tipo de imóvel |
| **Chain of Responsibility** | `validacao/` – Preço → Título → Imóvel |
| **Singleton** | `config/Configuracao` |
| **Prototype** | `prototype/` (RF02) – configuração padrão por tipo |
| **Filtros (estratégia)** | `busca/` – `FiltroBusca`, filtros combinados em AND |

---

## Classes (pacotes principais)

| Pacote | Classes principais |
|--------|---------------------|
| `anuncio` | Anuncio, LogMudancaEstado, TipoOferta, InteressadosAnuncio |
| `busca` | BuscaService, FiltroBusca, FiltroTipoImovel, FiltroBairro, FiltroPreco, FiltroArea, FiltroQuartos, FiltroTitulo |
| `chat` | ChatMediator, Conversa, Mensagem |
| `compra` | Compra, SolicitacaoCompra |
| `config` | Configuracao |
| `csv` | CarregadorCSV |
| `estado` | EstadoAnuncio, RascunhoState, ModeracaoState, AtivoState, ArquivadoState, SuspensoState, VendidoState |
| `factory` | ImovelFactory, CasaFactory, ApartamentoFactory, TerrenoFactory, SalaComercialFactory |
| `imovel` | Imovel, Casa, Apartamento, Terreno, SalaComercial, Galpao |
| `moderacao` | ServicoModeracao, ResultadoModeracao |
| `notificacao` | Observer, NotificacaoObserver, NotificacaoInteressadosObserver, ServicoNotificacaoExterno, EmailAdapter, SmsAdapter |
| `pagamento` | FormaPagamento, PagamentoCartao, PagamentoPix, PagamentoBoleto, ServicoPagamento |
| `prototype` | ImovelPrototype (interface), ImovelPrototypeRegistry; Casa, Apartamento, Terreno (imovel/) implementam ImovelPrototype |
| `saida` | Saida, ConsoleSaida |
| `usuario` | Usuario |
| `validacao` | ValidadorAnuncio, ValidadorPreco, ValidadorTitulo, ValidadorImovel |

---

## Estrutura de diretórios

```
myhome/
├── README.md
├── pom.xml
├── config.properties
├── dados/ (usuarios.csv, anuncios.csv)
└── src/main/java/br/edu/ifpb/myhome/
    ├── Main.java
    ├── anuncio/, busca/, chat/, compra/, config/, csv/, estado/, factory/,
    ├── imovel/, moderacao/, notificacao/, pagamento/, prototype/, saida/,
    ├── usuario/, util/, validacao/
    └── resources/config.properties
```

---

*Projeto acadêmico – IFPB.*
