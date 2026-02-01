package src.br.edu.ifpb.myhome;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;
import src.br.edu.ifpb.myhome.anuncio.LogMudancaEstado;
import src.br.edu.ifpb.myhome.busca.BuscaService;
import src.br.edu.ifpb.myhome.busca.FiltroArea;
import src.br.edu.ifpb.myhome.busca.FiltroBusca;
import src.br.edu.ifpb.myhome.busca.FiltroPreco;
import src.br.edu.ifpb.myhome.busca.FiltroTitulo;
import src.br.edu.ifpb.myhome.busca.FiltroTipoImovel;
import src.br.edu.ifpb.myhome.config.Configuracao;
import src.br.edu.ifpb.myhome.csv.CarregadorCSV;
import src.br.edu.ifpb.myhome.chat.Conversa;
import src.br.edu.ifpb.myhome.compra.Compra;
import src.br.edu.ifpb.myhome.estado.ArquivadoState;
import src.br.edu.ifpb.myhome.estado.AtivoState;
import src.br.edu.ifpb.myhome.chat.Mensagem;
import src.br.edu.ifpb.myhome.factory.ApartamentoFactory;
import src.br.edu.ifpb.myhome.factory.CasaFactory;
import src.br.edu.ifpb.myhome.factory.ImovelFactory;
import src.br.edu.ifpb.myhome.factory.TerrenoFactory;
import src.br.edu.ifpb.myhome.imovel.Apartamento;
import src.br.edu.ifpb.myhome.imovel.Casa;
import src.br.edu.ifpb.myhome.imovel.Imovel;
import src.br.edu.ifpb.myhome.imovel.Terreno;
import src.br.edu.ifpb.myhome.notificacao.EmailAdapter;
import src.br.edu.ifpb.myhome.notificacao.NotificacaoInteressadosObserver;
import src.br.edu.ifpb.myhome.notificacao.NotificacaoObserver;
import src.br.edu.ifpb.myhome.notificacao.Observer;
import src.br.edu.ifpb.myhome.notificacao.ServicoNotificacaoExterno;
import src.br.edu.ifpb.myhome.saida.ConsoleSaida;
import src.br.edu.ifpb.myhome.saida.Saida;
import src.br.edu.ifpb.myhome.usuario.Usuario;
import src.br.edu.ifpb.myhome.util.FormatadorMoeda;
import src.br.edu.ifpb.myhome.validacao.ValidadorAnuncio;
import src.br.edu.ifpb.myhome.validacao.ValidadorImovel;
import src.br.edu.ifpb.myhome.validacao.ValidadorPreco;
import src.br.edu.ifpb.myhome.validacao.ValidadorTitulo;

public class Main {

