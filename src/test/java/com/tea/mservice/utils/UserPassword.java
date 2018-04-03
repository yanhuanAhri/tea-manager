package com.tea.mservice.utils;

import com.tea.mservice.core.util.Digests;
import com.tea.mservice.core.util.Encodes;

public class UserPassword {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	
	
	public static void main(String[] args) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		System.out.println(Encodes.encodeHex(salt));
		byte[] hashPassword = Digests.sha1("mzj@2017!".getBytes(), salt, HASH_INTERATIONS);
		System.out.println(Encodes.encodeHex(hashPassword));
	}

}
