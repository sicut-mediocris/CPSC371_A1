import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        int topMenu = -1;

        do {
            //section for the User Interface
            System.out.println("Hi there, welcome to Unsupervised Guys' Assignment 1");
            System.out.println("Here are the problems that we can solve:");
            System.out.println("1. 0-1 Knapsack Problem");
            System.out.println("2. General Knapsack Problem");
            System.out.println("3. 0-1 Knapsack Problem with restraints");
            do {
                System.out.print("Please input the number of the problem you want to solve, or 0 to exit the program: ");
                topMenu = scanner.nextInt();
                if(topMenu > 3 || topMenu < 0) {
                    System.out.println("That is not a valid input. Please only input a number from 0 to 3.");
                }
            } while (topMenu > 3 || topMenu < 0);

            if(topMenu > 0) {
                //section for getting the file from user input
                boolean fileValid = true;
                Scanner fileScan = null;
                do {
                    System.out.print("Perfect. Now, please input whole the path of the txt file with the knapsack data: ");
                    File data = new File(scanner.next()); //needs to be entire file path

                    try{
                        fileScan = new Scanner(data);
                        fileValid = true;
                    } catch(Exception e) {
                        System.out.println("That was not a valid file. Please try again.");
                        fileValid = false;
                    }
                } while(!fileValid); //use C:\Users\karst\Downloads\sample_knapsack_data.txt

                //section that stores the information from the sample knapsack data into variables
                int maxWeight = fileScan.nextInt();
                ArrayList<int[]> items = new ArrayList<int[]>();
                //notes on items:
                //index 0 of each item is the Item ID
                //index 1 of each item is the Item Weight
                //index 2 of each item is the Item Price

                while(fileScan.hasNextInt()) {
                    int[] item = new int[3];
                    item[0] = fileScan.nextInt();
                    item[1] = fileScan.nextInt();
                    item[2] = fileScan.nextInt();

                    items.add(item);
                }
                fileScan.close();

                //printing parts that show the data, just uncomment if you want to display it
//                System.out.println(maxWeight);
//                for(int i = 0; i < items.size(); i++) {
//                    System.out.println(i + ": " + items.get(i)[0] + " " + items.get(i)[1] + " " + items.get(i)[2]);
//                }

                int[][] maxValueTable = new int[maxWeight][items.size()]; //table is set up like [max Weight][items considered]

                for(int i = 0; i < maxWeight; i++) {
                    for(int j = 0; j < items.size(); j++) {

                        int foundMaxValue = -1;

                        switch(topMenu) {
                            case 1:
                                //part to find the max value of the 0-1 knapsack problem with the above parameters
                                break;
                            case 2:
                                //part to find the max value of the general knapsack problem with the above parameters
                                break;
                            case 3:
                                //part to find the max value of the 0-1 knapsack problem with restrictions with the above parameters
                                break;
                            default:
                                break;
                        }

                        maxValueTable[i][j] = foundMaxValue;
                    }
                }


            } else {
                System.out.println("Thank you for trying our assignment. Good Bye.");
            }

            System.out.println();
        } while(topMenu > 0);

    }
}