    public static void main(String[] args) {
        List<Usuario> usuarios = new ArrayList<>();
        List<Anuncio> anuncios = new ArrayList<>();
        Map<Anuncio, Usuario> donoDoAnuncio = new HashMap<>();
        List<Conversa> conversas = new ArrayList<>();
        List<Compra> compras = new ArrayList<>();
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

        int opcao;
        do {
            saida.escrever("\n=== MYHOME ===");
            saida.escrever("1 - Cadastrar usuário");
            saida.escrever("2 - Listar usuários");
            saida.escrever("3 - Usuário: criar anúncio");
            saida.escrever("4 - Usuário: buscar imóveis / visualizar / favoritar / comprar anúncio");
            saida.escrever("5 - Chat (conversar sobre anúncio)");
            saida.escrever("6 - Listar compras");
            saida.escrever("7 - Consultar log de mudanças de estado");
            saida.escrever("0 - Sair");
            saida.escreverSemQuebra("Opção: ");
            try {
                opcao = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            if (opcao == 1) {
                saida.escrever("\n--- Cadastro de usuário ---");
                saida.escreverSemQuebra("Nome: ");
                String nome = sc.nextLine().trim();
                saida.escreverSemQuebra("Email: ");
                String email = sc.nextLine().trim();
                Usuario u = new Usuario(nome, email);
                usuarios.add(u);
                saida.escrever("Usuário cadastrado (ID: " + u.getId() + "): " + u.getNome());
            } else if (opcao == 2) {
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
            } else if (opcao == 3) {
                if (usuarios.isEmpty()) {
                    saida.escrever("Cadastre um usuário primeiro.");
                } else {
                    saida.escrever("\n--- Usuários ---");
                    for (int i = 0; i < usuarios.size(); i++) {
                        Usuario u = usuarios.get(i);
                        saida.escrever((i + 1) + ") ID: " + u.getId() + " | " + u.getNome());
                    }
                    saida.escreverSemQuebra("Número do usuário (1-" + usuarios.size() + "): ");
                    int idx;
                    try {
                        idx = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        idx = 0;
                    }
                    if (idx >= 1 && idx <= usuarios.size()) {
                        Usuario usuario = usuarios.get(idx - 1);
                        saida.escrever("\n--- Novo anúncio (por " + usuario.getNome() + ") ---");
                        saida.escreverSemQuebra("Título do anúncio: ");
                        String titulo = sc.nextLine().trim();
                        saida.escreverSemQuebra("Preço: ");
                        double preco;
                        try {
                            preco = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
                        } catch (NumberFormatException e) {
                            preco = 0.0;
                        }
                        saida.escrever("Tipo de imóvel: 1=Casa 2=Apartamento 3=Terreno");
                        saida.escreverSemQuebra("Tipo: ");
                        int tipoImovel;
                        try {
                            tipoImovel = Integer.parseInt(sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            tipoImovel = 0;
                        }
                        ImovelFactory factory = null;
                        if (tipoImovel == 1) factory = new CasaFactory();
                        else if (tipoImovel == 2) factory = new ApartamentoFactory();
                        else if (tipoImovel == 3) factory = new TerrenoFactory();
                        if (factory == null) {
                            saida.escrever("Tipo inválido. Anúncio cancelado.");
                        } else {
                            Imovel imovel = factory.criarImovel();
                            imovel.setPreco(preco);
                            saida.escreverSemQuebra("Endereço do imóvel: ");
                            imovel.setEndereco(sc.nextLine().trim());
                            if (imovel instanceof Casa) {
                                saida.escreverSemQuebra("Possui quintal? (s/n): ");
                                ((Casa) imovel).setPossuiQuintal(sc.nextLine().trim().equalsIgnoreCase("s"));
                            } else if (imovel instanceof Apartamento) {
                                saida.escreverSemQuebra("Número de quartos: ");
                                try {
                                    ((Apartamento) imovel).setQuartos(Integer.parseInt(sc.nextLine().trim()));
                                } catch (NumberFormatException e) {}
                                saida.escreverSemQuebra("Área (m²): ");
                                try {
                                    ((Apartamento) imovel).setArea(Double.parseDouble(sc.nextLine().trim().replace(",", ".")));
                                } catch (NumberFormatException e) {}
                                saida.escreverSemQuebra("Andar: ");
                                try {
                                    ((Apartamento) imovel).setAndar(Integer.parseInt(sc.nextLine().trim()));
                                } catch (NumberFormatException e) {}
                                saida.escreverSemQuebra("Possui elevador? (s/n): ");
                                ((Apartamento) imovel).setPossuiElevador(sc.nextLine().trim().equalsIgnoreCase("s"));
                            } else if (imovel instanceof Terreno) {
                                saida.escreverSemQuebra("Área (m²): ");
                                try {
                                    ((Terreno) imovel).setArea(Double.parseDouble(sc.nextLine().trim().replace(",", ".")));
                                } catch (NumberFormatException e) {}
                            }
                            Anuncio anuncio = new Anuncio(titulo, preco, imovel);
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
                                saida.escrever("Anúncio criado: " + anuncio.getTitulo() + " | " + FormatadorMoeda.formatarReal(anuncio.getPreco()));
                            } else {
                                saida.escrever("Validação falhou. Anúncio não criado.");
                            }
                        }
                    } else {
                        saida.escrever("Usuário inválido.");
                    }
                }
            } else if (opcao == 4) {
                if (usuarios.isEmpty()) {
                    saida.escrever("Cadastre um usuário primeiro.");
                } else {
                    saida.escrever("\n--- Usuários ---");
                    for (int i = 0; i < usuarios.size(); i++) {
                        Usuario u = usuarios.get(i);
                        saida.escrever((i + 1) + ") ID: " + u.getId() + " | " + u.getNome());
                    }
                    saida.escreverSemQuebra("Número do usuário (1-" + usuarios.size() + "): ");
                    int idx;
                    try {
                        idx = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        idx = 0;
                    }
                    if (idx >= 1 && idx <= usuarios.size()) {
                        Usuario usuario = usuarios.get(idx - 1);
                        saida.escrever("\n--- Ações: " + usuario.getNome() + " ---");
                        saida.escrever("1 - Buscar imóveis");
                        saida.escrever("2 - Visualizar anúncio");
                        saida.escrever("3 - Favoritar anúncio");
                        saida.escrever("4 - Comprar imóvel");
                        saida.escreverSemQuebra("Opção: ");
                        int acao;
                        try {
                            acao = Integer.parseInt(sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            acao = 0;
                        }
                        if (acao == 1) {
                            saida.escrever("\n--- Buscar imóveis (filtro por tipo) ---");
                            saida.escrever("Filtrar por tipo: 1=Casa 2=Apartamento 3=Terreno 0=Todos");
                            saida.escreverSemQuebra("Opção: ");
                            int tipoFiltro;
                            try {
                                tipoFiltro = Integer.parseInt(sc.nextLine().trim());
                            } catch (NumberFormatException e) {
                                tipoFiltro = 0;
                            }
                            List<FiltroBusca> filtros = new ArrayList<>();
                            if (tipoFiltro == 1) filtros.add(new FiltroTipoImovel("casa"));
                            else if (tipoFiltro == 2) filtros.add(new FiltroTipoImovel("apartamento"));
                            else if (tipoFiltro == 3) filtros.add(new FiltroTipoImovel("terreno"));
                            List<Anuncio> resultado = BuscaService.buscar(anuncios, filtros);
                            saida.escrever("\n--- Resultado da busca ---");
                            if (resultado.isEmpty()) {
                                saida.escrever("Nenhum anúncio encontrado com esse filtro.");
                            } else {
                                for (int i = 0; i < resultado.size(); i++) {
                                    Anuncio a = resultado.get(i);
                                    saida.escrever((i + 1) + ") " + a.getTitulo() + " | " + FormatadorMoeda.formatarReal(a.getPreco()) + " | " + a.getTipoImovel());
                                }
                                saida.escrever("");
                                saida.escrever("Comprar algum? Digite o número do anúncio (1-" + resultado.size() + ") ou 0 para voltar.");
                                saida.escreverSemQuebra("Opção: ");
                                int numCompra;
                                try {
                                    numCompra = Integer.parseInt(sc.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    numCompra = 0;
                                }
                                if (numCompra >= 1 && numCompra <= resultado.size()) {
                                    Anuncio anuncioCompra = resultado.get(numCompra - 1);
                                    if (!"Arquivado".equals(anuncioCompra.getEstadoAtual())) {
                                        Compra compra = new Compra(usuario, anuncioCompra);
                                        compras.add(compra);
                                        anuncioCompra.setEstado(new ArquivadoState());
                                        saida.escrever("Compra realizada. Anúncio \"" + anuncioCompra.getTitulo() + "\" arquivado.");
                                    } else {
                                        saida.escrever("Este anúncio já foi vendido.");
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
                                    saida.escrever("Visualizando: " + a.getTitulo() + " | " + FormatadorMoeda.formatarReal(a.getPreco()) + " | " + a.getTipoImovel());
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
                            List<Anuncio> disponiveis = new ArrayList<>();
                            for (Anuncio a : anuncios) {
                                if (!"Arquivado".equals(a.getEstadoAtual())) disponiveis.add(a);
                            }
                            if (disponiveis.isEmpty()) {
                                saida.escrever("Nenhum anúncio disponível para compra.");
                            } else {
                                saida.escrever("\n--- Comprar imóvel ---");
                                saida.escrever("Comprador: " + usuario.getNome());
                                for (int i = 0; i < disponiveis.size(); i++) {
                                    Anuncio a = disponiveis.get(i);
                                    saida.escrever((i + 1) + ") " + a.getTitulo() + " | " + FormatadorMoeda.formatarReal(a.getPreco()));
                                }
                                saida.escreverSemQuebra("Número do anúncio para comprar (1-" + disponiveis.size() + "): ");
                                int ia;
                                try { ia = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { ia = 0; }
                                if (ia >= 1 && ia <= disponiveis.size()) {
                                    Anuncio anuncio = disponiveis.get(ia - 1);
                                    Compra compra = new Compra(usuario, anuncio);
                                    compras.add(compra);
                                    anuncio.setEstado(new ArquivadoState());
                                    saida.escrever("Compra realizada. Anúncio \"" + anuncio.getTitulo() + "\" arquivado.");
                                } else {
                                    saida.escrever("Anúncio inválido.");
                                }
                            }
                        } else {
                            saida.escrever("Opção inválida.");
                        }
                    } else {
                        saida.escrever("Usuário inválido.");
                    }
                }
            } else if (opcao == 5) {
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
            } else if (opcao == 6) {
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
            } else if (opcao == 7) {
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
