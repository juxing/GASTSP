import java.util.ArrayList;
import java.util.Random;

public class DPX {
	private int[] offspring;
	ArrayList<ArrayList<Integer>> fragments;
	private int[] parent1;
	private int[] parent2;
	private int[][] distancesMatrix;
	
	public DPX(int[] parent1, int[] parent2, int[][] distances) {
		this.parent1 = parent1;
		this.parent2 = parent2;
		this.distancesMatrix = distances;
		offspring = new int[parent1.length];
		fragments = new ArrayList<ArrayList<Integer>>();
		deleteEdges();
		//printFrag();
		greedyReconnect();
		//printChild();
	}
	
	public void deleteEdges() {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		int i = 0;
		while(i < parent1.length) {
			if(i == parent1.length - 1) {
				tmp.add(parent1[i]);
				fragments.add(tmp);
				i++;
			}
			else {
				int city1 = parent1[i];
				int city2 = parent1[i + 1];				
				if(isEdgeExists(city1, city2, parent2)) {
					tmp.add(city1);
					i++;
				}
				else {
					tmp.add(city1);
					fragments.add(tmp);
					tmp = new ArrayList<Integer>();
					i++;
				}
			}			
		}
	}
	
	public void greedyReconnect() {
		int indexOfPath = 0;		
		Random rand = new Random();
		int index = rand.nextInt(fragments.size());
		//System.out.println("Insert index is " + index);
		ArrayList<Integer> current = fragments.get(index);
		int order = rand.nextInt(2);	
		//System.out.println("Insert order is " + order);
		ArrayList<Integer> ends = new ArrayList<Integer>();
		for(int i = 0; i < fragments.size(); i++) {
			if(i != index) {
				ArrayList<Integer> tmp = fragments.get(i);
				if(tmp.size() == 1)
					ends.add(tmp.get(0));
				else {
					ends.add(tmp.get(0));
					ends.add(tmp.get(tmp.size() - 1));
				}				
			}
		}
		
		/*for(int i = 0; i < ends.size(); i++) {
			System.out.print(ends.get(i) + " ");
		}
		System.out.println();*/
		
		int indexOfFragments = 0;
		while(indexOfFragments < fragments.size()) {
			if(order == 0) {
				for(int i = 0; i < current.size(); i++) {
					offspring[indexOfPath] = current.get(i);
					indexOfPath++;
				}
			}
			else {
				for(int i = current.size() - 1; i >= 0; i--) {
					offspring[indexOfPath] = current.get(i);
					indexOfPath++;
				}
			}
			
			//printChild();
			
			ends.remove(current.get(0));
			ends.remove(current.get(current.size() - 1));
			if(ends.isEmpty())
				break;
			/*for(int i = 0; i < ends.size(); i++) {
				System.out.print(ends.get(i) + " ");
			}
			System.out.println();*/
			
			int currentCity = offspring[indexOfPath - 1];
			int nextCity = findMin(currentCity, ends);
			//System.out.println("Next city is " + nextCity);
			int loc[] = findFragment(nextCity);
			current = fragments.get(loc[0]);
			order = loc[1];
			
			indexOfFragments++;
		}
				
		//printChild();		
		/*if(loc[1] == 0) {
			for(int i = 0; i < fragments.get(loc[0]).size(); i++) {
				offspring[indexOfPath] = fragments.get(loc[0]).get(i);
				indexOfPath++;
			}
		}
		else if(loc[1] == 1) {
			for(int i = fragments.get(loc[0]).size() - 1; i >= 0; i--) {
				offspring[indexOfPath] = fragments.get(loc[0]).get(i);
				indexOfPath++;
			}
		}		
		printChild();		
		System.out.println("Index is " + loc[0] + " order is " + loc[1]);
		System.out.println("Nearest city is " + nextCity);*/
		
	}
	
	public static boolean isEdgeExists(int city1, int city2, int[] path) {
		int before, after;
		
		for(int i = 0; i < path.length; i++) {			
			if(path[i] == city1) {			
				if(i == 0)
					before = i;
				else
					before = i - 1;
				
				if(i == path.length - 1)
					after = i;
				else 
					after = i + 1;
				
				if((path[before] == city2) || (path[after] == city2))
					return true;
			}			
		}
		
		return false;
	}
	
	public int findMin(int currentCity, ArrayList<Integer> ends) {
		int min = Integer.MAX_VALUE;
		int nearestCity = -1;
		for(int i = 0; i < ends.size(); i++) {
			int city = ends.get(i);
			if(!isEdgeExists(currentCity, city, parent1) && !isEdgeExists(currentCity, city, parent2)) {
				if(distancesMatrix[currentCity][city] < min) {
					min = distancesMatrix[currentCity][city];
					nearestCity = city;
				} 
			}
		}
		
		if(nearestCity == -1) {
			Random rand = new Random();
			int tmp = rand.nextInt(ends.size());
			nearestCity = ends.get(tmp);
			//System.out.println("I am random.");
		}
		
		return nearestCity;
	}
	
	public int[] findFragment(int city) {
		int[] res = new int[2];
		res[0] = res[1] = -1;
		for(int i = 0; i < fragments.size(); i++) {
			ArrayList<Integer> tmp = fragments.get(i);
			if(tmp.get(0) == city) {
				res[0] = i;
				res[1] = 0;
				return res;
			}
			else if(tmp.get(tmp.size() - 1) == city) {
				res[0] = i;
				res[1] = 1;
				return res;
			}
		}
		
		return res;
	}
	
	public void printFrag() {
		for(int i = 0; i < fragments.size(); i++) {
			for(int j = 0; j < fragments.get(i).size(); j++) {
				System.out.print(fragments.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}
	
	public void printChild() {
		for(int i = 0; i < offspring.length; i++)
			System.out.print(offspring[i] + " ");
		System.out.println();
	}
	
	public int[] getChild() {
		return offspring;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*int[] path = {1, 2, 3, 4, 5};
		int city1 = 2;
		int city2 = 1;
		System.out.println(isEdgeExists(city1, city2, path));*/
		/*DistanceGraph dg = new DistanceGraph();
		int[] parent1 = {5, 3, 9, 1, 2, 8, 0, 6, 7, 4};
		int[] parent2 = {5, 3, 9, 1, 2, 8, 0, 6, 7, 4};*/
		//int[] parent2 = {1, 2, 5, 3, 9, 4, 8, 6, 0, 7};
		/*int[][] distances = dg.getDistances();
		DPX dpx = new DPX(parent1, parent2, distances);*/
		//dpx.printFrag();		
	}
}
