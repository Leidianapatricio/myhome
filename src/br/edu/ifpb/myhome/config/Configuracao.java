package src.br.edu.ifpb.myhome.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

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
}
