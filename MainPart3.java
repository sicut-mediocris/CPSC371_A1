/*
* CPSC 371 - Artificial Intelligence
* Assignment - 1 
* Part - 3
* The Unsupervised Guys
*/

import java.io.*;
import java.util.*;

/*
 * Part III: 0-1 Knapsack Problem with Constraints
 *
 * Constraints:
 * 1. Total weight of selected items must be ODD.
 * 2. Total price (value) of selected items must be EVEN.
 */

public class MainPart3 {

    // Used to represent impossible states in DP
    private static final int NEG_INF = -1_000_000_000;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Ask user for input file
        System.out.print("Please enter the data file name: ");
        String fileName = sc.nextLine();

        try {

            // Read input file
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);

            // First line = knapsack capacity
            int capacity = Integer.parseInt(fileScanner.nextLine().trim());

            // Lists to store item data
            List<String> ids = new ArrayList<>();
            List<Integer> weights = new ArrayList<>();
            List<Integer> prices = new ArrayList<>();

            // Read item data
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");

                ids.add(parts[0]);
                weights.add(Integer.parseInt(parts[1]));
                prices.add(Integer.parseInt(parts[2]));
            }

            fileScanner.close();

            int n = ids.size();

            /*
             * DP Definition:
             * dp[i][c][wp][vp]
             *
             * i  = first i items
             * c  = capacity
             * wp = weight parity (0 = even, 1 = odd)
             * vp = value parity  (0 = even, 1 = odd)
             *
             * Stores maximum achievable value under those conditions.
             */
            int[][][][] dp = new int[n + 1][capacity + 1][2][2];

            // For reconstruction
            boolean[][][][] took = new boolean[n + 1][capacity + 1][2][2];
            int[][][][] prevC = new int[n + 1][capacity + 1][2][2];
            int[][][][] prevWP = new int[n + 1][capacity + 1][2][2];
            int[][][][] prevVP = new int[n + 1][capacity + 1][2][2];

            // Initialize all states as impossible
            for (int i = 0; i <= n; i++) {
                for (int c = 0; c <= capacity; c++) {
                    for (int wp = 0; wp < 2; wp++) {
                        for (int vp = 0; vp < 2; vp++) {
                            dp[i][c][wp][vp] = NEG_INF;
                        }
                    }
                }
            }

            // Base case: selecting nothing
            for (int c = 0; c <= capacity; c++) {
                dp[0][c][0][0] = 0;  // weight even, value even
            }

            // Fill DP table
            for (int i = 1; i <= n; i++) {

                int wi = weights.get(i - 1);
                int vi = prices.get(i - 1);

                int wParity = wi % 2;
                int vParity = vi % 2;

                for (int c = 0; c <= capacity; c++) {
                    for (int wp = 0; wp < 2; wp++) {
                        for (int vp = 0; vp < 2; vp++) {

                            // Option 1: Don't take item
                            int best = dp[i - 1][c][wp][vp];
                            boolean bestTook = false;
                            int bestPrevC = c;
                            int bestPrevWP = wp;
                            int bestPrevVP = vp;

                            // Option 2: Take item (if it fits)
                            if (wi <= c) {
                                int prevWp = wp ^ wParity;
                                int prevVp = vp ^ vParity;

                                int prevVal = dp[i - 1][c - wi][prevWp][prevVp];

                                if (prevVal != NEG_INF) {
                                    int candidate = prevVal + vi;

                                    if (candidate > best) {
                                        best = candidate;
                                        bestTook = true;
                                        bestPrevC = c - wi;
                                        bestPrevWP = prevWp;
                                        bestPrevVP = prevVp;
                                    }
                                }
                            }

                            dp[i][c][wp][vp] = best;
                            took[i][c][wp][vp] = bestTook;
                            prevC[i][c][wp][vp] = bestPrevC;
                            prevWP[i][c][wp][vp] = bestPrevWP;
                            prevVP[i][c][wp][vp] = bestPrevVP;
                        }
                    }
                }
            }

            // Required constraint: weight odd (1), value even (0)
            int answer = dp[n][capacity][1][0];

            List<String> pickedIds = new ArrayList<>();

            // Backtracking only if solution exists
            if (answer != NEG_INF) {

                int i = n;
                int c = capacity;
                int wp = 1;
                int vp = 0;

                while (i > 0) {
                    if (took[i][c][wp][vp]) {
                        pickedIds.add(ids.get(i - 1));
                    }

                    int nc = prevC[i][c][wp][vp];
                    int nwp = prevWP[i][c][wp][vp];
                    int nvp = prevVP[i][c][wp][vp];

                    c = nc;
                    wp = nwp;
                    vp = nvp;
                    i--;
                }

                Collections.reverse(pickedIds);
            }

            // Write dynamic table (only constrained results)
            PrintWriter writer = new PrintWriter(new FileWriter("dynamic_table_part3.txt"));

            for (int i = 0; i <= n; i++) {
                StringBuilder row = new StringBuilder();
                for (int c = 0; c <= capacity; c++) {

                    int val = dp[i][c][1][0];
                    if (val == NEG_INF) val = -1;

                    row.append("\"").append(val).append("\"");
                    if (c < capacity) row.append(",");
                }
                writer.println(row.toString());
            }

            writer.close();

            // Console Output
            System.out.println("Processing...");
            System.out.println("Done!");
            System.out.println("\nResult:");

            if (answer == NEG_INF) {
                System.out.println("No valid solution exists for the given input data.");
            } else {
                System.out.println("Total Value: " + answer);
                System.out.print("Item ID List: ");
                System.out.println(String.join(", ", pickedIds));
            }

            System.out.println("=====");
            System.out.println("Outputting dynamic_table_part3.txt...");
            System.out.println("Done!");
            System.out.println("End of Processing.");

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}


// Output
/*
PS F:\My files\Personal documents\Studies\Winter Semester 2026\CPSC 371\Assignments\Assignment 1\CPSC371_Winter2026_A1_The_Unsupervised_Guys\CPSC371_A1> java Main
Please enter the data file name: sample_knapsack_data.txt
Processing...
Done!

Result:
Total Value: 7
Item ID List: 1, 2

Result:
Total Value: 6
Item ID List: 4
=====
Outputting dynamic_table.txt...
Done!
End of Processing.
*/