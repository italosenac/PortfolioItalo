package javaSistemaDeVendas;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


import java.util.Base64;

public class GeradorDeHash {

    private static final String ALGORITMO = "AES";
    private static final String CHAVE_PADRAO = "1234567890123456";

    public static String criptografar(byte[] senhaBytes) {
        try {
            String texto = new String();
            byte[] chaveBytes = CHAVE_PADRAO.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec chave = new SecretKeySpec(chaveBytes, ALGORITMO);

            Cipher cifra = Cipher.getInstance(ALGORITMO);
            cifra.init(Cipher.ENCRYPT_MODE, chave);

            byte[] criptografado = cifra.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(criptografado);

        } catch (Exception erro) {
            throw new IllegalStateException("Erro ao criptografar senha", erro);
        }
    }
}