package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.saida.Saida;
import br.edu.ifpb.myhome.usuario.Usuario;

/**
 * Observer que notifica os usuários interessados quando o ESTADO do anúncio mudar.
 * Trata falhas (ex.: erro ao escrever na saída) via NotificacaoFalhaHandler.
 * E2: usa Saida em vez de System.out.
 */
public class NotificacaoInteressadosObserver implements Observer {

    private final Saida saida;
    private final NotificacaoFalhaHandler falhaHandler;

    public NotificacaoInteressadosObserver(Saida saida) {
        this(saida, null);
    }

    public NotificacaoInteressadosObserver(Saida saida, NotificacaoFalhaHandler falhaHandler) {
        this.saida = saida;
        this.falhaHandler = falhaHandler;
    }

    @Override
    public void atualizar(Anuncio a, TipoEvento evento) {
        if (evento != TipoEvento.MUDANCA_ESTADO || a == null || a.getInteressados().isEmpty() || saida == null) return;
        try {
            String estadoAtual = a.getEstadoAtual();
            String titulo = a.getTitulo() != null ? a.getTitulo() : "(sem título)";
            for (Usuario u : a.getInteressados()) {
                saida.escrever("[Notificação] Usuário " + u.getNome() + " (" + u.getEmail() + "): "
                        + "O anúncio \"" + titulo + "\" mudou de estado para: " + estadoAtual + ".");
            }
        } catch (Exception e) {
            if (falhaHandler != null) {
                falhaHandler.onFalha(a, evento, e);
            }
        }
    }
}
