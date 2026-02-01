package br.edu.ifpb.myhome;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.anuncio.LogMudancaEstado;
import br.edu.ifpb.myhome.anuncio.TipoOferta;
import br.edu.ifpb.myhome.busca.BuscaService;
import br.edu.ifpb.myhome.busca.FiltroArea;
import br.edu.ifpb.myhome.busca.FiltroBusca;
import br.edu.ifpb.myhome.busca.FiltroBairro;
import br.edu.ifpb.myhome.busca.FiltroPreco;
import br.edu.ifpb.myhome.busca.FiltroQuartos;
import br.edu.ifpb.myhome.busca.FiltroTitulo;
import br.edu.ifpb.myhome.busca.FiltroTipoImovel;
import br.edu.ifpb.myhome.config.Configuracao;
import br.edu.ifpb.myhome.pagamento.FormaPagamento;
import br.edu.ifpb.myhome.pagamento.PagamentoBoleto;
import br.edu.ifpb.myhome.pagamento.PagamentoCartao;
import br.edu.ifpb.myhome.pagamento.PagamentoPix;
import br.edu.ifpb.myhome.pagamento.ServicoPagamento;
import br.edu.ifpb.myhome.csv.CarregadorCSV;
import br.edu.ifpb.myhome.chat.Conversa;
import br.edu.ifpb.myhome.compra.Compra;
import br.edu.ifpb.myhome.compra.SolicitacaoCompra;
import br.edu.ifpb.myhome.estado.ArquivadoState;
import br.edu.ifpb.myhome.estado.AtivoState;
import br.edu.ifpb.myhome.chat.Mensagem;
import br.edu.ifpb.myhome.factory.ApartamentoFactory;
import br.edu.ifpb.myhome.factory.CasaFactory;
import br.edu.ifpb.myhome.factory.ImovelFactory;
import br.edu.ifpb.myhome.factory.TerrenoFactory;
import br.edu.ifpb.myhome.imovel.Apartamento;
import br.edu.ifpb.myhome.imovel.Casa;
import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.imovel.Terreno;
import br.edu.ifpb.myhome.notificacao.EmailAdapter;
import br.edu.ifpb.myhome.notificacao.NotificacaoInteressadosObserver;
import br.edu.ifpb.myhome.notificacao.NotificacaoObserver;
import br.edu.ifpb.myhome.notificacao.Observer;
import br.edu.ifpb.myhome.notificacao.ServicoNotificacaoExterno;
import br.edu.ifpb.myhome.saida.ConsoleSaida;
import br.edu.ifpb.myhome.saida.Saida;
import br.edu.ifpb.myhome.usuario.Usuario;
import br.edu.ifpb.myhome.util.FormatadorMoeda;
import br.edu.ifpb.myhome.validacao.ValidadorAnuncio;
import br.edu.ifpb.myhome.validacao.ValidadorImovel;
import br.edu.ifpb.myhome.validacao.ValidadorPreco;
import br.edu.ifpb.myhome.validacao.ValidadorTitulo;

public class Main {

