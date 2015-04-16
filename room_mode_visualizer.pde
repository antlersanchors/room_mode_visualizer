import ddf.minim.analysis.*;
import ddf.minim.*;

Minim minim;
AudioInput in;
FFT fft;

int sampleRate;

float freqAmplitude;
float freqSelected;

int visualWidth;
int visualHeight;

final int _WIDTH = 800;
final int _HEIGHT = 800;

void setup() {
	size(_WIDTH, _HEIGHT);

	minim = new Minim(this);
	sampleRate = 44100;

	// what frequency are we sampling?
	freqSelected = 3000;
	freqAmplitude = 0;

	in = minim.getLineIn(Minim.MONO, 4096, sampleRate); 
	fft = new FFT(in.left.size(), sampleRate);

}

void draw() {
	background(0);

	listen();

	visualize();

	// map that value to a variable

	// draw a shape or colour or something with that variable

	// eat cake
}

float listen() {
	fft.forward(in.left);
	freqAmplitude = fft.getFreq(freqSelected);
	
	println("freqAmplitude: "+freqAmplitude);

	return(freqAmplitude);

}

void visualize() {
	ellipse(_WIDTH/2, _HEIGHT/2, visualWidth, visualHeight);
}