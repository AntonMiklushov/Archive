package com.company;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.util.FPSAnimator;
 
import java.util.Random;

import static java.lang.Integer.min;
import static java.lang.Integer.max;

public class Main {

    public static Random random = new Random();

    public static int width = 1280;
    public static int height = 720;
    public static int size = 5;
    public static int fps = 800;
    public static int mpx = 0;
    public static int mpy = 0;
    public static boolean pressed = false;
    public static int mouseButton = 0;

    public static class Renderer {
        private static GLWindow window = null;

        public static void init() {
            GLProfile.initSingleton();
            GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities caps = new GLCapabilities(profile);

            window = GLWindow.create(caps);
            window.setTitle("Falling grains");
            window.setSize(width, height);
            window.setResizable(false);
            window.setVisible(true);
            window.addGLEventListener(new EventListener());
            window.addMouseListener(new MouseInput());
            FPSAnimator animator = new FPSAnimator(window, fps);
            animator.start();
        }
    }

    public static class EventListener implements GLEventListener {

        @Override
        public void init(GLAutoDrawable glAutoDrawable) {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {
            System.exit(0);
        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
            GL2 gl = glAutoDrawable.getGL().getGL2();

            gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

            Grain.render(gl);

            Grain.Motion();

            if (pressed) {
                if (mouseButton == 1) {
                    Grain.newGrain(new Grain.Sand(), mpx, mpy);
                }
                if (mouseButton == 3) {
                    Grain.newGrain(new Grain.Water(), mpx, mpy);
                }
            }
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(0, width, height, 0, -1, 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
        }
    }

    public static class MouseInput implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            pressed = true;
            mouseButton = mouseEvent.getButton();
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            pressed = false;
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            mpx = mouseEvent.getX();
            mpy = mouseEvent.getY();
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            mpx = mouseEvent.getX();
            mpy = mouseEvent.getY();
        }

        @Override
        public void mouseWheelMoved(MouseEvent mouseEvent) {

        }
    }

    public static class Grain {
        byte id = -1;

        private static Grain world[][] = new Grain[width / size][height / size];


        public static class Sand extends Grain {
            public Sand() {
                this.id = 0;
            }
        }

        public static class Water extends Grain {
            public Water() {
                this.id = 1;
            }
        }

        public static void Motion() {
            int b;
            for (int i = 0; i < world.length; i++) {
                for (int j = world[0].length - 1; j >= 0; j--) {
                    float rand = random.nextFloat();
                    // Sand
                    if (world[i][j].id == 0) {
                        if (j + 1 != world[0].length) {
                            if (world[i][j + 1].id == -1) {
                                world[i][j].id = -1;
                                world[i][j + 1].id = 0;
                            } else if (i != 0 && i != world.length - 1 && world[i - 1][j + 1].id == -1 && world[i + 1][j + 1].id == -1 && (j == 0 || world[i][j - 1].id == -1)) {
                                world[i][j].id = -1;
                                if (rand > 0.5f) {
                                    world[i - 1][j + 1].id = 0;
                                } else {
                                    world[i + 1][j + 1].id = 0;
                                }
                            } else if (i != 0 && world[i - 1][j + 1].id == -1 && (j == 0 || world[i][j - 1].id == -1)) {
                                world[i][j].id = -1;
                                world[i - 1][j + 1].id = 0;
                            } else if (i != world.length - 1 && world[i + 1][j + 1].id == -1 && (j == 0 || world[i][j - 1].id == -1)) {
                                world[i][j].id = -1;
                                world[i + 1][j + 1].id = 0;
                            }
                        }
                    }
                    // Water
                    if (world[i][j].id == 1) {
                        if (j + 1 != world[0].length) {
                            if (world[i][j + 1].id == -1) {
                                world[i][j].id = -1;
                                world[i][j + 1].id = 1;
                            } else if (i != 0 && i != world.length - 1 && world[i - 1][j + 1].id == -1 && world[i + 1][j + 1].id == -1 && (j == 0 || world[i][j - 1].id == -1)) {
                                world[i][j].id = -1;
                                if (rand > 0.5f) {
                                    world[i - 1][j + 1].id = 1;
                                } else {
                                    world[i + 1][j + 1].id = 1;
                                }
                            } else if (i != 0 && world[i - 1][j + 1].id == -1 && (j == 0 || world[i][j - 1].id == -1)) {
                                world[i][j].id = -1;
                                world[i - 1][j + 1].id = 1;
                            } else if (i != world.length - 1 && world[i + 1][j + 1].id == -1 && (j == 0 || world[i][j - 1].id == -1)) {
                                world[i][j].id = -1;
                                world[i + 1][j + 1].id = 1;
                            } else {
                                if (rand < 0.5f) {
                                    if (i != 0 && i != 1 && world[i - 2][j].id == -1 && (j == 0 || world[i][j - 1].id == -1) && rand > 0.25f) {
                                        world[i][j].id = -1;
                                        world[i - 2][j].id = 1;
                                    } else if (i != world.length - 1 && i != world.length - 2 && world[i + 2][j].id == -1 && (j == 0 || world[i][j - 1].id == -1)) {
                                        world[i][j].id = -1;
                                        world[i + 2][j].id = 1;
                                    }
                                } else {
                                    if (i != 0 && i != 1 && world[i - 1][j].id == -1 && (j == 0 || world[i][j - 1].id == -1) && rand > 0.75f) {
                                        world[i][j].id = -1;
                                        world[i - 1][j].id = 1;
                                    } else if (i != world.length - 1 && i != world.length - 2 && world[i + 1][j].id == -1 && (j == 0 || world[i][j - 1].id == -1)) {
                                        world[i][j].id = -1;
                                        world[i + 1][j].id = 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public static void render(GL2 gl) {
            for (int i = 0; i < world.length; i++) {
                for (int j = 0; j < world[0].length; j++) {
                    if (world[i][j].id == 0) {
                        gl.glColor3f(1, 1, 0);
                        gl.glBegin(GL2.GL_QUADS);
                        gl.glVertex2i(i * size, j * size);
                        gl.glVertex2i(i * size + size, j * size);
                        gl.glVertex2i(i * size + size, j * size + size);
                        gl.glVertex2i(i * size, j * size + size);
                        gl.glEnd();
                    }
                    if (world[i][j].id == 1) {
                        gl.glColor3f(0.2f, 0.2f, 0.8f);
                        gl.glBegin(GL2.GL_QUADS);
                        gl.glVertex2i(i * size, j * size);
                        gl.glVertex2i(i * size + size, j * size);
                        gl.glVertex2i(i * size + size, j * size + size);
                        gl.glVertex2i(i * size, j * size + size);
                        gl.glEnd();
                    }
                }
            }
        }

        public static void newGrain(Grain a, int ipx, int ipy) {
            int px = max(min(ipx / size * size, width - size), 0);
            int py = max(min(ipy / size * size, height - size), 0);
            if (world[px / size][py / size].id != -1) {
                return;
            }
            world[px / size][py / size] = a;
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < Grain.world.length; i++) {
            for (int j = 0; j < Grain.world[0].length; j++) {
                Grain.world[i][j] = new Grain();
            }
        }

        Renderer.init();
    }
}
