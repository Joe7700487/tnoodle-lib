package cs.threephase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

/**
 * Simple CLI application to demonstrate threephase solver usage.
 * Solves random 4x4x4 cube configurations using the three-phase reduction algorithm.
 * 
 * Usage:
 *   gradle :threephase:run               # Solve 1 random 4x4x4 cube
 *   gradle :threephase:run --args="N"    # Solve N random cubes (e.g., gradle :threephase:run --args="5")
 *   gradle :threephase:run --args="FACELET_STRING"  # Solve a specific cube state (96 chars)
 */
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            System.out.println("=== TNoodle Threephase 4x4x4 Solver ===");
            System.out.println("Initializing threephase solver...");
            long initStart = System.currentTimeMillis();
            Search.init();
            long initTime = System.currentTimeMillis() - initStart;
            System.out.println("Initialization complete! (" + initTime + " ms)\n");

            if (args.length == 0) {
                // Default: solve 1 random cube
                solveRandomCubes(1);
            } else if (args.length == 1) {
                // Try to parse as number of random cubes, or as a specific cube facelet string
                try {
                    int numCubes = Integer.parseInt(args[0]);
                    solveRandomCubes(numCubes);
                } catch (NumberFormatException e) {
                    // Not a number, treat as cube facelet state (must be 96 characters)
                    if (args[0].length() == 96) {
                        solveCubeState(args[0]);
                    } else {
                        System.out.println("Invalid input. Expecting either:");
                        System.out.println("  - A number (for N random cubes)");
                        System.out.println("  - A 96-character facelet string (URFDLB colors)");
                    }
                }
            } else {
                printUsage();
            }
        } catch (Exception e) {
            logger.error("Error during execution", e);
            System.exit(1);
        }
    }

    private static void solveRandomCubes(int numCubes) {
        System.out.println("Solving " + numCubes + " random 4x4x4 cube(s)...\n");
        Random random = new Random();
        
        long totalTime = 0;
        for (int i = 0; i < numCubes; i++) {
            Search searcher = new Search();
            
            // Create a random cube state
            String solution = searcher.randomState(random);
            
            // Measure just the solve time (randomState includes solving)
            long startTime = System.currentTimeMillis();
            searcher = new Search();
            solution = searcher.randomState(random);
            long elapsedTime = System.currentTimeMillis() - startTime;
            
            totalTime += elapsedTime;
            System.out.println("Cube " + (i + 1) + ": " + solution + " (" + elapsedTime + " ms)");
        }
        
        if (numCubes > 1) {
            System.out.println("\nAverage solve time: " + (totalTime / numCubes) + " ms");
        }
    }

    private static void solveCubeState(String faceletString) {
        System.out.println("Solving specific cube state (facelet notation)...\n");
        
        try {
            Search searcher = new Search();
            long startTime = System.currentTimeMillis();
            String solution = searcher.solution(faceletString);
            long elapsedTime = System.currentTimeMillis() - startTime;
            
            System.out.println("Solution: " + solution);
            System.out.println("Solve time: " + elapsedTime + " ms");
        } catch (Exception e) {
            logger.error("Error solving cube state", e);
            System.err.println("Failed to solve cube: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  gradle :threephase:run                              # Solve 1 random cube");
        System.out.println("  gradle :threephase:run --args=\"5\"                  # Solve 5 random cubes");
        System.out.println("  gradle :threephase:run --args=\"FACELET_STRING\"    # Solve specific state (96 chars)");
        System.out.println("\nFacelet notation: 96 characters using colors URFDLB (Up, Right, Front, Down, Left, Back)");
    }
}
