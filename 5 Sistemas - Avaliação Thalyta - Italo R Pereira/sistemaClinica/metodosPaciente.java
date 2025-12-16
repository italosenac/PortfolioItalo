package sistemaClinica;

import java.util.Scanner;

public class metodosPaciente {

    static Scanner inputUsuario = new Scanner(System.in);

    int id;
    String nome;
    String nascimento;
    Sexo sexo;
    String endereco;
    String telefone;

    String historicoMedico = ""; 
    boolean cadastrado;

    public metodosPaciente(int id) {
        this.id = id;
    }

    public void exibirInformacoes() {
        System.out.println("--------------------------");
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Nascimento: " + nascimento);
        System.out.println("Sexo: " + sexo);
        System.out.println("Endereço: " + endereco);
        System.out.println("Telefone: " + telefone);
        System.out.println("Histórico médico: " + historicoMedico);
        System.out.println("--------------------------");
    }

    public void adicionarInfoHistorico() {
        System.out.println("Descreva a informação a ser adicionada ao histórico médico:");
        String add = inputUsuario.nextLine();
        historicoMedico += "\n- " + add;
    }

    public void atualizarInfoPaciente() {
        System.out.println("Atualizar nome: ");
        nome = inputUsuario.nextLine();

        System.out.println("Atualizar data de nascimento: ");
        nascimento = inputUsuario.nextLine();

        System.out.println("Atualizar endereço: ");
        endereco = inputUsuario.nextLine();

        System.out.println("Atualizar telefone: ");
        telefone = inputUsuario.nextLine();

        System.out.println("Dados atualizados com sucesso!");
    }

    public void removerInfoHistorico() {
        historicoMedico = "";
        System.out.println("Histórico médico apagado.");
    }
}
