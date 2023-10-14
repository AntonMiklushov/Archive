import tkinter
from math import *

tk = tkinter.Tk()
canv = tkinter.Canvas(tk, width = 400, height = 400)
canv.pack()

m = [['#','#','#','#','#','#','#','#','#','#','#','#'],
     ['#','.','.','.','.','.','.','.','.','.','.','#'],
     ['#','#','.','#','#','.','#','#','#','#','.','#'],
     ['#','#','.','.','#','.','#','.','.','#','.','#'],
     ['#','#','.','.','#','.','.','.','.','#','#','#'],
     ['#','#','#','.','#','#','#','.','.','.','.','#'],
     ['#','.','.','.','#','.','#','.','#','#','.','#'],
     ['#','.','#','#','#','.','.','.','#','.','.','#'],
     ['#','.','#','#','#','#','#','#','#','.','#','#'],
     ['#','.','.','.','.','.','.','.','.','.','.','#'],
     ['#','.','.','.','.','.','.','.','.','.','.','#'],
     ['#','#','#','#','#','#','#','#','#','#','#','#']]
FOV = pi/3
Ang = 0
Pos = (15 + 15j)
RayC = 400
Step = 0.2
V = 5
D = 1
x = 0
a = []

def Cam_move(event):
    global Pos
    global Ang
    if event.keysym == "Up":
        Pos += V*(cos(Ang) + sin(Ang)*(0+1j))
        raycast()
    if event.keysym == "Down":
        Pos -= V*(cos(Ang) + sin(Ang)*(0+1j))
        raycast()
    if event.keysym == "Left":
        Ang += pi/12
        raycast()
    if event.keysym == "Right":
        Ang -= pi/12
        raycast()

canv.bind_all("<KeyPress-Left>", Cam_move)
canv.bind_all("<KeyPress-Right>", Cam_move)
canv.bind_all("<KeyPress-Up>", Cam_move)
canv.bind_all("<KeyPress-Down>", Cam_move)

def raycast():
    for i in range(len(a)):
        canv.delete(a[i])
    for i in range(RayC):
        alpha = Ang + FOV/2 - (FOV/RayC)/2 - (FOV/RayC)*(i-1)
        V1 = Step*(cos(alpha) + sin(alpha)*(0+1j))
        V2 = Pos
        while m[int(floor(V2.real)/10)][int(floor(V2.imag)/10)] != '#':
            V2 += V1
        h = 400*(D/(abs(Pos-V2)*(cos(Ang - alpha))))
        x = i*(400/RayC)
        a.append(canv.create_rectangle(x, 200+h, x+400/RayC, 200-h, fill = 'black'))

raycast()
