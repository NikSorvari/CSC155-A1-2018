#version 430

in vec4 vc;
in float f;

out vec4 color;
void main(void)
{
	
	if (f == 0) {
		color = vec4(0.0, 0.0, 1.0, 1.0);
	} else {
		color = vc;
}
}