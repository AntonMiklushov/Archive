float[][] deps = new float[][]{{-3.5, 15, -17.5, 21.5}, {17, 20, -12.5, -7.5}, {-25, 17, -22.5, -12.5}, {-12.5, 5, -15, 5}};
float[] vl = new float[]{3, 7, 5, 5};
float[] dl = new float[]{20, 40, 20, 20};
final int[][] colors = new int[][]{{255, 0, 0}, {0, 255, 0}, {255, 255, 255}, {0, 0, 255}};
Point[] points;


final float radius = 3;
final int sw = 500;
final int sh = 500;


class Point
{
  private int type;
  private float[] pos, vel;
  Point(int type)
  {
    this.type = type;
    this.pos = new float[]{random(sw), random(sh)};
    this.vel = new float[]{0, 0};
    if (points != null)
    {
      Point[] buffer = new Point[points.length + 1];
      for (int i = 0; i < points.length; i++) buffer[i] = points[i];
      buffer[points.length] = this;
      points = buffer;
      return;
    }
    points = new Point[]{this};
  }
  
  public int type()
  {
    return this.type;
  }
  
  public void render()
  {
    int[] c = colors[this.type];
    fill(c[0], c[1], c[2]);
    ellipse(this.pos[0], this.pos[1], radius * 2, radius * 2);
  }
  
  public void force(Point other)
  {
    float dx = other.pos[0] - pos[0];
    float dy = other.pos[1] - pos[1];
    //dx = abs(dx) < (sw - other.pos[0] + pos[0]) ? dx : ((sw - other.pos[0] + pos[0]) * dx > 0 ? -1 : 1);
    //dy = abs(dy) < (sh - other.pos[1] + pos[1]) ? dy : ((sh - other.pos[1] + pos[1]) * dy > 0 ? -1 : 1);
    float d = sqrt(sq(dx) + sq(dy));
    if (d < dl[type()])
    {
      vel[0] += deps[type()][other.type()] / d * (dx / abs(dx));
      vel[1] += deps[type()][other.type()] / d * (dy / abs(dy));
      vel[0] = vel[0] > 0 ? min(vel[0], vl[type()]) : max(vel[0], -vl[type()]);
      vel[1] = vel[1] > 0 ? min(vel[1], vl[type()]) : max(vel[1], -vl[type()]);
    }
  }
  
  public void move()
  {
    pos[0] += vel[0];
    pos[1] += vel[1];
    if (pos[0] < 0)
    {
      pos[0] = 0;
      vel[0] *= -1;
    }
    else if (pos[0] > sw)
    {
      pos[0] = sw;
      vel[0] *= -1;
    }
    if (pos[1] < 0)
    {
      pos[1] = 0;
      vel[1] *= -1;
    }
    else if (pos[1] > sh)
    {
      pos[1] = sh;
      vel[1] *= -1;
    }
  }
}

public void render()
{
  for (Point p : points) p.render();
}

public void calcPhys()
{
  for (int i = 0; i < points.length; i++)
    for (int j = 0; j < points.length; j++)
      if (i != j) points[i].force(points[j]);
  for (Point p : points) p.move();
}

void setup()
{
  noStroke();
  size(500, 500);
  textSize(28);
  for (int i = 0; i < 1000; i++) new Point(0);
  for (int i = 0; i < 100; i++) new Point(1);
  for (int i = 0; i < 100; i++) new Point(2);
  for (int i = 0; i < 100; i++) new Point(3);
}

void draw()
{
  background(0);
  render();
  calcPhys();
  fill(255);
  text(frameRate, 0, 30);
}
