package javaSistemaBiblioteca;

import java.util.ArrayList;
import java.util.Scanner;

public class metodosBiblioteca {

    static Scanner inputUsuario = new Scanner(System.in);
    static ArrayList<metodosLivro> arrayDeLivros = new ArrayList<>();
    static int totalLivros = 0;
    int capacidadeMaximaLivros = 4;
    boolean novoCadastroLivro;

    public boolean novoCadastro() {
        String escolhaUsuario;
        do {
            System.out.println("Olá! Deseja realizar o cadastro de novos livros? S/N");
            escolhaUsuario = inputUsuario.nextLine();
        } while (!escolhaUsuario.equalsIgnoreCase("S") && !escolhaUsuario.equalsIgnoreCase("N"));
        
        if (escolhaUsuario.equalsIgnoreCase("S")) {
            return novoCadastroLivro = true;
        }
        return novoCadastroLivro = false;
    }

    public metodosLivro cadastrarLivro() {
        if (totalLivros >= capacidadeMaximaLivros) {
            System.out.println("Capacidade máxima de livros atingida.");
            return null;
        }
        metodosLivro novoLivro = new metodosLivro();
        novoLivro.titulo = novoLivro.setTitulo();
        novoLivro.autor = novoLivro.setAutor();
        novoLivro.exibirInformacoesDoLivro();
        
        boolean confirmar = novoLivro.confirmacaoCadastro();
        if (confirmar) {
            arrayDeLivros.add(novoLivro);
            totalLivros++;
        } else {

        }
        
        return novoLivro;
    }
    
    public void escolherLivro() throws Exception {
        if (totalLivros != 0) {
            int idEscolhido = 0;

            while (true) {
                idEscolhido = tratamentoDeErro.capturarInteiro("Escolha o ID do livro desejado para reserva: ");
                
                if (idEscolhido > 0 && idEscolhido <= totalLivros) {
                    metodosLivro novoLivro = arrayDeLivros.get(idEscolhido - 1);
                    System.out.println("Você escolheu o livro: ");
                    novoLivro.exibirInformacoesDoLivro();
                    boolean emprestado = novoLivro.emprestarLivro();
                    if (emprestado) {
                        novoLivro.emprestado = true;
                        System.out.println("Livro emprestado com sucesso!");
                        break;
                    } else {
                        System.out.println("O livro não foi emprestado.");
                        break;
                    }
                } else {
                    System.out.println("ID inválido! Por favor, insira um ID válido.");
                }
            }
        } else {
            System.out.println("Não há livros cadastrados para reserva.");
        }
    }

    public void devolverLivro(metodosLivro novoLivro) {
        if (novoLivro != null && novoLivro.cadastrado) {
            novoLivro.devolverLivro();
        } else {
            System.out.println("Não é possível devolver o livro! Não se encontra na base de livros cadastrados.");
        }
    }
    
    public void listarLivros() {
        if (totalLivros == 0) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            System.out.println("Total de livros cadastrados: " + totalLivros);
            for (metodosLivro novoLivro : arrayDeLivros) {
            	novoLivro.exibirInformacoesDoLivro();
            }
        }
    }

    public void exibirInfo(metodosLivro novoLivro) {
        if (novoLivro != null) {
            novoLivro.exibirInformacoesDoLivro();
            
        }
    }
    public void iniciarPrograma() {
        boolean escolhaUsuario;
        metodosBiblioteca novoMetodo = new metodosBiblioteca();
        metodosLivro novoLivro = new metodosLivro();
        
        do {
            escolhaUsuario = novoMetodo.novoCadastro();
            if (escolhaUsuario) {
                novoLivro = novoMetodo.cadastrarLivro();
            }
        } while (escolhaUsuario && novoLivro != null && metodosBiblioteca.totalLivros < novoMetodo.capacidadeMaximaLivros);
        
        if(escolhaUsuario && novoLivro != null && metodosBiblioteca.totalLivros >= novoMetodo.capacidadeMaximaLivros) {
        	System.out.println("Capacidade máxima de livros atingida!"); 	
        }
    }
}
