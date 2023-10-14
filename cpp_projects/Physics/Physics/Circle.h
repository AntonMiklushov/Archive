#pragma once

typedef unsigned char uchar;

class Circle
{
public:
	float r;
	float pos[2];
	float mass;
	uchar color[3];
	float v[2];
	float bv[2]; // Buffer velosity

	Circle()
	{
		this->r = NULL;
		this->pos[0] = NULL;
		this->pos[1] = NULL;
		this->mass = NULL;
		this->color[0] = NULL;
		this->color[1] = NULL;
		this->color[2] = NULL;
		this->v[0] = NULL;
		this->v[1] = NULL;
		this->bv[0] = NULL;
		this->bv[1] = NULL;
	}

	Circle(float ipx, float ipy, float ir, float imass, float vx, float vy, uchar rc, uchar gc, uchar bc)
	{
		this->r = ir;
		this->pos[0] = ipx;
		this->pos[1] = ipy;
		this->mass = imass;
		this->color[0] = rc;
		this->color[1] = gc;
		this->color[2] = bc;
		this->v[0] = vx;
		this->v[1] = vy;
		this->bv[0] = this->v[0];
		this->bv[1] = this->v[1];
	}
	/*
	static void newCircle(float ipx, float ipy, float ir, float imass, float vx, float vy, uchar rc, uchar gc, uchar bc)
	{
		const int s = sizeof(circles) / sizeof(circles[0]);
		Circle b[s + 1];
		for (int i = 0; i < s; i++)
		{
			b[i] = circles[i];
			b[s] = Circle(ipx, ipy, ir, imass, vx, vy, rc, gc, bc);
		}
	}
	*/
	float distanse(Circle a)
	{
		return sqrtf(pow((this->pos[0] - a.pos[0]), 2.0f) + pow((this->pos[1] - a.pos[1]), 2.0f)) - this->r - a.r;
	}

	float* normalize(float vx, float vy)
	{
		float l = sqrtf((vx * vx) + (vy * vy));
		float b[2] = {vx / l, vy / l};
		return b;
	}

	float  dot(float v1x, float v1y, float v2x ,float v2y)
	{
		return v1x * v2x + v1y * v2y;
	}

	void motion()
	{
		this->v[0] = this->bv[0];
		this->v[1] = this->bv[1];
		this->pos[0] += this->v[0] * deltaT;
		this->pos[1] += this->v[1] * deltaT;
		if (this->pos[0] + this->r > w || this->pos[0] - this->r < 0.0f)
			this->bv[0] *= -1.0f;
		if (this->pos[1] + this->r > h || this->pos[1] - this->r < 0.0f)
			this->bv[1] *= -1.0f;
	}

	void collision(Circle a)
	{
		if ((a.pos[0] != this->pos[0] || a.pos[1] != this->pos[1]))
		{
			if (distanse(a) < 0.0)
			{
				float m1 = this->mass;
				float m2 = a.mass;
				float v1x = this->v[0];
				float v1y = this->v[1];
				float v2x = a.v[0];
				float v2y = a.v[1];
				float ux = ((2 * m2 * v2x + v1x * (m1 - m2)) / (m1 + m2));
				float uy = ((2 * m2 * v2y + v1y * (m1 - m2)) / (m1 + m2));
				this->bv[0] = ux;
				this->bv[1] = uy;
			}
		}
	}

	void gravity()
	{
		this->bv[1] -= g * deltaT;
	}
};
