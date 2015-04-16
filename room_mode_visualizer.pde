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

int fontSize;

final int _WIDTH = 800;
final int _HEIGHT = 800;

void setup() {
	size(_WIDTH, _HEIGHT);

	minim = new Minim(this);
	sampleRate = 44100;

	// what frequency are we sampling?
	freqSelected = 3000;
	freqAmplitude = 0;

	fontSize = 125;

	in = minim.getLineIn(Minim.MONO, 4096, sampleRate); 
	fft = new FFT(in.left.size(), sampleRate);

}

void draw() {
	background(0);

	selectFreq();
	writeFreq();
	listen();
	visualize();

	// map that value to a variable

	// draw a shape or colour or something with that variable

	// eat cake
}

void selectFreq() {
	freqSelected = map(mouseY, _HEIGHT, 0, 20, 20000);
}

void writeFreq() {
	textSize(fontSize);
	textAlign(CENTER);
	fill(235);
	text(freqSelected, _WIDTH/2, _HEIGHT/2);
}

float listen() {
	fft.forward(in.left);
	freqAmplitude = fft.getFreq(freqSelected);
	
	println("freqSelected: "+freqSelected);
	println("freqAmplitude: "+freqAmplitude);

	return(freqAmplitude);

}

void visualize() {


	ellipse(_WIDTH/2, _HEIGHT/2, visualWidth, visualHeight);
}