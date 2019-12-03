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
public class Cliente implements Runnable {

    private Restaurante restaurante;

    //atributos principales 
    private String nombre;   //nombre del gordo que come
    private int comer;       //tiempo que tarda en comer el puto gordo este
    private int v;           //posible velocidad para ir a la mesa mas cercana para ponerse ciego y gordo.
    private long time;
    private long frametime;
    private Mesa mesa;

    //sprite
    private BufferedImage sprite;
    private int framesx = 0;    // frames de los sprites
    private int framesy = 0;

    //estados
    private boolean idle;
    private boolean wait;
    private boolean right;
    private boolean eat;

    //coordenadas
    private int x;
    private int y = 116;
    private int pos;

    //variables estaticas
    static final int MARIO = 1;
    static final int LUIGI = 2;
    static final int WARIO = 3;

    public Cliente(String nombre, int comer, int v, int pj, int pos) throws IOException {
        this.nombre = nombre;
        this.comer = comer;
        this.v = v;
        this.pos = pos;
        this.x = pos;
        time = System.currentTimeMillis();
        frametime = time;

        idle = true;
        wait = false;
        right = false;
        eat = false;

        switch (pj) {
            case 1:
                this.sprite = ImageIO.read(getClass().getResource("sprites/mario.png"));
                break;
            case 2:
                this.sprite = ImageIO.read(getClass().getResource("sprites/luigi.png"));
                break;
            case 3:
                this.sprite = ImageIO.read(getClass().getResource("sprites/wario.png"));
                break;
            default:
                this.sprite = ImageIO.read(getClass().getResource("sprites/mario.png"));
                break;
        }
    }

    public void run() {
        while (true) {
            try {
                comer();
            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void comer() throws InterruptedException {
        int varx = 20;

        if (x == pos) {
            if (!eat) {
                right = false;
                Thread.sleep(300);
                mesa = comprobarMesa();
                System.out.println(nombre + " se dirige a " + mesa.getNombre()
                        + " Hamburguesas: " + mesa.getHamb());
                idle = false;
            } else {
                Thread.sleep(comer);
                System.out.println("El señor " + nombre + " ha terminado de comer");
                eat = false;
                idle = true;

            }
        }
        if (x == mesa.getX() + varx && !right) {
            mesa.comerComida(this);
            eat = true;
            right = true;
        }
        mover(varx);
    }

    public Mesa comprobarMesa() {
        Restaurante res = this.restaurante;
        while (true) {
            for (int i = res.getMesas().size() - 1; i >= 0; i--) {
                if (res.getMesas().get(i).getHamb() > 0) {
                    return res.getMesas().get(i);
                }
            }
        }
//        target.setFocused(true);

    }

    public void paint(Graphics g) throws InterruptedException {
        if (System.currentTimeMillis() - frametime >= 55) {
            animar();
            frametime = System.currentTimeMillis();
        }
        g.drawImage(sprite(), x, y, restaurante);

    }

    public void animar() {

        if (idle) {
            framesx = 0;
            framesy = 0;
        } else if (!idle && !wait && x != pos) {
            if (right) {
                framesy = 120;
            } else {
                framesy = 240;
            }
            framesx += 122;
            if (framesx > 975) {
                framesx = 0;
            }

        } else if (wait) {
            framesy = 0;

            if (System.currentTimeMillis() - time >= 1000) {
                if (framesx < 300) {
                    framesx += 122;

                } else {
                    framesx = 122;
                }
                time = System.currentTimeMillis();
            }
        } else if (eat) {
            framesy = 358;
            //  framesx = 0;

            if (framesx < 300) {
                framesx += 122;

            } else {
                framesx = 0;
            }

        }

    }

    public void mover(int varx) throws InterruptedException {
        if (!right) {
            if (x != mesa.getX() + varx && !idle) {
                if (x - v < mesa.getX() + varx) {
                    x--;
                } else {
                    x -= v;
                }
            }
        } else if (right && !idle) {
            if (x + v > pos) {
                x++;
            } else {
                x += v;
            }
        }
        Thread.sleep(60);
    }

    public BufferedImage sprite() throws InterruptedException {

        return sprite.getSubimage(framesx, framesy, 122, 119);
    }

    public void cicloComer() {

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

    public int getComer() {
        return comer;
    }

    public void setComer(int comer) {
        this.comer = comer;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isEat() {
        return eat;
    }

    public void setEat(boolean eat) {
        this.eat = eat;
    }

}
