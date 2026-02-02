package br.edu.ifpb.myhome.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * RF07 - Configuração centralizada: taxas, limites, termos impróprios, URLs.
 * Carrega de arquivo .properties (ou equivalente).
 */
public class Configuracao {

    private static Configuracao instancia;
    private Properties parametros = new Properties();
    private static final String ARQUIVO_PADRAO = "config.properties";

    private Configuracao() {}

    public static Configuracao getInstancia() {
        if (instancia == null) {
            instancia = new Configuracao();
        }
        return instancia;
    }

    /** Carrega parâmetros do arquivo config.properties (raiz do projeto ou classpath). */
    public void carregarParametros() {
        try {
            Path path = Paths.get(ARQUIVO_PADRAO);
            if (Files.exists(path)) {
                try (InputStream in = Files.newInputStream(path)) {
                    parametros.load(in);
                }
            } else {
                InputStream in = getClass().getResourceAsStream("/" + ARQUIVO_PADRAO);
                if (in != null) {
                    parametros.load(in);
                    in.close();
                }
            }
        } catch (Exception e) {
            // mantém parametros vazios ou já carregados
        }
    }

    public String getParametro(String chave) {
        return parametros.getProperty(chave);
    }

    public void setParametro(String chave, String valor) {
        parametros.setProperty(chave, valor);
    }

    /** RF03 - Lista de termos proibidos (título/descrição). Regra dinâmica carregada de config. */
    public List<String> getTermosImproprios() {
        String v = parametros.getProperty("termos.improprios", "");
        if (v == null || v.trim().isEmpty()) return new ArrayList<>();
        return Arrays.stream(v.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                .map(String::toLowerCase).collect(Collectors.toList());
    }

    /** RF03 - Preço mínimo em reais (evita zero, um real, valores sem sentido). */
    public double getPrecoMinimoModeracao() {
        try {
            String v = parametros.getProperty("preco.minimo", "1000");
            return Double.parseDouble(v.trim().replace(",", "."));
        } catch (NumberFormatException e) { return 1000.0; }
    }

    /** RF03 - Quantidade mínima de caracteres na descrição (ou fotos mínimas). */
    public int getDescricaoTamanhoMinimo() {
        try {
            return Integer.parseInt(parametros.getProperty("moderacao.descricao.minimo", "10").trim());
        } catch (NumberFormatException e) { return 10; }
    }

    /** RF03 - Quantidade mínima de fotos (se descrição for menor que o mínimo). */
    public int getFotosMinimoModeracao() {
        try {
            return Integer.parseInt(parametros.getProperty("moderacao.fotos.minimo", "1").trim());
        } catch (NumberFormatException e) { return 1; }
    }
}
