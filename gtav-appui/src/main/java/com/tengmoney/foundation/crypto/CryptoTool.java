/*
 * Copyright 2013 QAPROSOFT (http://qaprosoft.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tengmoney.foundation.crypto;


import com.tmoney.foundation.utils.Configuration;
import com.tmoney.foundation.utils.Configuration.Parameter;
import com.tmoney.foundation.utils.R;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CryptoTool
{
	private String algorithm = R.CONFIG.get("crypto_algorithm");
	private Cipher cipher;
	private Key key;

	public CryptoTool() throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, URISyntaxException
	{
		this(SecretKeyManager.loadKey(new File(Configuration.get(Parameter.CRYPTO_KEY_PATH))));
	}
	
	public CryptoTool(Key secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		this.key = secretKey;
		this.cipher = Cipher.getInstance(algorithm);
	}

	public String encrypt(String strToEncrypt)
	{
		try
		{
			cipher.init(Cipher.ENCRYPT_MODE, key);
			final String encryptedString = new String(Base64.encodeBase64(cipher.doFinal(strToEncrypt.getBytes())));
			return encryptedString;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error while encrypting, check your crypto key! " + e.getMessage());
		}
	}

	public String decrypt(String strToDecrypt)
	{
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, key);
			final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt.getBytes())));
			return decryptedString;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error while decrypting, check your crypto key! " + e.getMessage());
		}
	}
	
	public String encryptByPattern(String content, Pattern pattern)
	{
		String wildcard = pattern.pattern().substring(pattern.pattern().indexOf("{") + 1, pattern.pattern().indexOf(":"));
		if(content != null && content.contains(wildcard))
		{
			Matcher matcher = pattern.matcher(content);
			while(matcher.find())
			{
				String group = matcher.group();
				String crypt = StringUtils.removeStart(group, "{" + wildcard + ":").replace("}", "");
				content = StringUtils.replace(content, group, encrypt(crypt));
			}
		}
		return content;
	}
	
	public String decryptByPattern(String content, Pattern pattern)
	{
		String wildcard = pattern.pattern().substring(pattern.pattern().indexOf("{") + 1, pattern.pattern().indexOf(":"));
		if(content != null && content.contains(wildcard))
		{
			Matcher matcher = pattern.matcher(content);
			while(matcher.find())
			{
				String group = matcher.group();
				String crypt = StringUtils.removeStart(group, "{" + wildcard + ":").replace("}", "");
				content = StringUtils.replace(content, group, decrypt(crypt));
			}
		}
		return content;
	}
	
	public String encryptByPatternAndWrap(String content, Pattern pattern, String wrapper)
	{
		String wildcard = pattern.pattern().substring(pattern.pattern().indexOf("{") + 1, pattern.pattern().indexOf(":"));
		if(content != null && content.contains(wildcard))
		{
			Matcher matcher = pattern.matcher(content);
			while(matcher.find())
			{
				String group = matcher.group();
				String crypt = StringUtils.removeStart(group, "{" + wildcard + ":").replace("}", "");
				content = StringUtils.replace(content, group, String.format(wrapper, encrypt(crypt)));
			}
		}
		return content;
	}
	
	public String decryptByPatternAndWrap(String content, Pattern pattern, String wrapper)
	{
		String wildcard = pattern.pattern().substring(pattern.pattern().indexOf("{") + 1, pattern.pattern().indexOf(":"));
		if(content != null && content.contains(wildcard))
		{
			Matcher matcher = pattern.matcher(content);
			while(matcher.find())
			{
				String group = matcher.group();
				String crypt = StringUtils.removeStart(group, "{" + wildcard + ":").replace("}", "");
				content = StringUtils.replace(content, group, String.format(wrapper, decrypt(crypt)));
			}
		}
		return content;
	}

	public String getAlgorithm()
	{
		return algorithm;
	}

	public void setAlgorithm(String algorithm)
	{
		this.algorithm = algorithm;
	}

	public Cipher getCipher()
	{
		return cipher;
	}

	public void setCipher(Cipher cipher)
	{
		this.cipher = cipher;
	}
}
