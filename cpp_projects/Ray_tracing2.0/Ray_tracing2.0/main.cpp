#include <SFML/Graphics.hpp>

using namespace sf;

int main()
{
	// Math pi
	float pi = 3.141592653589f;

	// Screen size in pixels
	float ws = 2304.0;
	float hs = 1296.0;

	// Max distance
	float max_dist = 999.0f;

	// Max frlection count
	int max_ref = 8;

	//Sensivity
	float mSens = 0.3f;

	float mmx = 0.0f;
	float mmy = 0.0f;

	// Camera position
	Vector3f cp = Vector3f(-5.0, 1.6, 0.0);

	// Camera vector
	Vector3f cv = Vector3f(0.0, 0.0, 0.0);

	// Create window
	RenderWindow window(VideoMode(ws, hs), "ray_tracing");
	window.setFramerateLimit(60);
	window.setMouseCursorVisible(false);

	Clock clock;

	// Create texture
	RenderTexture firstTexture;
	firstTexture.create(ws, hs);
	Sprite firstTextureSprite = Sprite(firstTexture.getTexture());

	// Create shader
	Shader shader;
	shader.loadFromFile("shader.frag", Shader::Fragment);

	// Screen sizes to shader
	shader.setUniform("ws", ws);
	shader.setUniform("hs", hs);

	shader.setUniform("max_dist", max_dist);
	shader.setUniform("max_ref", max_ref);

	// Main
	while (window.isOpen()) {

		// Events
		Event event;
		while (window.pollEvent(event))
		{
			// Closing the window
			if (event.type == Event::Closed)
			{
				window.close();
			}
			if (event.type == Event::MouseMoved)
			{
				int mx = event.mouseMove.x - static_cast<int>(ws) / 2;
				int my = event.mouseMove.y - static_cast<int>(hs) / 2;
				mmx += mx * mSens;
				mmy += my * mSens;
				Mouse::setPosition(Vector2i(static_cast<int>(ws) / 2, static_cast<int>(hs) / 2), window);
			}
			if (event.type == Event::KeyPressed)
			{
				if (event.key.code == Keyboard::Escape)
				{
					window.close();
				}
				if (event.key.code == Keyboard::Space)
				{
					cv.y = 0.05f;
				}
				if (event.key.code == Keyboard::LControl)
				{
					cv.y = -0.05f;
				}
				if (event.key.code == Keyboard::W)
				{
					cv.x = 0.05f * cos(mmx / ws);
					cv.z = 0.05f * sin(mmx / ws);
				}
				if (event.key.code == Keyboard::S)
				{
					cv.x = -0.05f * cos(mmx / ws);
					cv.z = -0.05f * sin(mmx / ws);
				}
				if (event.key.code == Keyboard::A)
				{
					cv.x = 0.05f * cos(mmx / ws - pi / 2);
					cv.z = 0.05f * sin(mmx / ws - pi / 2);
				}
				if (event.key.code == Keyboard::D)
				{
					cv.x = 0.05f * cos(mmx / ws + pi / 2);
					cv.z = 0.05f * sin(mmx / ws + pi / 2);
				}
			}
			else if (event.type == Event::KeyReleased) {
				if (event.key.code == Keyboard::Space)
				{
					cv.y = 0.0f;
				}
				if (event.key.code == Keyboard::LControl)
				{
					cv.y = 0.0f;
				}
				if (event.key.code == Keyboard::W)
				{
					cv.x = 0.0f;
					cv.z = 0.0f;
				}
				if (event.key.code == Keyboard::S)
				{
					cv.x = 0.0f;
					cv.z = 0.0f;
				}
				if (event.key.code == Keyboard::A)
				{
					cv.x = 0.0f;
					cv.z = 0.0f;
				}
				if (event.key.code == Keyboard::D)
				{
					cv.x = 0.0f;
					cv.z = 0.0f;
				}
			}
		}
		// Camera motion
		cp += cv;
		// Time in seconds to shader
		shader.setUniform("time", clock.getElapsedTime().asSeconds());
		shader.setUniform("rand", Vector3f((float)rand(), (float)rand(), (float)rand()));
		shader.setUniform("mmx", mmx);
		shader.setUniform("mmy", mmy);
		shader.setUniform("cp", cp);
		
		// Clearing and rendering
		window.clear();
		firstTexture.draw(firstTextureSprite, &shader);
		window.draw(firstTextureSprite);
		window.display();
	}
}