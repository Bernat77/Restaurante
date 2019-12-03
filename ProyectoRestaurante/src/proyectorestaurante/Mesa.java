/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectorestaurante;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

/**
 *
 * @author berna
 */
public class Mesa {

    private Restaurante restaurante;
    private String nombre; //numero de mesa
    private int hamb; //cantidad de hamburguesas
    private BufferedImage sprite;
    private int x;
    private int y = 124;
    private int frame = 1;

    private boolean focused;

    public Mesa(String nombre, int pos, int hamb) throws IOException {
        this.nombre = nombre;
        this.x = pos;
        String culo = "hola hola";
        this.hamb = hamb;
        sprite = ImageIO.read(getClass().getResource("sprites/mesa1.png"));
        actualizarSprite();
    }

    public Mesa(String nombre) {
        this.nombre = nombre;
        this.hamb = hamb;
    }

    public void actualizarSprite() {
        if (hamb < 9) {
            frame = (hamb * 115);
        } else {
            frame = (8 * 115);
        }
    }

    public synchronized void añadirComida(Cuiner cuiner) {
        if (hamb >= 8) {
            System.out.println("El chef " + cuiner.getNombre() + " quiso servir"
                    + " comida en la " + this.nombre + " pero está llena así que"
                    + " esperará.");
        }
        while (hamb >= 8) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Se interrumpió el " + Thread.currentThread().getName());
            }
        }

        if (hamb < 8 && hamb >= 0) {

            hamb += cuiner.getProduccion();
            actualizarSprite();
            System.out.println(cuiner.getNombre() + " ha cocinado y servido "
                    + cuiner.getProduccion() + " hamburguesas para la " + nombre);
            System.err.println("-- Hay " + hamb + " hamburguesas en la " + nombre + " --");
            focused = false;
            notifyAll();
        }
    }

    public synchronized void comerComida(Cliente cliente) {

        if (hamb <= 0) {
            System.out.println("El señor " + cliente.getNombre() + " se acercó a la "
                    + this.nombre + " para comer pero no había comida. "
                    + "Tendrá que esperar...");
        }
        while (hamb <= 0) {
            cliente.setWait(true);

            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Se interrumpió el " + Thread.currentThread().getName());
            }
        }
        if (hamb > 0) {
            cliente.setWait(false);
            System.out.println("El señor " + cliente.getNombre() + " agarra una hamburguesa y se va a comer");
            hamb--;
            actualizarSprite();
            System.out.println("-- Hay " + hamb + " hamburguesas en la " + nombre + " --");
            notifyAll();
        }
    }

    public void paint(Graphics g) {

        g.drawImage(sprite.getSubimage(frame, 0, 113, sprite.getHeight()), x, y, null);
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getHamb() {
        return hamb;
    }

    public void setHamb(int hamb) {
        this.hamb = hamb;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

}
