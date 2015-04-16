import ddf.minim.analysis.*;
import ddf.minim.*;

Minim minim;
AudioInput in;

const int _WIDTH = 800;
const int _HEIGHT = 800;

void setup() {
	size(_WIDTH, _HEIGHT);

	minim = new Minim(this);

}

void draw() {

	background(0);

	in = minim.getLineIn();

	// then get a specific frequency's amplitude
	loudnessLevel = map(in.right.level(),0,1,0,255);
	
	// map that value to a variable

	// draw a shape or colour or something with that variable

	// eat cake
}