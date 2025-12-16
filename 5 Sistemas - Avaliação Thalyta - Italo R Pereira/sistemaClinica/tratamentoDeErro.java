package sistemaClinica;

public class tratamentoDeErro {

    public static int capturarInteiro(String mensagem) {
        while (true) {
            try {
                System.out.println(mensagem);
                String input = metodosSistema.inputUsuario.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Erro — digite um número válido!");
            }
        }
    }
}
