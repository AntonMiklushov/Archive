import com.jogamp.opengl.GL2;

import static com.jogamp.opengl.math.FloatUtil.pow;
import static com.jogamp.opengl.math.FloatUtil.sqrt;

public class Particle {
    private static Particle[] particles;
    private static final float size = 1.2f;
    public float px, py;
    public float vx, vy;
    public float mass;

    public Particle(float px, float py, float vx, float vy, float mass) {
        this.px = px;
        this.py = py;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        if (particles == null) particles = new Particle[]{this};
        else {
            Particle[] buffer = new Particle[particles.length + 1];
            System.arraycopy(particles, 0, buffer, 0, particles.length);
            buffer[particles.length] = this;
            particles = buffer;
        }
    }

    public static int getCount() {
        if (particles == null) return 0;
        return particles.length;
    }

    public static void render(GL2 gl) {
        if (particles == null) return;
        for (Particle p : particles) {
            gl.glColor3f(0.9f, 0.9f, 0.0f);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(p.px - size / 2.0f, Main.height - (p.py - size / 2.0f));
            gl.glVertex2f(p.px + size / 2.0f, Main.height - (p.py - size / 2.0f));
            gl.glVertex2f(p.px + size / 2.0f, Main.height - (p.py + size / 2.0f));
            gl.glVertex2f(p.px - size / 2.0f, Main.height - (p.py + size / 2.0f));
            gl.glEnd();
        }
    }

    public static void motion() {
        if (particles == null) return;
        for (Particle p : particles) {
            p.px += p.vx * Main.deltaT;
            p.py += p.vy * Main.deltaT;
            if (p.px > Main.width || p.px < 0) p.vx *= -1;
            if (p.py > Main.height || p.py < 0) p.vy *= -1;
        }
    }

    public static void stop() {
        if (particles == null) return;
        for (Particle p : particles) {
            p.vx = 0;
            p.vy = 0;
        }
    }

    public static void forceDirection(float force, float px, float py, float power) {
        if (particles == null) return;
        for (Particle p : particles) {
            float dx = px - p.px;
            float dy = py - p.py;
            float l = sqrt(dx * dx + dy * dy);
            p.vx += dx / pow(l, 1 + power) * force / p.mass * Main.deltaT;
            p.vy += dy / pow(l, 1 + power) * force / p.mass * Main.deltaT;
        }
    }
}
