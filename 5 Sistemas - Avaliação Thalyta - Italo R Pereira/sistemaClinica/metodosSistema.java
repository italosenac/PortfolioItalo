package sistemaClinica;

import java.util.Scanner;
import static sistemaClinica.Constantes.*;

public class metodosSistema {

    static Scanner inputUsuario = new Scanner(System.in);
    static metodosPaciente[] arrayPacientes = new metodosPaciente[CAPACIDADE_MAXIMA];
    static int totalPacientes = 0;

    public static void iniciarPrograma() {
        System.out.println(MSG_BOAS_VINDAS);

        boolean cadastrar;
        do {
            cadastrar = perguntarCadastro();

            if (cadastrar) {
                cadastrarPaciente();
            }

        } while (cadastrar && totalPacientes < CAPACIDADE_MAXIMA);

        if (totalPacientes >= CAPACIDADE_MAXIMA) {
            System.out.println("Capacidade máxima atingida!");
        }
    }

    public static boolean perguntarCadastro() {
        String escolha;
        do {
            System.out.println(MSG_CADASTRO);
            escolha = inputUsuario.nextLine();
        } while (!escolha.equalsIgnoreCase("S") && !escolha.equalsIgnoreCase("N"));

        return escolha.equalsIgnoreCase("S");
    }

    public static void cadastrarPaciente() {
        if (totalPacientes >= CAPACIDADE_MAXIMA) {
            System.out.println("Limite atingido.");
            return;
        }

        metodosPaciente novo = new metodosPaciente(totalPacientes);

        System.out.print(MSG_NOME);
        novo.nome = inputUsuario.nextLine();

        System.out.print(MSG_NASC);
        novo.nascimento = inputUsuario.nextLine();

        System.out.print(MSG_SEXO);
        String sx = inputUsuario.nextLine();
        novo.sexo = sx.equalsIgnoreCase("M") ? Sexo.MASCULINO : Sexo.FEMININO;

        System.out.print(MSG_END);
        novo.endereco = inputUsuario.nextLine();

        System.out.print(MSG_TEL);
        novo.telefone = inputUsuario.nextLine();

        novo.cadastrado = true;
        arrayPacientes[totalPacientes] = novo;
        totalPacientes++;

        System.out.println("Paciente cadastrado com sucesso!");
    }

    public static void listarPacientes() {
        System.out.println(MSG_LISTA);
        if (totalPacientes == 0) {
            System.out.println("Nenhum paciente cadastrado.");
        } else {
            for (int i = 0; i < totalPacientes; i++) {
                arrayPacientes[i].exibirInformacoes();
            }
        }
    }

    public static void buscarPacientePorNome() {
        System.out.println("Digite o nome para busca:");
        String busca = inputUsuario.nextLine();

        for (int i = 0; i < totalPacientes; i++) {
            if (arrayPacientes[i].nome.equalsIgnoreCase(busca)) {
                arrayPacientes[i].exibirInformacoes();
                return;
            }
        }
        System.out.println("Paciente não encontrado.");
    }
}
