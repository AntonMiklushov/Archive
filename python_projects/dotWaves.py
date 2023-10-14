import pygame as pg
from math import sin, sqrt
from time import time

window = pg.display.set_mode((1000, 500))


def f(x, y):
    return sin(0.1*(x**2 + y**2 - time() * 10))


while True:
    window.fill(pg.Color(0, 0, 0))
    for b in range(-25, 26):
        pg.draw.lines(window, pg.Color(255, 255, 255), False, [(i + 500, f(i / 30, b/5) * 50 + 255 + b * 5) for i in range(-300 + int((1 - sqrt(1 - (b / 25)**2)) * 120), 301 - int((1 - sqrt(1 - (b / 25)**2)) * 120))])
    pg.display.flip()
