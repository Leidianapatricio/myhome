# MyHome – O que ainda pode faltar ou ser melhorado

Com base no **documento de base (PDF)** e no estado atual do projeto.

---

## 1. Requisitos de execução (PDF)

| Item | Situação | Sugestão |
|------|----------|----------|
| **E1 – Povoar dados via CSV** | Não implementado | Carregar usuários e/ou anúncios iniciais a partir de arquivos CSV para testes sem digitar tudo. |
| **E2 – Evitar System.out em métodos** | Parcial | Apenas `NotificacaoInteressadosObserver` usa `Saida`. O **Main** ainda tem muitas chamadas a `System.out.println`. Para atender ao requisito: fazer o Main usar uma **Saida** (ex.: `ConsoleSaida`) e chamar `saida.escrever(...)` em vez de `System.out.println`. |

---

## 2. Requisitos funcionais

| Item | Situação | Sugestão |
|------|----------|----------|
| **RF06 – Busca avançada** | Básico | Existe `buscarImoveis()` que lista anúncios. O PDF pede filtros combináveis (preço, localização, área, quartos) e extensíveis sem alterar o código de busca principal. Possível evolução: interface **FiltroBusca** (ou Strategy) e filtros concretos (por preço, área, etc.) combináveis. |

---

## 3. Entrega (PDF – IV)

| Item | Situação | Sugestão |
|------|----------|----------|
| **Diagrama de classes** | Não verificado no repositório | Entregar diagrama da solução indicando onde cada padrão se encaixa. |
| **README.md completo** | README atual é mínimo | Incluir: disciplina, período, professor, equipe; classes e padrões utilizados (e onde); descrição da solução; como colocar em funcionamento; **como cada requisito (RF01–RF08, E1, E2) foi atendido**. O IMPLEMENTACAO.md já cobre parte disso e pode ser usado como base. |
| **Pasta/link do projeto** | Depende de você | Garantir que a pasta do projeto ou link (ex.: GitHub) esteja disponível e que as instruções do README permitam rodar no ambiente do professor. |

---

## 4. Melhorias opcionais (não obrigatórias pelo PDF)

| Item | Situação | Sugestão |
|------|----------|----------|
| **Listar compras** | Não existe no menu | Opção de menu "Listar compras" para exibir histórico (comprador, anúncio, data). |
| **Consultar log de mudanças** | Log existe, mas não há menu | Opção de menu "Consultar log de mudanças de estado" para exibir as entradas de `LogMudancaEstado`. |
| **Chamar carregarParametros()** | Configuracao tem o método | Garantir que `Configuracao.getInstancia().carregarParametros()` seja chamado no início da aplicação (ex.: no Main) para carregar `config.properties`. |

---

## 5. Resumo

- **Obrigatório pelo PDF e ainda em aberto:** E1 (CSV), E2 (Saida no Main), README completo, diagrama de classes.
- **Requisito que pode ser aprofundado:** RF06 (busca com filtros combináveis e extensíveis).
- **Opcionais:** listar compras, consultar log, chamar `carregarParametros()` no startup.

Se quiser, posso implementar algum desses itens (por exemplo: E2 no Main, opção de listar compras, opção de consultar log, chamada a `carregarParametros()` no Main).
