package javaSistemaBiblioteca;

public class mainBiblioteca {

    public static void main(String[] args) throws Exception {

        metodosBiblioteca novoMetodo = new metodosBiblioteca();
        novoMetodo.iniciarPrograma();
        novoMetodo.listarLivros();
        novoMetodo.escolherLivro();
        novoMetodo.listarLivros();
    }
}
