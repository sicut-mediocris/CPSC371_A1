import java.io.*;
import java.util.*;

public class MainPart2 {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Please enter the data file name: ");
        String fileName = sc.nextLine();

        try {
            // 1. Open the file
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);

            // 2. First line = knapsack capacity
            int capacity = Integer.parseInt(fileScanner.nextLine().trim());

            // 3. Store item data
            List<String> ids = new ArrayList<>();
            List<Integer> weights = new ArrayList<>();
            List<Integer> prices = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Split by spaces or tabs
                String[] parts = line.split("\\s+");
                ids.add(parts[0]);                         // Item ID
                weights.add(Integer.parseInt(parts[1]));   // Item Weight
                prices.add(Integer.parseInt(parts[2]));    // Item Price
            }
            fileScanner.close();

            int n = ids.size();

            // 4. Create DP Table
            // Rows: 0..n (items), Columns: 0..capacity
            int[][] dp = new int[n + 1][capacity + 1];

            // 5. Fill the table (UNBOUNDED / GENERAL knapsack)
            // Key difference from Part I:
            // If we take an item, we stay in the SAME row (dp[i][...]) so we can take it again.
            for (int i = 1; i <= n; i++) {
                int currentWeight = weights.get(i - 1);
                int currentPrice = prices.get(i - 1);

                for (int w = 1; w <= capacity; w++) {
                    if (currentWeight <= w) {
                        // Can we fit it?
                        // MAX of: (Not taking it) vs (Taking it + best value at remaining capacity in SAME row)
                        dp[i][w] = Math.max(
                                dp[i - 1][w],
                                currentPrice + dp[i][w - currentWeight]
                        );
                    } else {
                        // Too heavy? Just take the value from the row above
                        dp[i][w] = dp[i - 1][w];
                    }
                }
            }

            // 6. Backtrack to find which items were picked (UNBOUNDED)
            // If dp[i][w] == dp[i-1][w], we did NOT pick item i.
            // Otherwise, we DID pick it, and we stay at i (because we might pick it again).
            List<String> pickedIds = new ArrayList<>();
            int tempW = capacity;
            int i = n;

            while (i > 0 && tempW > 0) {
                if (dp[i][tempW] == dp[i - 1][tempW]) {
                    // Didn't use this item, move up
                    i--;
                } else {
                    // Used this item (and we can use it again)
                    pickedIds.add(ids.get(i - 1));
                    tempW -= weights.get(i - 1);
                }
            }

            Collections.reverse(pickedIds); 

            // 7. Output the dynamic table to "dynamic_table.txt"
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

            // 8. Print results to console 
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
