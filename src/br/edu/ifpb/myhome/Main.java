package src.br.edu.ifpb.myhome;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;
import src.br.edu.ifpb.myhome.factory.ApartamentoFactory;
import src.br.edu.ifpb.myhome.factory.CasaFactory;
import src.br.edu.ifpb.myhome.factory.ImovelFactory;
import src.br.edu.ifpb.myhome.factory.TerrenoFactory;
import src.br.edu.ifpb.myhome.imovel.Apartamento;
import src.br.edu.ifpb.myhome.imovel.Casa;
import src.br.edu.ifpb.myhome.imovel.Imovel;
import src.br.edu.ifpb.myhome.imovel.Terreno;
import src.br.edu.ifpb.myhome.notificacao.EmailAdapter;
import src.br.edu.ifpb.myhome.notificacao.NotificacaoObserver;
import src.br.edu.ifpb.myhome.notificacao.Observer;
import src.br.edu.ifpb.myhome.notificacao.ServicoNotificacaoExterno;
import src.br.edu.ifpb.myhome.usuario.Anunciante;
import src.br.edu.ifpb.myhome.usuario.Cliente;
import src.br.edu.ifpb.myhome.validacao.ValidadorAnuncio;
import src.br.edu.ifpb.myhome.validacao.ValidadorImovel;
import src.br.edu.ifpb.myhome.validacao.ValidadorPreco;
import src.br.edu.ifpb.myhome.validacao.ValidadorTitulo;

public class Main {

