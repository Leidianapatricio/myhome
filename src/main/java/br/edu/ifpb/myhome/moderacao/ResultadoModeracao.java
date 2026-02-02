package br.edu.ifpb.myhome.moderacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RF03 - Resultado da validação automática de moderação (regras dinâmicas).
 * Aprovado = true se todas as regras passaram; erros lista as mensagens de falha.
 */
public class ResultadoModeracao {

    private final boolean aprovado;
    private final List<String> erros;

    public ResultadoModeracao(boolean aprovado, List<String> erros) {
        this.aprovado = aprovado;
        this.erros = erros != null ? new ArrayList<>(erros) : new ArrayList<>();
    }

    public boolean isAprovado() { return aprovado; }
    public List<String> getErros() { return Collections.unmodifiableList(erros); }
}
