/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

/**
 *
 * @author 2dam
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private final ExecutorService executorService;
    private final int maxHilos;

    public ThreadPool(int maxHilos) {
        this.maxHilos = maxHilos;
        this.executorService = Executors.newFixedThreadPool(maxHilos);
    }

    public synchronized void agregarHilo(Runnable tarea) {
        if (executorService != null) {
            executorService.execute(tarea);
            System.out.println("Tarea agregada al pool. Total hilos activos: " + getNumeroDeHilos());
        } else {
            System.out.println("El pool de hilos no está disponible.");
        }
    }

    public void cerrarTodosLosHilos() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            System.out.println("Todos los hilos del pool han sido cerrados.");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public int getNumeroDeHilos() {
        
        return ((ThreadPoolExecutor) executorService).getActiveCount();
    }
}