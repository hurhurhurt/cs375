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
    public static ArrayList<String> bestSet = new ArrayList<String>();
    public static ArrayList<String> include = new ArrayList<String>();
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
	    System.out.println(algo1.totalProfit);
	    return algo1;
	}
	else{
	    System.out.println(bestSoFar.profit);
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
	    include.set(index+1, "yes");
	    knapsack(index+1, profit + copy.get(index+1).profit, weight + copy.get(index+1).weight, copy, capacity, size);
	    include.set(index+1, "no");
	    knapsack(index+1, profit, weight, copy, capacity, size);
	}
    }
    
    public static void backtracking(int capacity, int size, ArrayList<pair> copy){	
	bestNum = 0;
	maxProfit = algo2(capacity, copy).totalProfit;
	knapsack(-1,0,0, copy, capacity, size);
	System.out.println("maxprofit: " + maxProfit);
	System.out.println("bestnum = " + bestNum);
	
	//for (int i = 1; i <= bestNum; i++)
	    //System.out.println("bestSet at index " + i + " = " + bestSet.get(i));
	
    }
    
    
    public static void main(String[] args){
	File input = new File(args[0]);
	BufferedWriter out = null;
	try{
	    Scanner scanner = new Scanner(input);
	    while (scanner.hasNextInt()){
		ArrayList<pair> knapsack = new ArrayList<pair>();
		int numItems = scanner.nextInt();
		int capacity = scanner.nextInt();
  
		for (int i = 0; i < numItems; i++)
		    knapsack.add(new pair(scanner.nextInt(), scanner.nextInt(), i+1));

	        //profit p = algo1(capacity, knapsack);
		//System.out.println(p.totalProfit);
		//algo2(capacity, knapsack);
		//knapsack.clear();

		ArrayList<pair> copy = new ArrayList<>();
		for (pair p : knapsack) copy.add(p);
		Collections.sort(copy, new Comparator<pair>(){
			@Override
			public int compare(pair a, pair b){
			    return Float.compare((float) (b.profit / b.weight), (float) (a.profit / a.weight));
			}
		    });

		for (int i = 0; i < copy.size(); i++){
		    include.add("null");
		    bestSet.add("null");
		}
		
		for (pair p : copy)
		    System.out.println("weight = " + p.weight + " profit = " + p.profit + " position = " + p.position);

		backtracking(capacity, numItems, copy);
		System.out.println("-------------------");
		//include.clear();
		//bestSet.clear();
		//System.out.println(upperBound(0, 0, 0, copy, capacity, numItems));

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
