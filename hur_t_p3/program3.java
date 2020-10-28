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

class profit{
    int maxProfit;
    int maxSize;
    ArrayList<pair> c;

    profit(int p, int s, ArrayList<pair> c){
	this.maxProfit = p;
	this.maxSize = s;
	this.c = c;
    }
}

class pair{
    int weight;
    int profit;

    pair(int w, int p){
	this.weight = w;
	this.profit = p;
    }

}
class program3{
    public static ArrayList<pair> knapsack = new ArrayList<pair>();    
    public static profit algo1(int capacity, ArrayList<pair> knapsack){
	ArrayList<pair> copy = knapsack;
        Collections.sort(copy, new Comparator<pair>(){
		@Override
		public int compare(pair a, pair b){
		    return -Float.compare((float) a.weight / a.profit, (float) b.weight / b.profit);
		}
	    });

	ArrayList<pair> retList = new ArrayList<pair>();
	int current = 0;
	while (current <= capacity){
	    current += copy.get(0).weight;
	    if (current > capacity) break;
	    else{
		retList.add(copy.remove(0));
	    }
 	}   
	for (pair p : retList)
	    System.out.println(p.weight + " " + p.profit);
	
	return null;
    }

    public profit algo2(int capacity, ArrayList<pair> knapsack){
	return null;
    }

    public profit backtracking(int capacity, ArrayList<pair> knapsack){
	return null;
    }
   
    public static void main(String[] args){
	File input = new File(args[0]);
        
	BufferedWriter out = null;
	try{
	    Scanner scanner = new Scanner(input);
	    while (scanner.hasNextInt()){
		int numItems = scanner.nextInt();
		int capacity = scanner.nextInt();
	    
		for (int i = 0; i < numItems; i++)
		    knapsack.add(new pair(scanner.nextInt(), scanner.nextInt()));

		algo1(capacity, knapsack);
		


	    /*
	    out = new BufferedWriter(new FileWriter("output.txt"));
	    Scanner scanner2 = new Scanner(pricesFile);
	    while (scanner2.hasNext()){
		HashMap<String, Integer> price =  new HashMap<String, Integer>();
		int numCards = scanner2.nextInt();
		int budget = scanner2.nextInt();
		for (int i = 0; i < numCards; i++){
		    price.put(scanner2.next(), scanner2.nextInt());
		}
		long start = System.nanoTime();
		profit profits = ComputeMaxProfit(price, budget);
		float duration = ((System.nanoTime() - start)/ (float)1000000);
		try{
		    out.write(Integer.toString(numCards) + " ");
		    out.write(Integer.toString(profits.maxProfit) + " ");
		    out.write(Integer.toString(profits.maxSize) + " ");
		    out.write(Float.toString(duration) + "\n");
		    for (String s : profits.cards.keySet()){
			out.write(s + "\n");
		    }
		    out.write("\n");
		}
		catch (Exception e){
		    e.printStackTrace();
		}
	    }
	    */
	    }
	}
	catch (Exception e){
	    e.printStackTrace();
	}/*
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
	 */
    }
}
