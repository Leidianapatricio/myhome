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
    /** Valor principal (venda). Mantido para compatibilidade; ver getValorVenda/getValorAluguel/getValorTemporada. */
    private double preco;
    private Imovel imovel;
    private EstadoAnuncio estado;
    private TipoOferta tipoOferta;
    private double valorVenda;
    private double valorAluguel;
    private double valorTemporada;
    /** RF03 - Quantidade de fotos do anúncio (regra: mínimo 1 foto OU descrição com tamanho mínimo). */
    private int quantidadeFotos;
    private List<Observer> observers = new ArrayList<>();
    private final InteressadosAnuncio interessados = new InteressadosAnuncio();

    public Anuncio() {
        this.estado = new RascunhoState();
        this.tipoOferta = TipoOferta.VENDA;
    }

    public Anuncio(String titulo, double preco, Imovel imovel) {
        this.titulo = titulo;
        this.preco = preco;
        this.valorVenda = preco;
        this.imovel = imovel;
        this.estado = new RascunhoState();
        this.tipoOferta = TipoOferta.VENDA;
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

    /** Retorna o preço principal (valor de venda, para compatibilidade). */
    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
        this.valorVenda = preco;
    }

    public TipoOferta getTipoOferta() { return tipoOferta; }
    public void setTipoOferta(TipoOferta tipoOferta) { this.tipoOferta = tipoOferta != null ? tipoOferta : TipoOferta.VENDA; }
    public double getValorVenda() { return valorVenda; }
    public void setValorVenda(double valorVenda) { this.valorVenda = valorVenda; this.preco = valorVenda; }
    public double getValorAluguel() { return valorAluguel; }
    public void setValorAluguel(double valorAluguel) { this.valorAluguel = valorAluguel; }
    public double getValorTemporada() { return valorTemporada; }
    public void setValorTemporada(double valorTemporada) { this.valorTemporada = valorTemporada; }

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

    public int getQuantidadeFotos() { return quantidadeFotos; }
    public void setQuantidadeFotos(int quantidadeFotos) { this.quantidadeFotos = Math.max(0, quantidadeFotos); }
}
