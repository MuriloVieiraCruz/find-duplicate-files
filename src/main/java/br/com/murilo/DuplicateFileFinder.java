package br.com.murilo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DuplicateFileFinder {

    private static final String HASH = "SHA-256";

    public void  mainProcess(File directory) {
        if (directory.isDirectory()) {
            Map<String, List<String>> fileHashes = new HashMap<>();
            try {
                findDuplicates(directory, fileHashes);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The provided path is not a directory");
        }
    }


    public void findDuplicates(File directory, Map<String, List<String>> fileHashes) throws NoSuchAlgorithmException, IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for(File file : files) {
                if (file.isDirectory()) {
                    findDuplicates(file, fileHashes);
                } else if (file.isFile()) {
                    String hash = calculateFileHash(file);
                    if (fileHashes.containsKey(hash)) {
                        fileHashes.get(hash).add(file.getAbsolutePath());
                        //System.out.println("Duplicate found: " + file.getAbsolutePath() + "  |||||||  " + fileHashes.get(hash));
                    } else {
                        List<String> listValues = new ArrayList<>();
                        listValues.add(file.getAbsolutePath());
                        fileHashes.put(hash, listValues);
                    }
                }
            }
            listDuplicates(fileHashes);
            System.out.println("-----------------------------------------------------------------------------------");
        }
    }

    private void listDuplicates(Map<String, List<String>> fileHashes) {
        fileHashes.forEach((hash, list) -> {
            if (list.size() > 1) {
                System.out.println("Duplicates found: " + list);
            }
        });
    }

    public String calculateFileHash(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance(HASH);

        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;

            while ((bytesCount = fileInputStream.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }

        // Transforma os bytes do hash em uma string hexadecimal
        byte[] bytes = digest.digest();
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return builder.toString();
    }
}
