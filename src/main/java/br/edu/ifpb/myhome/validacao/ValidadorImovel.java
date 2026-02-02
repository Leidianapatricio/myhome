package br.edu.ifpb.myhome.validacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class ValidadorImovel extends ValidadorAnuncio {

    @Override
    public ResultadoValidacao validar(Anuncio a) {
        if (a.getImovel() == null) {
            return ResultadoValidacao.erro("Imóvel é obrigatório.");
        }
        return passarParaProximo(a);
    }
}
