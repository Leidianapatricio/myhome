package br.edu.ifpb.myhome.validacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Validação de cadastro de usuário: nome e email são obrigatórios (não vazios).
 */
public class ValidadorUsuario {

    /**
     * Valida nome e email para cadastro.
     * @return Lista de mensagens de erro; vazia se válido.
     */
    public List<String> validar(String nome, String email) {
        List<String> erros = new ArrayList<>();
        if (nome == null || nome.trim().isEmpty()) {
            erros.add("Nome é obrigatório.");
        }
        if (email == null || email.trim().isEmpty()) {
            erros.add("Email é obrigatório.");
        }
        return erros;
    }
}
