import pygame
from time import sleep
from random import randint

Size = 25
X = 1000
Y = 1000
win = pygame.display.set_mode((X, Y))
m = [[randint(0,0) for i in range(X//Size)] for k in range(Y//Size)]
play = False
c = 0
b = []

def main():
    global play
    global m
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.display.quit()
        if event.type == pygame.MOUSEBUTTONDOWN:
            pos = (pygame.mouse.get_pos())
            if m[pos[1]//Size][pos[0]//Size] == 1:
                m[pos[1]//Size][pos[0]//Size] = 0
            else:
                m[pos[1]//Size][pos[0]//Size] = 1
        if event.type == pygame.KEYDOWN:
            play = True
def rendering():
    for i in range(len(m)):
        for k in range(len(m[i])):
            if m[i][k] != 0:
                pygame.draw.rect(win, pygame.Color('green'), (k*Size, i*Size, Size, Size))
def iteration():
    global m
    global c
    global b
    b = [[0 for i in range(X//Size)] for k in range(Y//Size)]
    for i in range(len(m)-2):
        for k in range(len(m[i])-2):
            c = 0
            if i != (Y//Size)-1 and i != 0 and k != (X//Size)-1 and k != 0:
                if m[i-1][k] == 1:
                    c+=1
                if m[i+1][k] == 1:
                    c+=1
                if m[i][k+1] == 1:
                    c+=1
                if m[i][k-1] == 1:
                    c+=1
                if m[i-1][k-1] == 1:
                    c+=1
                if m[i-1][k+1] == 1:
                    c+=1
                if m[i+1][k+1] == 1:
                    c+=1
                if m[i+1][k-1] == 1:
                    c+=1
                if c < 2 or c > 3:
                    b[i][k] = 0
                elif c == 3:
                    b[i][k] = 1
                elif c == 2 and m[i][k] != 0:
                    b[i][k] = 1
    m = b
while True:
    pygame.draw.rect(win, pygame.Color('black'), (0, 0, X, Y))
    main()
    rendering()
    pygame.display.flip()
    if play == True:
        iteration()
        sleep(0.2)
