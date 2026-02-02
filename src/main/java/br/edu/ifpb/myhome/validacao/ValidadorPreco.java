package br.edu.ifpb.myhome.validacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class ValidadorPreco extends ValidadorAnuncio {

    @Override
    public ResultadoValidacao validar(Anuncio anuncio) {
        if (anuncio.getPreco() <= 0) {
            return ResultadoValidacao.erro("Preço deve ser maior que zero.");
        }
        return passarParaProximo(anuncio);
    }
}


