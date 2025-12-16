package javaSistemaDeVendas;
import javax.swing.*;

public class Usuario {

    private String usuario;
    private JPasswordField senha;
    private String senhaFinal;

    public String getUsuario() {
        return usuario;
    }

    public String getSenhaCriptografada() {
        return senhaFinal;
    }

    public String setUsuario() {
        usuario = JOptionPane.showInputDialog("Digite seu usuário:");
        return usuario;
    }

    public JPasswordField setSenha() {
        senha = new JPasswordField();
        Object[] campos = { "Digite sua senha:", senha };
        int opcao = JOptionPane.showConfirmDialog(null, campos, "Senha", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            String senhaTexto = new String(senha.getPassword());
            byte[] senhaBytes = senhaTexto.getBytes();
            senhaFinal = GeradorDeHash.criptografar(senhaBytes);
            JOptionPane.showMessageDialog(null, "Senha cadastrada com sucesso!");
        }

        return senha;
    }
}