    public static void main(String[] args) {
        List<Usuario> usuarios = new ArrayList<>();
        List<Anuncio> anuncios = new ArrayList<>();
        Map<Anuncio, Usuario> donoDoAnuncio = new HashMap<>();
        List<Conversa> conversas = new ArrayList<>();
        List<Compra> compras = new ArrayList<>();
        List<SolicitacaoCompra> solicitacoesPendentes = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        ServicoNotificacaoExterno servicoNotif = new EmailAdapter();
        Observer notifObserver = new NotificacaoObserver(servicoNotif);
        Saida saida = new ConsoleSaida();
        Observer interessadosObserver = new NotificacaoInteressadosObserver(saida);

        Configuracao.getInstancia().carregarParametros();
        try {
            Path pathUsuarios = Paths.get("dados/usuarios.csv");
            Path pathAnuncios = Paths.get("dados/anuncios.csv");
            if (Files.exists(pathUsuarios)) {
                List<Usuario> carregados = CarregadorCSV.carregarUsuarios(pathUsuarios);
                usuarios.addAll(carregados);
                saida.escrever("E1 - Carregados " + carregados.size() + " usuário(s) de dados/usuarios.csv");
            }
            if (Files.exists(pathAnuncios) && !usuarios.isEmpty()) {
                CarregadorCSV.carregarAnuncios(pathAnuncios, usuarios, anuncios, donoDoAnuncio);
                for (Anuncio a : anuncios) {
                    a.adicionarObserver(notifObserver);
                    a.adicionarObserver(interessadosObserver);
                    a.setEstado(new AtivoState());
                }
                saida.escrever("E1 - Carregados " + anuncios.size() + " anúncio(s) de dados/anuncios.csv");
            }
        } catch (Exception e) {
            saida.escrever("Aviso: não foi possível carregar CSV - " + e.getMessage());
        }

        Usuario usuarioLogado = null;
        boolean exibirMenuCompleto = false;
        int opcao;
        do {
            if (usuarioLogado != null && !exibirMenuCompleto) {
                saida.escrever("\n=== MYHOME (menu reduzido) ===");
                saida.escrever("Logado: " + usuarioLogado.getNome());
                saida.escrever("1 - Ver mensagens do chat");
                saida.escrever("2 - Ver mensagens de confirmação de pagamento");
                saida.escrever("3 - Menu completo");
                saida.escrever("0 - Sair do sistema");
                saida.escreverSemQuebra("Opção: ");
                try {
                    opcao = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }
                if (opcao == 1) {
                    listarMensagensChat(saida, conversas, usuarioLogado);
                    saida.escrever("");
                    saida.escrever("Pressione Enter para continuar...");
                    sc.nextLine();
                } else if (opcao == 2) {
                    listarMensagensConfirmacaoPagamento(saida, solicitacoesPendentes, donoDoAnuncio, usuarioLogado);
                    saida.escrever("");
                    saida.escrever("Pressione Enter para continuar...");
                    sc.nextLine();
                } else if (opcao == 3) {
                    exibirMenuCompleto = true;
                } else if (opcao == 0) {
                    saida.escrever(usuarioLogado.getNome() + " saiu do sistema.");
                    usuarioLogado = null;
                    exibirMenuCompleto = false;
                } else {
                    saida.escrever("Opção inválida.");
                }
                continue;
            }

            saida.escrever("\n=== MYHOME ===");
            if (usuarioLogado != null) {
                saida.escrever("Logado: " + usuarioLogado.getNome());
            } else {
                saida.escrever("Nenhum usuário logado.");
            }
            saida.escrever("1 - Entrar no sistema (digite seu email)");
            saida.escrever("2 - Sair do sistema");
            saida.escrever("3 - Cadastrar usuário");
            saida.escrever("4 - Listar usuários");
            saida.escrever("5 - Criar anúncio");
            saida.escrever("6 - Buscar imóveis / visualizar / favoritar / comprar anúncio");
            saida.escrever("7 - Chat (conversar sobre anúncio)");
            saida.escrever("8 - Listar compras");
            saida.escrever("9 - Consultar log de mudanças de estado");
            if (usuarioLogado != null) {
                saida.escrever("10 - Voltar ao menu reduzido");
            }
            saida.escrever("0 - Sair");
            saida.escreverSemQuebra("Opção: ");
            try {
                opcao = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            if (opcao == 10 && usuarioLogado != null) {
                exibirMenuCompleto = false;
            } else if (opcao == 1) {
                saida.escrever("\n--- Entrar no sistema ---");
                saida.escreverSemQuebra("Digite seu email: ");
                String email = sc.nextLine().trim();
                if (email.isEmpty()) {
                    saida.escrever("Email não pode ser vazio.");
                } else {
                    Usuario encontrado = buscarUsuarioPorEmail(usuarios, email);
                    if (encontrado == null) {
                        saida.escrever("Usuário com email \"" + email + "\" não encontrado. Deseja cadastrar? (s/n): ");
                        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
                            saida.escreverSemQuebra("Nome: ");
                            String nome = sc.nextLine().trim();
                            Usuario novo = new Usuario(nome.isEmpty() ? "Usuário" : nome, email);
                            usuarios.add(novo);
                            usuarioLogado = novo;
                            saida.escrever("Cadastrado e logado como " + novo.getNome() + ".");
                            saida.escrever("");
                            saida.escrever("Pressione Enter para continuar...");
                            sc.nextLine();
                        } else {
                            saida.escrever("Entrada cancelada.");
                        }
                    } else {
                        usuarioLogado = encontrado;
                        saida.escrever("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
                        saida.escrever("");
                        saida.escrever("Pressione Enter para continuar...");
                        sc.nextLine();
                    }
                }
            } else if (opcao == 2) {
                if (usuarioLogado != null) {
                    saida.escrever(usuarioLogado.getNome() + " saiu do sistema.");
                    usuarioLogado = null;
                } else {
                    saida.escrever("Nenhum usuário logado.");
                }
            } else if (opcao == 3) {
                saida.escrever("\n--- Cadastro de usuário ---");
                saida.escreverSemQuebra("Nome: ");
                String nome = sc.nextLine().trim();
                saida.escreverSemQuebra("Email: ");
                String email = sc.nextLine().trim();
                Usuario u = new Usuario(nome, email);
                usuarios.add(u);
                saida.escrever("Usuário cadastrado (ID: " + u.getId() + "): " + u.getNome());
            } else if (opcao == 4) {
                saida.escrever("\n--- Usuários ---");
                if (usuarios.isEmpty()) {
                    saida.escrever("Nenhum usuário cadastrado.");
                } else {
                    for (int i = 0; i < usuarios.size(); i++) {
                        Usuario u = usuarios.get(i);
                        saida.escrever((i + 1) + ") ID: " + u.getId() + " | " + u.getNome() + " | " + u.getEmail());
                    }
                }
                saida.escrever("");
                saida.escrever("Pressione Enter para continuar...");
                sc.nextLine();
            } else if (opcao == 5) {
                Usuario usuario = usuarioLogado;
                if (usuario == null && !usuarios.isEmpty()) {
                    saida.escrever("\n--- Usuários ---");
                    for (int i = 0; i < usuarios.size(); i++) {
                        Usuario u = usuarios.get(i);
                        saida.escrever((i + 1) + ") ID: " + u.getId() + " | " + u.getNome());
                    }
                    saida.escreverSemQuebra("Número do usuário (1-" + usuarios.size() + "): ");
                    int idx = lerInt(sc, 0);
                    if (idx >= 1 && idx <= usuarios.size()) usuario = usuarios.get(idx - 1);
                }
                if (usuario != null) {
                    saida.escrever("\n--- Novo anúncio (por " + usuario.getNome() + ") ---");
                        saida.escreverSemQuebra("Título do anúncio: ");
                        String titulo = sc.nextLine().trim();
                        saida.escrever("Tipo de oferta: 1=Venda 2=Aluguel 3=Temporada");
                        saida.escreverSemQuebra("Opção: ");
                        int tipoOfertaOp;
                        try { tipoOfertaOp = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { tipoOfertaOp = 1; }
                        TipoOferta tipoOferta = tipoOfertaOp == 2 ? TipoOferta.ALUGUEL : (tipoOfertaOp == 3 ? TipoOferta.TEMPORADA : TipoOferta.VENDA);
                        saida.escreverSemQuebra("Valor venda (R$): ");
                        double valorVenda = lerDouble(sc, 0);
                        saida.escreverSemQuebra("Valor aluguel (R$/mês): ");
                        double valorAluguel = lerDouble(sc, 0);
                        saida.escreverSemQuebra("Valor temporada (R$/diária): ");
                        double valorTemporada = lerDouble(sc, 0);
                        saida.escrever("Tipo de imóvel: 1=Casa 2=Apartamento 3=Terreno");
                        saida.escreverSemQuebra("Tipo: ");
                        int tipoImovel;
                        try { tipoImovel = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { tipoImovel = 0; }
                        ImovelFactory factory = null;
                        if (tipoImovel == 1) factory = new CasaFactory();
                        else if (tipoImovel == 2) factory = new ApartamentoFactory();
                        else if (tipoImovel == 3) factory = new TerrenoFactory();
                        if (factory == null) {
                            saida.escrever("Tipo inválido. Anúncio cancelado.");
                        } else {
                            Imovel imovel = factory.criarImovel();
                            saida.escreverSemQuebra("Endereço do imóvel: ");
                            imovel.setEndereco(sc.nextLine().trim());
                            saida.escreverSemQuebra("Bairro (ex.: Jaguaribe, Castelo Branco, Bancários, Bessa, Tambaú, Altiplano): ");
                            imovel.setBairro(sc.nextLine().trim());
                            saida.escreverSemQuebra("Área (m²): ");
                            imovel.setAreaMetrosQuadrados(lerDouble(sc, 0));
                            saida.escreverSemQuebra("Descrição do imóvel (detalhes): ");
                            imovel.setDescricao(sc.nextLine().trim());
                            saida.escreverSemQuebra("Quantidade de suítes: ");
                            imovel.setQuantidadeSuites(lerInt(sc, 0));
                            if (imovel instanceof Casa) {
                                saida.escreverSemQuebra("Possui quintal? (s/n): ");
                                ((Casa) imovel).setPossuiQuintal(sc.nextLine().trim().equalsIgnoreCase("s"));
                            } else if (imovel instanceof Apartamento) {
                                saida.escreverSemQuebra("Número de quartos: ");
                                try { ((Apartamento) imovel).setQuartos(Integer.parseInt(sc.nextLine().trim())); } catch (NumberFormatException e) {}
                                ((Apartamento) imovel).setArea(imovel.getAreaMetrosQuadrados());
                                saida.escreverSemQuebra("Andar: ");
                                try {
                                    ((Apartamento) imovel).setAndar(Integer.parseInt(sc.nextLine().trim()));
                                } catch (NumberFormatException e) {}
                                saida.escreverSemQuebra("Possui elevador? (s/n): ");
                                ((Apartamento) imovel).setPossuiElevador(sc.nextLine().trim().equalsIgnoreCase("s"));
                            } else if (imovel instanceof Terreno) {
                                ((Terreno) imovel).setArea(imovel.getAreaMetrosQuadrados());
                            }
                            Anuncio anuncio = new Anuncio(titulo, valorVenda, imovel);
                            anuncio.setTipoOferta(tipoOferta);
                            anuncio.setValorAluguel(valorAluguel);
                            anuncio.setValorTemporada(valorTemporada);
                            ValidadorAnuncio v1 = new ValidadorPreco();
                            ValidadorAnuncio v2 = new ValidadorTitulo();
                            ValidadorAnuncio v3 = new ValidadorImovel();
                            v1.setProximo(v2);
                            v2.setProximo(v3);
                            if (v1.validar(anuncio)) {
                                anuncio.adicionarObserver(notifObserver);
                                anuncio.adicionarObserver(interessadosObserver);
                                usuario.editarAnuncio(anuncio);
                                anuncios.add(anuncio);
                                donoDoAnuncio.put(anuncio, usuario);
                                saida.escrever("Anúncio criado: " + anuncio.getTitulo() + " | " + anuncio.getTipoOferta().getLabel() + " | Venda: " + FormatadorMoeda.formatarReal(anuncio.getValorVenda()) + " | " + anuncio.getImovel().getAreaMetrosQuadrados() + " m² | " + anuncio.getImovel().getQuantidadeSuites() + " suítes");
                            } else {
                                saida.escrever("Validação falhou. Anúncio não criado.");
                            }
                        }
                } else if (usuarios.isEmpty()) {
                    saida.escrever("Cadastre um usuário primeiro.");
                } else {
                    saida.escrever("Usuário inválido.");
                }
            } else if (opcao == 6) {
                Usuario usuario = usuarioLogado;
                if (usuario == null && !usuarios.isEmpty()) {
                    saida.escrever("\n--- Usuários ---");
                    for (int i = 0; i < usuarios.size(); i++) {
                        Usuario u = usuarios.get(i);
                        saida.escrever((i + 1) + ") ID: " + u.getId() + " | " + u.getNome());
                    }
                    saida.escreverSemQuebra("Número do usuário (1-" + usuarios.size() + "): ");
                    int idx = lerInt(sc, 0);
                    if (idx >= 1 && idx <= usuarios.size()) usuario = usuarios.get(idx - 1);
                }
                if (usuario != null) {
                    saida.escrever("\n--- Ações: " + usuario.getNome() + " ---");
                        saida.escrever("1 - Buscar imóveis");
                        saida.escrever("2 - Visualizar anúncio");
                        saida.escrever("3 - Favoritar anúncio");
                        saida.escrever("4 - Ver mensagens (pagamentos aguardando confirmação)");
                        saida.escreverSemQuebra("Opção: ");
                        int acao;
                        try {
                            acao = Integer.parseInt(sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            acao = 0;
                        }
                        if (acao == 1) {
                            saida.escrever("\n--- Busca avançada (RF06) ---");
                            saida.escrever("Escolha apenas os critérios que deseja usar.");
                            saida.escrever("Digite os números separados por vírgula (ex: 1,2 para tipo e bairro).");
                            saida.escrever("1=Tipo  2=Bairro  3=Preço  4=Área  5=Quartos");
                            saida.escreverSemQuebra("Critérios: ");
                            String criteriosStr = sc.nextLine().trim();
                            java.util.Set<Integer> criterios = new java.util.HashSet<>();
                            if (!criteriosStr.isEmpty()) {
                                for (String s : criteriosStr.split("[,;\\s]+")) {
                                    try {
                                        int n = Integer.parseInt(s.trim());
                                        if (n >= 1 && n <= 5) criterios.add(n);
                                    } catch (NumberFormatException e) { }
                                }
                            }
                            List<FiltroBusca> filtros = new ArrayList<>();
                            if (criterios.contains(1)) {
                                saida.escrever("Tipo de imóvel:  1=Casa 2=Apartamento 3=Terreno 0=Não filtrar");
                                saida.escreverSemQuebra("Opção: ");
                                int tipoFiltro = lerInt(sc, 0);
                                if (tipoFiltro == 1) filtros.add(new FiltroTipoImovel("casa"));
                                else if (tipoFiltro == 2) filtros.add(new FiltroTipoImovel("apartamento"));
                                else if (tipoFiltro == 3) filtros.add(new FiltroTipoImovel("terreno"));
                            }
                            if (criterios.contains(2)) {
                                saida.escreverSemQuebra("Bairro (ex.: Jaguaribe, Castelo Branco, Bancários, Bessa, Tambaú, Altiplano): ");
                                String bairro = sc.nextLine().trim();
                                if (!bairro.isEmpty()) filtros.add(new FiltroBairro(bairro));
                            }
                            if (criterios.contains(3)) {
                                saida.escreverSemQuebra("Preço mínimo (R$): ");
                                double precoMin = lerDouble(sc, -1);
                                saida.escreverSemQuebra("Preço máximo (R$): ");
                                double precoMax = lerDouble(sc, -1);
                                if (precoMin >= 0 || precoMax >= 0) {
                                    double pMin = precoMin >= 0 ? precoMin : 0;
                                    double pMax = precoMax >= 0 ? precoMax : Double.MAX_VALUE;
                                    if (pMin <= pMax) filtros.add(new FiltroPreco(pMin, pMax));
                                }
                            }
                            if (criterios.contains(4)) {
                                saida.escreverSemQuebra("Área mínima (m²): ");
                                double areaMin = lerDouble(sc, -1);
                                saida.escreverSemQuebra("Área máxima (m²): ");
                                double areaMax = lerDouble(sc, -1);
                                if (areaMin >= 0 || areaMax >= 0) {
                                    double aMin = areaMin >= 0 ? areaMin : 0;
                                    double aMax = areaMax >= 0 ? areaMax : Double.MAX_VALUE;
                                    if (aMin <= aMax) filtros.add(new FiltroArea(aMin, aMax));
                                }
                            }
                            if (criterios.contains(5)) {
                                saida.escreverSemQuebra("Número mínimo de quartos (apartamentos): ");
                                int quartosMin = lerInt(sc, -1);
                                if (quartosMin >= 0) filtros.add(new FiltroQuartos(quartosMin));
                            }
                            if (criterios.isEmpty()) saida.escrever("Nenhum critério escolhido. Listando todos os anúncios.");
                            List<Anuncio> resultado = BuscaService.buscar(anuncios, filtros);
                            saida.escrever("\n--- Resultado da busca ---");
                            if (resultado.isEmpty()) {
                                saida.escrever("Nenhum anúncio encontrado com esse filtro.");
                            } else {
                                for (int i = 0; i < resultado.size(); i++) {
                                    Anuncio a = resultado.get(i);
                                    Usuario anunciante = donoDoAnuncio.get(a);
                                    String nomeAnunciante = anunciante != null ? anunciante.getNome() : "-";
                                    saida.escrever((i + 1) + ") " + a.getTitulo() + " | " + FormatadorMoeda.formatarReal(a.getPreco()) + " | " + a.getTipoImovel() + " | Anunciante: " + nomeAnunciante);
                                }
                                saida.escrever("");
                                saida.escrever("Comprar/Alugar algum? Digite o número do anúncio (1-" + resultado.size() + ") ou 0 para voltar.");
                                saida.escreverSemQuebra("Opção: ");
                                int numCompra;
                                try { numCompra = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { numCompra = 0; }
                                if (numCompra >= 1 && numCompra <= resultado.size()) {
                                    Anuncio anuncioCompra = resultado.get(numCompra - 1);
                                    if (!"Arquivado".equals(anuncioCompra.getEstadoAtual())) {
                                        saida.escrever("Forma de pagamento: 1=Cartão 2=PIX 3=Boleto");
                                        saida.escreverSemQuebra("Opção: ");
                                        int formaOp = lerInt(sc, 1);
                                        FormaPagamento forma = formaOp == 2 ? new PagamentoPix() : (formaOp == 3 ? new PagamentoBoleto() : new PagamentoCartao());
                                        double valor = anuncioCompra.getTipoOferta() == TipoOferta.ALUGUEL ? anuncioCompra.getValorAluguel() : (anuncioCompra.getTipoOferta() == TipoOferta.TEMPORADA ? anuncioCompra.getValorTemporada() : anuncioCompra.getValorVenda());
                                        ServicoPagamento servicoPagamento = new ServicoPagamento(forma);
                                        saida.escrever(servicoPagamento.pagar(valor));
                                        saida.escrever("Executar pagamento? (s/n): ");
                                        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
                                            solicitacoesPendentes.add(new SolicitacaoCompra(usuario, anuncioCompra, forma));
                                            saida.escrever("Pagamento executado via " + forma.getNome() + ". Aguardando confirmação do vendedor.");
                                        } else {
                                            saida.escrever("Pagamento cancelado.");
                                        }
                                    } else {
                                        saida.escrever("Este anúncio já foi vendido/alugado.");
                                    }
                                }
                            }
                            saida.escrever("");
                            saida.escrever("Pressione Enter para continuar...");
                            sc.nextLine();
                        } else if (acao == 2) {
                            if (anuncios.isEmpty()) {
                                saida.escrever("Não há anúncios.");
                            } else {
                                saida.escrever("\n--- Anúncios ---");
                                for (int i = 0; i < anuncios.size(); i++) {
                                    Anuncio a = anuncios.get(i);
                                    saida.escrever((i + 1) + ") " + a.getTitulo() + " | " + FormatadorMoeda.formatarReal(a.getPreco()));
                                }
                                saida.escreverSemQuebra("Número do anúncio para visualizar: ");
                                int n;
                                try {
                                    n = Integer.parseInt(sc.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    n = 0;
                                }
                                if (n >= 1 && n <= anuncios.size()) {
                                    Anuncio a = anuncios.get(n - 1);
                                    usuario.visualizarAnuncio(a);
                                    detalhesAnuncio(saida, a);
                                    if (!"Arquivado".equals(a.getEstadoAtual())) {
                                        saida.escrever("");
                                        saida.escrever("Comprar/Alugar este imóvel? (s/n): ");
                                        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
                                            saida.escrever("Forma de pagamento: 1=Cartão 2=PIX 3=Boleto");
                                            saida.escreverSemQuebra("Opção: ");
                                            int formaOp = lerInt(sc, 1);
                                            FormaPagamento forma = formaOp == 2 ? new PagamentoPix() : (formaOp == 3 ? new PagamentoBoleto() : new PagamentoCartao());
                                            double valor = a.getTipoOferta() == TipoOferta.ALUGUEL ? a.getValorAluguel() : (a.getTipoOferta() == TipoOferta.TEMPORADA ? a.getValorTemporada() : a.getValorVenda());
                                            ServicoPagamento servicoPagamento = new ServicoPagamento(forma);
                                            saida.escrever(servicoPagamento.pagar(valor));
                                            saida.escrever("Executar pagamento? (s/n): ");
                                            if (sc.nextLine().trim().equalsIgnoreCase("s")) {
                                                solicitacoesPendentes.add(new SolicitacaoCompra(usuario, a, forma));
                                                saida.escrever("Pagamento executado via " + forma.getNome() + ". Aguardando confirmação do vendedor.");
                                            } else {
                                                saida.escrever("Pagamento cancelado.");
                                            }
                                        }
                                    }
                                } else {
                                    saida.escrever("Anúncio inválido.");
                                }
                            }
                        } else if (acao == 3) {
                            if (anuncios.isEmpty()) {
                                saida.escrever("Não há anúncios.");
                            } else {
                                saida.escrever("\n--- Anúncios ---");
                                for (int i = 0; i < anuncios.size(); i++) {
                                    Anuncio a = anuncios.get(i);
                                    saida.escrever((i + 1) + ") " + a.getTitulo() + " | " + FormatadorMoeda.formatarReal(a.getPreco()));
                                }
                                saida.escreverSemQuebra("Número do anúncio para favoritar: ");
                                int n;
                                try {
                                    n = Integer.parseInt(sc.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    n = 0;
                                }
                                if (n >= 1 && n <= anuncios.size()) {
                                    usuario.favoritarAnuncio(anuncios.get(n - 1));
                                    saida.escrever("Anúncio favoritado.");
                                } else {
                                    saida.escrever("Anúncio inválido.");
                                }
                            }
                        } else if (acao == 4) {
                            List<SolicitacaoCompra> pendentesDoVendedor = new ArrayList<>();
                            for (SolicitacaoCompra s : solicitacoesPendentes) {
                                if (donoDoAnuncio.get(s.getAnuncio()) == usuario) pendentesDoVendedor.add(s);
                            }
                            if (pendentesDoVendedor.isEmpty()) {
                                saida.escrever("Nenhuma mensagem (pagamento pendente de confirmação) para seus anúncios.");
                            } else {
                                saida.escrever("\n--- Mensagens: pagamentos aguardando sua confirmação ---");
                                for (int i = 0; i < pendentesDoVendedor.size(); i++) {
                                    SolicitacaoCompra s = pendentesDoVendedor.get(i);
                                    saida.escrever((i + 1) + ") \"" + s.getAnuncio().getTitulo() + "\" | Comprador: " + s.getComprador().getNome() + " | " + s.getFormaPagamentoNome() + " — confirme o recebimento");
                                }
                                saida.escreverSemQuebra("Número da mensagem para confirmar recebimento (1-" + pendentesDoVendedor.size() + "): ");
                                int is = lerInt(sc, 0);
                                if (is >= 1 && is <= pendentesDoVendedor.size()) {
                                    SolicitacaoCompra s = pendentesDoVendedor.get(is - 1);
                                    solicitacoesPendentes.remove(s);
                                    compras.add(new Compra(s.getComprador(), s.getAnuncio()));
                                    s.getAnuncio().setEstado(new ArquivadoState());
                                    saida.escrever("Pagamento confirmado. Imóvel marcado como " + (s.getAnuncio().getTipoOferta() == TipoOferta.ALUGUEL ? "alugado" : s.getAnuncio().getTipoOferta() == TipoOferta.TEMPORADA ? "alugado (temporada)" : "vendido") + ". Anúncio arquivado.");
                                } else {
                                    saida.escrever("Solicitação inválida.");
                                }
                            }
                        } else {
                            saida.escrever("Opção inválida.");
                        }
                } else if (usuarios.isEmpty()) {
                    saida.escrever("Cadastre um usuário primeiro.");
                } else {
                    saida.escrever("Usuário inválido.");
                }
            } else if (opcao == 7) {
                if (usuarios.isEmpty() || anuncios.isEmpty()) {
                    saida.escrever("Cadastre usuários e anúncios primeiro.");
                } else {
                    saida.escrever("\n--- Chat ---");
                    saida.escrever("1 - Interessado: conversar sobre um anúncio");
                    saida.escrever("2 - Dono do anúncio: ver e responder conversas");
                    saida.escreverSemQuebra("Opção: ");
                    int chatOp;
                    try {
                        chatOp = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        chatOp = 0;
                    }
                    if (chatOp == 1) {
                        saida.escrever("\n--- Usuários ---");
                        for (int i = 0; i < usuarios.size(); i++) {
                            saida.escrever((i + 1) + ") " + usuarios.get(i).getNome());
                        }
                        saida.escreverSemQuebra("Número do usuário (interessado): ");
                        int ic;
                        try { ic = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { ic = 0; }
                        if (ic < 1 || ic > usuarios.size()) {
                            saida.escrever("Usuário inválido.");
                        } else {
                            saida.escrever("\n--- Anúncios ---");
                            for (int i = 0; i < anuncios.size(); i++) {
                                Anuncio a = anuncios.get(i);
                                saida.escrever((i + 1) + ") " + a.getTitulo() + " | " + FormatadorMoeda.formatarReal(a.getPreco()));
                            }
                            saida.escreverSemQuebra("Número do anúncio: ");
                            int ia;
                            try { ia = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { ia = 0; }
                            if (ia < 1 || ia > anuncios.size()) {
                                saida.escrever("Anúncio inválido.");
                            } else {
                                Usuario interessado = usuarios.get(ic - 1);
                                Anuncio anuncio = anuncios.get(ia - 1);
                                Usuario dono = donoDoAnuncio.get(anuncio);
                                if (dono == null) {
                                    saida.escrever("Anúncio sem dono associado.");
                                } else {
                                    anuncio.adicionarInteressado(interessado);
                                    Conversa conv = null;
                                    for (Conversa c : conversas) {
                                        if (c.ehMesma(anuncio, interessado, dono)) {
                                            conv = c;
                                            break;
                                        }
                                    }
                                    if (conv == null) {
                                        conv = new Conversa(anuncio, interessado, dono);
                                        conversas.add(conv);
                                    }
                                    entrarNoChat(sc, saida, conv, interessado, dono, true);
                                }
                            }
                        }
                    } else if (chatOp == 2) {
                        saida.escrever("\n--- Usuários ---");
                        for (int i = 0; i < usuarios.size(); i++) {
                            saida.escrever((i + 1) + ") " + usuarios.get(i).getNome());
                        }
                        saida.escreverSemQuebra("Número do usuário (dono do anúncio): ");
                        int ia;
                        try { ia = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { ia = 0; }
                        if (ia < 1 || ia > usuarios.size()) {
                            saida.escrever("Usuário inválido.");
                        } else {
                            Usuario dono = usuarios.get(ia - 1);
                            List<Conversa> convsDoDono = new ArrayList<>();
                            for (Conversa c : conversas) {
                                if (c.getDonoAnuncio() == dono) convsDoDono.add(c);
                            }
                            if (convsDoDono.isEmpty()) {
                                saida.escrever("Nenhuma conversa ainda.");
                            } else {
                                saida.escrever("\n--- Suas conversas ---");
                                for (int i = 0; i < convsDoDono.size(); i++) {
                                    Conversa c = convsDoDono.get(i);
                                    saida.escrever((i + 1) + ") Anúncio: " + c.getAnuncio().getTitulo() + " | Interessado: " + c.getInteressado().getNome());
                                }
                                saida.escreverSemQuebra("Número da conversa: ");
                                int ic;
                                try { ic = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { ic = 0; }
                                if (ic >= 1 && ic <= convsDoDono.size()) {
                                    Conversa conv = convsDoDono.get(ic - 1);
                                    entrarNoChat(sc, saida, conv, conv.getInteressado(), dono, false);
                                } else {
                                    saida.escrever("Conversa inválida.");
                                }
                            }
                        }
                    } else {
                        saida.escrever("Opção inválida.");
                    }
                }
            } else if (opcao == 8) {
                saida.escrever("\n--- Listar compras ---");
                if (compras.isEmpty()) {
                    saida.escrever("Nenhuma compra registrada.");
                } else {
                    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    for (int i = 0; i < compras.size(); i++) {
                        Compra c = compras.get(i);
                        saida.escrever((i + 1) + ") " + c.getComprador().getNome() + " | \"" + c.getAnuncio().getTitulo() + "\" | " + FormatadorMoeda.formatarReal(c.getAnuncio().getPreco()) + " | " + c.getData().format(fmt));
                    }
                }
                saida.escrever("");
                saida.escrever("Pressione Enter para continuar...");
                sc.nextLine();
            } else if (opcao == 9) {
                saida.escrever("\n--- Log de mudanças de estado ---");
                List<LogMudancaEstado.EntradaLogEstado> entradas = LogMudancaEstado.getInstancia().getEntradas();
                if (entradas.isEmpty()) {
                    saida.escrever("Nenhuma mudança de estado registrada.");
                } else {
                    for (LogMudancaEstado.EntradaLogEstado e : entradas) {
                        saida.escrever(e.toString());
                    }
                }
                saida.escrever("");
                saida.escrever("Pressione Enter para continuar...");
                sc.nextLine();
            } else if (opcao == 0) {
                saida.escrever("Até logo.");
            } else {
                saida.escrever("Opção inválida.");
            }
        } while (opcao != 0);

        sc.close();
    }

    /** Lista conversas do chat em que o usuário participa (como interessado ou dono do anúncio). */
    private static void listarMensagensChat(Saida saida, List<Conversa> conversas, Usuario usuario) {
        saida.escrever("\n--- Mensagens do chat ---");
        List<Conversa> doUsuario = new ArrayList<>();
        for (Conversa c : conversas) {
            if (c.getInteressado() == usuario || c.getDonoAnuncio() == usuario) {
                doUsuario.add(c);
            }
        }
        if (doUsuario.isEmpty()) {
            saida.escrever("Nenhuma conversa ainda.");
        } else {
            for (int i = 0; i < doUsuario.size(); i++) {
                Conversa c = doUsuario.get(i);
                String outro = c.getInteressado() == usuario ? c.getDonoAnuncio().getNome() : c.getInteressado().getNome();
                String papel = c.getInteressado() == usuario ? "interessado" : "dono do anúncio";
                saida.escrever((i + 1) + ") Anúncio: \"" + c.getAnuncio().getTitulo() + "\" | Com: " + outro + " (" + papel + ") | " + c.getMensagens().size() + " mensagem(ns)");
                if (!c.getMensagens().isEmpty()) {
                    Mensagem ultima = c.getMensagens().get(c.getMensagens().size() - 1);
                    saida.escrever("   Última: " + ultima.toString());
                }
            }
        }
    }

    /** Lista mensagens de confirmação de pagamento (solicitações pendentes para anúncios do usuário). */
    private static void listarMensagensConfirmacaoPagamento(Saida saida, List<SolicitacaoCompra> solicitacoesPendentes,
                                                            Map<Anuncio, Usuario> donoDoAnuncio, Usuario usuario) {
        saida.escrever("\n--- Mensagens de confirmação de pagamento ---");
        List<SolicitacaoCompra> doVendedor = new ArrayList<>();
        for (SolicitacaoCompra s : solicitacoesPendentes) {
            if (donoDoAnuncio.get(s.getAnuncio()) == usuario) {
                doVendedor.add(s);
            }
        }
        if (doVendedor.isEmpty()) {
            saida.escrever("Nenhum pagamento aguardando sua confirmação.");
        } else {
            for (int i = 0; i < doVendedor.size(); i++) {
                SolicitacaoCompra s = doVendedor.get(i);
                saida.escrever((i + 1) + ") \"" + s.getAnuncio().getTitulo() + "\" | Comprador: " + s.getComprador().getNome() + " | " + s.getFormaPagamentoNome() + " — confirme o recebimento");
            }
        }
    }

    /** Busca usuário por email (ignora maiúsculas/minúsculas). Retorna o primeiro encontrado ou null. */
    private static Usuario buscarUsuarioPorEmail(List<Usuario> usuarios, String email) {
        if (email == null || email.isEmpty()) return null;
        String e = email.trim().toLowerCase();
        for (Usuario u : usuarios) {
            if (u.getEmail() != null && u.getEmail().trim().toLowerCase().equals(e)) return u;
        }
        return null;
    }

    private static double lerDouble(Scanner sc, double padrao) {
        try { return Double.parseDouble(sc.nextLine().trim().replace(",", ".")); } catch (NumberFormatException e) { return padrao; }
    }

    private static int lerInt(Scanner sc, int padrao) {
        try { return Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { return padrao; }
    }

    private static void detalhesAnuncio(Saida saida, Anuncio a) {
        saida.escrever("--- " + a.getTitulo() + " ---");
        saida.escrever("Tipo: " + a.getTipoImovel() + " | Oferta: " + a.getTipoOferta().getLabel());
        saida.escrever("Venda: " + FormatadorMoeda.formatarReal(a.getValorVenda()) + " | Aluguel: " + FormatadorMoeda.formatarReal(a.getValorAluguel()) + "/mês | Temporada: " + FormatadorMoeda.formatarReal(a.getValorTemporada()) + "/diária");
        if (a.getImovel() != null) {
            saida.escrever("Área: " + a.getImovel().getAreaMetrosQuadrados() + " m² | Suítes: " + a.getImovel().getQuantidadeSuites());
            if (a.getImovel().getDescricao() != null && !a.getImovel().getDescricao().isEmpty()) {
                saida.escrever("Descrição: " + a.getImovel().getDescricao());
            }
        }
    }

    private static void entrarNoChat(Scanner sc, Saida saida, Conversa conv, Usuario interessado, Usuario donoAnuncio, boolean souInteressado) {
        Usuario usuarioAtual = souInteressado ? interessado : donoAnuncio;
        String quem = souInteressado ? "Interessado" : "Dono do anúncio";
        saida.escrever("\n--- Chat: " + conv.getAnuncio().getTitulo() + " ---");
        saida.escrever("Você está como " + quem + ". Digite 'sair' para voltar ao menu.\n");
        while (true) {
            for (Mensagem m : conv.getMensagens()) {
                saida.escrever(m.toString());
            }
            saida.escreverSemQuebra(quem + " > ");
            String texto = sc.nextLine();
            if (texto == null) texto = "";
            if (texto.trim().equalsIgnoreCase("sair")) {
                saida.escrever("Saindo do chat.");
                break;
            }
            usuarioAtual.enviarMensagemVia(conv, texto);
        }
    }
}
