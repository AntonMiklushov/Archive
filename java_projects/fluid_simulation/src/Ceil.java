import static java.lang.Math.floor;

public class Ceil {
    private static Ceil[][] lattice = new Ceil[Main.splittingX][Main.splittingY];

    float[] vec = new float[2];
    float density;

    public static float linearInterpolation(float a, float b, float k) {
        return a + k * (b - a);
    }

    public static int mod(int a, int b) {
        return a >= 0 ? a % b : (-b * (a / b) + b + a) % b;
    }

    public static float getDensity(int x, int y) {
        return lattice[x][y].density;
    }

    public static float[] getVector(int x, int y) {
        return lattice[x][y].vec;
    }

    public Ceil() {
        this.vec[0] = 0;
        this.vec[1] = 0;
        this.density = 0;
    }

    public static void init() {
        for (int i = 0; i < Main.splittingX; i++)
            for (int j = 0; j < Main.splittingY; j++)
                lattice[i][j] = new Ceil();
        new Thread("Physics") {
            @Override
            public void run() {
                while (true) {
                    try {
                        Ceil.diffusion();
                        Ceil.advection();
                        Ceil.clearingDivergence();
                        Ceil.addFluid();
                        sleep(400 / Main.fps);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static void addFluid() {
        lattice[Main.splittingX / 2][Main.splittingY / 8].vec = new float[]{15, 100};
        lattice[Main.splittingX / 2][Main.splittingY / 8].density += 300;
        lattice[Main.splittingX / 2][Main.splittingY / 8 * 7].vec = new float[]{-35, -100};
        lattice[Main.splittingX / 2][Main.splittingY / 8 * 7].density += 300;
    }

    public static void diffusion() {
        float k = Main.diffusion * Main.deltaT;
        Ceil[][] buffer = new Ceil[Main.splittingX][Main.splittingY];
        for (int i = 0; i < Main.splittingX; i++)
            for (int j = 0; j < Main.splittingY; j++)
                buffer[i][j] = new Ceil();
        for (int r = 0; r < Main.accuracy; r++) {
            for (int i = 0; i < Main.splittingX; i++) {
                for (int j = 0; j < Main.splittingY; j++) {
                    buffer[i][j].density = (lattice[i][j].density + k * ((
                            buffer[mod(i + 1, Main.splittingX)][j].density +
                                    buffer[mod(i - 1, Main.splittingX)][j].density +
                                    buffer[i][mod(j - 1, Main.splittingY)].density +
                                    buffer[i][mod(j + 1, Main.splittingY)].density) / 4f)) / (1 + k);
                    buffer[i][j].vec[0] = (lattice[i][j].vec[0] + k * ((
                            buffer[mod(i + 1, Main.splittingX)][j].vec[0] +
                                    buffer[mod(i - 1, Main.splittingX)][j].vec[0] +
                                    buffer[i][mod(j - 1, Main.splittingY)].vec[0] +
                                    buffer[i][mod(j + 1, Main.splittingY)].vec[0]) / 4f)) / (1 + k);
                    buffer[i][j].vec[1] = (lattice[i][j].vec[1] + k * ((
                            buffer[mod(i + 1, Main.splittingX)][j].vec[1] +
                                    buffer[mod(i - 1, Main.splittingX)][j].vec[1] +
                                    buffer[i][mod(j - 1, Main.splittingY)].vec[1] +
                                    buffer[i][mod(j + 1, Main.splittingY)].vec[1]) / 4f)) / (1 + k);
                }
            }
        }
        lattice = buffer;
    }

    public static void advection() {
        Ceil[][] buffer = new Ceil[Main.splittingX][Main.splittingY];
        for (int i = 0; i < Main.splittingX; i++)
            for (int j = 0; j < Main.splittingY; j++)
                buffer[i][j] = new Ceil();
        for (int i = 0; i < Main.splittingX; i++) {
            for (int j = 0; j < Main.splittingY; j++) {
                float[] pos = {i - lattice[i][j].vec[0] * Main.deltaT,
                        j - lattice[i][j].vec[1] * Main.deltaT};
                int cx = mod((int) (floor(pos[0])), Main.splittingX);
                int cy = mod((int) (floor(pos[1])), Main.splittingY);
                float fx = pos[0] - (int) floor(pos[0]);
                float fy = pos[1] - (int) floor(pos[1]);
                float z1 = linearInterpolation(lattice[cx][cy].density,
                        lattice[mod(cx + 1, Main.splittingX)][cy].density, fx);
                float z2 = linearInterpolation(lattice[cx][mod(cy + 1, Main.splittingY)].density,
                        lattice[mod(cx + 1, Main.splittingX)][mod(cy + 1, Main.splittingY)].density, fx);
                buffer[i][j].density = linearInterpolation(z1, z2, fy);
                z1 = linearInterpolation(lattice[cx][cy].vec[0],
                        lattice[mod(cx + 1, Main.splittingX)][cy].vec[0], fx);
                z2 = linearInterpolation(lattice[cx][mod(cy + 1, Main.splittingY)].vec[0],
                        lattice[mod(cx + 1, Main.splittingX)][mod(cy + 1, Main.splittingY)].vec[0], fx);
                buffer[i][j].vec[0] = linearInterpolation(z1, z2, fy);
                z1 = linearInterpolation(lattice[cx][cy].vec[1],
                        lattice[mod(cx + 1, Main.splittingX)][cy].vec[1], fx);
                z2 = linearInterpolation(lattice[cx][mod(cy + 1, Main.splittingY)].vec[1],
                        lattice[mod(cx + 1, Main.splittingX)][mod(cy + 1, Main.splittingY)].vec[1], fx);
                buffer[i][j].vec[1] = linearInterpolation(z1, z2, fy);
            }
        }
        lattice = buffer;
    }

    public static void clearingDivergence() {
        float[][] divergences = new float[Main.splittingX][Main.splittingY];
        float[][] p = new float[Main.splittingX][Main.splittingY];
        float[][][] gradient = new float[Main.splittingX][Main.splittingY][2];
        for (int i = 0; i < Main.splittingX; i++)
            for (int j = 0; j < Main.splittingY; j++)
                p[i][j] = 0f;
        for (int i = 0; i < Main.splittingX; i++) {
            for (int j = 0; j < Main.splittingY; j++) {
                divergences[i][j] = (lattice[mod(i + 1, Main.splittingX)][j].vec[0] -
                        lattice[mod(i - 1, Main.splittingX)][j].vec[0] +
                        lattice[i][mod(j + 1, Main.splittingY)].vec[1] -
                        lattice[i][mod(j - 1, Main.splittingY)].vec[1]) / 2f;
            }
        }
        for (int r = 0; r < Main.accuracy; r++) {
            for (int i = 0; i < Main.splittingX; i++) {
                for (int j = 0; j < Main.splittingY; j++) {
                    p[i][j] = ((p[mod(i + 1, Main.splittingX)][j] +
                            p[mod(i - 1, Main.splittingX)][j] +
                            p[i][mod(j + 1, Main.splittingY)] +
                            p[i][mod(j - 1, Main.splittingY)]) - divergences[i][j]) / 4f;
                }
            }
        }
        for (int i = 0; i < Main.splittingX; i++) {
            for (int j = 0; j < Main.splittingY; j++) {
                gradient[i][j] = new float[]{
                        (p[mod(i + 1, Main.splittingX)][j] - p[mod(i - 1, Main.splittingX)][j]) / 2,
                        (p[i][mod(j + 1, Main.splittingY)] - p[i][mod(j - 1, Main.splittingY)]) / 2
                };
            }
        }
        for (int i = 0; i < Main.splittingX; i++) {
            for (int j = 0; j < Main.splittingY; j++) {
                lattice[i][j].vec[0] -= gradient[i][j][0];
                lattice[i][j].vec[1] -= gradient[i][j][1];
            }
        }
    }
}
