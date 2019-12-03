/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectorestaurante;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;

/**
 *
 * @author berna
 */
public class Viewer extends Canvas implements Runnable {

    private Restaurante restaurante;
    private BufferedImage fondo;
    private BufferedImage frame;
    private Image offImg;
    public Graphics gra;
    private long time;

    public Viewer(int w, int h) throws IOException {
        setBackground(Color.black);
        setSize(w, h);
        fondo = ImageIO.read(getClass().getResource("sprites/restaurante.png"));
        time = System.currentTimeMillis();

    }

    @Override
    public void run() {
        int fps = 30;
        while (true) {
            try {
                if (System.currentTimeMillis() - time >= 1000 / fps) {
                    pintarComponente(this.getGraphics());
                    time=System.currentTimeMillis();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
    }

    public void pintar(Graphics g) throws InterruptedException {
        g.drawImage(fondo, 0, 0, this);
        frame = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        pintarComponentes(frame.getGraphics());
        g.drawImage(frame, 0, 0, this);
    }

    public void pintarComponente(Graphics g) throws InterruptedException {
        offImg = createImage(getWidth(), getHeight());
        gra = offImg.getGraphics();
        pintar(gra);
        g.drawImage(offImg, 0, 0, null);
    }

    public void pintarComponentes(Graphics g) throws InterruptedException {
        Restaurante res = restaurante;

        //primero pintamos las mesas
        for (int i = 0; i < res.getMesas().size(); i++) {
            res.getMesas().get(i).paint(g);
        }
        //luego los cocineros

        //por ultimo los clientes
        for (int i = 0; i < res.getClientes().size(); i++) {
            res.getClientes().get(i).paint(g);
        }
        for (int i = 0; i < res.getCuiners().size(); i++) {
            res.getCuiners().get(i).paint(g);
        }
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public BufferedImage getFondo() {
        return fondo;
    }

    public void setFondo(BufferedImage fondo) {
        this.fondo = fondo;
    }

    public BufferedImage getFrame() {
        return frame;
    }

    public void setFrame(BufferedImage frame) {
        this.frame = frame;
    }
}
