package br.com.murilo.core.command;

import br.com.murilo.core.hashers.FileHasher;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDuplicateCommand {

    private final FileHasher fileHasher;

    public FindDuplicateCommand(FileHasher fileHasher) {
        this.fileHasher = fileHasher;
    }

    public List<File> execute(Map<Long, List<File>> filesBySize) throws NoSuchAlgorithmException, IOException {
        return findDuplicates(filesBySize);
    }

    private List<File> findDuplicates(Map<Long, List<File>> filesBySize) throws NoSuchAlgorithmException, IOException {
        Map<String, String> fileHashes = new HashMap<>();
        List<File> duplicates = new ArrayList<>();

        for (List<File> files : filesBySize.values()) {
            if (files.size() > 1) {
                for (File file : files) {
                    String hashKey = fileHasher.calculateFileHash(file);
                    if (fileHashes.containsKey(hashKey)) {
                        duplicates.add(file);
                    } else {
                        fileHashes.put(hashKey, file.getAbsolutePath());
                    }
                }
            }
        }
        return duplicates;
    }

}
