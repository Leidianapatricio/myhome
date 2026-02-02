# Análise de Conformidade com o Enunciado (PDF)

Documento que verifica se o que foi implementado no projeto MyHome está de acordo com o arquivo **[PPS]-20252-projeto-sistema-classificados (1).pdf**.

---

## I – Introdução ao Problema

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| MyHome: plataforma de classificados imobiliários; conecta proprietários, corretores, imobiliárias e compradores/locatários | **Conforme.** README e Main descrevem e implementam o domínio. |
| Anunciantes publicam anúncios para venda ou aluguel; usuários pesquisam, filtram e visualizam | **Conforme.** Criação de anúncio (venda/aluguel/temporada), busca avançada, visualização. |
| Diferentes tipos de imóveis (casas, apartamentos, terrenos, imóveis comerciais) | **Conforme.** Casa, Apartamento, Terreno, SalaComercial, Galpão existem (imovel/ e factory/). Criação guiada no menu usa Casa, Apartamento, Terreno (prototype). Sala/Galpão têm factory; podem ser integrados ao menu se desejar. |
| Múltiplos tipos de anúncios (venda, aluguel, temporada) | **Conforme.** `TipoOferta` (VENDA, ALUGUEL, TEMPORADA); valores e filtros por tipo de oferta quando aplicável. |
| Flexível para futuras expansões | **Conforme.** Novos tipos de imóvel (factory/prototype), novos filtros (FiltroBusca), configuração em .properties. |
| a) Expandir novos tipos de imóveis e serviços | **Conforme.** Factory Method + Prototype; novos tipos sem alterar código de criação principal. |
| b) Gerenciar formatos de pagamento e planos | **Conforme.** Strategy (FormaPagamento: Cartão, PIX, Boleto); ServicoPagamento; PlanoAssinatura existe. |
| c) Notificação por múltiplos canais (email, SMS, etc.) | **Conforme.** Observer + Adapter (EmailAdapter, SmsAdapter); uma opção (e-mail) implementada de fato. |
| d) Buscas com múltiplos filtros | **Conforme.** RF06 – ver abaixo. |

---

## II – Requisitos Funcionais e Não Funcionais

### a) Gestão de Imóveis (Anúncios)

#### RF01 - Criação de Anúncios

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| Cadastro de anúncios de diferentes tipos (Casa, Apartamento, Terreno, Sala Comercial, Galpão, ou outro) | **Conforme.** Classes existem; menu de criação oferece Casa, Apartamento, Terreno (prototype). Sala Comercial e Galpão têm factory; integração no menu de criação é opcional. |
| Processo de criação guiado; informações obrigatórias para cada tipo coletadas corretamente | **Conforme.** Main: fluxo guiado (título, tipo oferta, valores, tipo imóvel, endereço, bairro, área, descrição, suítes, quartos/andar/elevador/quintal conforme o tipo, quantidade de fotos). |
| Atributos obrigatórios: título, tipo do imóvel e preço | **Conforme.** Título e preço solicitados; tipo do imóvel escolhido; validação em cadeia (ValidadorPreco, ValidadorTitulo, ValidadorImovel). |
| Características específicas por tipo (apartamento: andar, elevador; casa: quintal) | **Conforme.** Apartamento: quartos, andar, elevador; Casa: quintal; Terreno: área. |
| Flexível para adicionar novos tipos sem modificar código existente | **Conforme.** Factory Method (ImovelFactory e concretas); Prototype (registro e clone); novos tipos = novas classes. |

#### RF02 - Instâncias de anúncios padrão

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| Certos tipos iniciam com configuração padrão (ex.: Apartamento: unidade habitacional, 2 quartos, 60 m²; casas com configuração padrão) | **Conforme.** ApartamentoPadrao: 2 quartos, 60 m², andar 1, 1 suíte, descrição padrão. CasaPadrao: 80 m², 1 suíte, descrição padrão. TerrenoPadrao: 200 m². |
| Outras configurações poderão surgir no futuro | **Conforme.** ImovelPrototypeRegistry; novos protótipos podem ser registrados. |

#### RF03 - Publicação e Moderação

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| Anunciante submete anúncio; todos passam por moderação antes de se tornarem públicos | **Conforme.** Anúncio criado em Rascunho; opção "Submeter anúncio" (11); validação automática; aprovado → Ativo (publicado). Reprovação mantém em Rascunho com erros exibidos. |
| Regras dinâmicas; aprovadas para publicar | **Conforme.** config.properties: termos.improprios, preco.minimo, moderacao.descricao.minimo, moderacao.fotos.minimo. |
| Moderação manual ou automatizada | **Conforme.** Moderação automatizada implementada (ServicoModeracao.validarRegras). Opção manual foi removida a pedido do usuário. |
| Título e descrição não podem conter termos proibidos (palavras de baixo calão, termos pejorativos/inadequados) | **Conforme.** ServicoModeracao valida título e descrição contra termos.improprios; mensagem explícita "palavras de baixo calão ou termos inadequados". |
| Preço condizente (evitar zero, um real, valor sem sentido) | **Conforme.** preco.minimo em config; validação por tipo de oferta (venda/aluguel/temporada). |
| Ao menos uma foto ou quantidade mínima de texto na descrição | **Conforme.** Anuncio.quantidadeFotos; moderacao.fotos.minimo e moderacao.descricao.minimo; regra: (fotos >= mínimo) OU (descrição >= mínimo de caracteres). |

