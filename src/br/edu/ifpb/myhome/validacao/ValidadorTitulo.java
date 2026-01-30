package src.br.edu.ifpb.myhome.validacao;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public class ValidadorTitulo extends ValidadorAnuncio {

    @Override
    public boolean validar(Anuncio anuncio) {
        if (anuncio.getTitulo() == null || anuncio.getTitulo().isBlank()) {
            return false;
        }
        return passarParaProximo(anuncio);
    }
}
