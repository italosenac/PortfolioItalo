package facsenacMaven.sistemaChat.console;

import facsenacMaven.sistemaChat.domain.ApplicationService.AlunoService;
import facsenacMaven.sistemaChat.domain.ApplicationService.MessageService;
import facsenacMaven.sistemaChat.domain.ApplicationService.SessionService;
import facsenacMaven.sistemaChat.domain.entity.Message;
import facsenacMaven.sistemaChat.domain.entity.Aluno;
import facsenacMaven.sistemaChat.infrastructure.dto.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ChatConsole {

    private final AlunoService alunoService;
    private final SessionService sessionService;
    private final MessageService messageService;
    private final Scanner scanner = new Scanner(System.in);
    private String currentUserEmail;

    public void run() {
        if (!fazerLogin()) return;
        boolean logado = true;
        while (logado) {
            System.out.println("\nMenu:\n1) Listar usuários logados\n2) Enviar mensagem\n3) Ver inbox\n4) Logout e sair");
            System.out.print("Escolha: ");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    List<String> logged = sessionService.listLogged();
                    if (logged.isEmpty()) {
                        System.out.println("Nenhum usuário logado no momento.");
                    } else {
                        System.out.println("Usuários logados:");
                        logged.forEach(System.out::println);
                    }
                    break;
                case "2":
                    System.out.print("Email do destinatário: ");
                    String recipient = scanner.nextLine();
                    System.out.print("Mensagem: ");
                    String content = scanner.nextLine();
                    try {
                        Message sent = messageService.sendMessage(currentUserEmail, recipient, content);
                        System.out.println("Mensagem enviada em: " + sent.getMessageDate());
                    } catch (Exception exception) {
                        System.out.println("Erro ao enviar: " + exception.getMessage());
                    }
                    break;
                case "3":
                    List<Message> inbox = messageService.inbox(currentUserEmail);
                    if (inbox.isEmpty()) {
                        System.out.println("Inbox vazio.");
                    } else {
                        System.out.println("Mensagens recebidas:");
                        inbox.forEach(message -> System.out.println("De: " + message.getSender() + " | " + message.getContent() + " | " + message.getMessageDate()));
                    }
                    break;
                case "4":
                    fazerLogout();
                    logado = false;
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
        System.out.println("Sessão finalizada.");
    }

    private boolean fazerLogin() {
        System.out.println("Bem-vindo ao chat. Faça login (nome, email, senha).");
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String secret = scanner.nextLine();

        try {
            Aluno user = alunoService.login(new LoginDTO(name, email, secret));
            currentUserEmail = email;
            System.out.println("Login efetuado com sucesso. Bem-vindo, " + user.getName());
            return true;
        } catch (Exception exception) {
            System.out.println("Falha no login: " + exception.getMessage());
            return false;
        }
    }

    private void fazerLogout() {
        if (currentUserEmail == null) return;
        try {
            alunoService.logoutByEmail(currentUserEmail);
        } catch (Exception e) {
            alunoService.logout("00000000-0000-0000-0000-000000000000");
        }
        System.out.println("Logout efetuado.");
    }
}
