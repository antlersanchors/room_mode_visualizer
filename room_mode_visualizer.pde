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
int visualColour;
int visualSaturation;
int visualBrightness;
int visualAlpha;

int fontSize;

final int _WIDTH = 800;
final int _HEIGHT = 800;

void setup() {
	size(_WIDTH, _HEIGHT);

	colorMode(HSB, 360, 100, 100, 100);

	minim = new Minim(this);
	sampleRate = 44100;

	// what frequency are we sampling?
	freqSelected = 3000;
	freqAmplitude = 0;

	visualWidth = 50;
	visualHeight = 50;
	visualColour = 360;
	visualSaturation = 80;
	visualBrightness = 80;
	visualAlpha = 100;

	fontSize = 125;

	in = minim.getLineIn(Minim.MONO, 4096, sampleRate); 
	fft = new FFT(in.left.size(), sampleRate);

}

void draw() {
	background(0);

	selectFreq();
	listen();
	visualize();
	writeFreq();

	// map that value to a variable

	// draw a shape or colour or something with that variable

	// eat cake
}

void selectFreq() {
	// change the frequency of interest based on mouseY
	freqSelected = map(mouseY, _HEIGHT, 0, 20, 20000);
}

void writeFreq() {
	// more just for debugging
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
	// don't like this mapping, it goes all the way around to red at both ends
	visualColour = int(map(freqSelected, 20, 20000, 0, 325));
	fill(visualColour, visualSaturation, visualBrightness, visualAlpha);

	visualWidth = int(map(freqAmplitude, 0.001, 45.0, 5, _WIDTH));
	visualHeight = visualWidth;
	ellipse(_WIDTH/2, _HEIGHT/2, visualWidth, visualHeight);
}