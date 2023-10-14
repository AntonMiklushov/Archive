import pygame as pg
import numpy as np

window = pg.display.set_mode((1000, 1000))
points = []


def scale(a):
    return a[0] / pg.display.get_window_size()[0] - 0.5, a[1] / pg.display.get_window_size()[1] - 0.5


def events():
    global points
    for i in pg.event.get():
        window.fill(pg.Color(100, 100, 100))
        render()
        pg.display.flip()
        if i.type == pg.QUIT:
            pg.display.quit()
        if i.type == pg.MOUSEBUTTONDOWN:
            if i.button == 1:
                points.insert(0, np.array([*scale(i.pos)]))
            if i.button == 3:
                points = []


def render():
    pg.draw.circle(window, pg.Color(200, 0, 0),
                   (pg.display.get_window_size()[0] / 2, pg.display.get_window_size()[1] / 2), 5)
    for i in points:
        pg.draw.circle(window, pg.Color(200, 0, 0),
                       ((i[0] + 0.5) * pg.display.get_window_size()[0], (i[1] + 0.5) * pg.display.get_window_size()[1]),
                       5)
    if len(points) > 0:
        t = 0.0
        dt = 0.01
        buffer = []
        for _ in range(int(1 / dt) + 1):
            buffer.append(bezier(t))
            t += dt
        for i in range(len(buffer)):
            buffer[i][0] += 0.5
            buffer[i][0] *= pg.display.get_window_size()[0]
            buffer[i][1] += 0.5
            buffer[i][1] *= pg.display.get_window_size()[1]
        pg.draw.lines(window, pg.Color(150, 150, 250), False, buffer, 3)


def factorial(n):
    if n == 1 or n == 0:
        return 1
    return n * factorial(n - 1)


def combinations(n, k):
    return factorial(n) / (factorial(k) * factorial(n - k))


def bezier(t):
    n = len(points)
    a = points[0] * b(0, n, t)
    for k in range(1, n):
        a += points[k] * b(k, n, t)
    return a


def b(k, n, t):
    return combinations(n, k) * t ** k * (1 - t) ** (n - k)


while pg.display.get_active():
    events()
else:
    print(points)
