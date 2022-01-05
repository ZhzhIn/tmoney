package com.tengmoney.foundation.crypto;

import com.tengmoney.foundation.crypto.CryptoTool;
import com.tmoney.foundation.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

/**
 * 〈testclass〉
 *
 * @author zhzh.yin
 * @create 2022/1/5
 */
@Slf4j
public class CryptoToolTest {
    @Test
public void testInitialization() throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, URISyntaxException, URISyntaxException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
    CryptoTool cryptoTool = new CryptoTool();
    log.info(cryptoTool.getAlgorithm());
    log.info(cryptoTool.getCipher().toString());
    log.info(R.CONFIG.get("crypto_algorithm"));
    Assert.assertNotNull(cryptoTool.getAlgorithm());
    Assert.assertNotNull(cryptoTool.getCipher());
    Assert.assertEquals(R.CONFIG.get("crypto_algorithm"), cryptoTool.getAlgorithm());
}

    @Test
    public void testEncrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, URISyntaxException
    {
        CryptoTool cryptoTool = new CryptoTool();
        String input = "EncryptMe";
        String encrypted = cryptoTool.encrypt(input);
        Assert.assertNotNull(encrypted);
        Assert.assertFalse(encrypted.equals(input));
    }

    @Test
    public void testDecrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, URISyntaxException
    {
        CryptoTool cryptoTool = new CryptoTool();
        String input = "EncryptMe";
        String encrypted = cryptoTool.encrypt(input);
        String decrypted = cryptoTool.decrypt(encrypted);
        Assert.assertNotNull(decrypted);
        Assert.assertEquals(input, decrypted);
    }
}
