package br.edu.ifpb.myhome.anuncio;

import br.edu.ifpb.myhome.estado.EstadoAnuncio;
import br.edu.ifpb.myhome.estado.RascunhoState;
import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.moderacao.ResultadoModeracao;
import br.edu.ifpb.myhome.moderacao.ServicoModeracao;
import br.edu.ifpb.myhome.notificacao.Observer;
import br.edu.ifpb.myhome.notificacao.TipoEvento;
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
    /** Dono/anunciante do anúncio (para notificação: e-mail, etc.). */
    private Usuario dono;
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

    /** Chama o state: delega ao estado atual a transição (Rascunho → Moderação). */
    public boolean submeter() {
        if (estado != null) {
            estado.proximo(this);
            return true;
        }
        return false;
    }

    /**
     * Método que orquestra a submissão para publicação: valida (moderação) e chama o state
     * para transicionar (Rascunho → Moderação → Ativo ou Suspenso). O cliente (Main) só chama este método.
     */
    public ResultadoModeracao submeterParaPublicacao() {
        ServicoModeracao servico = new ServicoModeracao();
        ResultadoModeracao res = servico.validarRegras(this);
        if (res.isAprovado()) {
            submeter();
            aplicarResultadoModeracao(true);
        }
        return res;
    }

    /** Padrão State: delega ao estado a decisão Ativo vs Suspenso (após moderação). */
    public void aplicarResultadoModeracao(boolean aprovado) {
        if (estado != null) estado.aplicarResultadoModeracao(this, aprovado);
    }

    /** Padrão State: delega ao estado a transição para Arquivado (confirmar pagamento). */
    public void confirmarPagamento() {
        if (estado != null) estado.confirmarPagamento(this);
    }

    /** Padrão State: delega ao estado a ativação direta (ex.: anúncios carregados do CSV). */
    public void ativar() {
        if (estado != null) estado.ativar(this);
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

    public void setEstado(EstadoAnuncio estado) {
        EstadoAnuncio estadoAnterior = this.estado;
        this.estado = estado;
        LogMudancaEstado.getInstancia().registrar(this, estadoAnterior, estado);
        notificar(TipoEvento.MUDANCA_ESTADO);
    }

    /** Registra usuário interessado (ex.: quem abriu conversa sobre o anúncio) e notifica. */
    public void adicionarInteressado(Usuario u) {
        interessados.adicionar(u);
        notificar(TipoEvento.NOVO_INTERESSADO);
    }

    public List<Usuario> getInteressados() {
        return interessados.getInteressados();
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

    public void setPreco(double preco) {
        this.preco = preco;
        this.valorVenda = preco;
        notificar(TipoEvento.PRECO_ALTERADO);
    }

    public TipoOferta getTipoOferta() { return tipoOferta; }
    public void setTipoOferta(TipoOferta tipoOferta) { this.tipoOferta = tipoOferta != null ? tipoOferta : TipoOferta.VENDA; }
    public double getValorVenda() { return valorVenda; }
    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
        this.preco = valorVenda;
        notificar(TipoEvento.PRECO_ALTERADO);
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
