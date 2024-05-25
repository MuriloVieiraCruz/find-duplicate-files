package br.com.murilo;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        String directoryPath = "";

        File directory = new File(directoryPath);

        try {

        } catch (IOException | NoSuchAlgorithmException e) {

        }

        for (File f : Objects.requireNonNull(directory.listFiles())) {
            System.out.println(f.getName());
            System.out.println("\n");
        }
    }


    public static void findDuplicates() {

    }

    public static String calculateFileHash() {

        return null;
    }



}
