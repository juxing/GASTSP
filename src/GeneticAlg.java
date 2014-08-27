import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticAlg {
	public int dim;
	public static int popSize;
	public static int[][] distances;
	public static int[][] population;
	int replaceThreshold;
	int convergeThreshold;
	int mutationNum;
	
	public GeneticAlg(DistanceGraph dg) {
		dim = dg.getDim();
		popSize = 500;
		replaceThreshold = 10;
		convergeThreshold = 100;
		mutationNum = dim / 10;
		distances = dg.getDistances();
		//printDistances();
		population = new int[popSize][dim];
		initPopulation();
		//printPop();
		for(int i = 0; i < popSize; i++) {
			TwoOpt lk = new TwoOpt(population[i], distances);
		}
		//printPop();
		
		int round = 0;
		
		long startTime = System.currentTimeMillis();
		
		//while(!isConverged()) {
		while(round < 100) {
			int crossNum = 50;
			for(int i = 0; i < crossNum; i++) {
				Random rand = new Random();
				int p1 = rand.nextInt(popSize);
				int p2 = rand.nextInt(popSize);
				DPX dpx = new DPX(population[p1], population[p2], distances);
				int[] child = dpx.getChild();
				
				System.out.print("P1 is " + p1 + " P2 is " + p2 + " child is ");
				for(int j = 0; j < child.length; j++) {
					System.out.print(child[j] + " ");
				}
				System.out.println();
				
				TwoOpt top = new TwoOpt(child, distances);
				
				System.out.print("After lk: ");
				for(int j = 0; j < child.length; j++) {
					System.out.print(child[j] + " ");
				}
				System.out.println();
				
				double rate = Math.random();
				if(rate >= 0.9 && rate < 1) {
					mutation(child);
					System.out.print("After mutation: ");
					for(int j = 0; j < child.length; j++) {
						System.out.print(child[j] + " ");
					}
					System.out.println();
				}
				
				//int repl = rand.nextInt(popSize);
				replace(child);	
				//printPop();
				
				bestIndividuals();  //Just want to print best and worst.
				worstIndividuals();
			}
			
			round++;
						
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("running time is " + (endTime - startTime));
		
		ArrayList<Integer> answers = bestIndividuals();
		System.out.println("after " + round + " rounds, Best path is:");
		int ans = answers.get(0);
		printPath(population[ans]);
		
		isConverged();  //Just want to print best cost;
		
	}
		
	public void initPopulation() {
		for(int i = 0; i < popSize; i++) {
			Random rand = new Random();
			int start = rand.nextInt(dim);
			//System.out.println(start);
			NearestNeighbor nb = new NearestNeighbor(distances, start);
			//System.out.println("hello");
			population[i] = nb.getPath();
		}
	}
	
	public void mutation(int[] path) {
		Random rand = new Random();	
		int actMut = rand.nextInt(mutationNum) + 1;
		for(int k = 0; k < actMut; k++) {
			//Random rand1 = new Random();
			int i = rand.nextInt(path.length);
			int j = rand.nextInt(path.length);
			int tmp = path[i];
			path[i] = path[j];
			path[j] = tmp;			
		}
		TwoOpt to = new TwoOpt(path, distances);
	}
	
	public void replace(int[] child) {
		int repl = -1;
		int distance = Integer.MAX_VALUE;
		for(int i = 0; i < popSize; i++) {
			int tmp = distance(child, population[i]);
			if(tmp < distance) {
				distance = tmp;
				repl = i;
			}
		}
		
		System.out.println("distance is " + distance + " repl is " + repl);
		
		ArrayList<Integer> bests = bestIndividuals();
		
		System.out.println(bests);
		
		if((distance <= replaceThreshold) && (!bests.contains(repl))) {
			System.out.println("replace");
			for(int i = 0; i < dim; i++)
				population[repl][i] = child[i];
		}
		else if((distance <= replaceThreshold) && (bests.contains(repl))) {
			int costc = cost(child);
			int costp = cost(population[repl]);
			System.out.println("child cost is " + costc + " parent cost is " + costp);
			if(costc < costp) {
				System.out.println("child beats parent");
				for(int i = 0; i < dim; i++)
					population[repl][i] = child[i];
			}
			else {
				System.out.println("child lost");
				int worst = worstIndividuals();
				System.out.println("replace worst " + worst);
				for(int i = 0; i < dim; i++)
					population[worst][i] = child[i];
			}			
		}
		else {
			int worst = worstIndividuals();
			System.out.println("replace worst " + worst);
			for(int i = 0; i < dim; i++)
				population[worst][i] = child[i];
		}
	}
	
	public static int distance(int[] path1, int[] path2) {
		int similar = 0;
		for(int i = 0; i < path1.length - 1; i++) {
			int city1 = path1[i];
			int city2 = path1[i + 1];
			if(DPX.isEdgeExists(city1, city2, path2))
				similar++;
		}
		
		return path1.length - 1 - similar;
	}
	
	public static int cost(int path[]) {
		int cost = 0;
		for(int i = 0; i < path.length - 1; i++)
			cost += distances[path[i]][path[i + 1]];
		
		//Change for loop.
		int plen = path.length;
		int startCity = path[0];
		int endCity = path[plen - 1];
		cost += distances[startCity][endCity];
		//end.
		
		return cost;
	}
	
	public static ArrayList<Integer> bestIndividuals() {
		int smallest = Integer.MAX_VALUE;
		int[] costs = new int[popSize];
		ArrayList<Integer> res = new ArrayList<Integer>();
		
		for(int i = 0; i < popSize; i++) {
			int cost = cost(population[i]);
			costs[i] = cost;
			if(cost < smallest)
				smallest = cost;
		}
		
		System.out.println("Best cost is " + smallest);
		
		for(int i = 0; i < popSize; i++) {
			if(costs[i] == smallest)
				res.add(i);
		}
		
		return res;
	}
	
	public static int worstIndividuals() {
		int cost = Integer.MIN_VALUE;
		int index = -1;
		for(int i = 0; i < popSize; i++) {
			int tmp = cost(population[i]);
			if(tmp > cost) {
				cost = tmp;
				index = i;
			}
		}
		
		System.out.println("worst cost is " + cost);
		return index;
	}
	
	public boolean isConverged() {
		int best = Integer.MAX_VALUE;
		int worst = Integer.MIN_VALUE;
		for(int i = 0; i < popSize; i++) {
			int tmp = cost(population[i]);
			if(tmp < best)
				best = tmp;
			if(tmp > worst)
				worst = tmp;
		}
		
		System.out.println(best);  //Just want to print best.
		
		if(worst - best < convergeThreshold) {
			System.out.println("converged: diff is " + (worst - best));
			System.out.println("Best cost is " + best);
			return true;
		}
		else {
			System.out.println("not converged: diff is " + (worst - best));
			return false;
		}		
	}
		
	public void printPop() {
		for(int i = 0; i < popSize; i++) {
			for(int j = 0; j < dim; j++) {
				System.out.print(population[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void printDistances() {
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				System.out.print(distances[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void printPath(int[] path) {
		for(int i = 0; i < path.length; i++)
			System.out.print(path[i] + " ");
		System.out.println();
	}
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		DistanceGraph dg = new DistanceGraph("D:\\att532.tsp");
		GeneticAlg ga = new GeneticAlg(dg);
		/*int[] parent1 = {5, 3, 9, 1, 2, 8, 0, 6, 7, 4};
		int[] parent2 = {1, 2, 5, 3, 9, 4, 8, 6, 0, 7};
		System.out.println(similar(parent1, parent2));*/
	}

}
