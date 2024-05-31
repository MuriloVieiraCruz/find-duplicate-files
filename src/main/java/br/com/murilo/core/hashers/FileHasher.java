package br.com.murilo.core.hashers;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface FileHasher {
    String calculateFileHash(File file) throws NoSuchAlgorithmException, IOException;
}
