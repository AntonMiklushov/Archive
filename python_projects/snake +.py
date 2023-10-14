import tkinter
import random
import time

f = 0
speed = 30
s_pos = [0,0]
v = [0,1]
score = 0
head = 0
apple = 0
body = []
a_pos = [0,1]

tk = tkinter.Tk()
canv = tkinter.Canvas(tk, width = 400, height = 400)
canv.pack()

CYAN = ('#b5faff')
LIME = ('#99ffbb')
COLOR = 0
COUNT = 10
SIZE = 35
RET = 2
ROW = 10

for n in range(ROW):
    for i in range(COUNT):
        if (i + n)%2 == 0:
            COLOR = LIME
        else:
            COLOR = CYAN
        canv.create_rectangle((RET * i) + (SIZE * i) + 8, (RET * n) + (SIZE * n) + 8,
                              (RET * i) + (SIZE * i) + SIZE + 8, (RET * n) + (SIZE * n) + SIZE + 8, fill=COLOR )

m = [['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'],
     ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_']]

def r():
    global a_pos
    a_pos = [random.randint(0,9), random.randint(0,9)]
    if m[a_pos[1]][a_pos[0]] != '_' or m[a_pos[1]][a_pos[0]] == '#':
        r()

def snake_move(event):
    global snake
    if event.keysym == "Up" and v[0] != 1:
        v[0] = -1
        v[1] = 0
    if event.keysym == "Down" and v[0] != -1:
        v[0] = 1
        v[1] = 0
    if event.keysym == "Left" and v[1] != 1:
        v[1] = -1
        v[0] = 0
    if event.keysym == "Right" and v[1] != -1:
        v[1] = 1
        v[0] = 0

canv.bind_all("<KeyPress-Left>", snake_move)
canv.bind_all("<KeyPress-Right>", snake_move)
canv.bind_all("<KeyPress-Up>", snake_move)
canv.bind_all("<KeyPress-Down>", snake_move)

while True:
    if f % speed == 0:
        m[a_pos[1]][a_pos[0]] = '@'
        m[s_pos[1]][s_pos[0]] = '#'
        for n in range(len(body)):
            if len(body) > 0:
                canv.delete(body[0])
            body.pop(0)
        m[s_pos[1]][s_pos[0]] = str(score)
        s_pos[0] += v[0]
        s_pos[1] += v[1]
        if s_pos[0] > 9 or s_pos[0] < 0:
            break
        elif s_pos[1] > 9 or s_pos[1] < 0:
            break
        elif m[s_pos[1]][s_pos[0]] != '@' and m[s_pos[1]][s_pos[0]] != '#':
            if m[s_pos[1]][s_pos[0]] == '_':
                pass
            else:
                break
        if s_pos[0] == a_pos[0] and s_pos[1] == a_pos[1]:
            score += 1
            speed -= 1
            r()
        m[a_pos[1]][a_pos[0]] = '@'
        for n in range(10):
            for i in range(10):
                if m[n][i] != '_' and m[n][i] != '#' and m[n][i] != '@':
                    m[n][i] = str(int(m[n][i]) - 1)
                    if int(m[n][i]) < 0:
                        m[n][i] = '_'
        canv.delete(apple)
        apple = canv.create_rectangle((RET * a_pos[1]) + (SIZE * a_pos[1]) + 12, (RET * a_pos[0]) + (SIZE * a_pos[0]) + 12,
                                     (RET * a_pos[1]) + (SIZE * a_pos[1]) + SIZE + 4, (RET * a_pos[0]) + (SIZE * a_pos[0]) + SIZE + 4, fill="red")
        canv.delete(head)
        head = canv.create_rectangle((RET * s_pos[1]) + (SIZE * s_pos[1]) + 12, (RET * s_pos[0]) + (SIZE * s_pos[0]) + 12,
                                     (RET * s_pos[1]) + (SIZE * s_pos[1]) + SIZE + 4, (RET * s_pos[0]) + (SIZE * s_pos[0]) + SIZE + 4, fill="green")
        for n in range(10):
            for i in range(10):
                if m[n][i] != '_' and m[n][i] != '#' and m[n][i] != '@':
                    body.append(canv.create_rectangle((RET * n) + (SIZE * n) + 12, (RET * i) + (SIZE * i) + 12,
                                                      (RET * n) + (SIZE * n) + SIZE + 4, (RET * i) + (SIZE * i) + SIZE + 4, fill="black"))
        tk.update_idletasks()
        tk.update()
    f += 1
    time.sleep(0.0035)
