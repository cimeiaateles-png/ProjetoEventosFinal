import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private int id;
    private String nome;
    private String descricao;
    private String categoria;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String localizacao;
    private List<Usuario> participantes;

    public Evento(int id, String nome, String descricao, String categoria, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, String localizacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.localizacao = localizacao;
        this.participantes = new ArrayList<>(); 
    }

    // Método para a participação (requisito do trabalho)
    public void adicionarParticipante(Usuario usuario) {
        if (!participantes.contains(usuario)) {
            participantes.add(usuario);
            System.out.println(usuario.getNome() + " confirmou presença no evento " + this.nome + ".");
        }
    }
    
    // Método para cancelar a participação (requisito do trabalho)
    public void removerParticipante(Usuario usuario) {
        if (participantes.remove(usuario)) {
            System.out.println(usuario.getNome() + " cancelou a presença no evento " + this.nome + ".");
        }
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public String getLocalizacao() { return localizacao; }
    public List<Usuario> getParticipantes() { return participantes; }
}