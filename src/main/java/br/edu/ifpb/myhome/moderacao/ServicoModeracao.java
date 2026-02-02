package br.edu.ifpb.myhome.moderacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.anuncio.TipoOferta;
import br.edu.ifpb.myhome.config.Configuracao;

import java.util.ArrayList;
import java.util.List;

/**
 * RF03 - Publicação e Moderação. Regras dinâmicas (carregadas de config):
 * - Título e descrição não podem conter palavras de baixo calão, termos pejorativos ou inadequados.
 * - Preço deve ser condizente (mínimo configurável; evita zero, um real, valores sem sentido).
 * - Anúncio deve ter ao menos uma foto OU quantidade mínima de texto na descrição.
 * Moderação pode ser manual (aprovar/rejeitar após submissão) ou automatizada (validação na submissão).
 */
public class ServicoModeracao {

    private final Configuracao config = Configuracao.getInstancia();

    /**
     * Valida o anúncio conforme as regras de moderação (automática).
     * Retorna aprovado = true se todas as regras passaram; caso contrário, erros contém as mensagens.
     */
    public ResultadoModeracao validarRegras(Anuncio anuncio) {
        List<String> erros = new ArrayList<>();

        if (anuncio == null) {
            erros.add("Anúncio inválido.");
            return new ResultadoModeracao(false, erros);
        }

        // Regra: título e descrição não podem conter palavras de baixo calão / termos proibidos
        List<String> termosProibidos = config.getTermosImproprios();
        if (!termosProibidos.isEmpty()) {
            String titulo = anuncio.getTitulo() != null ? anuncio.getTitulo().toLowerCase() : "";
            String descricao = "";
            if (anuncio.getImovel() != null && anuncio.getImovel().getDescricao() != null) {
                descricao = anuncio.getImovel().getDescricao().toLowerCase();
            }
            for (String termo : termosProibidos) {
                if (termo.isEmpty()) continue;
                if (titulo.contains(termo)) {
                    erros.add("O título não pode conter palavras de baixo calão ou termos inadequados: \"" + termo + "\".");
                }
                if (descricao.contains(termo)) {
                    erros.add("A descrição não pode conter palavras de baixo calão ou termos inadequados: \"" + termo + "\".");
                }
            }
        }

        // Regra: preço condizente conforme tipo de oferta (mínimo; evita zero, um real, valores sem sentido)
        double precoMin = config.getPrecoMinimoModeracao();
        double valor;
        switch (anuncio.getTipoOferta() != null ? anuncio.getTipoOferta() : TipoOferta.VENDA) {
            case ALUGUEL:
                valor = anuncio.getValorAluguel();
                break;
            case TEMPORADA:
                valor = anuncio.getValorTemporada();
                break;
            default:
                valor = anuncio.getValorVenda() > 0 ? anuncio.getValorVenda() : anuncio.getPreco();
                break;
        }
        if (valor <= 0) {
            erros.add("Preço deve ser maior que zero.");
        } else if (valor < precoMin) {
            erros.add("Preço (R$ " + String.format("%.2f", valor) + ") abaixo do mínimo permitido (R$ " + String.format("%.2f", precoMin) + ").");
        }

        // Regra: ao menos uma foto OU quantidade mínima de texto na descrição
        int descMin = config.getDescricaoTamanhoMinimo();
        int fotosMin = config.getFotosMinimoModeracao();
        int descLen = 0;
        if (anuncio.getImovel() != null && anuncio.getImovel().getDescricao() != null) {
            descLen = anuncio.getImovel().getDescricao().trim().length();
        }
        int fotos = anuncio.getQuantidadeFotos();
        if (descLen < descMin && fotos < fotosMin) {
            erros.add("Anúncio deve ter ao menos " + fotosMin + " foto(s) OU descrição com no mínimo " + descMin + " caracteres (atual: " + descLen + " caracteres, " + fotos + " foto(s)).");
        }

        return new ResultadoModeracao(erros.isEmpty(), erros);
    }
}
