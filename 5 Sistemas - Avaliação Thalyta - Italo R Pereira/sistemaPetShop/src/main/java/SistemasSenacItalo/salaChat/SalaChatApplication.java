package SistemasSenacItalo.salaChat;

import SistemasSenacItalo.salaChat.domain.dto.LoginDTO;
import SistemasSenacItalo.salaChat.domain.entity.Message;
import SistemasSenacItalo.salaChat.domain.entity.User;
import SistemasSenacItalo.salaChat.domain.service.ChatService;
import SistemasSenacItalo.salaChat.infrastructure.cli.ConsoleHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SalaChatApplication implements CommandLineRunner {

    private final ChatService chatService;
    private static ConfigurableApplicationContext ctx;

    public SalaChatApplication(ChatService chatService) {
        this.chatService = chatService;
    }

    public static void main(String[] args) {
        ctx = SpringApplication.run(SalaChatApplication.class, args);
        ConsoleHelper.setContext(ctx);
    }

    @Override
    public void run(String... args) throws Exception {
        String activeUser = null;
        System.out.println("Bem-vindo ao SalaChat (console) - iniciado como Spring Boot.");

        boolean running = true;
        while (running) {
            ConsoleHelper.printMenu();
            String opt = ConsoleHelper.readLine("Escolha uma opção: ").trim();
            try {
                switch (opt) {
                    case "1":
                        handleLogin();
                        break;
                    case "2":
                        listUsers();
                        break;
                    case "3":
                        activeUser = selectActiveUser();
                        if (activeUser != null) System.out.println("Usuário ativo agora: " + activeUser);
                        break;
                    case "4":
                        if (activeUser == null) {
                            System.out.println("Nenhum usuário ativo. Faça login / selecione um usuário ativo primeiro.");
                        } else {
                            sendMessageFlow(activeUser);
                        }
                        break;
                    case "5":
                        if (activeUser == null) {
                            System.out.println("Nenhum usuário ativo.");
                        } else {
                            showMessages(activeUser);
                        }
                        break;
                    case "6":
                        if (activeUser == null) {
                            System.out.println("Nenhum usuário ativo.");
                        } else {
                            chatService.logout(activeUser);
                            System.out.println("Logout realizado para: " + activeUser);
                            activeUser = null;
                        }
                        break;
                    case "7":
                        running = false;
                        System.out.println("Saindo... Obrigado por usar o SalaChat.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        }

        // Ao finalizar, encerre a aplicação Spring
        SpringApplication.exit(ctx);
    }

    private void handleLogin() {
        String username = ConsoleHelper.readLine("Username: ").trim();
        String email = ConsoleHelper.readLine("Email: ").trim();
        LoginDTO dto = new LoginDTO(username, email);
        User u = chatService.login(dto);
        System.out.println("Logado com sucesso: " + u);
    }

    private void listUsers() {
        List<User> usuarios = chatService.listLoggedUsers();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário logado no momento.");
        } else {
            System.out.println("Usuários logados:");
            for (User u : usuarios) {
                System.out.println(" - " + u.getUsername() + " (" + u.getEmail() + ")");
            }
        }
    }

    private String selectActiveUser() {
        String username = ConsoleHelper.readLine("Digite o username para ativar: ").trim();
        Optional<User> u = chatService.listLoggedUsers().stream()
                .filter(x -> x.getUsername().equalsIgnoreCase(username))
                .findFirst();
        if (!u.isPresent()) {
            System.out.println("Usuário não encontrado entre os logados.");
            return null;
        }
        return u.get().getUsername();
    }

    private void sendMessageFlow(String fromUser) {
        String to = ConsoleHelper.readLine("Para (username): ").trim();
        String content = ConsoleHelper.readLine("Mensagem: ").trim();
        chatService.sendMessage(fromUser, to, content);
        System.out.println("Mensagem enviada de " + fromUser + " para " + to);
    }

    private void showMessages(String username) {
        List<Message> msgs = chatService.getMessagesForUser(username);
        if (msgs.isEmpty()) {
            System.out.println("Nenhuma mensagem para esse usuário.");
        } else {
            System.out.println("Mensagens relacionadas a " + username + ":");
            for (Message m : msgs) {
                System.out.println(m);
            }
        }
    }
}
