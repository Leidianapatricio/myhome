package src.br.edu.ifpb.myhome.usuario;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public class Anunciante extends Usuario {

    public Anunciante(Long id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public boolean autenticar() {
        return true;
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
}
