#include <SFML/Graphics.hpp>

using namespace sf;

int main()
{
	unsigned int w = 2304, h = 1296;
	int maxIter = 4000;
	float minDist = 0.000001, maxDist = 100.0;

	RenderWindow window(VideoMode(w, h), "ray_tracing");
	window.setFramerateLimit(60);

	Clock clock;

	RenderTexture firstTexture;
	firstTexture.create(w, h);
	Sprite firstTextureSprite = Sprite(firstTexture.getTexture());

	Shader shader;
	shader.loadFromFile("shader.frag", Shader::Fragment);

	shader.setUniform("w", (float)w);
	shader.setUniform("h", (float)h);
	shader.setUniform("minDist", minDist);
	shader.setUniform("maxDist", maxDist);
	shader.setUniform("maxIter", maxIter);

	while (window.isOpen())
	{
		// Events
		Event event;
		while (window.pollEvent(event))
		{
			// Closing the window
			if (event.type == Event::Closed)
			{
				window.close();
			}
		}
		shader.setUniform("time", clock.getElapsedTime().asSeconds());

		window.clear();
		firstTexture.draw(firstTextureSprite, &shader);
		window.draw(firstTextureSprite);
		window.display();
	}
}