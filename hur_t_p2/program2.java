import java.util.*;
import java.lang.Integer;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

class pair{
    int a;
    int b;
    int c;

    pair(int a, int b){
	this.a = a;
	this.b = b;
    }

    pair(int a, int b, int c){
	this.a = a;
	this.b = b;
	this.c = c;
    }
}

class contestant{
    int id;
    int points;

    contestant(int id, int points){
	this.id = id;
	this.points = points;
    }
}

class heap{
    contestant[] contestants;
    int[] locations;
    int size;
    int maxSize;

    heap(int maxSize){
	this.maxSize = maxSize;
	this.size = 0;
	contestants = new contestant[this.maxSize+1];
	locations = new int[this.maxSize+1];
	Arrays.fill(locations, -1);
    }

    public void swap(int a, int b){
	contestant temp;
	temp = this.contestants[a];
	this.contestants[a] = this.contestants[b];
	this.contestants[b] = temp;
    }

    public int leftChild(int pos){
	return 2 * pos;
    }

    public int rightChild(int pos){
	return 2 * pos + 1;
    }

    public int parent(int pos){
	return pos / 2;
    }

    public void minHeapify(int pos){
	int left = leftChild(pos);
	int right = rightChild(pos);
	int smallest = pos;

	if (left < this.size && contestants[left].points < contestants[pos].points) smallest = left;

	if (right < this.size && contestants[right].points < contestants[smallest].points) smallest = right;

	if (smallest != pos){
	    locations[contestants[pos].id] = smallest;
	    locations[contestants[smallest].id] = pos;
	    swap(pos, smallest);
	    minHeapify(smallest);
	}
    }

    public void buildMinHeap(){
	for (int i = maxSize / 2; i >= 0; i--){
	    minHeapify(i);
	}
    }

    public int insertContestant(int id, int points){
	contestant con = new contestant(id, points);
	if (this.size >= maxSize){
	    return 1;
	}
	if (findContestant(id) != null){
	    return 2;
	}
	else{
	    locations[con.id] = this.size;
	    this.contestants[this.size++] = con;
	    buildMinHeap();
	    return 3;
	}
    }

    public contestant findContestant(int id){
	if (locations[id] != -1){
	    return contestants[locations[id]];
	}
	return null;
    }

    public pair eliminateWeakest(){
	if (this.size < 1) return new pair(0,0,-1);
	else{
	    pair retPair = new pair(contestants[0].id, contestants[0].points, 1);
	    locations[this.size] = 1;
	    locations[contestants[0].id] = -1;
	    contestants[0] = contestants[--this.size];
	    minHeapify(0);
	    return retPair;
	}
    }

    public pair earnPoints(int id, int p){
	findContestant(id).points += p;
	buildMinHeap();
	return new pair(id, findContestant(id).points);
    }

    public pair losePoints(int id, int p){
	findContestant(id).points -= p;
	buildMinHeap();
	return new pair(id, findContestant(id).points);
    }

    public ArrayList<pair> showContestants(){
	ArrayList<pair> retPair = new ArrayList<pair>();
	for (int i = 0; i < this.size; i++){
	    if (contestants[i] != null){
		retPair.add(new pair(contestants[i].id, locations[contestants[i].id] + 1, contestants[i].points));
	    }
	}
	return retPair;
    }

    public ArrayList<pair> showHandles(){
	ArrayList<pair> retPair = new ArrayList<pair>();
	for (int i = 1; i < this.maxSize + 1; i++){
	    if (locations[i] == -1){
		retPair.add(new pair(i, locations[i], -1));
	    }
	    else{
		retPair.add(new pair(i, locations[i] + 1, 1));
	    }
	}
	return retPair;
    }

    public pair showLocation(int id){
	if (findContestant(id) != null){
	    return new pair(id, locations[id] + 1, 1);
	}
	else{
	    return new pair(0,0,-1);
	}
    }

    public void crownWinner(){
	while (this.size > 1){
	    eliminateWeakest();
	}
    }
}

