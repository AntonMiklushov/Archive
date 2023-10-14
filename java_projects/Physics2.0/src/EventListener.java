import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class EventListener implements GLEventListener {

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

        Renderer.window.setTitle("Physics Simulation" + ", particle count:" + Particle.getCount());

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        Particle.render(gl);

        if (Main.pressed) Particle.forceDirection(1000f, Main.fx / Main.screenWidth * Main.width,
                Main.height - Main.fy / Main.screenHeight * Main.height, 0.0f);
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