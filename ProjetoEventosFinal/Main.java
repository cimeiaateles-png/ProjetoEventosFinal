import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GerenciadorDeEventos gerenciador = new GerenciadorDeEventos();
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String opcao;

        System.out.println("Olá! Bem-vindo ao Sistema de Gerenciamento de Eventos.");

        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Cadastrar novo usuário");
            System.out.println("2. Cadastrar novo evento");
            System.out.println("3. Ver lista de eventos (ordenada)");
            System.out.println("4. Confirmar/Cancelar participação");
            System.out.println("5. Verificar eventos próximos (24h)");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    cadastrarUsuario(scanner, gerenciador);
                    break;
                case "2":
                    cadastrarEvento(scanner, gerenciador, formatoEntrada);
                    break;
                case "3":
                    mostrarEventosOrdenados(gerenciador);
                    break;
                case "4":
                    gerenciarParticipacao(scanner, gerenciador);
                    break;
                case "5":
                    gerenciador.verificarEventosProximos();
                    break;
                case "6":
                    gerenciador.salvarEventosNoArquivo();
                    System.out.println("\nSaindo do sistema. Até logo!");
                    break;
                default:
                    System.out.println("\nOpção inválida. Tente novamente.");
            }
        } while (!opcao.equals("6"));
        
        scanner.close();
    }

    private static void cadastrarUsuario(Scanner scanner, GerenciadorDeEventos gerenciador) {
        System.out.println("\n--- CADASTRO DE USUÁRIO ---");
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite seu email: ");
        String email = scanner.nextLine();
        System.out.print("Crie uma senha: ");
        String senha = scanner.nextLine();
        
        Usuario novoUsuario = gerenciador.criarEAdicionarUsuario(nome, email, senha);
        
        System.out.println("\nUsuário '" + novoUsuario.getNome() + "' cadastrado com sucesso! ID: " + novoUsuario.getId());
    }

    private static void cadastrarEvento(Scanner scanner, GerenciadorDeEventos gerenciador, DateTimeFormatter formato) {
        System.out.println("\n--- CADASTRO DE EVENTO ---");

        System.out.print("Digite o nome do evento: ");
        String nomeEvento = scanner.nextLine();
        
        System.out.print("Digite a descrição do evento: ");
        String descricaoEvento = scanner.nextLine();

        System.out.print("Digite a categoria do evento: ");
        String categoriaEvento = scanner.nextLine();
        
        System.out.print("Digite a localização do evento: ");
        String localizacaoEvento = scanner.nextLine();
        
        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;
        
        try {
            System.out.print("Digite a data e hora de INÍCIO (Ex: 31/12/2025 18:00): ");
            dataInicio = LocalDateTime.parse(scanner.nextLine(), formato);
            
            System.out.print("Digite a data e hora de FIM (Opcional - Pressione Enter se não houver): ");
            String fimStr = scanner.nextLine();
            if (!fimStr.trim().isEmpty()) {
                dataFim = LocalDateTime.parse(fimStr, formato);
            }
        } catch (DateTimeParseException e) {
            System.out.println("ERRO: Formato de data e hora inválido. O evento não foi cadastrado.");
            return;
        }

        int idEvento = gerenciador.getEventos().size() + 1; 
        
        Evento novoEvento = new Evento(idEvento, nomeEvento, descricaoEvento, categoriaEvento, dataInicio, dataFim, localizacaoEvento);

        gerenciador.adicionarEvento(novoEvento);

        System.out.println("\nEvento '" + novoEvento.getNome() + "' cadastrado com sucesso! ID: " + novoEvento.getId());
    }

    private static void mostrarEventosOrdenados(GerenciadorDeEventos gerenciador) {
        System.out.println("\n--- LISTA DE EVENTOS (ORDENADA POR DATA) ---");
        List<Evento> eventosOrdenados = gerenciador.getEventosOrdenadosPorData();
        
        if (eventosOrdenados.isEmpty()) {
            System.out.println("Nenhum evento cadastrado ainda.");
        } else {
            for (Evento evento : eventosOrdenados) {
                System.out.println("ID: " + evento.getId());
                System.out.println("Nome: " + evento.getNome());
                System.out.println("Data/Hora: " + (evento.getDataHoraInicio() != null ? evento.getDataHoraInicio().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) : "Data Não Informada"));
                System.out.println("Local: " + evento.getLocalizacao());
                System.out.println("Participantes: " + evento.getParticipantes().size());
                System.out.println("--------------------");
            }
        }
    }
    
    private static void gerenciarParticipacao(Scanner scanner, GerenciadorDeEventos gerenciador) {
        System.out.println("\n--- CONFIRMAR/CANCELAR PARTICIPAÇÃO ---");
        System.out.print("Digite o ID do seu Usuário: ");
        
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de usuário inválido.");
            return;
        }
        
        Usuario usuario = gerenciador.buscarUsuarioPorId(idUsuario);

        if (usuario == null) {
            System.out.println("Usuário não encontrado. Cadastre um usuário primeiro (Opção 1).");
            return;
        }

        System.out.print("Digite o ID do Evento: ");
        int idEvento;
        try {
            idEvento = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de evento inválido.");
            return;
        }
        
        Evento evento = gerenciador.buscarEventoPorId(idEvento);

        if (evento == null) {
            System.out.println("Evento não encontrado. Cadastre um evento primeiro (Opção 2).");
            return;
        }

        System.out.print("Deseja (C)onfirmar ou (R)emover a participação? ");
        String acao = scanner.nextLine().toUpperCase();

        if (acao.equals("C")) {
            evento.adicionarParticipante(usuario);
        } else if (acao.equals("R")) {
            evento.removerParticipante(usuario);
        } else {
            System.out.println("Opção inválida.");
        }
    }
}