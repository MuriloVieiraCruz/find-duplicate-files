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

            Map<Long, List<File>> filesBySize = new HashMap<>();
            listFilesBySize(directory, filesBySize);
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

    private void listFilesBySize(File directory, Map<Long, List<File>> filesBySize) {
        File[] files = directory.listFiles();
        if (files != null) {
            for(File file : files) {
                if (file.isFile()) {
                    filesBySize.computeIfAbsent(file.length(), k -> new ArrayList<>()).add(file);
                } else if (file.isDirectory()) {
                    listFilesBySize(file, filesBySize);
                }
            }
        }
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

//Uso de Threads para Leitura de Arquivos:
//
//Se você tiver muitos arquivos, a leitura e o hash de arquivos podem ser paralelizadas usando threads para aproveitar os múltiplos núcleos do processador.
//Otimização de I/O:
//
//Garantir que a leitura de arquivos seja feita de forma eficiente, possivelmente ajustando o tamanho dos buffers ou utilizando técnicas de I/O assíncrono.
//Pré-filtragem por Tamanho de Arquivo:
//
//Antes de calcular os hashes, agrupar os arquivos pelo seu tamanho. Arquivos com tamanhos diferentes não podem ser duplicatas, então você pode evitar calcular o hash de arquivos de tamanhos diferentes.
}