package src.br.edu.ifpb.myhome.usuario;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;
import src.br.edu.ifpb.myhome.chat.ChatMediator;

/**
 * Único tipo de usuário do sistema. Colleague do padrão Mediator no chat:
 * os participantes comunicam-se apenas através do ChatMediator (Conversa).
 */
public class Usuario {

    private static long contadorId = 1;

    private Long id;
    private String nome;
    private String email;

    /** Construtor com ID gerado automaticamente. */
    public Usuario(String nome, String email) {
        this.id = contadorId++;
        this.nome = nome;
        this.email = email;
    }

    public Usuario(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public boolean autenticar() {
        return true;
    }

    /**
     * Envia mensagem através do mediator (padrão Mediator).
     */
    public void enviarMensagemVia(ChatMediator mediator, String texto) {
        mediator.enviarMensagem(this, texto);
    }

    public Anuncio criarAnuncio() {
        return new Anuncio();
    }

    public void editarAnuncio(Anuncio a) {
        // lógica de edição
    }

    public void removerAnuncio(Anuncio a) {
        // lógica de remoção
    }

    public void buscarImoveis() {
        // lógica de busca
    }

    public void visualizarAnuncio(Anuncio a) {
        // lógica de visualização
    }

    public void favoritarAnuncio(Anuncio a) {
        // lógica de favoritar
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
