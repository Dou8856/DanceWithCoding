#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

vec4 sky()
{
    float y = gl_FragCoord.y/resolution.y;        
    return vec4(1.0-y*0.5,1.0,1.0,1.0);
}

vec4 petal(vec2 center, vec2 myFragCoord)
{
    float r = distance(myFragCoord,center);
    float a = atan(myFragCoord.y-center.y,myFragCoord.x-center.x);
    float a_wave = sin(a+time);
    float f = pow(sin(a*10.0+time),1.0)*(1.0-r);
    vec3 cx = vec3(f*1.5,f*2.0,f);
    if( f<0.5){
        cx = cx + 1.3;
    }
    else cx = 1.5 - cx;

    cx = cx + 1.0;
    if(cx.x>1.0&&cx.y>1.0&&cx.z>1.0) cx=vec3(0.0,0.0,0.0);

    return vec4( cx, 1.0 );
}


void main( void ) {

    vec2 myMouse =  mouse ;
    myMouse.x *= resolution.x/resolution.y;
    vec2 myFragCoord = gl_FragCoord.xy;
    myFragCoord.x = gl_FragCoord.x/resolution.y;
    myFragCoord.y = gl_FragCoord.y/resolution.y;
    vec4 color = vec4(0.0,0.0,0.0,1.0);
    for(float i=1.0; i<3.0; i++){
        color+= petal(myMouse+vec2(0.2*sin(time/5.0+i*30.0), 0.3*cos(time/5.0+i*60.0)), myFragCoord);
    }

    if( color.r<=0.0 && color.g<=0.0 && color.b<=0.0 ) color = sky();
    else if(color.r>=1.0 && color.g>=1.0 && color.b>=0.0) color = color/2.0;
    gl_FragColor = color;
}

