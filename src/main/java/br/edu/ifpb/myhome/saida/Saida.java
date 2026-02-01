package br.edu.ifpb.myhome.saida;

/**
 * E2 - Abstração para exibição de mensagens (evita System.out em métodos de domínio).
 * Facilita reuso e troca do canal de saída (console, log, UI).
 */
public interface Saida {

    void escrever(String mensagem);

    /** Mensagem sem quebra de linha (ex.: prompts). */
    default void escreverSemQuebra(String mensagem) {
        escrever(mensagem);
    }
}
