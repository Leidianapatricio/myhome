package br.edu.ifpb.myhome.anuncio;

/**
 * Tipo de oferta do anúncio: venda, aluguel ou temporada.
 */
public enum TipoOferta {
    VENDA("Venda"),
    ALUGUEL("Aluguel"),
    TEMPORADA("Temporada");

    private final String label;

    TipoOferta(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
