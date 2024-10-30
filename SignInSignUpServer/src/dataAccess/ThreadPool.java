/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private final ExecutorService executorService;

    public ThreadPool(int maxHilos) {
        this.executorService = Executors.newFixedThreadPool(maxHilos);
    }

    public void executeTask(Runnable tarea) {
        executorService.execute(tarea); // Ejecuta la tarea sin verificar estados complejos
    }

    public void shutdown() {
        executorService.shutdownNow(); // Cierra inmediatamente, sin esperar
        System.out.println("Todos los hilos del pool han sido cerrados.");
    }
}