    public static void main(String[] args) {
        List<Anunciante> anunciantes = new ArrayList<>();
        List<Cliente> clientes = new ArrayList<>();
        List<Anuncio> anuncios = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        ServicoNotificacaoExterno servicoNotif = new EmailAdapter();
        Observer notifObserver = new NotificacaoObserver(servicoNotif);

        int opcao;
        do {
            System.out.println("\n=== MYHOME ===");
            System.out.println("1 - Cadastrar Anunciante");
            System.out.println("2 - Cadastrar Cliente");
            System.out.println("3 - Listar Anunciantes");
            System.out.println("4 - Listar Clientes");
            System.out.println("5 - Anunciante: criar anúncio");
            System.out.println("6 - Cliente: buscar imóveis / visualizar / favoritar anúncio");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            try {
                opcao = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            if (opcao == 1) {
                System.out.println("\n--- Cadastro de Anunciante ---");
                System.out.print("ID: ");
                long id;
                try {
                    id = Long.parseLong(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    id = 0;
                }
                System.out.print("Nome: ");
                String nome = sc.nextLine().trim();
                System.out.print("Email: ");
                String email = sc.nextLine().trim();
                Anunciante a = new Anunciante(id, nome, email);
                anunciantes.add(a);
                System.out.println("Anunciante cadastrado: " + a.getNome());
            } else if (opcao == 2) {
                System.out.println("\n--- Cadastro de Cliente ---");
                System.out.print("ID: ");
                long id;
                try {
                    id = Long.parseLong(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    id = 0;
                }
                System.out.print("Nome: ");
                String nome = sc.nextLine().trim();
                System.out.print("Email: ");
                String email = sc.nextLine().trim();
                Cliente c = new Cliente(id, nome, email);
                clientes.add(c);
                System.out.println("Cliente cadastrado: " + c.getNome());
            } else if (opcao == 3) {
                System.out.println("\n--- Anunciantes ---");
                if (anunciantes.isEmpty()) {
                    System.out.println("Nenhum anunciante cadastrado.");
                } else {
                    for (int i = 0; i < anunciantes.size(); i++) {
                        Anunciante a = anunciantes.get(i);
                        System.out.println((i + 1) + ") ID: " + a.getId() + " | " + a.getNome() + " | " + a.getEmail());
                    }
                }
            } else if (opcao == 4) {
                System.out.println("\n--- Clientes ---");
                if (clientes.isEmpty()) {
                    System.out.println("Nenhum cliente cadastrado.");
                } else {
                    for (int i = 0; i < clientes.size(); i++) {
                        Cliente c = clientes.get(i);
                        System.out.println((i + 1) + ") ID: " + c.getId() + " | " + c.getNome() + " | " + c.getEmail());
                    }
                }
            } else if (opcao == 5) {
                if (anunciantes.isEmpty()) {
                    System.out.println("Cadastre um anunciante primeiro.");
                } else {
                    System.out.println("\n--- Anunciantes ---");
                    for (int i = 0; i < anunciantes.size(); i++) {
                        Anunciante a = anunciantes.get(i);
                        System.out.println((i + 1) + ") ID: " + a.getId() + " | " + a.getNome());
                    }
                    System.out.print("Número do anunciante (1-" + anunciantes.size() + "): ");
                    int idx;
                    try {
                        idx = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        idx = 0;
                    }
                    if (idx >= 1 && idx <= anunciantes.size()) {
                        Anunciante anunciante = anunciantes.get(idx - 1);
                        System.out.println("\n--- Novo anúncio (por " + anunciante.getNome() + ") ---");
                        System.out.print("Título do anúncio: ");
                        String titulo = sc.nextLine().trim();
                        System.out.print("Preço: ");
                        double preco;
                        try {
                            preco = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
                        } catch (NumberFormatException e) {
                            preco = 0.0;
                        }
                        System.out.println("Tipo de imóvel: 1=Casa 2=Apartamento 3=Terreno");
                        System.out.print("Tipo: ");
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
                            System.out.println("Tipo inválido. Anúncio cancelado.");
                        } else {
                            Imovel imovel = factory.criarImovel();
                            imovel.setPreco(preco);
                            System.out.print("Endereço do imóvel: ");
                            imovel.setEndereco(sc.nextLine().trim());
                            if (imovel instanceof Casa) {
                                System.out.print("Possui quintal? (s/n): ");
                                ((Casa) imovel).setPossuiQuintal(sc.nextLine().trim().equalsIgnoreCase("s"));
                            } else if (imovel instanceof Apartamento) {
                                System.out.print("Número de quartos: ");
                                try {
                                    ((Apartamento) imovel).setQuartos(Integer.parseInt(sc.nextLine().trim()));
                                } catch (NumberFormatException e) {}
                                System.out.print("Área (m²): ");
                                try {
                                    ((Apartamento) imovel).setArea(Double.parseDouble(sc.nextLine().trim().replace(",", ".")));
                                } catch (NumberFormatException e) {}
                                System.out.print("Andar: ");
                                try {
                                    ((Apartamento) imovel).setAndar(Integer.parseInt(sc.nextLine().trim()));
                                } catch (NumberFormatException e) {}
                                System.out.print("Possui elevador? (s/n): ");
                                ((Apartamento) imovel).setPossuiElevador(sc.nextLine().trim().equalsIgnoreCase("s"));
                            } else if (imovel instanceof Terreno) {
                                System.out.print("Área (m²): ");
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
                                anunciante.editarAnuncio(anuncio);
                                anuncios.add(anuncio);
                                System.out.println("Anúncio criado: " + anuncio.getTitulo() + " | R$ " + anuncio.getPreco());
                            } else {
                                System.out.println("Validação falhou. Anúncio não criado.");
                            }
                        }
                    } else {
                        System.out.println("Anunciante inválido.");
                    }
                }
            } else if (opcao == 6) {
                if (clientes.isEmpty()) {
                    System.out.println("Cadastre um cliente primeiro.");
                } else {
                    System.out.println("\n--- Clientes ---");
                    for (int i = 0; i < clientes.size(); i++) {
                        Cliente c = clientes.get(i);
                        System.out.println((i + 1) + ") ID: " + c.getId() + " | " + c.getNome());
                    }
                    System.out.print("Número do cliente (1-" + clientes.size() + "): ");
                    int idx;
                    try {
                        idx = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        idx = 0;
                    }
                    if (idx >= 1 && idx <= clientes.size()) {
                        Cliente cliente = clientes.get(idx - 1);
                        System.out.println("\n--- Ações do Cliente: " + cliente.getNome() + " ---");
                        System.out.println("1 - Buscar imóveis");
                        System.out.println("2 - Visualizar anúncio");
                        System.out.println("3 - Favoritar anúncio");
                        System.out.print("Opção: ");
                        int acao;
                        try {
                            acao = Integer.parseInt(sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            acao = 0;
                        }
                        if (acao == 1) {
                            cliente.buscarImoveis();
                            System.out.println("\n--- Anúncios ---");
                            if (anuncios.isEmpty()) {
                                System.out.println("Nenhum anúncio cadastrado.");
                            } else {
                                for (int i = 0; i < anuncios.size(); i++) {
                                    Anuncio a = anuncios.get(i);
                                    System.out.println((i + 1) + ") " + a.getTitulo() + " | R$ " + a.getPreco() + " | " + a.getTipoImovel());
                                }
                            }
                        } else if (acao == 2) {
                            if (anuncios.isEmpty()) {
                                System.out.println("Não há anúncios.");
                            } else {
                                System.out.println("\n--- Anúncios ---");
                                for (int i = 0; i < anuncios.size(); i++) {
                                    Anuncio a = anuncios.get(i);
                                    System.out.println((i + 1) + ") " + a.getTitulo() + " | R$ " + a.getPreco());
                                }
                                System.out.print("Número do anúncio para visualizar: ");
                                int n;
                                try {
                                    n = Integer.parseInt(sc.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    n = 0;
                                }
                                if (n >= 1 && n <= anuncios.size()) {
                                    Anuncio a = anuncios.get(n - 1);
                                    cliente.visualizarAnuncio(a);
                                    System.out.println("Visualizando: " + a.getTitulo() + " | R$ " + a.getPreco() + " | " + a.getTipoImovel());
                                } else {
                                    System.out.println("Anúncio inválido.");
                                }
                            }
                        } else if (acao == 3) {
                            if (anuncios.isEmpty()) {
                                System.out.println("Não há anúncios.");
                            } else {
                                System.out.println("\n--- Anúncios ---");
                                for (int i = 0; i < anuncios.size(); i++) {
                                    Anuncio a = anuncios.get(i);
                                    System.out.println((i + 1) + ") " + a.getTitulo() + " | R$ " + a.getPreco());
                                }
                                System.out.print("Número do anúncio para favoritar: ");
                                int n;
                                try {
                                    n = Integer.parseInt(sc.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    n = 0;
                                }
                                if (n >= 1 && n <= anuncios.size()) {
                                    cliente.favoritarAnuncio(anuncios.get(n - 1));
                                    System.out.println("Anúncio favoritado.");
                                } else {
                                    System.out.println("Anúncio inválido.");
                                }
                            }
                        } else {
                            System.out.println("Opção inválida.");
                        }
                    } else {
                        System.out.println("Cliente inválido.");
                    }
                }
            } else if (opcao == 0) {
                System.out.println("Até logo.");
            } else {
                System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        sc.close();
    }
}