#### RF04 - Fases do ciclo de vida de um anúncio

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| Ciclo de vida: Rascunho, Pendente de Moderação, Ativo, Vendido/Alugado, Suspenso | **Conforme.** RascunhoState, ModeracaoState, AtivoState, ArquivadoState (Vendido/Alugado), SuspensoState. |
| Sempre que mudar de estado: anunciante notificado automaticamente | **Conforme.** Anuncio.setEstado chama notificar(); Observer (NotificacaoObserver, NotificacaoInteressadosObserver). |
| Mecanismo de Log retém informação sobre mudança de status | **Conforme.** LogMudancaEstado (singleton); registrar(anuncio, estadoAnterior, estadoNovo) chamado em Anuncio.setEstado; opção "Consultar log de mudanças de estado" no menu. |
| (i) Rascunho: inicial até enviar para moderação | **Conforme.** Anúncio criado em RascunhoState; opção 11 envia (validação automática → Ativo). |
| (ii) Moderação: em revisão, checagem automática | **Conforme.** Estado ModeracaoState existe; fluxo atual usa moderação automática (aprovado → Ativo direto). |
| (iii) Ativo: aprovado e visível; pode ser vendido ou suspenso | **Conforme.** AtivoState; busca/visualização mostram só ativos; venda/aluguel e confirmação de pagamento levam a ArquivadoState. |
| (iv) Vendido: estado final (arquivado) | **Conforme.** ArquivadoState; aplicado ao confirmar pagamento pelo vendedor. |
| (v) Suspenso: reprovado ou retirado pelo usuário; volta para rascunho | **Conforme.** SuspensoState; proximo() volta para RascunhoState. |

### b) Mecanismos de notificação do usuário

#### RF05 - Notificação do usuário

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| Notificar usuários sobre eventos no ciclo de vida | **Conforme.** Observers notificados em Anuncio.setEstado (mudança de estado). |
| Solução flexível para alterar canal de notificação | **Conforme.** Adapter (ServicoNotificacaoExterno); EmailAdapter, SmsAdapter. |
| Notificações via Email, SMS, Telegram e/ou WhatsApp | **Conforme.** E-mail implementado (EmailAdapter); SMS (SmsAdapter) preparado; estrutura permite outros canais. |
| Uma das opções implementada na prática (não só exibição na tela) | **Conforme.** E-mail implementado via EmailAdapter (serviço externo); NotificacaoInteressadosObserver usa Saida para exibir. |

### c) Pesquisa e Visualização

#### RF06 - Busca Avançada

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| Múltiplos critérios: faixa de preço, localização, área, número de quartos | **Conforme.** FiltroPreco, FiltroBairro (localização), FiltroArea, FiltroQuartos. |
| Filtros combinados dinamicamente | **Conforme.** BuscaService.buscar(anuncios, filtros) aplica todos em AND; usuário escolhe critérios (1–5). |
| Filtros específicos por tipo de imóvel | **Conforme.** FiltroTipoImovel; FiltroQuartos aplicado a apartamentos. |
| Novos filtros sem modificar código de busca principal | **Conforme.** Interface FiltroBusca; novas classes implementam aceita(Anuncio); BuscaService não precisa mudar. |

### d) Estrutura e Extensibilidade

#### RF07 - Configuração Centralizada

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| Carregar configurações (taxas de comissão, limites de fotos, termos impróprios, URLs de serviços externos) de fonte única e acessível globalmente | **Conforme.** Configuracao (Singleton); config.properties; getTermosImproprios, getPrecoMinimoModeracao, getDescricaoTamanhoMinimo, getFotosMinimoModeracao, etc. |
| Informações carregadas de .properties (ou equivalente) | **Conforme.** config.properties na raiz; carregarParametros() no início. |

### 4) Novo Requisito

#### RF08 - Adicionar um novo padrão

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| Novo requisito coerente e funcional, resolvido com padrão adicional não usado nos anteriores | **Conforme.** Chat com padrão **Mediator**: Conversa (mediador), ChatMediator; usuários não se comunicam diretamente; opção Chat no menu (interessado e dono do anúncio). |

---

## III – Requisitos de Execução

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| **E1** - Povoar dados automaticamente a partir de arquivos CSV; evitar digitações iniciais para testar | **Conforme.** CarregadorCSV; dados/usuarios.csv e dados/anuncios.csv; carregamento ao iniciar (Main). |
| **E2** - Não definir System.out.println(); fluxo de mensagens reutilizável | **Conforme.** Interface Saida e ConsoleSaida; Main e observadores usam Saida; sem System.out no domínio. |

---

## IV – Entrega

| Exigência do PDF | Situação no Projeto |
|------------------|---------------------|
| a) Diagrama de classes indicando onde os padrões se encaixam | A ser fornecido separadamente (entrega). |
| b) README.MD com disciplina, período, professor, equipe, classes, padrões utilizados e onde, descrição da solução, especificação de como cada requisito foi resolvido | **Conforme.** README contém essas seções. A seção "Como colocar o projeto em funcionamento" foi removida a pedido do usuário; o PDF pede essa informação na entrega — recomenda-se recolocar instruções de execução (Maven/scripts) se exigido. |
| c) Pasta com arquivos da aplicação / link GitHub | A cargo do repositório e da entrega. |

---

## Resumo

- **RF01 a RF08, E1 e E2:** atendidos conforme o enunciado.
- **Moderação:** automatizada (validação na submissão; aprovado → Ativo). Estados Rascunho, Moderação, Ativo, Arquivado, Suspenso existem e são usados; fluxo atual não passa pelo estado “Moderação” porque a aprovação é imediata quando as regras passam.
- **Correção aplicada:** na opção 11 (Submeter anúncio), quando a validação automática aprova, o anúncio passa direto para **Ativo** (`setEstado(new AtivoState())`), em vez de ir para ModeracaoState sem opção de aprovação manual.

---

*Documento gerado com base no PDF do enunciado e no código do projeto MyHome.*
