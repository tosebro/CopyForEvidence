package util;

import java.io.UnsupportedEncodingException;

public class StringValidator {
	private static boolean checkCharacterCode(String str, String encoding) {
		if (str == null) {
			return true;
		}

		try {
			byte[] bytes = str.getBytes(encoding);
			return str.equals(new String(bytes, encoding));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("エンコード名称が正しくありません。", ex);
		}
	}

	public static boolean isWindows31j(String str) {
		return checkCharacterCode(str, "Windows-31j");
	}

	public static boolean isSJIS(String str) {
		return checkCharacterCode(str, "SJIS");
	}

	public static boolean isEUC(String str) {
		return checkCharacterCode(str, "euc-jp");
	}

	public static boolean isUTF8(String str) {
		return checkCharacterCode(str, "UTF-8");
	}

	public static boolean isWindows31j(byte[] bytes)
			throws UnsupportedEncodingException {
		return isWindows31j(new String(bytes, "Windows-31j"));
	}

	public static boolean isSJIS(byte[] bytes)
			throws UnsupportedEncodingException {
		return isSJIS(new String(bytes, "SJIS"));
	}

	public static boolean isEUC(byte[] bytes)
			throws UnsupportedEncodingException {
		return isEUC(new String(bytes, "euc-jp"));
	}

	public static boolean isUTF8(byte[] bytes)
			throws UnsupportedEncodingException {
		return isUTF8(new String(bytes, "UTF-8"));
	}
}