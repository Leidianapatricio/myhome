package br.edu.ifpb.myhome.validacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class ValidadorTitulo extends ValidadorAnuncio {

    @Override
    public ResultadoValidacao validar(Anuncio anuncio) {
        if (anuncio.getTitulo() == null || anuncio.getTitulo().isBlank()) {
            return ResultadoValidacao.erro("Título é obrigatório.");
        }
        return passarParaProximo(anuncio);
    }
}
