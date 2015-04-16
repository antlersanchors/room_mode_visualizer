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

int loudnessLevel;
float freqAmplitude;
int freqSelected;

final int _WIDTH = 800;
final int _HEIGHT = 800;

public void setup() {
	size(_WIDTH, _HEIGHT);

	minim = new Minim(this);

	sampleRate = 44100;

	loudnessLevel = 0;
	freqAmplitude = 0;
	freqSelected = 3000;

}

public void draw() {

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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "room_mode_visualizer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
