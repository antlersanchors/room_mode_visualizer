import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.analysis.*; 
import ddf.minim.*; 

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
FFT fft;

int bufferSize;
int sampleRate;

float freqAmplitude;
float freqSelected;

int numBands;
int bandSelected;
float centerFreq;

float minAmp;
float maxAmp;

int visualWidth;
int visualHeight;
int visualColour;
int visualSaturation;
int visualBrightness;
int visualAlpha;

int fontSize;
boolean showFreq;

final int _WIDTH = 800;
final int _HEIGHT = 800;

public void setup() {
	size(_WIDTH, _HEIGHT);
	colorMode(HSB, 360, 100, 100, 100);
	fontSize = 125;
	showFreq = true;

	// *** TUNE AMPLITUDE RANGE HERE ***
	minAmp = 0.01f;
	maxAmp = 25;

	// *** TUNE VISUALIZATION HERE ***
	visualWidth = 50;
	visualHeight = 50;
	visualColour = 360;
	visualSaturation = 80;
	visualBrightness = 80;
	visualAlpha = 100;

	freqSelected = 3000; // what frequency are we sampling?
	freqAmplitude = 0;

	numBands = 0;
	bandSelected = 1;

	minim = new Minim(this);
	bufferSize = 256; // the number of freq bands will be this/2
	sampleRate = 44100;

	in = minim.getLineIn(Minim.MONO, bufferSize, sampleRate); 
	fft = new FFT(in.left.size(), sampleRate);

}

public void draw() {
	background(0);

	selectBand();
	listenBand();

	visualize();
	writeFreq();

	// eat cake
}

public void selectBand() {
	// change the frequency BAND of interest based on mouseY
	numBands = fft.specSize();

	bandSelected = PApplet.parseInt(map(mouseY, _HEIGHT, 0, 1, numBands));
	centerFreq = fft.indexToFreq(bandSelected); // get the center frequency from that band
	freqSelected = centerFreq; // make that our selected frequency

	println("BandWidth: "+fft.getBandWidth());
}

public void listenBand() {
	fft.forward(in.left);

	freqAmplitude = fft.getBand(bandSelected); // get amplitude of selected band
	
	println("bandSelected: "+bandSelected);
	println("freqAmplitude: "+freqAmplitude);

}

public void visualize() {
	// don't like this mapping, it goes all the way around to red at both ends
	visualColour = PApplet.parseInt(map(freqSelected, 20, 20000, 0, 325));
	fill(visualColour, visualSaturation, visualBrightness, visualAlpha);

	visualWidth = PApplet.parseInt(map(freqAmplitude, minAmp, maxAmp, 5, _WIDTH));
	visualHeight = visualWidth;

	ellipse(_WIDTH/2, _HEIGHT/2, visualWidth, visualHeight);
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
		showFreq = !showFreq;
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
