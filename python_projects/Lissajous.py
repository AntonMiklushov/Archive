import turtle; from math import *
tur = turtle.Pen(); tur.ht(); tur.speed(0)
t = 0
deltaT = 0.005
a, b, A, B = 51, 50, 4, 4
delta = 0.0
tur.right(90)
def motion():
    global t
    x = A * sin(a * t + delta)
    y = B * sin(b * t)
    tur.goto(x * 100, y * 100)
    t += deltaT
while t < (2 * 3.15) / (a - b):
    motion()
t.exitonclick()