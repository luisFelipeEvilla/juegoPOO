package principal.entes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.control.GestorControles;
import principal.herramientas.DibujoOpciones;
import principal.mapas.Mapa;
import principal.sprites.HojaSprites;

public class Jugador {

    private double posicionX;
    private double posicionY;

    private double velocidadMovimiento = 0.7;
    private boolean enMovimiento;
    private int animacion;
    private int a;
    private int d;

    private int direccion;

    private HojaSprites hs;
    private BufferedImage imagenActual;

    private final int anchoJugador = 16;
    private final int altoJugador = 16;

    private final Rectangle LIMITE_ARRIBA = new Rectangle(Constantes.CENTRO_VENTANA_X - 25, Constantes.CENTRO_VENTANA_Y - anchoJugador - 2, anchoJugador, 1);
    private final Rectangle LIMITE_ABAJO = new Rectangle(Constantes.CENTRO_VENTANA_X - 25, Constantes.CENTRO_VENTANA_Y - 2, anchoJugador, 1);
    private final Rectangle LIMITE_IZQUIERDA = new Rectangle(Constantes.CENTRO_VENTANA_X - 25, Constantes.CENTRO_VENTANA_Y - altoJugador - 2, 1, altoJugador);
    private final Rectangle LIMITE_DERECHA = new Rectangle(Constantes.CENTRO_VENTANA_X - 9, Constantes.CENTRO_VENTANA_Y - altoJugador - 2, 1, altoJugador);

    public int resistencia = 300;
    private int recuperacion = 100;
    private boolean recuperado = true;

    private Mapa mapa;

    public Jugador(Mapa mapa, String Ruta) {

        this.posicionX = mapa.getCoordenadaInicial().getX();
        this.posicionY = mapa.getCoordenadaInicial().getY();
        this.enMovimiento = false;
        direccion = 2;

        this.hs = new HojaSprites(Ruta, Constantes.LADO_SPRITE, false);

        imagenActual = hs.getSprite(direccion, 0).getImagen();
        animacion = 0;
        a = 0;
        d = 3;

        this.mapa = mapa;
    }

    public Jugador(Mapa mapa) {

        this.posicionX = mapa.getCoordenadaInicial().getX();
        this.posicionY = mapa.getCoordenadaInicial().getY();
        this.enMovimiento = false;
        direccion = 2;

        this.hs = new HojaSprites("/imagenes/hojas_personajes/Personaje4.png", Constantes.LADO_SPRITE, false);

        imagenActual = hs.getSprite(direccion, 0).getImagen();
        animacion = 0;
        a = 0;
        d = 4;

        this.mapa = mapa;
    }

    public void actualizar() {

        if (animacion < 32767) {

            animacion++;
        } else {
            animacion = 0;
        }
        gestionarVelocidadResistencia();
        enMovimiento = false;
        determinarDireccion();
        animar();
    }

    private void gestionarVelocidadResistencia() {

        if (GestorControles.teclado.run && resistencia > 0) {
            velocidadMovimiento = 1.2;
            recuperado = false;
            recuperacion = 0;
        } else {
            velocidadMovimiento = 0.7;
            if (!recuperado && recuperacion < 100) {
                recuperacion++;
            }
            if (recuperacion == 100 && resistencia < 300) {
                resistencia++;
            }
        }
    }

    private void determinarDireccion() {

        final int velocidadX = getVelocidadX();
        final int velocidadY = getVelocidadY();

        if (velocidadX == 0 && velocidadY == 0) {
            return;
        }
        if ((velocidadX != 0 && velocidadY == 0) || (velocidadX == 0 && velocidadY != 0)) {
            mover(velocidadX, velocidadY);
        } else {
            if (velocidadX == -1 && velocidadY == -1) {
                if (GestorControles.teclado.left.getUltimaPulsacion() > GestorControles.teclado.up.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                } else {
                    mover(0, velocidadY);
                }
            }
            if (velocidadX == -1 && velocidadY == 1) {
                if (GestorControles.teclado.left.getUltimaPulsacion() > GestorControles.teclado.down.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                } else {
                    mover(0, velocidadY);
                }
            }
            if (velocidadX == 1 && velocidadY == -1) {
                if (GestorControles.teclado.right.getUltimaPulsacion() > GestorControles.teclado.up.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                } else {
                    mover(0, velocidadY);
                }
            }
            if (velocidadX == 1 && velocidadY == 1) {
                if (GestorControles.teclado.right.getUltimaPulsacion() > GestorControles.teclado.down.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                } else {
                    mover(0, velocidadY);
                }
            }
        }
    }

    private int getVelocidadX() {

        int velocidadX = 0;

        if (GestorControles.teclado.left.isPulsada() && !GestorControles.teclado.right.isPulsada()) {
            velocidadX = -1;
        } else if (GestorControles.teclado.right.isPulsada() && !GestorControles.teclado.left.isPulsada()) {
            velocidadX = 1;
        }
        return velocidadX;
    }

