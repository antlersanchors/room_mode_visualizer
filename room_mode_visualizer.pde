import ddf.minim.analysis.*;
import ddf.minim.*;

Minim minim;
AudioInput in;
FFT fft;

int sampleRate;

int loudnessLevel;
float freqAmplitude;
int freqSelected;

final int _WIDTH = 800;
final int _HEIGHT = 800;

void setup() {
	size(_WIDTH, _HEIGHT);

	minim = new Minim(this);

	sampleRate = 44100;

	loudnessLevel = 0;
	freqAmplitude = 0;
	freqSelected = 3000;

}

void draw() {

	background(0);

	in = minim.getLineIn(Minim.MONO, 4096, sampleRate); 
	fft = new FFT(in.left.size(), sampleRate);

	fft.forward(in.left);
	println("fft: "+fft);

	// then get a specific frequency's amplitude
	freqAmplitude = fft.getFreq(freqSelected);

	println("freqAmplitude: "+freqAmplitude);

	// loudnessLevel = map(in.right.level(),0,1,0,255);
	
	// map that value to a variable

	// draw a shape or colour or something with that variable

	// eat cake
}