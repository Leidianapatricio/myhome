package br.edu.ifpb.myhome.anuncio;

import br.edu.ifpb.myhome.estado.EstadoAnuncio;
import br.edu.ifpb.myhome.estado.RascunhoState;
import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.notificacao.Observer;
import br.edu.ifpb.myhome.notificacao.TipoEvento;
import br.edu.ifpb.myhome.usuario.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Anuncio {

    private String titulo;
    /** Valor principal (venda). Mantido para compatibilidade; ver getValorVenda/getValorAluguel/getValorTemporada. */
    private double preco;
    private Imovel imovel;
    private EstadoAnuncio estado;
    private double valorAluguel;
    private double valorTemporada;
    /** RF03 - Quantidade de fotos do anúncio (regra: mínimo 1 foto OU descrição com tamanho mínimo). */
    private int quantidadeFotos;
    /** Dono/anunciante do anúncio (para notificação: e-mail, etc.). */
    private Usuario dono;
    private List<Observer> observers = new ArrayList<>();
    private final List<Usuario> interessados = new ArrayList<>();

    public Anuncio() {
        this.estado = new RascunhoState();
        
    }

    public Anuncio(String titulo,
            double preco,
            Imovel imovel,
            double valorAluguel,
            double valorTemporada) {

        this.titulo = titulo;
        this.preco = preco;
        this.imovel = imovel;
        this.valorAluguel = valorAluguel;
        this.valorTemporada = valorTemporada;

        this.estado = new RascunhoState();
}


    /** Chama o state: delega ao estado atual a transição (Rascunho → Moderação). */
    public boolean submeter() {
        if (estado != null) {
            estado.proximo(this);
            return true;
        }
        return false;
    }

    /** Padrão State: delega ao estado a ativação direta (ex.: anúncios carregados do CSV). */
    public void ativar() {
        if (estado != null) estado.ativar(this);
    }

    /** Padrão State: altera o estado e registra no log; usado pelas implementações de EstadoAnuncio. */
    public void mudarEstado(EstadoAnuncio e) {
        EstadoAnuncio estadoAnterior = this.estado;
        this.estado = e;
        notificar(TipoEvento.MUDANCA_ESTADO);
    }

    public void adicionarObserver(Observer o) {
        observers.add(o);
    }

    public void removerObserver(Observer o) {
        observers.remove(o);
    }

    /** Notifica observadores com o tipo de evento ocorrido. Falha de um observer não interrompe os demais. */
    public void notificar(TipoEvento evento) {
        if (evento == null) return;
        for (Observer o : observers) {
            try {
                o.atualizar(this, evento);
            } catch (Exception e) {
                // Observer deve tratar falhas internamente; exceção inesperada não interrompe os demais
            }
        }
    }

    
    /** Registra usuário interessado (ex.: quem abriu conversa sobre o anúncio) e notifica. */
    public void adicionarInteressado(Usuario u) {
        if (u != null && !interessados.contains(u)) {
            interessados.add(u);
        }
        notificar(TipoEvento.NOVO_INTERESSADO);
    }

    public List<Usuario> getInteressados() {
        return Collections.unmodifiableList(interessados);
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
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

    
    public double getValorAluguel() { return valorAluguel; }
    public void setValorAluguel(double valorAluguel) {
        this.valorAluguel = valorAluguel;
        notificar(TipoEvento.PRECO_ALTERADO);
    }
    public double getValorTemporada() { return valorTemporada; }
    public void setValorTemporada(double valorTemporada) {
        this.valorTemporada = valorTemporada;
        notificar(TipoEvento.PRECO_ALTERADO);
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

    public int getQuantidadeFotos() { return quantidadeFotos; }
    public void setQuantidadeFotos(int quantidadeFotos) { this.quantidadeFotos = Math.max(0, quantidadeFotos); }
}