    private int getVelocidadY() {

        int velocidadY = 0;

        if (GestorControles.teclado.up.isPulsada() && !GestorControles.teclado.down.isPulsada()) {
            velocidadY = -1;
        } else if (GestorControles.teclado.down.isPulsada() && !GestorControles.teclado.up.isPulsada()) {
            velocidadY = 1;
        }
        return velocidadY;
    }

    private void mover(final int velocidadX, final int velocidadY) {

        enMovimiento = true;

        cambiarDireccion(velocidadX, velocidadY);

        if (!fueraMapa(velocidadX, velocidadY)) {

            if (velocidadX == -1 && !enColisionIzquierda(velocidadX)) {
                posicionX += velocidadX * velocidadMovimiento;
                if (GestorControles.teclado.run && resistencia > 0) {
                    resistencia--;
                }
            }
            if (velocidadX == 1 && !enColisionDerecha(velocidadX)) {
                posicionX += velocidadX * velocidadMovimiento;
                if (GestorControles.teclado.run && resistencia > 0) {
                    resistencia--;
                }
            }
            if (velocidadY == -1 && !enColisionArriba(velocidadY)) {
                posicionY += velocidadY * velocidadMovimiento;
                if (GestorControles.teclado.run && resistencia > 0) {
                    resistencia--;
                }
            }
            if (velocidadY == 1 && !enColisionAbajo(velocidadY)) {
                posicionY += velocidadY * velocidadMovimiento;
                if (GestorControles.teclado.run && resistencia > 0) {
                    resistencia--;
                }
            }
        }
    }

