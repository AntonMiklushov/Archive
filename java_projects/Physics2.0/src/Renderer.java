import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Renderer {
    public static GLWindow window;

    public static void init() {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.setTitle("Physics Simulation");
        window.setSize(Main.screenWidth, Main.screenHeight);
        window.setResizable(false);
        window.setVisible(true);
        window.addGLEventListener(new EventListener());
        window.addMouseListener(new MouseListener());
        FPSAnimator animator = new FPSAnimator(window, Main.fps);
        animator.start();
    }
}
