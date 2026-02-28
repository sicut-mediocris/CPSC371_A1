import java.io.*;
import java.util.*;

public class MainPart1 {


    /*Team name : Unsupervised Guys
    Sukirat Singh Dhillon
    Akshay Arulkrishnan
    Amaan Hingora
    Karsten Ngai-Natsuhara*/

    static class Item {
        final String id;
        final int weight;
        final int price;

        Item(String id, int weight, int price) {
            this.id = id;
            this.weight = weight;
            this.price = price;
        }
    }

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
           List<Item> itemList = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Split by spaces or tabs 
                String[] parts = line.split("\\s+");
                String id = parts[0];
                int w = Integer.parseInt(parts[1]);
                int p = Integer.parseInt(parts[2]);
                itemList.add(new Item(id, w, p));
            }
            fileScanner.close();

            int n = itemList.size();

            //  Create the DP Table
            // Rows: 0 to n (items), Columns: 0 to capacity.
            int[][] table = new int[n + 1][capacity + 1];

            //  Fill the table 
            for (int i = 1; i <= n; i++) {
                Item current = itemList.get(i - 1); // Get the item for this row

                for (int w = 1; w <= capacity; w++) {
                    if (current.weight <= w) {
                       // If it fits, we check: is it better to take it or skip it?
                        int takeIt = current.price + table[i - 1][w - current.weight];
                        int skipIt = table[i - 1][w];
                        table[i][w] = Math.max(takeIt, skipIt);
                    } else {
                        // If it's too  heavy? Just take the value from the row above
                        table[i][w] = table[i - 1][w];
                    }
                }
            }

            //  Backtrack to find which items were picked
            // We compare current cell to the one above it. If different, we picked it!
            List<String> pickedIds = new ArrayList<>();
            int weightLeft = capacity;
            for (int i = n; i > 0; i--) {
                // If the value is different from the row above, we haev definitely used this item
                if (table[i][weightLeft] != table[i - 1][weightLeft]) {
                    Item picked = itemList.get(i - 1);
                    pickedIds.add(picked.id);
                    weightLeft -= picked.weight;
                }
            }
            Collections.reverse(pickedIds); // Flip it so it's in order of ID

            //  Output the  table to "dynamic_table.txt" 
            PrintWriter writer = new PrintWriter(new FileWriter("dynamic_table.txt"));
            for (int[] row : table) {
                StringBuilder rowStr = new StringBuilder();
                for (int j = 0; j < row.length; j++) {
                    rowStr.append("\"").append(row[j]).append("\"");
                    if (j < row.length - 1) rowStr.append(",");
                }
                writer.println(rowStr.toString());
            }
            writer.close();

           
            System.out.println("Processing...");
            System.out.println("Done!");
            System.out.println("\nResult:");
            System.out.println("Total Value: " + table[n][capacity]); 
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