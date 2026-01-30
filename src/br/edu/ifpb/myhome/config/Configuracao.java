package src.br.edu.ifpb.myhome.config;

import java.util.Properties;

public class Configuracao {

    private static Configuracao instancia;
    private Properties parametros = new Properties();

    private Configuracao() {}

    public static Configuracao getInstancia() {
        if (instancia == null) {
            instancia = new Configuracao();
        }
        return instancia;
    }

    public void carregarParametros() {
        // carrega parâmetros (ex.: de arquivo, env, etc.)
    }

    public String getParametro(String chave) {
        return parametros.getProperty(chave);
    }

    public void setParametro(String chave, String valor) {
        parametros.setProperty(chave, valor);
    }
}