class program2{
    public static void main(String[] args){
	File input = new File(args[0]);
	File output = new File(args[1]);
	BufferedWriter out = null;
	try{
	    FileWriter fstream = new FileWriter(args[1], true);
	    out = new BufferedWriter(fstream);
	    Scanner read = new Scanner(input);
	    int count = read.nextInt();
	    heap heap = new heap(count);
	    ArrayList<String> list = new ArrayList<String>();
	    while(read.hasNext()){
		String next = read.next();
		if(next.equals("insertContestant")){
		    int id = Integer.valueOf(read.next().replaceAll("[<>]",""));
		    int points = Integer.valueOf(read.next().replaceAll("[<>]",""));
		    int status = heap.insertContestant(id, points);
		    list.add("insertContestant <" + String.valueOf(id) + "> <" + String.valueOf(points) + ">\n");
		    if (status == 1){
			list.add("Contestant <" + String.valueOf(id) + "> could not be inserted because the extended heap is full.\n");
		    }
		    else if (status == 2){
			list.add("Contestant <" + String.valueOf(id) + ">  is already in the extended heap: cannot insert.\n");
		    }
		    else{
			list.add("Contestant <" + String.valueOf(id) + "> inserted into heap with initial score <" + String.valueOf(points) + ">.\n");
		    }
		}

		else if (next.equals("findContestant")){
		    int id = Integer.valueOf(read.next().replaceAll("[<>]",""));
		    list.add("findContestant <" + String.valueOf(id) + ">\n");
		    contestant c = heap.findContestant(id);
		    if (c != null){
			list.add("Contestant <" + String.valueOf(id) + "> is in the extended heap with score <" + String.valueOf(c.points) + ">.\n");
		    }
		    else{
			list.add("Contestant <" + String.valueOf(id) + "> is not in the extended heap.\n");
		    }
		}

		else if(next.equals("eliminateWeakest")){
		    list.add("eliminateWeakest\n");
		    pair pair = heap.eliminateWeakest();
		    if (pair.c == -1){
			list.add("No contestant can be eliminated since the extended heap is empty.\n");
		    }
		    else{
			list.add("Contestant <" + String.valueOf(pair.a) + "> with current lowest score <" + String.valueOf(pair.b) + "> eliminated.\n");
		    }
		}

		else if(next.equals("earnPoints")){
		    int id = Integer.valueOf(read.next().replaceAll("[<>]",""));
		    int earnPoints = Integer.valueOf(read.next().replaceAll("[<>]",""));
		    list.add("earnPoints <" + String.valueOf(id) + "> <" + String.valueOf(earnPoints) + ">\n");
		    pair pair = heap.earnPoints(id, earnPoints);
		    list.add("Contestant <" + String.valueOf(pair.a) + ">'s score increased by <" + String.valueOf(earnPoints) + "> points to <" + String.valueOf(pair.b) + ">.\n");
		}

		else if(next.equals("losePoints")){
		    int id = Integer.valueOf(read.next().replaceAll("[<>]",""));
		    int losePoints = Integer.valueOf(read.next().replaceAll("[<>]",""));
		    list.add("losePoints <" + String.valueOf(id) + "> <" + String.valueOf(losePoints) + ">\n");
		    pair pair = heap.losePoints(id, losePoints);
		    list.add("Contestant <" + String.valueOf(pair.a) + ">'s score decreased by <" + String.valueOf(losePoints) + "> points to <" + String.valueOf(pair.b) + ">.\n");
		}

		else if(next.equals("showContestants")){
		    list.add("showContestants\n");
		    ArrayList<pair> temp = heap.showContestants();
		    for (pair pair : temp){
			list.add("Contestant <" + pair.a + "> is in extended heap location <" + pair.b + "> with score <" + pair.c + ">.\n");
		    }
		}

		else if(next.equals("showHandles")){
		    list.add("showHandles\n");
		    ArrayList<pair> temp = heap.showHandles();
		    for (pair pair : temp){
			if (pair.c == -1){
			    list.add("There is no Contestant <" + pair.a + "> in the extended heap: handle[<" + pair.a + ">] = " + pair.b + ".\n");
			}
			else if (pair.c == 1){
			    list.add("Contestant <" + pair.a + "> stored in extended heap location <" + pair.b + ">.\n");
			}
		    }
		}

		else if (next.equals("showLocation")){
		    list.add("showLocation\n");
		    int id = Integer.valueOf(read.next().replaceAll("[<>]",""));
		    pair pair = heap.showLocation(id);
			if (pair.c == 1){
			    list.add("Contestant <" + pair.a + "> stored in extended heap location <" + pair.b + ">.\n");
			}
			else if (pair.c == -1){
			    list.add("There is no Contestant <" + pair.a + "> in the extended heap: handle[<" + pair.a + ">] = -1.\n");
			}

		}

		else if (next.equals("crownWinner")){
		    list.add("crownWinner\n");
		    heap.crownWinner();
		    list.add("Contestant <" + heap.contestants[1].id + "> wins with score <" + heap.contestants[1].points+ ">!\n");
		}
	    }
	    for (String s : list){
		out.write(s);
	    }
	    read.close();
	}
	catch (Exception e ){
	    e.printStackTrace();
	}
	finally{
	    if (out != null){
		try{
		    out.close();
		}
		catch (IOException e){
		    e.printStackTrace();
		}
	    }
	}
    }
}



