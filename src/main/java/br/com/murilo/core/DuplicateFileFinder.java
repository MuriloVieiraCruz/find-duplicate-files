package br.com.murilo.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class DuplicateFileFinder {

    private static final String HASH = "SHA-256";

    public void  mainProcess(File directory) {
        if (directory.isDirectory()) {
            ForkJoinPool pool = new ForkJoinPool();
            Map<Long, List<File>> filesBySize = pool.invoke(new FileGroup(directory));
            try {
                findDuplicates(filesBySize);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The provided path is not a directory");
        }
    }

    public void findDuplicates(Map<Long, List<File>> filesBySize) throws NoSuchAlgorithmException, IOException {
        Map<String, String> fileHashes = new HashMap<>();

        for(List<File> files : filesBySize.values()) {
            if (files.size() > 1) {
                for (File file : files) {
                    String hashKey = calculateFileHash(file);
                    if (fileHashes.containsKey(hashKey)) {
                        String existFilePath = fileHashes.get(hashKey);
                        System.out.println("Arquivo duplicado: " + file.getAbsolutePath());
                        System.out.println("Arquivo existente: " + existFilePath);
                    } else {
                        fileHashes.put(hashKey, file.getAbsolutePath());
                    }
                }
            }
        }
        System.out.println("-----------------------------------------------------------------------------------");
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

        byte[] bytes = digest.digest();
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return builder.toString();
    }
}