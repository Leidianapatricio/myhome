package src.br.edu.ifpb.myhome.compra;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;
import src.br.edu.ifpb.myhome.usuario.Usuario;

import java.time.LocalDateTime;

/**
 * Registro de compra de imóvel: comprador, anúncio e data.
 * Após a compra, o anúncio passa para estado Arquivado.
 */
public class Compra {

    private final Usuario comprador;
    private final Anuncio anuncio;
    private final LocalDateTime data;

    public Compra(Usuario comprador, Anuncio anuncio) {
        this.comprador = comprador;
        this.anuncio = anuncio;
        this.data = LocalDateTime.now();
    }

    public Usuario getComprador() {
        return comprador;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public LocalDateTime getData() {
        return data;
    }
}
