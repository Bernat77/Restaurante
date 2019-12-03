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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author berna
 */
public class Cuiner implements Runnable {

    private Restaurante restaurante;

    //atributos principales
    private String nombre; //nombre del cocinero
    private int rapidez; //cuanto tarda en cocinar
    private int produccion; //cuantas comida puede producir
    private int v; //cuanto se mueve
    private BufferedImage sprite;
    private Mesa focus;

    private int framesx = 0;
    private int framesy = 0;
    private long time;
    private long frametime;

    private boolean idle;
    private boolean idlecook;
    private boolean right;
    private boolean deliv;

    private int x;
    private int y = 75;

    private int cocina;

    static final int COCINA_1 = 1;
    static final int COCINA_2 = 2;
    static final int COCINA_3 = 3;
    static final int SPRITE_1 = 1;
    static final int SPRITE_2 = 2;

    public Cuiner(String nombre, int rapidez, int produccion, int velocidad, int cocina, int sprtnumb) throws IOException {
        this.nombre = nombre;
        this.rapidez = rapidez;
        this.produccion = produccion;
        this.v = velocidad;
        idle = true;
        right = true;
        idlecook = false;
        deliv = false;

        switch (sprtnumb) {
            case 1:
                this.sprite = ImageIO.read(getClass().getResource("sprites/cuiner.png"));
                break;
            case 2:
                this.sprite = ImageIO.read(getClass().getResource("sprites/cuiner2.png"));
                break;
            default:
                this.sprite = ImageIO.read(getClass().getResource("sprites/cuiner.png"));
        }
        //cocina donde cocina
        switch (cocina) {
            case 1:
                this.cocina = 150;
                x = this.cocina;
                break;
            case 2:
                this.cocina = 0;
                x = this.cocina;
                break;
            case 3:
                this.cocina = 100;
                x = this.cocina;
                break;
        }

        time = System.currentTimeMillis();
        frametime = time;

    }

    @Override
    public void run() {
        while (true) {
            try {
                cocinar();
            } catch (InterruptedException ex) {
                Logger.getLogger(Cuiner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cocinar() throws InterruptedException {

        if (x == cocina && y == 75) {   //si esta en la cocina
            right = true;
            System.out.println(nombre + " empieza a cocinar");
            Thread.sleep(rapidez);   //cocina
            focus = comprobarMesa();   //se queda con la mesa mas vacía
        }

        int vary = 11;
        int varx = 90;
        mover(vary, varx);

        if (x == focus.getX() - varx && y == focus.getY() + vary) {
            deliv = true;
            idle = false;
            focus.añadirComida(this);   //metodo sincronizado
            Thread.sleep((int) (500 + (1000 * Math.random())));
            right = false;
            x -= 50;
            frametime = 0;   //esto arregla el pequeño saltito que hacia el cocinero al girarse.
        }
    }

    public Mesa comprobarMesa() {
        Restaurante res = this.restaurante;
        Mesa target = res.getMesas().get(0);

        for (int i = 0; i < res.getMesas().size(); i++) {
            if (res.getMesas().get(i).getHamb() <= target.getHamb()
                    && !res.getMesas().get(i).isFocused()) {
                target = res.getMesas().get(i);
            }
        }
        target.setFocused(true);
        System.out.println(nombre + " se dirige a " + target.getNombre()
                + " Hamburguesas: " + target.getHamb());
        return target;
    }

    public void paint(Graphics g) throws InterruptedException {
        if (System.currentTimeMillis() - frametime >= 60) {
            animar();
            frametime = System.currentTimeMillis();
        }
        // animar();
        g.drawImage(sprite(), x, y, restaurante);

    }

    public void animar() {

        if (idle && !deliv) {
            framesy = 1;
            if (System.currentTimeMillis() - time > 3000 + (int) (Math.random() * 10000)) {
                idlecook = true;
                time = System.currentTimeMillis();
            }
            if (idlecook) {
                framesx += 139;
                if (framesx > 1100) {
                    framesx = 0;
                    idlecook = false;
                }
            }
        } else if (!idle && !deliv) {
            if (right) {
                framesy = 98;
            } else {
                framesy = 194;
            }

            framesx += 139;
            if (framesx > 950) {
                framesx = 0;
            }
        } else if (deliv) {
            framesx = 0;
            framesy = 291;
        }

    }

    public void mover(int vary, int varx) throws InterruptedException {
        if (right) {   //
            if (idle && y != focus.getY() + vary) {
                y += 5;
            } else if (y == focus.getY() + vary && x != focus.getX() - varx) {
                idle = false;
                if (x + v < focus.getX() - varx) {
                    x += v;
                } else {
                    x++;
                }
            }
        } else {
            deliv = false;
            if (x == cocina && y != 75) {
                idle = true;
                y -= 5;
            } else if (y == focus.getY() + vary && x != cocina) {
                idle = false;
                if (x - v < cocina) {
                    x++;
                } else {
                    x -= v;
                }
            }
        }
        Thread.sleep(60);
    }

    public BufferedImage sprite() throws InterruptedException {

        return sprite.getSubimage(framesx, framesy, 138, 96);

        //g.drawImage(sprite.getSubimage(140, 1, 138, 96), 150, 70, restaurante);
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

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getProduccion() {
        return produccion;
    }

    public void setProduccion(int produccion) {
        this.produccion = produccion;
    }

}
