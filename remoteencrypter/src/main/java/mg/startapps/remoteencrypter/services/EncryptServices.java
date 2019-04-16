package mg.startapps.remoteencrypter.services;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptServices
{
	public static final String ALGORITHM = "RSA";
	public static final String ENCODING = "UTF-8";

	public static String encrypt(String data, PublicKey publicKey, String algorithm) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException
	{
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return Base64.encodeToString(cipher.doFinal(data.getBytes(ENCODING)), Base64.DEFAULT);
	}

	public static String encrypt(Integer data, PublicKey publicKey, String algorithm) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException
	{
		return EncryptServices.encrypt(data.toString(), publicKey, algorithm);
	}

	public static String encrypt(Float data, PublicKey publicKey, String algorithm) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException
	{
		return EncryptServices.encrypt(data.toString(), publicKey, algorithm);
	}

	public static byte[] decrypt(String data, PrivateKey privateKey, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
	}

	public static PublicKey getPublicKey(String base64PublicKey, String algorithm)
	{
		byte[] decodedPublicKey = Base64.decode(base64PublicKey, Base64.DEFAULT);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedPublicKey);
		try
		{
			KeyFactory kf = KeyFactory.getInstance(algorithm);
			return kf.generatePublic(spec);
		}
		catch (InvalidKeySpecException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static PublicKey getPublicKey(String base64PublicKey)
	{
		return EncryptServices.getPublicKey(base64PublicKey, ALGORITHM);
	}

	public static PrivateKey getPrivateKey(String base64PrivateKey, String algorithm)
	{
		byte[] encodedPublicKey = Base64.decode(base64PrivateKey, Base64.DEFAULT);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encodedPublicKey);
		try
		{
			KeyFactory kf = KeyFactory.getInstance(algorithm);
			return kf.generatePrivate(spec);
		}
		catch (InvalidKeySpecException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static PrivateKey getPrivateKey(String base64PrivateKey)
	{
		return EncryptServices.getPrivateKey(base64PrivateKey, ALGORITHM);
	}
}