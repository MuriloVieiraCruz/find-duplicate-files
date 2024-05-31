package br.com.murilo.core;

import br.com.murilo.core.command.FindDuplicateCommand;
import br.com.murilo.core.factories.ForkJoinPoolFactory;
import br.com.murilo.core.hashers.FileHasher;
import br.com.murilo.core.hashers.SHA256FileHash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class DuplicateFileFinder {

    public List<File>  mainProcess(File directory) {
        if (directory.isDirectory()) {
            ForkJoinPool pool = ForkJoinPoolFactory.createPool();
            Map<Long, List<File>> filesBySize = pool.invoke(new FileGroup(directory));
            try {
                FileHasher fileHasher = new SHA256FileHash();
                return new FindDuplicateCommand(fileHasher).execute(filesBySize);
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("The provided path is not a directory");
            return null;
        }
    }
}