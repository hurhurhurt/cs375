import java.util.*;
import java.lang.Integer;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

class profit{
    int maxProfit;
    int maxSize;
    HashMap<String, Integer> cards;

    profit(int p, int s, HashMap<String, Integer> c){
	this.maxProfit = p;
	this.maxSize = s;
	this.cards = c;
    }
}
class program1{
    public static HashMap<String, Integer> market = new HashMap<String, Integer>();
    
    public static profit ComputeMaxProfit(HashMap<String, Integer> I, int W){
	int maxProfit = 0;

	HashMap<String, Integer> S = new HashMap<String, Integer>();
	HashMap<String, Integer> M = new HashMap<String, Integer>();
	
	if (ComputeWeights(I) <= W){
	    maxProfit = ComputeProfit(I);
	    profit ret = new profit(maxProfit, I.size(), I);
	    return ret;
	}
	
	Set<Set<String>> subsets = generateSubsets(I);
	for (Set<String> sets : subsets){
	    for (String card : I.keySet()){
		if (sets.contains(card)){
		    S.put(card, I.get(card));
		}
	    }
	    
	    if (ComputeWeights(S) <= W){
		int profit = ComputeProfit(S);
		if (profit > maxProfit){
		    maxProfit = profit;
		    M.putAll(S);
		}
	    }
	    S.clear();
	}
	profit ret = new profit(maxProfit, M.size(), M);
	return ret;
    }

    public static Set<Set<String>> generateSubsets(HashMap<String, Integer> map){
	Set<String> subsets = map.keySet();
	Set<Set<String>> powerSet = new HashSet<Set<String>>();

	List<String> list = new ArrayList<String>(subsets);
	int n = list.size();

	for (long i = 0; i < (1 << n); i++){
	    Set<String> element = new HashSet<String>();
	    for (int j = 0; j < n; j++){
		if ( (i >> j) % 2 == 1){
		    element.add(list.get(j));
		}
		powerSet.add(element);
	    }
	}
	return powerSet;
    }

    public static int ComputeWeights(HashMap<String, Integer> S){
	int retVal = 0;
	for (int i : S.values()){
	    retVal += i;
	}
	return retVal;
    }
    
    public static int ComputeSetWeight(Set<String> set, HashMap<String, Integer> map){
	int retVal = 0;
	for (String s : set){
	    if (map.containsKey(s)){
		    retVal += map.get(s);
	    }
	}
	return retVal;
    }

    public static int ComputeProfit(HashMap<String, Integer> map){
	int profit = 0;
	for (String s : map.keySet()){
	    if (market.containsKey(s)){
		profit += (market.get(s) - map.get(s));
	    }
	}
	return profit;	
    }
    
    public static void main(String[] args){
	File marketFile;
	File pricesFile;

	if (args[0].matches("-m")){
	    marketFile = new File(args[1]);
	    pricesFile = new File(args[3]);
	}
	else{
	    pricesFile = new File(args[1]);
	    marketFile = new File(args[3]);
	}
	BufferedWriter out = null;
	try{
	    Scanner scanner = new Scanner(marketFile);
	    int marketSize = scanner.nextInt();
	    for (int i = 0; i < marketSize; i++)
		market.put(scanner.next(), scanner.nextInt());

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
