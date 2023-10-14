import pygame as pg
import numpy as np

pg.font.init()

WIDTH = 2000
HEIGHT = 1000
WINDOW = pg.display.set_mode((WIDTH, HEIGHT))
CLOCK = pg.time.Clock()
font = pg.font.Font(None, 36)
point = np.array([15, 50], float)
DELTA_T = 0.012
ALPHA = 2.6
BETA = 2.6 / 50
GAMMA = 1
DELTA = 0.02
mem_x = [np.array([WIDTH, point[0] * 10])]
mem_y = [np.array([WIDTH, point[1] * 10])]


def motion():
    global point
    point += get_derivative(*point) * DELTA_T


def get_derivative(x, y):
    return np.array([x * (ALPHA - BETA * y), y * (DELTA * x - GAMMA)])


def render():
    WINDOW.fill(pg.Color(30, 30, 30))
    pg.draw.circle(WINDOW, pg.Color(255, 0, 0), (point[0] * 10, HEIGHT - point[1] * 10), 3)
    WINDOW.blit(font.render(f'{int(point[0])}', True, (150, 0, 0)),
                (point[0] * 10, HEIGHT - point[1] * 10))
    WINDOW.blit(font.render(f'{int(point[1])}', True, (0, 150, 0)),
                (point[0] * 10 + 40, HEIGHT - point[1] * 10))
    mem_x.append(np.array([WIDTH, HEIGHT - point[0] * 10]))
    mem_y.append(np.array([WIDTH, HEIGHT - point[1] * 10]))
    pg.draw.lines(WINDOW, pg.Color(0, 150, 0), False, mem_x, 2)
    pg.draw.lines(WINDOW, pg.Color(150, 0, 0), False, mem_y, 2)
    for i in range(len(mem_x)):
        mem_x[i][0] -= 1
        mem_y[i][0] -= 1
    if len(mem_x) >= WIDTH // 2:
        mem_x.pop(0)
        mem_y.pop(0)
    pg.display.flip()


def quit_check():
    for event in pg.event.get():
        if event.type == pg.QUIT:
            pg.display.quit()
            exit()


if __name__ == '__main__':
    while True:
        render()
        quit_check()
        motion()
        CLOCK.tick(60)
