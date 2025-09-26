package com.project;

import java.util.concurrent.*;
import java.util.*;
    

public class Main {
    
    public static void main(String[] args) {
        // Conjunt de dades
        List<Double> dades = Arrays.asList(10.0, 20.0, 30.0, 40.0, 50.0);
    
        // Mapa compartit per guardar els resultats
        ConcurrentMap<String, Double> resultats = new ConcurrentHashMap<>();
    
        // CyclicBarrier per esperar els 3 càlculs
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("\nTots els càlculs han finalitzat. Mostrant els resultats:");
            System.out.printf("Suma: %.2f%n", resultats.get("suma"));
            System.out.printf("Mitjana: %.2f%n", resultats.get("mitjana"));
            System.out.printf("Desviació estàndard: %.2f%n", resultats.get("desviacio"));
        });
    
        ExecutorService executor = Executors.newFixedThreadPool(3);
    
        // Tasca 1: Suma
        Runnable calculaSuma = () -> {
            try {
                double suma = dades.stream().mapToDouble(Double::doubleValue).sum();
                resultats.put("suma", suma);
                System.out.println("Suma calculada.");
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    
        // Tasca 2: Mitjana
        Runnable calculaMitjana = () -> {
            try {
                double mitjana = dades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                resultats.put("mitjana", mitjana);
                System.out.println("Mitjana calculada.");
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
           }
        };
    
        // Tasca 3: Desviació estàndard
        Runnable calculaDesviacio = () -> {
        try {
            double mitjana = dades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double sumQuadrats = dades.stream()
                .mapToDouble(x -> Math.pow(x - mitjana, 2))
                .sum();
            double desviacio = Math.sqrt(sumQuadrats / dades.size());
            resultats.put("desviacio", desviacio);
            System.out.println("Desviació estàndard calculada.");
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    
    // Executem les tasques
    executor.submit(calculaSuma);
    executor.submit(calculaMitjana);
    executor.submit(calculaDesviacio);
    
    // Tanquem l'executor
    executor.shutdown();
    
    }
}

