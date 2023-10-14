#include <SFML/Graphics.hpp>
#include <iostream>
#include <chrono>
#include "settings.h"
#include "Circle.h"
#include "Timer.h"

using namespace sf;
typedef unsigned int uint;

RenderWindow window(VideoMode(w, h), "Physics simulation");
CircleShape circle;

Circle circles[] = {
Circle(250, 500, 10, 70, 40, 0, 100, 255, 255),
Circle(750, 500, 30, 30, 0, 0, 255, 100, 255),
Circle(500, 800, 50, 20, 0, 0, 255, 255, 100),
Circle(500, 100, 90, 100, 100, 0, 255, 255, 100)
};

long long frame = 0;

void drawCircles()
{
	for (int i = 0; i < sizeof(circles) / sizeof(circles[0]); i++)
	{
		circle.setPosition((int)((circles[i].pos[0] - circles[i].r) * 10.0f) * 0.1f,
						   (int)((h - circles[i].pos[1] - circles[i].r) * 10.0f) * 0.1f);
		circle.setRadius(circles[i].r);
		circle.setFillColor(Color(circles[i].color[0], circles[i].color[1], circles[i].color[2]));
		window.draw(circle);
	}
}

void physics()
{
	for (int i = 0; i < sizeof(circles) / sizeof(circles[0]); i++)
	{
		for (int j = 0; j < sizeof(circles) / sizeof(circles[0]); j++)
		{
			circles[i].collision(circles[j]);
		}
		circles[i].gravity();
	}
	for (int i = 0; i < sizeof(circles) / sizeof(circles[0]); i++)
	{
		circles[i].motion();
	}
}

int main()
{
	Timer t;
	Timer t1;
	Timer t2;
	float physInterval = 1.0f / ips;
	float fInterval = 1.0f / fps;

	while (true)
	{
		if (t1.elapsed() >= physInterval)
		{
			// Engine
			physics();
			
			t1.reset();
		}

		if (t2.elapsed() >= fInterval)
		{
			// Rendering
			window.clear();
			drawCircles();
			window.display();

			t2.reset();
		}
		if (t.elapsed() >= 1.0)
		{
			std::cout << (long long)(frame / t.elapsed()) << "\n";
			t.reset();
			frame = 0;
		}
		frame++;
	}

	return 0;
}