    private boolean enColisionArriba(int velocidadY) {

        for (int r = 0; r < mapa.areasColision.size(); r++) {
            final Rectangle area = mapa.areasColision.get(r);

            int origenX = area.x;
            int origenY = area.y + velocidadY * (int) (velocidadMovimiento + 0.6) + 3 * (int) (velocidadMovimiento + 0.6);

            final Rectangle areaFutura = new Rectangle(origenX, origenY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);

            if (LIMITE_ARRIBA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    private boolean enColisionAbajo(int velocidadY) {

        for (int r = 0; r < mapa.areasColision.size(); r++) {
            final Rectangle area = mapa.areasColision.get(r);

            int origenX = area.x;
            int origenY = area.y + velocidadY * (int) (velocidadMovimiento + 0.6) - 3 * (int) (velocidadMovimiento + 0.6);

            final Rectangle areaFutura = new Rectangle(origenX, origenY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);

            if (LIMITE_ABAJO.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    private boolean enColisionIzquierda(int velocidadX) {

        for (int r = 0; r < mapa.areasColision.size(); r++) {
            final Rectangle area = mapa.areasColision.get(r);

            int origenX = area.x + velocidadX * (int) (velocidadMovimiento + 0.6) + 3 * (int) (velocidadMovimiento + 0.6);
            int origenY = area.y;

            final Rectangle areaFutura = new Rectangle(origenX, origenY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);

            if (LIMITE_IZQUIERDA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    private boolean enColisionDerecha(int velocidadX) {

        for (int r = 0; r < mapa.areasColision.size(); r++) {
            final Rectangle area = mapa.areasColision.get(r);

            int origenX = area.x + velocidadX * (int) (velocidadMovimiento + 0.6) - 3 * (int) (velocidadMovimiento + 0.6);
            int origenY = area.y;

            final Rectangle areaFutura = new Rectangle(origenX, origenY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);

            if (LIMITE_DERECHA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    private boolean fueraMapa(final int velocidadX, final int velocidadY) {

        int posicionFuturaX = (int) posicionX + velocidadX * (int) (velocidadMovimiento + 0.6);
        int posicionFuturaY = (int) posicionY + velocidadY * (int) (velocidadMovimiento + 0.6);

        final Rectangle bordesMapa = mapa.getBordes(posicionFuturaX, posicionFuturaY, altoJugador, anchoJugador);

        final boolean FUERA;

        if (LIMITE_ARRIBA.intersects(bordesMapa) || LIMITE_ABAJO.intersects(bordesMapa) || LIMITE_IZQUIERDA.intersects(bordesMapa)
                || LIMITE_DERECHA.intersects(bordesMapa)) {

            FUERA = false;
        } else {
            FUERA = true;
        }

        return FUERA;
    }

    private void cambiarDireccion(final int velocidadX, final int velocidadY) {

        if (velocidadX == -1) {
            direccion = 3;
        } else if (velocidadX == 1) {
            direccion = 2;
        }
        if (velocidadY == -1) {
            direccion = 1;
        } else if (velocidadY == 1) {
            direccion = 0;
        }
    }

    private void animar() {

        if (enMovimiento) {
            GestorControles.teclado.dance = false;
            if (animacion % 10 == 0) {
                a++;
                if (a >= 4) {
                    a = 0;
                }
            }
            switch (a) {
                case 0:
                    imagenActual = hs.getSprite(direccion, 1).getImagen();
                    break;
                case 1:
                    imagenActual = hs.getSprite(direccion, 0).getImagen();
                    break;
                case 2:
                    imagenActual = hs.getSprite(direccion, 2).getImagen();
                    break;
                case 3:
                    imagenActual = hs.getSprite(direccion, 0).getImagen();
                    break;
            }
        } else {
            if (!GestorControles.teclado.dance) {
                a = 0;
                d = 4;
            }
            if (GestorControles.teclado.dance) {
                if (animacion % 13 == 0) {
                    a++;
                    if (a >= 3) {
                        a = 0;
                        d++;
                        if (d >= 7) {
                            d = 4;
                        }
                    }
                }
                imagenActual = hs.getSprite(d, a).getImagen();
                direccion = 0;
            } else {
                imagenActual = hs.getSprite(direccion, 0).getImagen();
            }
        }

//        if (direccion == 2 || direccion == 0) {
//            if (enMovimiento) {
//                if (animacion * velocidadMovimiento % 46 > 23) {
//                    imagenActual = hs.getSprite(direccion + 1, 0).getImagen();
//                } else {
//                    imagenActual = hs.getSprite(direccion + 1, 1).getImagen();
//                }
//            } else {
//                if (animacion % 180 >= 0 && animacion % 180 <= 20) {
//                    imagenActual = hs.getSprite(direccion, 0).getImagen();
//                } else if (animacion % 180 > 20 && animacion % 180 <= 40) {
//                    imagenActual = hs.getSprite(direccion, 1).getImagen();
//                } else if (animacion % 180 > 40 && animacion % 180 <= 60) {
//                    imagenActual = hs.getSprite(direccion, 2).getImagen();
//                } else if (animacion % 180 > 60 && animacion % 180 <= 80) {
//                    imagenActual = hs.getSprite(direccion, 3).getImagen();
//                } else if (animacion % 180 > 80 && animacion % 180 <= 100) {
//                    imagenActual = hs.getSprite(direccion, 4).getImagen();
//                } else if (animacion % 180 > 100 && animacion % 180 <= 120) {
//                    imagenActual = hs.getSprite(direccion, 3).getImagen();
//                } else if (animacion % 180 > 120 && animacion % 180 <= 140) {
//                    imagenActual = hs.getSprite(direccion, 2).getImagen();
//                } else if (animacion % 180 > 140 && animacion % 180 <= 160) {
//                    imagenActual = hs.getSprite(direccion, 1).getImagen();
//                } else {
//                    imagenActual = hs.getSprite(direccion, 0).getImagen();
//                }
//            }
//        }
//        if (direccion == 4 || direccion == 5) {
//            if (enMovimiento) {
//                if (animacion * velocidadMovimiento % 80 >= 0 && animacion * velocidadMovimiento % 80 <= 20) {
//                    imagenActual = hs.getSprite(direccion, 1).getImagen();
//                } else if (animacion * velocidadMovimiento % 80 > 20 && animacion * velocidadMovimiento % 80 <= 40) {
//                    imagenActual = hs.getSprite(direccion, 0).getImagen();
//                } else if (animacion * velocidadMovimiento % 80 > 40 && animacion * velocidadMovimiento % 80 <= 60) {
//                    imagenActual = hs.getSprite(direccion, 2).getImagen();
//                } else {
//                    imagenActual = hs.getSprite(direccion, 0).getImagen();
//                }
//            } else {
//                imagenActual = hs.getSprite(direccion, 0).getImagen();
//            }
//        }
    }

    public void dibujar(Graphics g) {

        final int centroX = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE;
        final int centroY = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE;

        //Frente y espalda
//        g.drawRect(centroX + 7, centroY, 17, 32);
        // Derecha e izquierda
//        g.drawRect(centroX + 13, centroY, 6, 32);
        DibujoOpciones.dibujarImagen(g, imagenActual, centroX, centroY);
//        g.drawRect(LIMITE_ARRIBA.x, LIMITE_ARRIBA.y, LIMITE_ARRIBA.width, LIMITE_ARRIBA.height);
//        g.drawRect(LIMITE_ABAJO.x, LIMITE_ABAJO.y, LIMITE_ABAJO.width, LIMITE_ABAJO.height);
//        g.drawRect(LIMITE_IZQUIERDA.x, LIMITE_IZQUIERDA.y, LIMITE_IZQUIERDA.width, LIMITE_IZQUIERDA.height);
//        g.drawRect(LIMITE_DERECHA.x, LIMITE_DERECHA.y, LIMITE_DERECHA.width, LIMITE_DERECHA.height);
    }

    public void setPosicionX(double posicionX) {
        this.posicionX = posicionX;
    }

    public void setPosicionY(double posicionY) {
        this.posicionY = posicionY;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public Rectangle getLIMITE_ARRIBA() {
        return LIMITE_ARRIBA;
    }
}
