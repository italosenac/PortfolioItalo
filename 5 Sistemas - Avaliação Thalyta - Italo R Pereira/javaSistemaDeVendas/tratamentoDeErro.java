package javaSistemaDeVendas;
import java.util.Scanner;

public class tratamentoDeErro {

    private static final Scanner inputUsuario = new Scanner(System.in);

    public static int capturarInteiro(String mensagem) {
        while (true) {
            try {
                System.out.println(mensagem);
                String entrada = inputUsuario.nextLine();
                return Integer.parseInt(entrada);
            } catch (NumberFormatException erro) {
                System.out.println("Erro: Por favor, insira um número válido.");
            }
        }
    }
    
    public static double capturarDouble(String mensagem) {
        while (true) {
            try {
                System.out.println(mensagem);
                String entrada = inputUsuario.nextLine();
                return Double.parseDouble(entrada);
            } catch (NumberFormatException erro) {
                System.out.println("Erro: Por favor, insira um número válido.");
            }
        }
    }
}