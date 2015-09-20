package com.company;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * Created by franchoco on 9/20/15.
 */
public class MainThread extends Thread {
    public boolean[] keys;
    private final static String TITLE = "Juego - CC5303";
    private final static int WIDTH = 800, HEIGHT = 600;
    private final static int UPDATE_RATE = 60;
    private final static int DX = 5;
    private final static double DV = 0.1;
    private final static int framesToNewBench = 100;
    private final double vy = 0.3;

    private JFrame frame;
    private Board tablero;
    private Player player1, player2;

    int frames = new Random().nextInt(2 * framesToNewBench);

    public MainThread() {
        keys = new boolean[KeyEvent.KEY_LAST];

        //Jugadores
        player1 = new Player(WIDTH/3, 2);
        player2 = new Player(2*WIDTH/3, 1);

        //resumen
        System.out.println(tablero);

        frame = new JFrame(TITLE);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tablero = new Board(WIDTH, HEIGHT);
        tablero.p1 = player1;
        tablero.p2 = player2;
        Bench piso = new Bench(WIDTH, HEIGHT, true);
        tablero.bases.add(piso);

        frame.add(tablero);
        tablero.setSize(WIDTH, HEIGHT);

        frame.pack();
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });

    }

    @Override
    public void run() {
        while (true) { // Main loop
            frames++;
            if (frames >= framesToNewBench) {
                Bench barra = new Bench(WIDTH, HEIGHT);
                tablero.bases.add(barra);
                frames = new Random().nextInt(framesToNewBench);
            }

            //Check controls
            if (keys[KeyEvent.VK_UP]) {
                tablero.p1.jump();
            }
            if (keys[KeyEvent.VK_RIGHT]) {
                tablero.p1.posX += 2;
            }
            if (keys[KeyEvent.VK_LEFT]) {
                tablero.p1.posX -= 2;
            }

            if (keys[KeyEvent.VK_W]) {
                tablero.p2.jump();
            }
            if (keys[KeyEvent.VK_D]) {
                tablero.p2.posX += 2;
            }
            if (keys[KeyEvent.VK_A]) {
                tablero.p2.posX -= 2;
            }

            //update players
            tablero.p1.update(DX);
            tablero.p2.update(DX);

            //update barras
            for (Bench barra : tablero.bases) {
                barra.posY += barra.speed * DX;

                if (tablero.p1.bottom() <= barra.top() && tablero.p1.bottom() >= barra.bottom()) {
                    tablero.p1.speed = 0.01;
                    tablero.p1.standUp = true;
                }

                if (tablero.p2.bottom() <= barra.top() && tablero.p2.bottom() >= barra.bottom()) {
                    tablero.p2.speed = 0.01;
                    tablero.p2.standUp = true;
                }

            }

            tablero.repaint();

            try {
                this.sleep(1000 / UPDATE_RATE);
            } catch (InterruptedException ex) {

            }
        }


    }
}
