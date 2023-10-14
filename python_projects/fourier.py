from math import *
import matplotlib.pyplot as plt

a0 = 0
an = []
bn = []
iterations = 30


def func(x):
    return 1/(1+e**(-x))


def calculate_a0(f):
    global a0
    d = 2 * pi / 100
    s = 0
    for i in range(1, 101):
        t = i / 100
        s += f((1 - t) * -pi + t * pi)
    a0 = s * d / pi


def calculate_an(f):
    global an
    d = 2 * pi / 100
    for n in range(1, iterations + 1):
        s = 0
        for i in range(1, 101):
            t = i / 100
            s += f((1 - t) * -pi + t * pi) * cos(((1 - t) * -pi + t * pi) * n)
        an.append(s * d / pi)


def calculate_bn(f):
    global bn
    d = 2 * pi / 100
    for n in range(1, iterations + 1):
        s = 0
        for i in range(1, 101):
            t = i / 100
            s += f((1 - t) * -pi + t * pi) * sin(((1 - t) * -pi + t * pi) * n)
        bn.append(s * d / pi)


def fourier(x):
    y = a0 / 2
    for n in range(iterations):
        y += an[n] * cos((n + 1) * x) + bn[n] * sin((n + 1) * x)
    return y


calculate_a0(func)
calculate_an(func)
calculate_bn(func)
plt.figure(figsize=(10, 5))
plt.plot([i / 10 for i in range(-100, 101)], [func(i / 10) for i in range(-100, 101)])
plt.plot([i / 10 for i in range(-100, 101)], [fourier(i / 10) for i in range(-100, 101)])
plt.show()
