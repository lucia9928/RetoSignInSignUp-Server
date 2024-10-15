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
import java.util.Stack;
import signinsignupserver.HilosServidor;

public class ConnectionPool {
    private Stack<HilosServidor> hilosClientes; // Usamos Stack en lugar de List
    private final int maxHilos;

    public ConnectionPool(int maxHilos) {
        this.maxHilos = maxHilos;
        this.hilosClientes = new Stack<>();
    }

    // Método para agregar un hilo al pool
    public synchronized void agregarHilo(HilosServidor hilo) {
        if (hilosClientes.size() < maxHilos) {
            hilosClientes.push(hilo); // Agregar el hilo al stack
            hilo.start(); // Iniciar el hilo
            System.out.println("Hilo agregado al pool. Total hilos: " + hilosClientes.size());
        } else {
            System.out.println("Pool de conexiones lleno. No se puede agregar más hilos.");
        }
    }

    // Método para cerrar todos los hilos del pool
    public void cerrarTodosLosHilos() {
        while (!hilosClientes.isEmpty()) {
            HilosServidor hilo = hilosClientes.pop(); // Obtener el hilo del stack
            hilo.interrupt(); // Detener el hilo
        }
        System.out.println("Todos los hilos del pool han sido cerrados.");
    }

    public int getNumeroDeHilos() {
        return hilosClientes.size(); // Retorna el número actual de hilos
    }
}