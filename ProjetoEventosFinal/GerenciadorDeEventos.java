import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

public class GerenciadorDeEventos {
    private List<Evento> eventos = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<OuvinteDeEvento> ouvintes = new ArrayList<>(); 
    private final String ARQUIVO_EVENTOS = "eventos.data"; 
    private final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public GerenciadorDeEventos() {
        carregarEventosDoArquivo();
    }

    // --- Lógica do Padrão Observer ---
    private void adicionarOuvinte(OuvinteDeEvento ouvinte) {
        ouvintes.add(ouvinte);
    }
    
    private void notificarOuvintes(Evento novoEvento) {
        for (OuvinteDeEvento ouvinte : ouvintes) {
            ouvinte.receberNotificacao(novoEvento);
        }
    }

    // --- Lógica de Cadastro e ID CORRIGIDA ---
    // Esta função resolve a repetição de ID do usuário!
    public Usuario criarEAdicionarUsuario(String nome, String email, String senha) {
        int proximoId = usuarios.size() + 1;
        
        Usuario novoUsuario = new Usuario(proximoId, nome, email, senha);
        
        usuarios.add(novoUsuario);
        adicionarOuvinte(novoUsuario); 
        
        return novoUsuario; 
    }

    public void adicionarEvento(Evento evento) {
        eventos.add(evento);
        notificarOuvintes(evento); // Notificação para todos os usuários cadastrados
    }

    // --- Lógica de Persistência (Leitura/Escrita) ---
    private void carregarEventosDoArquivo() {
        try {
            File arquivo = new File(ARQUIVO_EVENTOS);
            Scanner scannerArquivo = new Scanner(arquivo);
            
            while (scannerArquivo.hasNextLine()) {
                String linha = scannerArquivo.nextLine();
                String[] partes = linha.split(";"); 
                if (partes.length >= 7) {
                    int id = Integer.parseInt(partes[0]);
                    String nome = partes[1];
                    String descricao = partes[2];
                    String categoria = partes[3];
                    
                    LocalDateTime inicio = partes[4].isEmpty() ? null : LocalDateTime.parse(partes[4], FORMATO_DATA);
                    LocalDateTime fim = partes[5].isEmpty() ? null : LocalDateTime.parse(partes[5], FORMATO_DATA);
                    String localizacao = partes[6];
                    
                    Evento evento = new Evento(id, nome, descricao, categoria, inicio, fim, localizacao);
                    eventos.add(evento);
                }
            }
            scannerArquivo.close();
        } catch (FileNotFoundException e) {
            // O sistema inicia do zero se o arquivo não existe
        } catch (Exception e) {
            System.err.println("Erro ao carregar eventos: " + e.getMessage());
        }
    }

    public void salvarEventosNoArquivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_EVENTOS))) {
            for (Evento evento : eventos) {
                String inicioStr = evento.getDataHoraInicio() != null ? evento.getDataHoraInicio().format(FORMATO_DATA) : "";
                String fimStr = evento.getDataHoraFim() != null ? evento.getDataHoraFim().format(FORMATO_DATA) : "";
                
                String linha = String.format("%d;%s;%s;%s;%s;%s;%s",
                    evento.getId(), evento.getNome(), evento.getDescricao(), evento.getCategoria(),
                    inicioStr, fimStr, evento.getLocalizacao());
                writer.println(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }

    // --- Lógica de Consulta e Organização ---
    
    public List<Evento> getEventosOrdenadosPorData() {
        List<Evento> listaOrdenada = new ArrayList<>(eventos);
        Collections.sort(listaOrdenada, Comparator.comparing(Evento::getDataHoraInicio, Comparator.nullsLast(Comparator.naturalOrder())));
        return listaOrdenada;
    }
    
    public void verificarEventosProximos() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proximoDia = agora.plus(24, ChronoUnit.HOURS);
        
        System.out.println("\n--- ALERTAS DE EVENTOS PRÓXIMOS (PRÓXIMAS 24H) ---");
        boolean encontrou = false;
        
        for (Evento evento : eventos) {
            LocalDateTime inicio = evento.getDataHoraInicio();
            if (inicio != null && inicio.isAfter(agora) && inicio.isBefore(proximoDia)) {
                System.out.println("ALERTA: " + evento.getNome() + " está marcado para " + inicio.format(FORMATO_DATA));
                encontrou = true;
            }
        }
        
        if (!encontrou) {
            System.out.println("Nenhum evento próximo nas próximas 24 horas.");
        }
    }
    
    // Getters e Busca
    public List<Evento> getEventos() { return eventos; }
    public List<Usuario> getUsuarios() { return usuarios; }
    
    public Evento buscarEventoPorId(int id) {
        for (Evento evento : eventos) {
            if (evento.getId() == id) {
                return evento;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorId(int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        return null;
    }
}