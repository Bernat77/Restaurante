/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectorestaurante;

import java.io.IOException;
import java.util.Random;

/**
 *
 * @author berna
 */
public class Main {

    public class Drop {
        // Message sent from producer
        // to consumer.

        private String message;
        // True if consumer should wait
        // for producer to send message,
        // false if producer should wait for
        // consumer to retrieve message.
        private boolean empty = true;

        public synchronized String take() {
            // Wait until message is
            // available.
            while (empty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            // Toggle status.
            empty = true;
            // Notify producer that
            // status has changed.
            notifyAll();
            return message;
        }

        public synchronized void put(String message) {
            // Wait until message has
            // been retrieved.
            while (!empty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            // Toggle status.
            empty = false;
            // Store message.
            this.message = message;
            // Notify consumer that status
            // has changed.
            notifyAll();
        }
    }

    public class Producer implements Runnable {

        private Drop drop;

        public Producer(Drop drop) {
            this.drop = drop;
        }

        public void run() {
            String importantInfo[] = {
                "Mares eat oats",
                "Does eat oats",
                "Little lambs eat ivy",
                "A kid will eat ivy too"
            };
            Random random = new Random();

            for (int i = 0;
                    i < importantInfo.length;
                    i++) {
                drop.put(importantInfo[i]);
                try {
                    Thread.sleep(random.nextInt(5000));
                } catch (InterruptedException e) {
                }
            }
            drop.put("DONE");
        }
    }

    public class Consumer implements Runnable {

        private Drop drop;

        public Consumer(Drop drop) {
            this.drop = drop;
        }

        public void run() {
            Random random = new Random();
            for (String message = drop.take();
                    !message.equals("DONE");
                    message = drop.take()) {
                System.out.format("MESSAGE RECEIVED: %s%n", message);
                try {
                    Thread.sleep(random.nextInt(5000));
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public static void main(String args[]) throws IOException {
        Restaurante hola = new Restaurante("Restaurante");

    }

}
