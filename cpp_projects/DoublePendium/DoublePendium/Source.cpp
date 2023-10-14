#include <SFML/Graphics.hpp>

using namespace sf;

int main()
{
	double 
		pi = 3.141592653589,
		g = 9.807,
		l1 = 25,
		l2 = 10,
		theta1 = pi,
		theta2 = pi + 0.0000001,
		v1 = 0,
		v2 = 0,
		m1 = 3.0,
		m2 = 1,
		px = 0,
		py = 0,
		alpha1, alpha2,
		dt = 0.001;


	RenderWindow window(VideoMode(1000, 1000), "Double Pendulum");
	CircleShape cir1(10);
	cir1.setFillColor(Color::Black);
	CircleShape cir2(10 * m1);
	cir2.setFillColor(Color::Black);
	CircleShape cir3(10 * m2);
	cir3.setFillColor(Color::Black);


	while (window.isOpen())
	{
		alpha1 = (-g * (2 * m1 + m2) * sin(theta1) - g * m2 * sin(theta1 - 2 * theta2) - 2 * m2 * sin(theta1 - theta2) * (v2 * v2 * l2 + v1 * v1 * l1 * cos(theta1 - theta2))) / (l1 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2)));
		alpha2 = (2 * sin(theta1 - theta2)) * (v1 * v1 * l1 * (m1 + m2) + g * (m1 + m2) * cos(theta1) + v2 * v2 * l2 * m2 * cos(theta1 - theta2)) / l2 / (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2));

		//time is sped up 10 times for better effect
		v1 += dt * alpha1;
		v2 += dt * alpha2;
		theta1 += dt * v1;
		theta2 += dt * v2;
		cir1.setPosition(px + 500 - 10 / 2, py + 500 - 10 / 2);
		cir2.setPosition(px + 500 + sin(theta1) * l1 * 10 - 10 * m1 / 2 - 10 * m1 / 2, py + 500 + cos(theta1) * l1 * 10 - 10 * m1 / 2 - 10 * m1 / 2);
		cir3.setPosition(px + 500 + sin(theta1) * l1 * 10 + sin(theta2) * l2 * 10 - 10 * m2 / 2 - 10 * m2 / 2, py + 500 + cos(theta1) * l1 * 10 + cos(theta2) * l2 * 10 - 10 * m2 / 2 - 10 * m2 / 2);
		Event event;
		while (window.pollEvent(event))
		{
			if (event.type == Event::Closed)
			{
				window.close();
			}
			if (event.type == Event::KeyPressed)
			{
				if (event.key.code == Keyboard::Escape) 
				{
					window.close();
				}
			}
		}
		window.clear(Color(255, 255, 255));
		window.draw(cir1);
		window.draw(cir2);
		window.draw(cir3);
		window.display();
	}
}