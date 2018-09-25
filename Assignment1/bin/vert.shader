#version 430

uniform float inc;
uniform float size;
uniform float cf;
uniform float x;
uniform float y;

out vec4 vc;
out float f;


void main(void)
{ 
	f=cf;
	
	if (gl_VertexID == 0) 
	{
		gl_Position = vec4( 0.25+x+size,-0.25+inc+y-size, 0.0, 1.0);
		vc = vec4(1.0, 0.0, 0.0, 1.0);
	}
	
  	else if (gl_VertexID == 1) 
  	{
  		gl_Position = vec4(-0.25+x-size,-0.25+inc+y-size, 0.0, 1.0);
  		vc = vec4(0.0, 1.0, 0.0, 1.0);
  	}
  	
  	else 
  	{
  		gl_Position = vec4( 0.25+x+size, 0.25+inc+y+size, 0.0, 1.0);
  		vc = vec4(0.0, 0.0, 1.0, 1.0);
  	}
  	
}