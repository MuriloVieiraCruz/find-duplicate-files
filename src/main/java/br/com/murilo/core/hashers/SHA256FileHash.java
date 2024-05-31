package br.com.murilo.core.hashers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256FileHash implements FileHasher {

    private static final String HASH = "SHA-256";

    @Override
    public String calculateFileHash(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance(HASH);

        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;

            while ((bytesCount = fileInputStream.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }

        byte[] bytes = digest.digest();
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return builder.toString();
    }
}
