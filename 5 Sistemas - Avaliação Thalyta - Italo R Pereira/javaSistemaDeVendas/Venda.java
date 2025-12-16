package javaSistemaDeVendas;
import java.util.Scanner;

public class Venda {
    Scanner inputUsuario = new Scanner(System.in);
    Produto produto;
    private int quantidadeProduto;
    private double valorTotal;


    public int getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setQuantidadeProduto(int quantidade) {
        this.quantidadeProduto = quantidade;
        this.valorTotal = produto.getPrecoProduto() * quantidade;
    }

    public void calcularValorTotal() {
        this.valorTotal = produto.getPrecoProduto() * quantidadeProduto;
    }
}