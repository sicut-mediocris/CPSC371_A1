import java.io.*;
import java.util.*;

/*
 * Part I: 0-1 Knapsack Problem
 *
 * Objective:
 * - Maximize total value within given capacity.
 */

public class MainPart2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Please enter the data file name: ");
        String fileName = sc.nextLine();

        try {

            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);

            // The first line is the knapsack capacity
            int capacity = Integer.parseInt(fileScanner.nextLine().trim());

            // Create lists to hold our item data
            List<String> ids = new ArrayList<>();
            List<Integer> weights = new ArrayList<>();
            List<Integer> prices = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Split by spaces or tabs
                String[] parts = line.split("\\s+");
                ids.add(parts[0]);                          // Item ID
                weights.add(Integer.parseInt(parts[1]));    // Item Weight
                prices.add(Integer.parseInt(parts[2]));     // Item Price
            }
            fileScanner.close();

            int n = ids.size();

            // 3. Create the DP Table
            // Rows: 0 to n (items), Columns: 0 to capacity.
            int[][] dp = new int[n + 1][capacity + 1];

            // 4. Fill the table (General / Unbounded Knapsack)
            // Difference from Part I: when we take an item, we can still take it again,
            // so we stay on the same row (dp[i][...]).
            for (int i = 1; i <= n; i++) {
                int currentWeight = weights.get(i - 1);
                int currentPrice = prices.get(i - 1);

                for (int w = 1; w <= capacity; w++) {
                    if (currentWeight <= w) {
                        // Can we fit it?
                        // MAX of: (Not taking it) vs (Taking it + best value with remaining capacity on SAME row)
                        dp[i][w] = Math.max(dp[i - 1][w], currentPrice + dp[i][w - currentWeight]);
                    } else {
                        // Too heavy? Just take the value from the row above
                        dp[i][w] = dp[i - 1][w];
                    }
                }
            }

            // 5. Backtrack to find which items were picked
            // If the current cell is the same as the one above, we didn't pick this item.
            // If different, we picked it. For Part II, we stay on the same item (can pick again).
            List<String> pickedIds = new ArrayList<>();
            int tempW = capacity;
            int i = n;

            while (i > 0 && tempW > 0) {
                if (dp[i][tempW] == dp[i - 1][tempW]) {
                    i--; // didn't pick this item
                } else {
                    pickedIds.add(ids.get(i - 1));
                    tempW -= weights.get(i - 1); // reduce capacity, but keep i the same
                }
            }

            Collections.reverse(pickedIds); // Order them by appearance (same as Part I)

            // 6. Output the dynamic table to "dynamic_table.txt"
            PrintWriter writer = new PrintWriter(new FileWriter("dynamic_table.txt"));
            for (int[] row : dp) {
                StringBuilder rowStr = new StringBuilder();
                for (int j = 0; j < row.length; j++) {
                    rowStr.append("\"").append(row[j]).append("\"");
                    if (j < row.length - 1) rowStr.append(",");
                }
                writer.println(rowStr.toString());
            }
            writer.close();

            // 7. Print results to console
            System.out.println("Processing...");
            System.out.println("Done!");
            System.out.println("\nResult:");
            System.out.println("Total Value: " + dp[n][capacity]);
            System.out.print("Item ID List: ");
            System.out.println(String.join(", ", pickedIds));
            System.out.println("=====");
            System.out.println("Outputting dynamicTable.txt...");
            System.out.println("Done!");
            System.out.println("End of Processing.");

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}


//Output
/*
PS F:\My files\Personal documents\Studies\Winter Semester 2026\CPSC 371\Assignments\Assignment 1\CPSC371_Winter2026_A1_The_Unsupervised_Guys\CPSC371_A1> java MainPart2
Please enter the data file name: sample_knapsack_data.txt        
Processing...
Done!

Result:
Total Value: 7
Item ID List: 1, 2
=====
Outputting dynamicTable.txt...
Done!
End of Processing.
*/