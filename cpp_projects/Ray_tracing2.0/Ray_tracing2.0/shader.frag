#version 420

out vec4 fragColor;

// Reception
uniform float hs;
uniform float time;
uniform vec3 cp;
uniform float mmx;
uniform float mmy;
uniform float max_dist;
uniform int max_ref;
uniform vec3 rand;
uniform float ws;

// x, y, z, r, rk, luminosity, rgb
mat3 spheres[] = {mat3(vec3(0.0, 0.0, 0.0), vec3(1.0, 1.0, 0.0), vec3(1.0, 0.2, 0.2)),
					mat3(vec3(2.0, 1.0, 0.0), vec3(1.0, 1.0, 0.0), vec3(0.2, 1.0, 0.2)),
					mat3(vec3(4.0, 2.0, 0.0), vec3(1.0, 1.0, 0.0), vec3(0.2, 0.2, 1.0)),
					mat3(vec3(0.0, -15.0, 0.0), vec3(13.0, 1.0, 0.0), vec3(0.85, 0.85, 0.85)),
					mat3(vec3(-1.0, 0.2, 2.0), vec3(1.0, -0.2, 0.0), vec3(1.0, 0.5, 1.0))};

vec3 mrd;
uvec4 R_STATE;

// Random

uint TausStep(uint z, int S1, int S2, int S3, uint M)
{
	uint b = (((z << S1) ^ z) >> S2);
	return (((z & M) << S3) ^ b);	
}

uint LCGStep(uint z, uint A, uint C)
{
	return (A * z + C);	
}

float random()
{
	R_STATE.x = TausStep(R_STATE.x, 13, 19, 12, uint(4294967294));
	R_STATE.y = TausStep(R_STATE.y, 2, 25, 4, uint(4294967288));
	R_STATE.z = TausStep(R_STATE.z, 3, 11, 17, uint(4294967280));
	R_STATE.w = LCGStep(R_STATE.w, uint(1664525), uint(1013904223));
	return 2.3283064365387e-10 * float((R_STATE.x ^ R_STATE.y ^ R_STATE.z ^ R_STATE.w));
}

// Color definition
vec4 colorDef(in vec3 col)
{
	return vec4(col, 1.0);
}

// Camera rotation
vec3 camRot(in vec3 rd)
{
	vec3 b = rd;
	float alpha = -mmy/hs;
	float beta = - mmx/ws;
	mat2 ry = mat2(cos(alpha), -sin(alpha), sin(alpha), cos(alpha));
	mat2 rx = mat2(cos(beta), -sin(beta), sin(beta), cos(beta));
	b.xy *= ry;
	b.zx *= rx;
	return b;
}

// Intersections

// Sphere of size ra centered at point ce
vec2 sphereIntersection(in vec3 ro, in vec3 rd, in vec3 ce, float ra, out vec3 sphN)
{
    vec3 oc = ro - ce;
    float b = dot(oc, rd);
    float c = dot(oc, oc) - ra * ra;
    float h = b * b - c;
    if(h < 0.0) return vec2(-1.0); // no intersection
    h = sqrt(h);
    sphN = normalize(ro - (ce - rd * (-b-h)));;
    return vec2(-b-h, -b+h);
}

// Nearest intersection for each object of the same type

// Spheres intersections
vec2 nearestSphIntersection(in vec3 ro, in vec3 rd, out vec3 N, out float rk, out vec3 col, out float l)
{
	vec2 b = vec2(max_dist);
	vec2 b2 = vec2(-1.0);
	vec3 n;
	for (int i = 0; i < spheres.length(); i++){
		b2 = sphereIntersection(ro, rd, spheres[i][0], spheres[i][1][0], n);
		if (sqrt(b2.x * b2.x + b2.y * b2.y) < sqrt(b.x * b.x + b.y * b.y) && b2.x > 0.0 && b2.y > 0.0){
			b = b2;
			N = n;
			rk = spheres[i][1][1];
			col = spheres[i][2];
			l = spheres[i][1][2];
		}
	}
	return b;
}

// Neares intersection of all objects

vec2 nearestIntersection(in vec3 cp, in vec3 rd, out vec3 N, out float rk, out vec3 col, out float l)
{
	vec2 b = nearestSphIntersection(cp, rd, N, rk, col, l);
	return b;
}

// Main raytracing

vec3 rayTracing()
{
	vec3 bcp = cp;
	vec3 brd = mrd;
	vec3 bcol;
	float rk;
	float l;
	vec3 N;
	vec3 color = vec3(1.0);
	vec3 rcolor = vec3(1.0);
	int i = 0;
	vec2 b = nearestIntersection(bcp, brd, N, rk, bcol, l);
	while (i < max_ref && b.x > 0 && b.x < max_dist)
	{
		b = nearestIntersection(bcp, brd, N, rk, bcol, l);
		if (l == 0.0)
		{
			color =  color * bcol;
			if (rk == 1.0)
			{
				bcp = bcp + brd * b.x;
				brd = reflect(brd, N);
			}
			if (rk <= 0.0)
			{
				bcp += brd * (b.y + 0.0001);
				brd = refract(brd, N, 1.0/(1.0-rk));
			}
			if (rk > 0.0 && rk != 1.0)
			{
				vec3 r = vec3(random(), random(), random());
				bcp += brd * (b.y + 0.0001);
				brd = normalize(r - vec3(0.5)) * normalize(dot(N, r));
			}
		}
		else
		{
			rcolor = vec3(min((color * bcol * l).x, 1), min((color * bcol * l).y, 1), min((color * bcol * l).z, 1));
			return rcolor;
		}
	i++;
	}
	return rcolor * color;
}

// Main
void main(void)
{
	// Positions for camera's ray vectors (reflection along the y-axis)
	vec2 uv = vec2((gl_FragCoord.x / ws - 0.5) * ws / hs, (1.0 - gl_FragCoord.y / hs) - 0.5);

	R_STATE.x = uint(rand.x + uv.x);
	R_STATE.y = uint(rand.y + uv.x);
	R_STATE.z = uint(rand.z + uv.y);
	R_STATE.w = uint(rand.x + uv.y);

    // Ray vectors
    mrd = camRot(normalize(vec3(1.0, uv.y, uv.x)));
	fragColor = colorDef(rayTracing());
}