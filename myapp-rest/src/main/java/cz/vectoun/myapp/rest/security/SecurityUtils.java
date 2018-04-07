package cz.vectoun.myapp.rest.security;

import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.enums.UserRole;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class SecurityUtils {

	public static final String AUTH_COOKIE = "myapp-token";
	public static final String IS_ADMIN_COOKIE = "myapp-is_admin";
	public static final String INIT_VECTOR;
	public static final String KEY;
	public static final String AUTHENTICATED_USER = "authenticatedUser";
	public static final int COOKIE_AGE = 60*60*1000;

	static {
		try(InputStream input = SecurityUtils.class.getResourceAsStream("/server.properties")) {
			Properties properties = new Properties();
			properties.load(input);

			INIT_VECTOR = properties.getProperty("initVector");
			KEY = properties.getProperty("key");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Cookie generateIsAdminCookie(UserDTO user) {
		String value = UserRole.ADMIN.equals(user.getRole()) ? "true" : "false";

		Cookie cookie = new Cookie(IS_ADMIN_COOKIE, value);
		cookie.setMaxAge(COOKIE_AGE);
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		return cookie;
	}

	public static Cookie generateAuthCookie(UserDTO user) {
		String userData = user.getId() + ";" + user.getEmail();

		String token = encrypt(KEY, INIT_VECTOR, userData);
		Cookie cookie = new Cookie(AUTH_COOKIE, token);
		cookie.setPath("/");
		cookie.setMaxAge(COOKIE_AGE);
		cookie.setHttpOnly(false);
		return cookie;
	}

	public static String encrypt(String key, String initVector, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			System.out.println("encrypted string: "
					+ Base64.encodeBase64String(encrypted));

			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decrypt(String key, String initVector, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
