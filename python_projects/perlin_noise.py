import matplotlib.pyplot as plt
from random import random


def linear_interpolation(a, b, t):
    return a + t * (b - a)


def linear(k, x0, x):
    return k * (x - x0)


def smooth_step(x):
    return 6 * x ** 5 - 15 * x ** 4 + 10 * x ** 3


def perlin_noise(r, o, splitting=100):
    noise = [0 for _ in range((len(r) - 1) * splitting // 2 ** (o - 1))]
    scale = (len(r) - 1) // 2 ** (o - 1)
    for k in range(o):
        for i in range(scale * 2 ** k):
            for j in range(splitting // 2 ** k):
                t = j / (splitting // 2 ** k)
                noise[i * (splitting // 2 ** k) + j] += linear_interpolation(linear(r[i], i, t + i),
                                                                             linear(r[i + 1], i + 1, t + i),
                                                                             smooth_step(t)) / 2 ** k
    return noise


o = int(input())
plt.figure(figsize=(25, 10))
plt.ylim([-1, 1])
plt.plot(perlin_noise([random() * 2 - 1 for i in range(2 ** (o + 2) + 1)], o, 100 * o))
plt.show()
