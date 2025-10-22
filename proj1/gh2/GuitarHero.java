package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    static String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarString[] strings = new GuitarString[37];

        for (int i = 0; i < 37; i++) {
            double frequency = 440.0 * Math.pow(2, (i - 24) / 12.0);
            strings[i] = new GuitarString(frequency);
        }
        while (true) {
            // Check if the user has typed a key
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);

                // If it's a valid key, pluck the corresponding string
                if (index != -1) {
                    strings[index].pluck();
                }
            }

            // Compute the superposition of samples from all strings
            double sample = 0.0;
            for (int i = 0; i < 37; i++) {
                sample += strings[i].sample();
            }

            // Play the sample on standard audio
            StdAudio.play(sample);

            // Advance the simulation of each guitar string by one step
            for (int i = 0; i < 37; i++) {
                strings[i].tic();
            }
        }
    }
}

