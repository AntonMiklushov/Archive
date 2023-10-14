public class Main {
    public static final int width = 1920;
    public static final int height = 1080;

    public static final int screenWidth = 1920;
    public static final int screenHeight = 1080;

    public static final int fps = 100;

    public static final float deltaT = 0.01f;

    public static float fx, fy;
    public static boolean pressed = false;

    public static void main(String[] args) {
        Renderer.init();
        for (int j = 0; j < 300; j++) {
            for (int i = 0; i < 450; i++) new Particle(j + width / 2f, i + height / 2f, 0, 0, 1f);
        }
        new Thread("Motion") {
            public void run() {
                while (true) {
                    Particle.motion();
                }
            }
        }.start();
    }
}
