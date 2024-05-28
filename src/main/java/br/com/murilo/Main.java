package br.com.murilo;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        String directoryPath = "C:\\Users\\muril\\Downloads";

        File directory = new File(directoryPath);
        DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder();
        duplicateFileFinder.mainProcess(directory);
    }

    //TODO: Finalizar lógica de busca de arquivos duplicados
    //TODO: Otimizar busca dos arquivos
    //TODO: Implemetar a busca por arquivos velhos no sistema
    //TODO: Fazer uma interface para a aplicação no Swing
}
