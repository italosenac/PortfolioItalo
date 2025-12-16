package javaSistemaDeVendas;
import java.util.Scanner;

public class Produto {
    private int codigoProduto;
    private String nomeProduto;
    private double precoProduto;
    private Scanner inputUsuario = new Scanner(System.in);

    public void produtoAtributos() {
        this.codigoProduto = tratamentoDeErro.capturarInteiro("Insira o código do produto:");
        System.out.print("Insira o nome do produto: ");
        this.nomeProduto = inputUsuario.nextLine();
        this.precoProduto = tratamentoDeErro.capturarDouble("Insira o valor do produto:");
    }


    public int getCodigoProduto() {
        return codigoProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto() {
        System.out.print("Atualize o preço do produto: ");
        this.precoProduto = Double.parseDouble(inputUsuario.nextLine());
    }
}