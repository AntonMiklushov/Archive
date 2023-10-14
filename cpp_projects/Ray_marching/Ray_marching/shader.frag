#version 420

out vec4 fragColor;

uniform float w, h, minDist, maxDist;
uniform int maxIter;
uniform float time;

vec3 cp = vec3(-2.5, 0.0, 0.0);
vec3 rd;

float distanceToMandelbulb(in vec3 p1, in vec3 p2)
{
	vec3 z = p1;
	float dr = 1.0;
	float r = 0.0;
	float Power = 8;
	int Iterations = 40;
	float Bailout = 10;
	for (int i = 0; i < Iterations ; i++) {
		r = length(z);
		if (r>Bailout) break;
		
		// convert to polar coordinates
		float theta = acos(z.z/r);
		float phi = atan(z.y,z.x);
		dr =  pow( r, Power-1.0)*Power*dr + 1.0;
		
		// scale and rotate the point
		float zr = pow( r,Power);
		theta = theta*Power;
		phi = phi*Power;
		
		// convert back to cartesian coordinates
		z = zr*vec3(sin(theta)*cos(phi), sin(phi)*sin(theta), cos(theta));
		z+=p1;
	}
	return 0.5*log(r)*r/dr;
}

float rayMarching()
{
	int i = 0;
	float d = 0;
	while (i < maxIter && d < maxDist)
	{
		d = distanceToMandelbulb(cp + rd, vec3(3.0, 0.0, 0.0));
		rd += normalize(rd) * d;
		if (d < minDist)
		{
			return distance(vec3(0.0), rd);
		}
		i++;
	}
	return -1.0;
}

void main(void)
{
	vec2 uv = vec2((gl_FragCoord.x / w - 0.5) * w / h, (1.0 - gl_FragCoord.y / h) - 0.5);
	rd = normalize(vec3(1.0, uv));
	fragColor = vec4(0.2);
	if (rayMarching() > 0) fragColor = vec4(vec3(pow(1.0/rayMarching(), 1.5)), 1.0); else fragColor = vec4(0.0);
}
