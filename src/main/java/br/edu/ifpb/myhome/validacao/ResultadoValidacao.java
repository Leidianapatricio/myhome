package br.edu.ifpb.myhome.validacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resultado da validação do anúncio (Chain of Responsibility).
 * Permite saber se a validação passou e, em caso de falha, QUAL validação falhou e POR QUÊ.
 */
public class ResultadoValidacao {

    private final boolean valido;
    private final List<String> erros;

    public ResultadoValidacao(boolean valido, List<String> erros) {
        this.valido = valido;
        this.erros = erros != null ? new ArrayList<>(erros) : new ArrayList<>();
    }

    /** Resultado válido (sem erros). */
    public static ResultadoValidacao ok() {
        return new ResultadoValidacao(true, new ArrayList<>());
    }

    /** Resultado inválido com uma mensagem de erro. */
    public static ResultadoValidacao erro(String mensagem) {
        List<String> list = new ArrayList<>();
        if (mensagem != null && !mensagem.isBlank()) list.add(mensagem);
        return new ResultadoValidacao(false, list);
    }

    public boolean isValido() {
        return valido;
    }

    public List<String> getErros() {
        return Collections.unmodifiableList(erros);
    }
}
