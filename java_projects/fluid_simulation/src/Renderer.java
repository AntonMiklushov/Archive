import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;

import static com.jogamp.opengl.math.FloatUtil.sqrt;

public class Renderer {
    public static void init() {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        GLWindow window = GLWindow.create(caps);
        window.setTitle("Fluid");
        window.setSize(Main.width, Main.height);
        window.setResizable(false);
        window.setVisible(true);
        window.addGLEventListener(new EventListener());
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() == ' ')
                    Main.debug = !Main.debug;
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });
        FPSAnimator animator = new FPSAnimator(window, Main.fps);
        animator.start();
    }

    public static void drawDensity(GL2 gl) {
        for (int i = 0; i < Main.splittingX; i++) {
            for (int j = 0; j < Main.splittingY; j++) {
                float d = Ceil.getDensity(i, j) / 255f;
                if (Main.debug) {
                    gl.glColor3f(d, d, d);
                } else {
                    float[] vec = Ceil.getVector(i, j);
                    float l = sqrt(vec[0] * vec[0] + vec[1] * vec[1]);
                    float b = Math.min(d, 0.75f);
                    gl.glColor3f(b + l / 120f * b, b, Math.max(b, d + (0.25f - l / 120f) * b));
                }
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex2i(Main.size * i, j * Main.size);
                gl.glVertex2i(Main.size * i + Main.size, j * Main.size);
                gl.glVertex2i(Main.size * i + Main.size, j * Main.size + Main.size);
                gl.glVertex2i(Main.size * i, j * Main.size + Main.size);
                gl.glEnd();
            }
        }
    }

    public static void drawVectors(GL2 gl) {
        for (int i = 0; i < Main.splittingX; i++) {
            for (int j = 0; j < Main.splittingY; j++) {
                float[] vec = Ceil.getVector(i, j);
                float l = sqrt(vec[0] * vec[0] + vec[1] * vec[1]);
                gl.glColor3f(l / 10f, 1f - l / 50f, 0f);
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex2f(i * Main.size + Main.size / 2f, j * Main.size + Main.size / 2f);
                gl.glVertex2f(i * Main.size + Main.size / 2f + vec[0] / l * Main.size / 2,
                        j * Main.size + vec[1] / l * Main.size / 2 + Main.size / 2f);
                gl.glVertex2f(i * Main.size + vec[0] / l * Main.size / 2 - vec[1] / l + Main.size / 2f,
                        j * Main.size + vec[1] / l * Main.size / 2 + vec[0] / l + Main.size / 2f);
                gl.glVertex2f(i * Main.size - vec[1] / l + Main.size / 2f,
                        j * Main.size + vec[0] / l + Main.size / 2f);
                gl.glEnd();
            }
        }
    }

    public static class EventListener implements GLEventListener {

        @Override
        public void init(GLAutoDrawable glAutoDrawable) {
            Ceil.init();
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {
            System.exit(0);
        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
            GL2 gl = glAutoDrawable.getGL().getGL2();

            Renderer.drawDensity(gl);
            if (Main.debug)
                Renderer.drawVectors(gl);
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(0, Main.width, Main.height, 0, -1, 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
        }
    }
}
