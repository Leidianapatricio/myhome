package src.br.edu.ifpb.myhome.chat;

import src.br.edu.ifpb.myhome.usuario.Usuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensagem {

    private static long contadorId = 1;

    private Long id;
    private String texto;
    private Usuario remetente;
    private LocalDateTime dataHora;

    public Mensagem(String texto, Usuario remetente) {
        this.id = contadorId++;
        this.texto = texto;
        this.remetente = remetente;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDataHoraFormatada() {
        return dataHora.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }

    @Override
    public String toString() {
        return "[" + getDataHoraFormatada() + "] " + remetente.getNome() + ": " + texto;
    }
}
