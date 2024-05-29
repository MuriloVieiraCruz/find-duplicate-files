package br.com.murilo.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class FileGroup extends RecursiveTask<Map<Long, List<File>>> {
    private final File directory;

    private final Map<Long, List<File>> filesBySize = new HashMap<>();

    public FileGroup(File directory) {
        this.directory = directory;
    }

    @Override
    protected Map<Long, List<File>> compute() {

        List<FileGroup> subTasks = new ArrayList<>();

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filesBySize.computeIfAbsent(file.length(), k -> new ArrayList<>()).add(file);
                } else if (file.isDirectory()) {
                    FileGroup subTask = new FileGroup(file);
                    subTask.fork();
                    subTasks.add(subTask);
                }
            }

            for (FileGroup subTask : subTasks) {
                Map<Long, List<File>> subTaskResult = subTask.join();
                subTaskResult.forEach((size, list) ->
                        filesBySize.merge(size, list, (originalList, newList) -> {
                            originalList.addAll(newList);
                            return originalList;
                        })
                );
            }
        }
        return filesBySize;
    }
}