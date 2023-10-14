public class Main {

    public static final int width = 1920;
    public static final int height = 1080;
    public static final int fps = 60;
    public static final int size = 8;
    public static final int splittingX = width / size;
    public static final int splittingY = height / size;
    public static final float diffusion = 0.1f;
    public static final float deltaT = 0.01f;
    public static final float accuracy = 4;
    public static boolean debug = false;

    public static void main(String[] args) {
        Renderer.init();
    }
}
