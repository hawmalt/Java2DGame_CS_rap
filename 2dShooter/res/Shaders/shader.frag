uniform sampler2D img;
varying vec2 texcoord;

void main()
{
	vec4 texcolor = texture2D(img, texcoord);
	gl_FragColor = texcolor;
}