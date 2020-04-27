package principal.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener {

    public Tecla up = new Tecla();
    public Tecla down = new Tecla();
    public Tecla left = new Tecla();
    public Tecla right = new Tecla();
    public boolean run = false;
    public boolean opciones = false;
    public boolean dance = false;

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                up.teclaPulsada();
                break;
            case KeyEvent.VK_S:
                down.teclaPulsada();
                break;
            case KeyEvent.VK_A:
                left.teclaPulsada();
                break;
            case KeyEvent.VK_D:
                right.teclaPulsada();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_SHIFT:
                run = true;
                break;
            case KeyEvent.VK_O:
                opciones = !opciones;
                break;
            case KeyEvent.VK_ENTER:
                dance = !dance;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                up.teclaLiberada();
                break;
            case KeyEvent.VK_S:
                down.teclaLiberada();
                break;
            case KeyEvent.VK_A:
                left.teclaLiberada();
                break;
            case KeyEvent.VK_D:
                right.teclaLiberada();
                break;
            case KeyEvent.VK_SHIFT:
                run = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
