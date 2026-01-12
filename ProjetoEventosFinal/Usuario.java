// O atributo id deve ser gerado automaticamente
public class Usuario implements OuvinteDeEvento {
    private int id;
    private String nome;
    private String email;
    private String senha;
    
    // Construtor completo
    public Usuario(int id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Método obrigatório da interface OuvinteDeEvento
    @Override
    public void receberNotificacao(Evento evento) {
        System.out.println("--- NOTIFICAÇÃO PARA " + nome + " ---");
        System.out.println("NOVO EVENTO CADASTRADO: " + evento.getNome() + " em " + evento.getDataHoraInicio());
        System.out.println("--------------------------------");
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}
