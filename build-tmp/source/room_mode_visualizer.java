import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.analysis.*; 
import ddf.minim.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class room_mode_visualizer extends PApplet {





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

final int _WIDTH = 1440;
final int _HEIGHT = 900;

public void setup() {
	size(_WIDTH, _HEIGHT);
	colorMode(HSB, 360, 100, 100, 100);
	fill(0);
	fontSize = 125;
	showFreq = true;

	currentTime = 0;
	prevTime = 0;
	elapsedTime = 0;
	ping = false;

	// *** TUNE AMPLITUDE RANGE HERE ***
	minAmp = 0.01f;
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

public void draw() {
	background(0);
	currentTime = millis();
	elapsedTime = PApplet.parseInt(currentTime - prevTime);

	selectBand();
	listenBand();
	visualize();
	playFreq();

	writeFreq();

	// eat cake
}

public void selectBand() {
	// change the frequency BAND of interest based on mouseY
	numBands = fft.specSize();

	bandSelected = PApplet.parseInt(map(mouseY, _HEIGHT, 0, 1, numBands));
	centerFreq = fft.indexToFreq(bandSelected); // get the center frequency from that band
	freqSelected = centerFreq; // make that our selected frequency

}

public void listenBand() {
	fft.forward(in.left);

	rawAmp = fft.getBand(bandSelected); // get amplitude of selected band

	freqList.append(rawAmp);

	if (freqList.size() > maxNumFreq) {
		calcFreq();
	}
}

public void calcFreq() {
	int numFreq = freqList.size();
	float freqTotal = 0;

	for (int i = 0; i <= numFreq; i++) {
		freqTotal = freqTotal + freqList.get(i);
	}

	println("freqTotal: "+freqTotal);
	println("numFreq: "+numFreq);

	freqAmplitude = freqTotal / numFreq;	
	freqList.clear();
}

public void visualize() {
	// don't like this mapping, it goes all the way around to red at both ends
	visualColour = PApplet.parseInt(map(freqAmplitude, minAmp, maxAmp, 325, 0));
	visualAlpha = PApplet.parseInt(map(freqAmplitude, minAmp, maxAmp, 65, 100));
	fill(0);
	strokeWeight(20);
	stroke(visualColour, visualSaturation, visualBrightness, visualAlpha);

	visualWidth = PApplet.parseInt(map(freqAmplitude, minAmp, maxAmp, 5, _HEIGHT*.9f));
	visualHeight = visualWidth;

	ellipse(_WIDTH/2, _HEIGHT/2, _HEIGHT*.9f, _HEIGHT*.9f);
}

public void playFreq() {
	if (!mute){
		wave.setAmplitude(0.5f);
		wave.setFrequency(freqSelected);
		
	} else {
		wave.setAmplitude(0);
	}

}

public void writeFreq() {
	textSize(fontSize);
	textAlign(CENTER);
	fill(235, 75);
	if (showFreq == true) {
		text(freqSelected, _WIDTH/2, _HEIGHT/2); // more just for debugging
	}
}

public void keyPressed() {
	if (key == 'f') {
		showFreq = !showFreq; // toggle our frequency display
	} else if (key == 'm') {
		mute = !mute; // toggle audio output
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "room_mode_visualizer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
