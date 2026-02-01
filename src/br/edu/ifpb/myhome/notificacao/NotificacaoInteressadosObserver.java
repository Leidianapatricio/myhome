package src.br.edu.ifpb.myhome.notificacao;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;
import src.br.edu.ifpb.myhome.saida.Saida;
import src.br.edu.ifpb.myhome.usuario.Usuario;

/**
 * Observer que notifica os usuários interessados sempre que o estado
 * do anúncio mudar. E2: usa Saida em vez de System.out.
 */
public class NotificacaoInteressadosObserver implements Observer {

    private final Saida saida;

    public NotificacaoInteressadosObserver(Saida saida) {
        this.saida = saida;
    }

    @Override
    public void atualizar(Anuncio a) {
        if (a == null || a.getInteressados().isEmpty() || saida == null) return;
        String estadoAtual = a.getEstadoAtual();
        String titulo = a.getTitulo() != null ? a.getTitulo() : "(sem título)";
        for (Usuario u : a.getInteressados()) {
            saida.escrever("[Notificação] Usuário " + u.getNome() + " (" + u.getEmail() + "): "
                    + "O anúncio \"" + titulo + "\" está agora em estado: " + estadoAtual + ".");
        }
    }
}
