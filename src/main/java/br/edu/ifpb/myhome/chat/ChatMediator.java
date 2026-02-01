package br.edu.ifpb.myhome.chat;

import br.edu.ifpb.myhome.usuario.Usuario;

import java.util.List;

/**
 * Mediator do padrão Mediator: intermediário da comunicação entre
 * os participantes do chat (Usuários). Os participantes não se comunicam
 * diretamente; enviam mensagens através do mediator.
 */
public interface ChatMediator {

    /**
     * Envia uma mensagem na conversa (chamado pelos colegas - Usuários).
     */
    void enviarMensagem(Usuario remetente, String texto);

    /**
     * Retorna o histórico de mensagens da conversa.
     */
    List<Mensagem> getMensagens();
}
