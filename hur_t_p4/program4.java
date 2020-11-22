import java.util.*;
import java.lang.Integer;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

class pair{
    int weight;
    int profit;

    pair(int weight, int profit){
	this.weight = weight;
	this.profit = profit;
    }
}

class program4{
    // DP ALGO STARTS HERE
    public static int max(int a, int b){
	return (a > b) ? a : b;
    }

    public static int dp(ArrayList<pair> knapsack, int capacity, int size){
	Integer[][] matrix = new Integer[size][capacity+1];
	return dpRecursive(matrix, knapsack, capacity, 0, size);
    }

    public static int dpRecursive(Integer[][] matrix, ArrayList<pair> knapsack, int capacity, int pos, int size){
	if (capacity <= 0 || pos >= size) return 0;
	if (matrix[pos][capacity] != null) return matrix[pos][capacity];
	int profit = 0;
	if (knapsack.get(pos).weight <= capacity){
	    profit = knapsack.get(pos).profit + dpRecursive(matrix, knapsack, capacity - knapsack.get(pos).weight, pos + 1, size);
	}

	int profit2 = dpRecursive(matrix, knapsack, capacity, pos+1, size);
	matrix[pos][capacity] = max(profit, profit2);
	return matrix[pos][capacity];
    }
    // AND HERE IS THE MAIN METHOD :)
    public static void main(String[] args){
	if (args.length < 2){
	    System.out.println("ERROR: usage: java program4 [inputfile] [outputfile]");
	    System.exit(0);
	}
	
	File input = new File(args[0]);
	BufferedWriter out = null;
	try{
	    Scanner scanner = new Scanner(input);
	    out = new BufferedWriter(new FileWriter(args[1]));
	    while (scanner.hasNextInt()){
		ArrayList<pair> knapsack = new ArrayList<pair>();
		int numItems = scanner.nextInt();
		int capacity = scanner.nextInt();
		for (int i = 0; i < numItems; i++){
		    knapsack.add(new pair(scanner.nextInt(), scanner.nextInt()));
		}
		long start = System.nanoTime();
		int profit = dp(knapsack, capacity, numItems);
		float duration = ((System.nanoTime() - start)/ (float)1000000);
		out.write(numItems + " " + profit + " " + duration + " ");
	    }
	}
	catch (Exception e){
	    e.printStackTrace();
	}
	finally{
	    try{
		if (out != null){
		    out.close();
		}
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	}
    }
}
