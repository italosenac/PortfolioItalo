package javaSistemaBiblioteca;

import java.util.Scanner;

public class metodosLivro {

    static Scanner inputUsuario = new Scanner(System.in);
    String titulo, autor, confirmarCadastro, confirmarEmprestimo, confirmarDevolucao;
    boolean emprestado, cadastrado;
    static int contadorID = 0;
    int id;

    public metodosLivro() {
    	if(metodosBiblioteca.totalLivros == contadorID) {
    		this.id = contadorID;
    	}
        	this.id = contadorID++;
    	}

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean getEmprestado() {
        return !emprestado;
    }

    public String setTitulo() {
        System.out.println("Digite o título da obra: ");
        return titulo = inputUsuario.nextLine();
    }

    public String setAutor() {
        System.out.println("Digite o autor da obra: ");
        return autor = inputUsuario.nextLine();
    }

    public boolean confirmacaoCadastro() {
        do {
            System.out.println("Deseja cadastrar este livro? S/N");
            confirmarCadastro = inputUsuario.nextLine();
        } while (!confirmarCadastro.equalsIgnoreCase("S") && !confirmarCadastro.equalsIgnoreCase("N"));

        if (confirmarCadastro.equalsIgnoreCase("S")) {
            System.out.println("Livro cadastrado com sucesso!");
            cadastrado = true;
        } else {
            System.out.println("O livro não foi cadastrado.");
            cadastrado = false;
        }
        return cadastrado;
    }

    public boolean emprestarLivro() {
        do {
            System.out.println("Deseja reservar este livro? S/N");
            confirmarEmprestimo = inputUsuario.nextLine();
            if (confirmarEmprestimo.equalsIgnoreCase("S")) {
                return true;
            } else {
                System.out.println("O livro não foi reservado.");
                return false;
            }
        } while (!confirmarEmprestimo.equalsIgnoreCase("S") && !confirmarEmprestimo.equalsIgnoreCase("N"));
    }

    public boolean devolverLivro() {
        if (emprestado) {
            do {
                System.out.println("Deseja devolver este livro? S/N");
                confirmarDevolucao = inputUsuario.nextLine();
                if (confirmarDevolucao.equalsIgnoreCase("S")) {
                    System.out.println("Livro devolvido com sucesso!");
                    emprestado = false;
                    return true;
                } else {
                    System.out.println("O livro não foi devolvido.");
                    return false;
                }
            } while (!confirmarDevolucao.equalsIgnoreCase("S") && !confirmarDevolucao.equalsIgnoreCase("N"));
        } else {
            System.out.println("O livro não se encontra emprestado. Logo, não pode ser devolvido.");
            return false;
        }
    }

    public void verificarEmprestimo() {
        System.out.println("Status de empréstimo: " + (emprestado ? "Emprestado" : "Disponível"));
    }

    public void exibirInformacoesDoLivro() {
    	System.out.println("--------------------------------------------");
        System.out.println("ID: " + id);
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
        System.out.println("Emprestado: " + (emprestado ? "Sim" : "Não"));
    	System.out.println("--------------------------------------------");
    }
}
