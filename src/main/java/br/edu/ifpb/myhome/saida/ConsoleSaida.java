package br.edu.ifpb.myhome.saida;

public class ConsoleSaida implements Saida {

    @Override
    public void escrever(String mensagem) {
        System.out.println(mensagem);
    }

    @Override
    public void escreverSemQuebra(String mensagem) {
        System.out.print(mensagem);
    }
}
