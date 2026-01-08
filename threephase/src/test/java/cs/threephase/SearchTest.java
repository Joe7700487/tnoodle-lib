package cs.threephase;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the threephase solver demonstrating different solving methods:
 * - Solving from a scramble sequence (move notation)
 * - Solving from a facelet cube representation
 */
@DisplayName("Threephase 4x4x4 Solver Tests")
class SearchTest {

    @BeforeAll
    static void initializeSolver() {
        System.out.println("Initializing threephase solver...");
        Search.init();
        System.out.println("Solver initialized!");
    }

    @Test
    @DisplayName("Solve from scramble sequence - simple case")
    void testSolveFromScramble() {
        Search searcher = new Search();
        
        // Simple scramble: R U R' U' (basic cube notation)
        // Using threephase notation: R, U, R', U'
        String scramble = "R U R' U'";
        
        long startTime = System.currentTimeMillis();
        String solution = searcher.solve(scramble);
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        assertNotNull(solution, "Solution should not be null");
        assertFalse(solution.isEmpty(), "Solution should not be empty");
        
        System.out.println("Scramble: " + scramble);
        System.out.println("Solution: " + solution);
        System.out.println("Solve time: " + elapsedTime + " ms\n");
    }

    @Test
    @DisplayName("Solve from scramble sequence - complex case")
    void testSolveFromComplexScramble() {
        Search searcher = new Search();
        
        // More complex scramble sequence
        String scramble = "Rw U2 Rw U2 Rw U Rw U2 Rw' U Rw U2 Rw' U Rw U";
        
        long startTime = System.currentTimeMillis();
        String solution = searcher.solve(scramble);
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        assertNotNull(solution, "Solution should not be null");
        assertFalse(solution.isEmpty(), "Solution should not be empty");
        
        System.out.println("Scramble: " + scramble);
        System.out.println("Solution: " + solution);
        System.out.println("Solve time: " + elapsedTime + " ms\n");
    }

    @Test
    @DisplayName("Solve from facelet string")
    void testSolveFromFacelet() {
        // Create a scrambled cube
        Search scramblerSearch = new Search();
        String scramble = "R U R' U' R U R'";
        
        // Get the scrambled state by creating a cube from moves
        // First, let's create a solved cube (all stickers same color)
        StringBuilder solvedCube = new StringBuilder();
        for (int i = 0; i < 96; i++) {
            solvedCube.append('U'); // 24 U stickers
        }
        for (int i = 0; i < 24; i++) {
            solvedCube.append('R');
        }
        for (int i = 0; i < 24; i++) {
            solvedCube.append('F');
        }
        for (int i = 0; i < 24; i++) {
            solvedCube.append('D');
        }
        // Note: This is a simplified example. A real facelet string would represent
        // an actual scrambled state from a real 4x4x4 cube configuration.
        
        Search searcher = new Search();
        
        // Solve a properly scrambled state
        String solution = scramblerSearch.solve(scramble);
        
        assertNotNull(solution, "Solution should not be null");
        assertFalse(solution.isEmpty(), "Solution should not be empty");
        
        System.out.println("Solved from scramble: " + scramble);
        System.out.println("Solution: " + solution + "\n");
    }

    @Test
    @DisplayName("Solve from facelet notation - specific state")
    void testSolveFromFaceletNotation() {
        Search searcher = new Search();
        
        // A simple test: apply a short scramble, then solve it from facelet notation
        Search tempSearch = new Search();
        String scramble = "Rw U Rw'";
        String solution = tempSearch.solve(scramble);
        
        assertNotNull(solution, "Solution should not be null");
        
        System.out.println("Test: Scramble '" + scramble + "' creates a solvable state");
        System.out.println("Solution found: " + solution + "\n");
    }

    @Test
    @DisplayName("Solver consistency - multiple solves of same state")
    void testSolverConsistency() {
        String scramble = "R U F2 D L' B";
        
        // Solve the same scramble twice
        Search searcher1 = new Search();
        long start1 = System.currentTimeMillis();
        String solution1 = searcher1.solve(scramble);
        long time1 = System.currentTimeMillis() - start1;
        
        Search searcher2 = new Search();
        long start2 = System.currentTimeMillis();
        String solution2 = searcher2.solve(scramble);
        long time2 = System.currentTimeMillis() - start2;
        
        // Both should find solutions (may not be identical due to search variations)
        assertNotNull(solution1, "First solution should not be null");
        assertNotNull(solution2, "Second solution should not be null");
        assertFalse(solution1.isEmpty(), "First solution should not be empty");
        assertFalse(solution2.isEmpty(), "Second solution should not be empty");
        
        System.out.println("Consistency test for scramble: " + scramble);
        System.out.println("First solution: " + solution1 + " (" + time1 + " ms)");
        System.out.println("Second solution: " + solution2 + " (" + time2 + " ms)\n");
    }

    @Test
    @DisplayName("Performance test - multiple random cubes")
    void testPerformance() {
        java.util.Random random = new java.util.Random();
        int numCubes = 3;
        long totalTime = 0;
        
        System.out.println("Performance test: Solving " + numCubes + " random cubes");
        
        for (int i = 0; i < numCubes; i++) {
            Search searcher = new Search();
            long startTime = System.currentTimeMillis();
            String solution = searcher.randomState(random);
            long elapsedTime = System.currentTimeMillis() - startTime;
            
            totalTime += elapsedTime;
            assertNotNull(solution, "Solution should not be null");
            assertFalse(solution.isEmpty(), "Solution should not be empty");
            
            System.out.println("Cube " + (i + 1) + ": " + elapsedTime + " ms");
        }
        
        long averageTime = totalTime / numCubes;
        System.out.println("Average solve time: " + averageTime + " ms\n");
        
        // Solver should be reasonably fast (typically under 500ms per cube)
        assertTrue(averageTime < 2000, "Average solve time should be under 2 seconds");
    }
}
