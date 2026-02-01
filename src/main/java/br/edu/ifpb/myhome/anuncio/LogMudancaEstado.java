package br.edu.ifpb.myhome.anuncio;

import br.edu.ifpb.myhome.estado.EstadoAnuncio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * RF04 - Mecanismo de Log que retém a informação sobre a mudança de status do anúncio.
 */
public class LogMudancaEstado {

    private static final LogMudancaEstado instancia = new LogMudancaEstado();
    private final List<EntradaLogEstado> entradas = new ArrayList<>();

    public static LogMudancaEstado getInstancia() {
        return instancia;
    }

    private LogMudancaEstado() {}

    public void registrar(Anuncio anuncio, EstadoAnuncio estadoAnterior, EstadoAnuncio estadoNovo) {
        String titulo = anuncio != null ? anuncio.getTitulo() : "";
        String antigo = estadoAnterior != null ? estadoAnterior.getNome() : "—";
        String novo = estadoNovo != null ? estadoNovo.getNome() : "—";
        entradas.add(new EntradaLogEstado(titulo, antigo, novo));
    }

    public List<EntradaLogEstado> getEntradas() {
        return new ArrayList<>(entradas);
    }

    public static class EntradaLogEstado {
        private final String tituloAnuncio;
        private final String estadoAnterior;
        private final String estadoNovo;
        private final LocalDateTime dataHora;

        public EntradaLogEstado(String tituloAnuncio, String estadoAnterior, String estadoNovo) {
            this.tituloAnuncio = tituloAnuncio;
            this.estadoAnterior = estadoAnterior;
            this.estadoNovo = estadoNovo;
            this.dataHora = LocalDateTime.now();
        }

        public String getTituloAnuncio() { return tituloAnuncio; }
        public String getEstadoAnterior() { return estadoAnterior; }
        public String getEstadoNovo() { return estadoNovo; }
        public LocalDateTime getDataHora() { return dataHora; }

        @Override
        public String toString() {
            return dataHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " | \"" + tituloAnuncio + "\" | " + estadoAnterior + " → " + estadoNovo;
        }
    }
}
