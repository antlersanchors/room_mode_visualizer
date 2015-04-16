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

public void setup() {
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

public void draw() {
	background(0);

	selectFreq();
	listen();
	visualize();
	writeFreq();

	// map that value to a variable

	// draw a shape or colour or something with that variable

	// eat cake
}

public void selectFreq() {
	// change the frequency of interest based on mouseY
	freqSelected = map(mouseY, _HEIGHT, 0, 20, 20000);
}

public void writeFreq() {
	// more just for debugging
	textSize(fontSize);
	textAlign(CENTER);
	fill(235);
	text(freqSelected, _WIDTH/2, _HEIGHT/2);
}

public float listen() {
	fft.forward(in.left);
	freqAmplitude = fft.getFreq(freqSelected);
	
	println("freqSelected: "+freqSelected);
	println("freqAmplitude: "+freqAmplitude);

	return(freqAmplitude);

}

public void visualize() {
	// don't like this mapping, it goes all the way around to red at both ends
	visualColour = PApplet.parseInt(map(freqSelected, 20, 20000, 0, 325));
	fill(visualColour, visualSaturation, visualBrightness, visualAlpha);

	visualWidth = PApplet.parseInt(map(freqAmplitude, 0.001f, 45.0f, 5, _WIDTH));
	visualHeight = visualWidth;
	ellipse(_WIDTH/2, _HEIGHT/2, visualWidth, visualHeight);
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
