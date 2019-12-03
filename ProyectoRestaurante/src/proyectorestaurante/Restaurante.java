/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectorestaurante;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author berna
 */
public class Restaurante extends JFrame {

    private Viewer viewer;
    private ArrayList<Cuiner> cuiners = new ArrayList<Cuiner>();
    private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    private ArrayList<Mesa> mesas = new ArrayList<Mesa>();

    public Restaurante() throws IOException {
//        añadirCuiner("Chicote", 10000, 2, 5, 1, 1);
//        añadirCliente("Papá Noel", 9000, 3);
//        añadirCliente("Goku", 5000, 3);
//        añadirCliente("Pepsi Man", 8000, 3);
//        //  añadirMesa("Mesa 1");
//
//        for (int i = 0; i < cuiners.size(); i++) {
//            new Thread(cuiners.get(i)).start();
//        }
//
//        for (int i = 0; i < clientes.size(); i++) {
//            new Thread(clientes.get(i)).start();
//        }
    }

    public Restaurante(String titulo) throws IOException {
        setTitle(titulo);
        setSize(1283, 291);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.viewer = new Viewer(this.getWidth(), this.getHeight());
        viewer.setRestaurante(this);
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(viewer);
        viewer.setVisible(true);
        setLocationRelativeTo(null);
        setVisible(true);

        añadirMesa("Mesa 1", 500, returnRandom());
        añadirMesa("Mesa 2", 700, returnRandom());

        añadirCuiner("Chicote", 9000, 1, 7, Cuiner.COCINA_1, Cuiner.SPRITE_1);
        añadirCuiner("Arguiñano", 6500, 1, 5, Cuiner.COCINA_2, Cuiner.SPRITE_2);
        //       añadirCuiner("Arguiñano", 2500, 1, 6, Cuiner.COCINA_3, Cuiner.SPRITE_1);

        añadirCliente("Luigi", 10000, 9, Cliente.LUIGI, 1120);
        añadirCliente("Mario", 8500, 7, Cliente.MARIO, 1000);
        añadirCliente("Wario", 1500, 4, Cliente.WARIO, 1060);

//        añadirCliente("Luigi", 10000, 10, Cliente.LUIGI, 1070);
//        añadirCliente("Mario", 8500, 6, Cliente.MARIO, 1000);
//        añadirCliente("Wario", 1500, 1, Cliente.WARIO, 1100);
//        
//          añadirCliente("Luigi", 10000, 10, Cliente.LUIGI, 1070);
//        añadirCliente("Mario", 8500, 6, Cliente.MARIO, 1000);
//        añadirCliente("Wario", 1500, 1, Cliente.WARIO, 1100);
//        
//          añadirCliente("Luigi", 10000, 10, Cliente.LUIGI, 1070);
//        añadirCliente("Mario", 8500, 6, Cliente.MARIO, 1000);
//        añadirCliente("Wario", 1500, 1, Cliente.WARIO, 1100);
//        
//          añadirCliente("Luigi", 10000, 10, Cliente.LUIGI, 1070);
//        añadirCliente("Mario", 8500, 6, Cliente.MARIO, 1000);
//        añadirCliente("Wario", 1500, 1, Cliente.WARIO, 1100);

        iniciarHilos();

        //se inician los hilos
    }

    public void iniciarHilos() {

        new Thread(viewer).start();                     //hilo del canvas 

        for (int i = 0; i < cuiners.size(); i++) {      //hilo de los cocineros
            new Thread(cuiners.get(i)).start();
        }
        for (int i = 0; i < clientes.size(); i++) {      //hilo de los clientes
            new Thread(clientes.get(i)).start();
        }
    }

    public void añadirCuiner(String n, int x, int y, int v, int c, int s) throws IOException {
        Cuiner cuiner = new Cuiner(n, x, y, v, c, s);
        cuiner.setRestaurante(this);
        cuiners.add(cuiner);
    }

    public void añadirCliente(String s, int c, int v, int pj, int pos) throws IOException {
        Cliente cl = new Cliente(s, c, v, pj, pos);
        cl.setRestaurante(this);
        clientes.add(cl);
    }

    public void añadirMesa(String s, int x, int h) throws IOException {
        Mesa m = new Mesa(s, x, h);
        m.setRestaurante(this);
        mesas.add(m);
    }

    public int returnRandom() {
        return (int) (Math.random() * (4));
    }

    public ArrayList<Cuiner> getCuiners() {
        return cuiners;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public ArrayList<Mesa> getMesas() {
        return mesas;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

}
