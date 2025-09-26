package com.project;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Main {

    // Creació classe ParkingLot
    static class ParkingLot {
        private final Semaphore semaphore; // Controla l'accés a l'aparcament

        public ParkingLot(int capacitat) {
            this.semaphore = new Semaphore(capacitat);
        }

        public void entrar(String cotxe) {
            System.out.println(cotxe + " intentant entrar a l'aparcament...");
            try {
                semaphore.acquire(); // Espera si no hi ha espai
                System.out.println(cotxe + " ha entrat a l'aparcament.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(cotxe + " ha estat interromput.");
            }
        }

        public void sortir(String cotxe) {
            System.out.println(cotxe + " està sortint de l'aparcament...");
            semaphore.release();
            System.out.println(cotxe + " ha sortit. Espai disponible.");
        }
    }

    public static void main(String[] args) {
        final int capacitatAparcament = 2;
        final int totalCotxes = 5;

        ParkingLot aparcament = new ParkingLot(capacitatAparcament); // Atribut capacitat aparcament
        ExecutorService executor = Executors.newFixedThreadPool(totalCotxes); // Executor per gestionar els cotxes

        for (int i = 1; i <= totalCotxes; i++) {
            final String cotxeNom = "Cotxe " + i;
            executor.submit(() -> {
                aparcament.entrar(cotxeNom);
                try {
                    Thread.sleep(3000); // Simulem estada a l'aparcament
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restaurar l'estat d'interrupció
                } finally {
                    aparcament.sortir(cotxeNom); // Assegurem que el cotxe surt
                }
            });
        }

        executor.shutdown();
    }
}
