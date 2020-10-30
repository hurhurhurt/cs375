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
    int totalProfit;
    ArrayList<Integer> positions;

    profit(int t, ArrayList<Integer> p){
	this.totalProfit = t;
	this.positions = p;
    }
}

class pair{
    int weight;
    int profit;
    int position;

    pair(int w, int p, int po){
	this.weight = w;
	this.profit = p;
	this.position = po;
    }

}
class program3{
    public static ArrayList<Boolean> bestSet = new ArrayList<Boolean>();
    public static ArrayList<Boolean> include = new ArrayList<Boolean>();
    public static int bestNum = -1;
    public static int maxProfit = -1;
    
    public static profit algo1(int capacity, ArrayList<pair> knapsack){
	if (knapsack.size() == 0) return new profit(0, new ArrayList<Integer>());
	ArrayList<pair> copy = new ArrayList<>();
	for (pair p : knapsack) copy.add(p);
        Collections.sort(copy, new Comparator<pair>(){
		@Override
		public int compare(pair a, pair b){
		    return Float.compare((float) (b.profit / b.weight), (float) (a.profit / a.weight));
		}
	    });
	
	ArrayList<Integer> retList = new ArrayList<Integer>();
	int current = 0;
	int total = 0;
	while (current <= capacity){
	    if (copy.size() == 0) break;
	    if (current + copy.get(0).weight > capacity){
		copy.remove(0);
		continue;
	    }
	    current += copy.get(0).weight;
	    if (current > capacity) break;
	    else{
		total += copy.get(0).profit;
	        retList.add(copy.get(0).position);
		copy.remove(0);
	    }
 	}
	return new profit(total, retList);
    }

    public static profit algo2(int capacity, ArrayList<pair> knapsack){
	profit algo1 = algo1(capacity, knapsack);
	pair bestSoFar = new pair(0,0,0);
	for (pair p : knapsack){
	    if (p.weight <= capacity){
		if (p.profit > bestSoFar.profit){
		    bestSoFar.profit = p.profit;
		    bestSoFar.position = p.position;
		}
	    }
	}
	
	if (algo1.totalProfit > bestSoFar.profit){
	    return algo1;
	}
	else{
	    return new profit(bestSoFar.profit, new ArrayList<>(Arrays.asList(bestSoFar.position)));
	}
    }
    
    public static float upperBound(int index, int weight, int profit, ArrayList<pair> copy, int capacity, int size){
        float bound = profit;
	ArrayList<Float> x = new ArrayList<Float>();

	for (int i = 0; i < size; i++)
	    x.add((float) -1);
	for (int j = index; j < size; j++)
	    x.set(j, (float) 0);

	while (weight < capacity && index < size){
	    if (weight + copy.get(index).weight <= capacity){
		x.set(index, (float) 1);
		weight += copy.get(index).weight;
		bound += copy.get(index).profit;
	    }
	    else{
		x.set(index, ((float)(capacity - weight) / copy.get(index).weight));
		weight = capacity;
		bound += (float) copy.get(index).profit * x.get(index);
	    }
	    index +=1;
	}
	return bound;
    }
    
    public static boolean promising(int i, int weight, int profit, ArrayList<pair> copy, int capacity, int size){
	if (weight >= capacity) return false;
	float bound = upperBound(i+1, weight, profit, copy, capacity, size);
	return (bound > maxProfit); 
    }
    
    public static void knapsack(int index, int profit, int weight, ArrayList<pair> copy, int capacity, int size){
	if (weight <= capacity && profit > maxProfit){
	    maxProfit = profit;
	    bestNum = index;
	    bestSet = include;
	}
	if (promising(index, weight, profit, copy, capacity, size)){
	    include.set(index+1, true);
	    knapsack(index+1, profit + copy.get(index+1).profit, weight + copy.get(index+1).weight, copy, capacity, size);
	    include.set(index+1, false);
	    knapsack(index+1, profit, weight, copy, capacity, size);
	}
    }
    
    public static void backtracking(int capacity, int size, ArrayList<pair> copy){	
	profit p = algo2(capacity, copy);
	maxProfit = p.totalProfit;
	for (int po : p.positions)
	    bestSet.set(po-1, true);
	bestNum = p.positions.size();
	
	knapsack(-1,0,0, copy, capacity, size);
    }
    
