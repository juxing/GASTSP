import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

class Coordinate {
	public int x;
	public int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

public class DistanceGraph {
	private int dim = 0;
	private int[][] distances;
	
	public DistanceGraph(String fileName) {
		try {
			List<Coordinate> cors = new ArrayList<Coordinate>();
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while(reader.ready()) {                
                String line = reader.readLine();
                line.trim();
                String[] data = line.split("\\s+");
                if(!data[0].equals("EOF")) {
                	int x = Integer.parseInt(data[1]);
                	int y = Integer.parseInt(data[2]);
                	//System.out.println(data[2]);
                	Coordinate co = new Coordinate(x, y);
                	cors.add(co);
                }                
            }
            
            dim = cors.size();
	    	distances = new int[dim][dim];	    	
	    	for(int i = 0; i < dim; i++) {
	    		for(int j = 0; j <= i; j++) {
	    			if(i == j) {
	    				distances[i][j]=0;
	    			}
	    			else {
	    				Coordinate pos1 = cors.get(i);
	    				Coordinate pos2 = cors.get(j);
	    				int d = (int)Math.sqrt((pos1.x - pos2.x) * (pos1.x - pos2.x) + (pos1.y - pos2.y) * (pos1.y - pos2.y));
	    				distances[i][j] = d;
	    				distances[j][i] = d;
	    			}	
	    		}
	    	}
	    	
	    	File file = new File(fileName + ".dis");
	    	FileWriter out = new FileWriter(file);
	    	for(int i = 0; i < dim; i++) {
	    		for(int j=0; j<dim; j++) {
	    			int t = distances[i][j];
	    			out.write(t + " ");
	    		}
	    		out.write("\r\n");
	    	}
	    	out.close();
	    	
	    	//printDistances();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			/*String s;
	    	System.out.println("Please input the dimension:");
	    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    	s = br.readLine();
	
	    	dim = Integer.parseInt(s);
	    	distances = new int[dim][dim];
	    	File file = new File("D:\\distances.txt");
	    	FileWriter out = new FileWriter(file);
	    	
	    	for(int i = 0; i < dim; i++) {
	    		for(int j = 0; j <= i; j++) {
	    			if(i == j) {
	    				distances[i][j]=0;
	    			}
	    			else {
	    				int rand = (int)(Math.random()*90 + 10);  //[10, 100)
	    				distances[i][j]=rand;
	    				distances[j][i]=rand;
	    			}	
	    		}
	    	}
	    	
	    	printDistances();
	    	
	    	for(int i = 0; i < dim; i++) {
	    		for(int j=0;j<dim;j++) {
	    			int t = distances[i][j];
	    			out.write(t + " ");
	    		}
	    		out.write("\r\n");
	    	}
	    	out.close();*/
		
    }
	
	//Need to change the cost calc.
	public int calcOptPathCost(String fileName) {		
		int cost = 0;
		try {
			List<Integer> path = new ArrayList<Integer>();
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(fileName));
			while(reader.ready()) {                
	            String line = reader.readLine();
	            String[] data = line.split("\\s+");
	            if(!data[0].equals("-1")) {
	            	int x = Integer.parseInt(data[0]) - 1;
	            	path.add(x);
	            }   
	            else
	            	break;
	        }
			
			System.out.println(path);
			for(int i = 0; i < path.size() - 1; i++) {
				int tmp = distances[path.get(i + 1)][path.get(i)];
				cost += tmp;
			} 
			cost += distances[path.get(0)][path.get(path.size() - 1)];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cost;
	}
	
	public int getDim() {
		return dim;
	}
	
	public int[][] getDistances() {
		return distances;
	}
	
	public void printDistances() {
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				System.out.print(distances[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DistanceGraph dg = new DistanceGraph("D:\\kroA100.tsp");
		
		int cost = dg.calcOptPathCost("D:\\kroA100.opt.tour");
		System.out.println(cost);
		/*int dim = dg.getDim();
		System.out.println(dg.getDim());
		int[][] tmp = dg.getDistances();
		System.out.println(tmp[dim-1][dim-1]);*/
	}

}
