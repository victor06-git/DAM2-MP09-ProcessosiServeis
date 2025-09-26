package com.project;

import java.util.concurrent.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Mapa compartit per emmagatzemar els resultats parcials
        ConcurrentMap<String, String> resultats = new ConcurrentHashMap<>();

        // CyclicBarrier que espera 3 microserveis i combina els resultats
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("\nTots els microserveis han acabat. Combinant els resultats...");

            // Combinar els resultats, agafant-los en ordre del hashMap
            String resultatFinal = resultats.get("m1") + " | " + 
                                   resultats.get("m2") + " | " +
                                   resultats.get("m3");

            System.out.println("Resultat Final: " + resultatFinal);
        });

        // ExecutorService amb 3 fils
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Microservei 1
        Runnable tasca1 = () -> {
            try {
                System.out.println("Microservei 1 processant dades...");
                Thread.sleep(1000); // Simula temps de processament
                resultats.put("m1", "memoria: 128MB, cpu: 30%");
                System.out.println("Microservei 1 completat.");
                barrier.await(); // Espera els altres microserveis
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace(); // Maneig d'errors
            }
        };

        // Microservei 2
        Runnable tasca2 = () -> {
            try {
                System.out.println("Microservei 2 processant dades...");
                Thread.sleep(1500); // Simula un temps de processament
                resultats.put("m2", "dades: 200MB");
                System.out.println("Microservei 2 completat.");
                barrier.await(); // Espera els altres microserveis
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace(); // Maneig d'errors
            }
        };

        // Microservei 3
        Runnable tasca3 = () -> {
            try {
                System.out.println("Microservei 3 processant dades...");
                Thread.sleep(2000); // Simula un temps de processament
                resultats.put("m3", "latencia: 50ms");
                System.out.println("Microservei 3 completat.");
                barrier.await(); // Espera els altres microserveis
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace(); // Maneig d'errors
            }
        };

        // Executar les tasques
        executor.submit(tasca1);
        executor.submit(tasca2);
        executor.submit(tasca3);

        // Tancar l'executor
        executor.shutdown();
    }
}
