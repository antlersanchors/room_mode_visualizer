import ddf.minim.analysis.*;
import ddf.minim.*;
import ddf.minim.ugens.*;

Minim minim;
AudioInput in;
AudioOutput out;
FFT fft;
Oscil wave;

long currentTime;
long prevTime;
int elapsedTime;
boolean ping;

int bufferSize;
int sampleRate;

float freqAmplitude;
float freqSelected;

FloatList freqList;
int maxNumFreq;

int numBands;
int bandSelected;
float centerFreq;

float rawFreq;
float rawAmp;

float minAmp;
float maxAmp;

int visualWidth;
int visualHeight;
int visualColour;
int visualSaturation;
int visualBrightness;
int visualAlpha;

boolean mute;

int fontSize;
boolean showFreq;

// final int _WIDTH = 1440;
// final int _HEIGHT = 900;
final int _WIDTH = 1440;
final int _HEIGHT = 900;

void setup() {
	size(_WIDTH, _HEIGHT);
	colorMode(HSB, 360, 100, 100, 100);
	fontSize = 125;
	showFreq = true;

	currentTime = 0;
	prevTime = 0;
	elapsedTime = 0;
	ping = false;

	// *** TUNE AMPLITUDE RANGE HERE ***
	minAmp = 0.01;
	maxAmp = 25;

	// *** TUNE VISUALIZATION HERE ***
	visualWidth = 50;
	visualHeight = 50;
	visualColour = 360;
	visualSaturation = 90;
	visualBrightness = 100;
	visualAlpha = 100;

	freqSelected = 600; // what frequency are we sampling?
	freqAmplitude = 0;

	numBands = 0;
	bandSelected = 1;

	minim = new Minim(this);
	bufferSize = 256; // the number of freq bands will be this/2
	sampleRate = 44100;

	mute = true;

	in = minim.getLineIn(Minim.MONO, bufferSize, sampleRate); 
	fft = new FFT(in.left.size(), sampleRate);
	out = minim.getLineOut();

	wave = new Oscil(freqSelected, 0.5f, Waves.SINE);
	wave.patch(out);

	freqList = new FloatList();
	maxNumFreq = 25;

}

void draw() {
	background(0);
	currentTime = millis();
	elapsedTime = int(currentTime - prevTime);

	selectBand();
	listenBand();

	if ( elapsedTime >= 2000 && elapsedTime <= 5000 ) {
		ping = true;
		visualize();
		playFreq();
	
	} else if (elapsedTime > 5000 ) {
		ping = false;
		prevTime = millis();
	}

	writeFreq();

	// eat cake
}

void selectBand() {
	// change the frequency BAND of interest based on mouseY
	numBands = fft.specSize();

	bandSelected = int(map(mouseY, _HEIGHT, 0, 1, numBands));
	centerFreq = fft.indexToFreq(bandSelected); // get the center frequency from that band
	freqSelected = centerFreq; // make that our selected frequency

	// println("BandWidth: "+fft.getBandWidth());
}

void listenBand() {
	fft.forward(in.left);

	rawAmp = fft.getBand(bandSelected); // get amplitude of selected band

	freqList.append(rawAmp);

	if (freqList.size() > maxNumFreq) {
		calcFreq();
	}
	
	// println("bandSelected: "+bandSelected);
	// println("freqAmplitude: "+freqAmplitude);

}

void calcFreq() {
	int numFreq = freqList.size();
	float freqTotal = 0;

	for (int i = 0; i < numFreq-1; i++) {
		freqTotal = freqTotal + freqList.get(i);
	}

	println("freqTotal: "+freqTotal);
	println("numFreq: "+numFreq);

	freqAmplitude = freqTotal / numFreq+1;	
	freqList.clear();
}

void visualize() {
	// don't like this mapping, it goes all the way around to red at both ends
	visualColour = int(map(freqSelected, 20, 20000, 0, 325));
	visualAlpha = int(map(freqAmplitude, minAmp, minAmp, 70, 100));
	fill(visualColour, visualSaturation, visualBrightness, visualAlpha);

	visualWidth = int(map(freqAmplitude, minAmp, maxAmp, 5, _WIDTH));
	visualHeight = visualWidth;

	ellipse(_WIDTH/2, _HEIGHT/2, visualWidth, visualHeight);
}

void playFreq() {
	if (!mute && ping){
		wave.setAmplitude(0.5);
		wave.setFrequency(freqSelected);
	} else {
		wave.setAmplitude(0);
	}
}

void writeFreq() {
	textSize(fontSize);
	textAlign(CENTER);
	fill(235, 75);
	if (showFreq == true) {
		text(freqSelected, _WIDTH/2, _HEIGHT/2); // more just for debugging
	}
}

void keyPressed() {
	if (key == 'f') {
		showFreq = !showFreq; // toggle our frequency display
	} else if (key == 'm') {
		mute = !mute; // toggle audio output
	}
}