package com.tea.mservice.portal.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileMD5Compare {

	public static boolean verifyInstallPackage(String packagePath, String crc) {
		String FileSrc = getFileMd5(packagePath);
		crc = crc.toLowerCase();
		if (FileSrc.equals(crc)) {
			return true;
		}else{
			return false;
		}
	}
	
	
	public static String getFileMd5(String packagePath){
		return getFileMd5(new File(packagePath));
	}

	public static String getFileMd5(File file){
		String digestStr = "";
		try {
			if (!file.isFile()) {
				return digestStr;
			}
			MessageDigest digest = null;
			FileInputStream in = null;
			byte buffer[] = new byte[1024];
			int len;
			try {
				digest = MessageDigest.getInstance("MD5");
				in = new FileInputStream(file);
				while ((len = in.read(buffer, 0, 1024)) != -1) {
					digest.update(buffer, 0, len);
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			byte[] digestByte = digest.digest();
			digestStr = bytesToHexString(digestByte);
			digestStr = digestStr.toLowerCase();
		} catch (Exception e) {
			return "";
		}
		
		return digestStr;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		int i = 0;
		while (i < src.length) {
			int v;
			String hv;
			v = (src[i] >> 4) & 0x0F;
			hv = Integer.toHexString(v);
			stringBuilder.append(hv);
			v = src[i] & 0x0F;
			hv = Integer.toHexString(v);
			stringBuilder.append(hv);
			i++;
		}
		return stringBuilder.toString();
	}

}
