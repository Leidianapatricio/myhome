package br.edu.ifpb.myhome.pagamento;

/**
 * Planos de assinatura da plataforma.
 */
public enum PlanoAssinatura {

    BASICO("Básico", 29.90, "Até 3 anúncios ativos"),
    PREMIUM("Premium", 79.90, "Até 15 anúncios, destaque na busca"),
    ILIMITADO("Ilimitado", 149.90, "Anúncios ilimitados, suporte prioritário");

    private final String nome;
    private final double valorMensal;
    private final String beneficios;

    PlanoAssinatura(String nome, double valorMensal, String beneficios) {
        this.nome = nome;
        this.valorMensal = valorMensal;
        this.beneficios = beneficios;
    }

    public String getNome() { return nome; }
    public double getValorMensal() { return valorMensal; }
    public String getBeneficios() { return beneficios; }
}