    public static void main(String[] args){
	File input = new File(args[0]);
	BufferedWriter out1 = null;
	BufferedWriter out2 = null;
	try{
	    Scanner scanner = new Scanner(input);
	    if (args[0].matches("smallInput.txt")){
		out1 = new BufferedWriter(new FileWriter("smallOutputGreedy2.txt"));
		out2 = new BufferedWriter(new FileWriter("smallOutputBacktracking.txt"));
	    }
	    else if (args[0].matches("Input.txt")){
		out1 = new BufferedWriter(new FileWriter("outputGreedy2.txt"));
		out2 = new BufferedWriter(new FileWriter("outputBacktracking.txt"));
	    }
	    else if (args[0].matches("mediumInput.txt")){
		out1 = new BufferedWriter(new FileWriter("mediumOutputGreedy2.txt"));
		out2 = new BufferedWriter(new FileWriter("mediumOutputBacktracking.txt"));
	    }
	    else if (args[0].matches("badGreedyInput.txt")){
		out1 = new BufferedWriter(new FileWriter("outputbadGreedy1.txt"));
		out2 = new BufferedWriter(new FileWriter("outputbadGreedy2.txt"));
	    }
	    else if (args[0].matches("badImprovedGreedyInput.txt")){
		out1 = new BufferedWriter(new FileWriter("outputbadImprovedGreedy2.txt"));
	    }
	    while (scanner.hasNextInt()){
		ArrayList<pair> knapsack = new ArrayList<pair>();
		int numItems = scanner.nextInt();
		int capacity = scanner.nextInt();
  
		for (int i = 0; i < numItems; i++)
		    knapsack.add(new pair(scanner.nextInt(), scanner.nextInt(), i+1));

		ArrayList<pair> copy = new ArrayList<>();
		for (pair p : knapsack) copy.add(p);
		Collections.sort(copy, new Comparator<pair>(){
			@Override
			public int compare(pair a, pair b){
			    return Float.compare((float) (b.profit / b.weight), (float) (a.profit / a.weight));
			}
		    });

		if (args[0].matches("smallInput.txt")){
		    // Greedy 2
		    long start = System.nanoTime();
		    profit p = algo2(capacity, knapsack);
		    float duration = ((System.nanoTime() - start)/ (float)1000000);
		    
		    out1.write(numItems + " " + p.totalProfit + " " + duration + " ");
		    for (int i : p.positions)
			out1.write(i + " ");
		    out1.write("\n");

		    // Backtracking
		    for (int i = 0; i <= copy.size(); i++){
			include.add(false);
			bestSet.add(false);
		    }
		    
		    long start2 = System.nanoTime();
		    backtracking(capacity, numItems, copy);
		    float duration2 = ((System.nanoTime() - start)/ (float)1000000);
		    out2.write(numItems + " " + maxProfit + " " + duration2 + " ");
		    for (int i = 0; i <= bestNum; i++)
			if (bestSet.get(i) == true) out2.write(copy.get(i).position + " ");
		    out2.write("\n");
		    bestSet.clear();
		    include.clear();
		}

		else if (args[0].matches("Input.txt")){
		    // Greedy 2
		    long start = System.nanoTime();
		    profit p = algo2(capacity, knapsack);
		    float duration = ((System.nanoTime() - start)/ (float)1000000);
		    out1.write(numItems + " " + p.totalProfit + " " + duration + " ");
		    for (int i : p.positions)
			out1.write(i + " ");
		    out1.write("\n");

		    // Backtracking
		    for (int i = 0; i <= copy.size(); i++){
			include.add(false);
			bestSet.add(false);
		    }
		    
		    long start2 = System.nanoTime();
		    backtracking(capacity, numItems, copy);
		    float duration2 = ((System.nanoTime() - start)/ (float)1000000);
		    out2.write(numItems + " " + maxProfit + " " + duration2 + " ");
		    for (int i = 0; i <= bestNum; i++)
			if (bestSet.get(i) == true) out2.write(copy.get(i).position + " ");
		    out2.write("\n");
		    bestSet.clear();
		    include.clear();
		}

		else if (args[0].matches("mediumInput.txt")){
		    // Greedy 2
		    long start = System.nanoTime();
		    profit p = algo2(capacity, knapsack);
		    float duration = ((System.nanoTime() - start)/ (float)1000000);
		    out1.write(numItems + " " + p.totalProfit + " " + duration + " ");
		    for (int i : p.positions)
			out1.write(i + " ");
		    out1.write("\n");

		    // Backtracking
		    for (int i = 0; i <= copy.size(); i++){
			include.add(true);
			bestSet.add(true);
		    }

		    long start2 = System.nanoTime();
		    backtracking(capacity, numItems, copy);
		    float duration2 = ((System.nanoTime() - start)/ (float)1000000);
		    out2.write(numItems + " " + maxProfit + " " + duration2 + " ");
		    for (int i = 0; i < bestNum; i++)
			if (bestSet.get(i) == true) out2.write(copy.get(i).position + " ");
		    out2.write("\n");
		    bestSet.clear();
		    include.clear();

		}
		else if (args[0].matches("badGreedyInput.txt")){
		    // Greedy 1
		    long start = System.nanoTime();
		    profit p = algo1(capacity, knapsack);
		    float duration = ((System.nanoTime() - start)/ (float)1000000);
		    out1.write(numItems + " " + p.totalProfit + " " + duration + " ");
		    for (int i : p.positions)
			out1.write(i + " ");
		    out1.write("\n");

		    // Greedy 2
		    long start2 = System.nanoTime();
		    profit po = algo2(capacity, knapsack);
		    float duration2 = ((System.nanoTime() - start)/ (float)1000000);
		    out2.write(numItems + " " + po.totalProfit + " " + duration2 + " ");
		    for (int i : po.positions)
			out2.write(i + " ");
		    out2.write("\n");

		}

		else if (args[0].matches("badImprovedGreedyInput.txt")){
		    long start = System.nanoTime();
		    profit p = algo2(capacity, knapsack);
		    float duration = ((System.nanoTime() - start)/ (float)1000000);
		    out1.write(numItems + " " + p.totalProfit + " " + duration + " ");
		    for (int i : p.positions)
			out1.write(i + " ");
		    out1.write("\n");
		}
	    }
	}
	catch (Exception e){
	    e.printStackTrace();
	}
	finally{
	    try{
		if (out1 != null){
		    out1.close();
		}
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	    try{
		if (out2 != null){
		    out2.close();
		}
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	}
    }
}
