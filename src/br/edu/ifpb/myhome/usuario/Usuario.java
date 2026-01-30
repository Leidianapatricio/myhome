package src.br.edu.ifpb.myhome.usuario;

public abstract class Usuario {

    private Long id;
    private String nome;
    private String email;

    public Usuario(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public abstract boolean autenticar();

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
