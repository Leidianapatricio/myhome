package br.edu.ifpb.myhome.anuncio;

import br.edu.ifpb.myhome.estado.EstadoAnuncio;
import br.edu.ifpb.myhome.estado.RascunhoState;
import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.notificacao.Observer;
import br.edu.ifpb.myhome.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Anuncio {

    private String titulo;
    private double preco;
    private Imovel imovel;
    private EstadoAnuncio estado;
    private List<Observer> observers = new ArrayList<>();
    private final InteressadosAnuncio interessados = new InteressadosAnuncio();

    public Anuncio() {
        this.estado = new RascunhoState();
    }

    public Anuncio(String titulo, double preco, Imovel imovel) {
        this.titulo = titulo;
        this.preco = preco;
        this.imovel = imovel;
        this.estado = new RascunhoState();
    }

    public boolean submeter() {
        if (estado != null) {
            estado.proximo(this);
            return true;
        }
        return false;
    }

    public void mudarEstado(EstadoAnuncio e) {
        this.estado = e;
    }

    public void adicionarObserver(Observer o) {
        observers.add(o);
    }

    public void removerObserver(Observer o) {
        observers.remove(o);
    }

    public void notificar() {
        for (Observer o : observers) {
            o.atualizar(this);
        }
    }

    public void setEstado(EstadoAnuncio estado) {
        EstadoAnuncio estadoAnterior = this.estado;
        this.estado = estado;
        LogMudancaEstado.getInstancia().registrar(this, estadoAnterior, estado);
        notificar();
    }

    /** Registra usuário interessado (ex.: quem abriu conversa sobre o anúncio). */
    public void adicionarInteressado(Usuario u) {
        interessados.adicionar(u);
    }

    public List<Usuario> getInteressados() {
        return interessados.getInteressados();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public EstadoAnuncio getEstado() {
        return estado;
    }

    public String getEstadoAtual() {
        return estado != null ? estado.getNome() : "";
    }

    public String getTipoImovel() {
        return imovel != null ? imovel.getClass().getSimpleName() : "";
    }
}
