package br.com.murilo.core.factories;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolFactory {
    public static ForkJoinPool createPool() {
        return new ForkJoinPool();
    }
}
