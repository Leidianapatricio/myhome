package br.edu.ifpb.myhome.chat;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Mediador concreto do padrão Mediator: centraliza a comunicação entre
 * dois Usuários (interessado no anúncio e dono do anúncio). Nenhum envia
 * mensagem diretamente ao outro; ambos enviam através desta Conversa.
 */
public class Conversa implements ChatMediator {

    private static long contadorId = 1;

    private Long id;
    private Anuncio anuncio;
    private Usuario interessado;
    private Usuario donoAnuncio;
    private List<Mensagem> mensagens = new ArrayList<>();

    public Conversa(Anuncio anuncio, Usuario interessado, Usuario donoAnuncio) {
        this.id = contadorId++;
        this.anuncio = anuncio;
        this.interessado = interessado;
        this.donoAnuncio = donoAnuncio;
    }

    public Long getId() {
        return id;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public Usuario getInteressado() {
        return interessado;
    }

    public Usuario getDonoAnuncio() {
        return donoAnuncio;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    @Override
    public void enviarMensagem(Usuario remetente, String texto) {
        if (texto == null || texto.isBlank()) return;
        mensagens.add(new Mensagem(texto.trim(), remetente));
    }

    public boolean ehMesma(Anuncio a, Usuario inter, Usuario dono) {
        return anuncio == a && interessado == inter && donoAnuncio == dono;
    }
}
