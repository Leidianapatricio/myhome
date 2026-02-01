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
 * anuncios.csv: titulo;preco;tipo;endereco;areaOuExtra;idDono (idDono = índice 1-based do usuário)
 *   tipo = Casa, Apartamento ou Terreno. areaOuExtra: para Casa (0 ou 1 quintal), Apartamento (quartos), Terreno (área)
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

    /** Formato: titulo;preco;tipo;endereco;idDono;... (idDono = 1-based). Extras: casa=quintal(0/1), apartamento=quartos;area;andar;elevador(0/1), terreno=area */
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
            Usuario dono = usuarios.get(idDono - 1);
            Imovel imovel = criarImovel(tipo, endereco, preco, parts);
            Anuncio anuncio = new Anuncio(titulo, preco, imovel);
            anuncios.add(anuncio);
            donoDoAnuncio.put(anuncio, dono);
        }
    }

    private static Imovel criarImovel(String tipo, String endereco, double preco, String[] parts) {
        switch (tipo) {
            case "casa":
                boolean quintal = parts.length > 5 && ("1".equals(parts[5].trim()) || "s".equalsIgnoreCase(parts[5].trim()));
                return new Casa(endereco, preco, quintal);
            case "apartamento":
                int quartos = parts.length > 5 ? parseInt(parts[5].trim(), 0) : 0;
                double area = parts.length > 6 ? parseDouble(parts[6].trim(), 0) : 0;
                int andar = parts.length > 7 ? parseInt(parts[7].trim(), 0) : 0;
                boolean elevador = parts.length > 8 && ("1".equals(parts[8].trim()) || "s".equalsIgnoreCase(parts[8].trim()));
                return new Apartamento(endereco, preco, quartos, area, andar, elevador);
            case "terreno":
                double areaT = parts.length > 5 ? parseDouble(parts[5].trim(), 0) : 0;
                return new Terreno(endereco, preco, areaT);
            default:
                return new Casa(endereco, preco, false);
        }
    }

    private static int parseInt(String s, int padrao) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return padrao; }
    }

    private static double parseDouble(String s, double padrao) {
        try { return Double.parseDouble(s.replace(",", ".")); } catch (NumberFormatException e) { return padrao; }
    }
}
