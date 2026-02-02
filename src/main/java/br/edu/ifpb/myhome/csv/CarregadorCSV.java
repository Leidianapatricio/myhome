package br.edu.ifpb.myhome.csv;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.imovel.Apartamento;
import br.edu.ifpb.myhome.imovel.Casa;
import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.imovel.Terreno;
import br.edu.ifpb.myhome.usuario.Usuario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * E1 - Carrega dados iniciais a partir de arquivos CSV para testes.
 * usuarios.csv: nome;email (uma linha por usuário)
 * anuncios.csv: titulo;preco;tipo;endereco;idDono;valorVenda;valorAluguel;valorTemporada;area;descricao;suites;bairro;extras...
 *   area = m². extras: casa=quintal(0/1), apartamento=quartos;andar;elevador(0/1)
 */
public class CarregadorCSV {

    private static final String SEP = ";";

    public static List<Usuario> carregarUsuarios(Path arquivo) throws IOException {
        List<Usuario> lista = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);
        for (String linha : linhas) {
            linha = linha.trim();
            if (linha.isEmpty() || linha.startsWith("#")) continue;
            String[] parts = linha.split(SEP, -1);
            if (parts.length >= 2) {
                String nome = parts[0].trim();
                String email = parts[1].trim();
                if (!nome.isEmpty()) lista.add(new Usuario(nome, email));
            }
        }
        return lista;
    }

    /** Formato: titulo;preco;tipo;endereco;idDono;valorVenda;valorAluguel;valorTemporada;area;descricao;suites;bairro;extras... (idDono = 1-based) */
    public static void carregarAnuncios(Path arquivo, List<Usuario> usuarios,
                                        List<Anuncio> anuncios, Map<Anuncio, Usuario> donoDoAnuncio) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);
        for (String linha : linhas) {
            linha = linha.trim();
            if (linha.isEmpty() || linha.startsWith("#")) continue;
            String[] parts = linha.split(SEP, -1);
            if (parts.length < 5) continue;
            String titulo = parts[0].trim();
            double preco = parseDouble(parts[1].trim(), 0);
            String tipo = parts[2].trim().toLowerCase();
            String endereco = parts[3].trim();
            int idDono = parseInt(parts[4].trim(), 1);
            if (titulo.isEmpty() || idDono < 1 || idDono > usuarios.size()) continue;
            double valorVenda = parts.length > 5 ? parseDouble(parts[5].trim(), preco) : preco;
            double valorAluguel = parts.length > 6 ? parseDouble(parts[6].trim(), 0) : 0;
            double valorTemporada = parts.length > 7 ? parseDouble(parts[7].trim(), 0) : 0;
            double area = parts.length > 8 ? parseDouble(parts[8].trim(), 0) : 0;
            String descricao = parts.length > 9 ? parts[9].trim() : "";
            int suites = parts.length > 10 ? parseInt(parts[10].trim(), 0) : 0;
            String bairro = parts.length > 11 ? parts[11].trim() : "";
            Usuario dono = usuarios.get(idDono - 1);
            Imovel imovel = criarImovel(tipo, endereco, preco, area, descricao, suites, bairro, parts);
            Anuncio anuncio = new Anuncio(titulo, preco, imovel, valorAluguel, valorTemporada);
            anuncio.setDono(dono);
            anuncios.add(anuncio);
            donoDoAnuncio.put(anuncio, dono);
        }
    }

    /** extras: bairro=parts[11]; casa=parts[12]=quintal, apartamento=parts[12]=quartos, parts[13]=andar, parts[14]=elevador */
    private static Imovel criarImovel(String tipo, String endereco, double preco,
                                      double area, String descricao, int suites, String bairro, String[] parts) {
        int base = 12; // índice dos extras após ...;suites;bairro
        switch (tipo) {
            case "casa":
                boolean quintal = parts.length > base && ("1".equals(parts[base].trim()) || "s".equalsIgnoreCase(parts[base].trim()));
                Casa casa = new Casa(endereco, preco, quintal);
                casa.setAreaMetrosQuadrados(area > 0 ? area : 0);
                casa.setDescricao(descricao);
                casa.setQuantidadeSuites(suites);
                casa.setBairro(bairro != null ? bairro : "");
                return casa;
            case "apartamento":
                int quartos = parts.length > base ? parseInt(parts[base].trim(), 0) : 0;
                double areaApt = area > 0 ? area : (parts.length > 9 ? parseDouble(parts[9].trim(), 0) : 0);
                int andar = parts.length > base + 1 ? parseInt(parts[base + 1].trim(), 0) : 0;
                boolean elevador = parts.length > base + 2 && ("1".equals(parts[base + 2].trim()) || "s".equalsIgnoreCase(parts[base + 2].trim()));
                Apartamento apt = new Apartamento(endereco, preco, quartos, areaApt, andar, elevador);
                apt.setDescricao(descricao);
                apt.setQuantidadeSuites(suites);
                apt.setBairro(bairro != null ? bairro : "");
                return apt;
            case "terreno":
                double areaT = area > 0 ? area : 0;
                Terreno terreno = new Terreno(endereco, preco, areaT);
                terreno.setDescricao(descricao);
                terreno.setQuantidadeSuites(suites);
                terreno.setBairro(bairro != null ? bairro : "");
                return terreno;
            default:
                Casa c = new Casa(endereco, preco, false);
                c.setAreaMetrosQuadrados(area);
                c.setDescricao(descricao);
                c.setQuantidadeSuites(suites);
                c.setBairro(bairro != null ? bairro : "");
                return c;
        }
    }

    private static int parseInt(String s, int padrao) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return padrao; }
    }

    private static double parseDouble(String s, double padrao) {
        try { return Double.parseDouble(s.replace(",", ".")); } catch (NumberFormatException e) { return padrao; }
    }
}
