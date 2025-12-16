package SistemasSenacItalo.salaChat.infrastructure.cli;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

public class ConsoleHelper {

    private static final Scanner scanner = new Scanner(System.in);
    private static ConfigurableApplicationContext context;

    public static void setContext(ConfigurableApplicationContext ctx) {
        context = ctx;
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void printMenu() {
        System.out.println();
        System.out.println("=== SalaChat - Menu ===");
        System.out.println("1) Login (username + email)");
        System.out.println("2) Listar usuários logados");
        System.out.println("3) Selecionar usuário ativo");
        System.out.println("4) Enviar mensagem (usuário ativo)");
        System.out.println("5) Ver mensagens do usuário ativo");
        System.out.println("6) Logout do usuário ativo");
        System.out.println("7) Sair do aplicativo");
        System.out.println("=======================");
    }
}
