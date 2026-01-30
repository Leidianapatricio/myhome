package src.br.edu.ifpb.myhome.usuario;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public class Cliente extends Usuario {

    public Cliente(Long id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public boolean autenticar() {
        return true;
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
}
