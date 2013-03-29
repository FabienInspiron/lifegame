package jeux;

import java.util.Random;

/**
 * Provide control over the randomization of the simulation.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class Randomizer {
    // The default seed for control of randomization.
    private static final int SEED = 1111;
    // A shared Random object, if required.
    private static final Random rand = new Random(SEED);
    // Determine whether a shared random generator is to be provided.
    private static final boolean useShared = true;

    /**
     * Constructor for objects of class Randomizer
     */
    public Randomizer() {
    }

    /**
     * Provide a random generator.
     * 
     * @return A random object.
     */
    public static Random getRandom() {
        if (useShared) {
            return rand;
        } else {
            return new Random();
        }
    }

    /**
     * Reset the randomization. This will have no effect if randomization is not
     * through a shared Random generator.
     */
    public static void reset() {
        if (useShared) {
            rand.setSeed(SEED);
        }
    }
    
    /**
     * Generate a pseudo-random boolean.
     * @return pseudo-random boolean
     */
    public static boolean GenerateRandomBoolean() {
        Random random = new Random();
        if(random.nextInt()%10 == 0) return true;
        //return random.nextBoolean();
		return false;
    }

    /**
     * Generate a pseudo-random double.
     * @return pseudo-random double.
     */
    public static double GenerateRandomDouble() {
        Random random = new Random();
        return random.nextDouble();
    }
}
