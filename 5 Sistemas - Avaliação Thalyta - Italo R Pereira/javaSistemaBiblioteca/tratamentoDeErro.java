package javaSistemaBiblioteca;

public class tratamentoDeErro {
	static int numero = -1;
    static boolean inputValido = false;
	
    public static int capturarInteiro(String mensagem) {
        while (true) {
            try {
                System.out.println(mensagem);
                String input = metodosBiblioteca.inputUsuario.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException erroIsNaN) {
                System.out.println("Erro: Por favor, insira um número válido.");
            }
        }
    }

}
