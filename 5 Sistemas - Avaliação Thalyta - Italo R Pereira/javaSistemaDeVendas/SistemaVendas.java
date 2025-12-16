package javaSistemaDeVendas;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.swing.*;

public class SistemaVendas  {

    ArrayList<Produto> produtos = new ArrayList<>();
    ArrayList<Venda> vendas = new ArrayList<>();
    ArrayList<Usuario> usuarios = new ArrayList<>();
    int totalProdutos, totalVendas, totalUsuarios, MAX_PRODUTOS = 3, MAX_VENDAS = 3, MAX_USUARIOS = 3;
    static JOptionPane inputUsuario;
    JPasswordField inputSenha;
    public boolean usuarioAutenticado;

    public void rodarSistemaDeVendas() {
		SistemaVendas sistema = new SistemaVendas();
		sistema.cadastrarUsuario(null, null);
        sistema.autenticarUsuario(null, null);
       
        if(sistema.usuarioAutenticado == true) {
			sistema.cadastrarProduto();
			sistema.realizarVenda();
	        sistema.exibirRelatorioVendas();
        }else {
        	do {
        		sistema.autenticarUsuario(null, null);
        	}while(sistema.usuarioAutenticado != true);
        }
    }
    
    public void cadastrarUsuario(String usuario, JPasswordField senha) {
    	if(totalUsuarios < MAX_USUARIOS) {
	    	Usuario cadastroUsuario = new Usuario();
	    	cadastroUsuario.setUsuario();
	    	cadastroUsuario.setSenha();
	    	cadastroUsuario.getSenhaCriptografada();
	    	usuarios.add(cadastroUsuario);
	    	totalUsuarios++;
    	}else {
    		System.out.println("Não é possível cadastrar novos usuários! - MAX 3");
    	}
    	
    }
    public void autenticarUsuario(String usuario, String senha) {
        String nomeDigitado = JOptionPane.showInputDialog("Digite seu usuário:");
        JPasswordField campoSenha = new JPasswordField();
        Object[] campos = { "Digite a senha:", campoSenha };
        int opcao = JOptionPane.showConfirmDialog(null, campos, "Autenticação", JOptionPane.OK_CANCEL_OPTION);
        if (opcao != JOptionPane.OK_OPTION) {
            usuarioAutenticado = false;
            return;
        }
        String senhaDigitada = new String(campoSenha.getPassword());
        byte[] senhaBytes = senhaDigitada.getBytes(StandardCharsets.UTF_8);
        String senhaCriptografada = GeradorDeHash.criptografar(senhaBytes);
        for (Usuario usuarioCadastrado : usuarios) {
            if (nomeDigitado.equals(usuarioCadastrado.getUsuario()) &&
                senhaCriptografada.equals(usuarioCadastrado.getSenhaCriptografada())) {
                System.out.println("Login realizado com sucesso!");
                usuarioAutenticado = true;
                return;
            }
        }

        System.out.println("Login ou Senha incorretos! Tente novamente.");
        usuarioAutenticado = false;
    }
    
    public void cadastrarProduto() {
        while (totalProdutos < MAX_PRODUTOS) {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja cadastrar um novo produto?", "Cadastro de Produto", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                Produto produtoAtual = new Produto();
                produtoAtual.produtoAtributos();
                produtos.add(produtoAtual);
                totalProdutos++;
            } else {
                break;
            }
        }

        if (totalProdutos >= MAX_PRODUTOS) {
            System.out.println("Não é possível cadastrar mais produtos!");
        }
    }

    public void realizarVenda() {
        while (totalVendas < MAX_VENDAS) {
            if (!confirmarAcao("Deseja realizar uma nova venda?")) 
            	break;

            Produto produto = selecionarProdutoPorId();
            if (produto == null) 
            	continue;

            exibirProduto(produto);

            int quantidade = solicitarQuantidade();
            if (quantidade <= 0) 
            	continue;

            double total = produto.getPrecoProduto() * quantidade;
            if (!confirmarVenda(produto, quantidade, total)) {
                JOptionPane.showMessageDialog(null, "Venda cancelada.");
                continue;
            }

            Venda novaVenda = new Venda();
            novaVenda.produto = produto;
            novaVenda.setQuantidadeProduto(quantidade);
            vendas.add(novaVenda);
            totalVendas++;
            JOptionPane.showMessageDialog(null, "Venda registrada com sucesso!");
        }

        if (totalVendas >= MAX_VENDAS) {
            JOptionPane.showMessageDialog(null, "Limite máximo de vendas atingido.");
        }
    }
    
    private boolean confirmarAcao(String mensagem) {
        int opcao = JOptionPane.showConfirmDialog(null, mensagem, "Confirmação", JOptionPane.YES_NO_OPTION);
        return opcao == JOptionPane.YES_OPTION;
    }

    private Produto selecionarProdutoPorId() {
        int idProduto = tratamentoDeErro.capturarInteiro("Digite o ID do produto:");

        for (Produto produto : produtos) {
            if (produto.getCodigoProduto() == idProduto) {
                return produto;
            }
        }

        JOptionPane.showMessageDialog(null, "Produto não encontrado.");
        return null;
    }

    private void exibirProduto(Produto produto) {
        String informacoes = "Produto: " + produto.getNomeProduto() +
                             "\nPreço: R$ " + String.format("%.2f", produto.getPrecoProduto());
        JOptionPane.showMessageDialog(null, informacoes);
    }

    private int solicitarQuantidade() {
        String entradaQuantidade = JOptionPane.showInputDialog("Digite a quantidade a ser vendida:");
        if (entradaQuantidade == null || entradaQuantidade.isEmpty()) return 0;

        return Integer.parseInt(entradaQuantidade);
    }

    private boolean confirmarVenda(Produto produto, int quantidade, double total) {
        String resumo = "Produto: " + produto.getNomeProduto() +
                        "\nQuantidade: " + quantidade +
                        "\nTotal: R$ " + String.format("%.2f", total);
        return confirmarAcao(resumo + "\nConfirmar venda?");
    }
    
    public void exibirRelatorioVendas(){
        System.out.println("=== RELATÓRIO DE VENDAS ===");
        for (int vendaInicial = 0; vendaInicial < vendas.size(); vendaInicial++) {
            Venda vendaNaLista = vendas.get(vendaInicial);
            System.out.println("-------------------------------");
            System.out.println("Venda " + (vendaInicial + 1));
            System.out.println("Produto: " + vendaNaLista.produto.getNomeProduto());
            System.out.println("Quantidade: " + vendaNaLista.getQuantidadeProduto());
            System.out.println("Total: R$ " + vendaNaLista.getValorTotal());
        }

    }
    
}