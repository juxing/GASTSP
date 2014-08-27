
public class TwoOpt {
	private int[] path;
	private int[][] distanceMatrix;
	
	/**
	 * @param args
	 */
	public TwoOpt(int[] inputPath, int[][] inputDistanceMatrix) {
		distanceMatrix = inputDistanceMatrix;
		path = inputPath;
		
		int bestGain = 0;
		int bestI = -1;
		int bestJ = -1;
		boolean mark = true;
		
		while(mark) {
			bestGain = 0;
			for(int i = 0; i < path.length - 1; i++) {
				for(int j = i + 1; j < path.length; j++) {
					int gain = computeGain(i, j);
					if(gain < bestGain) {
						bestGain = gain;
						bestI = i;
						bestJ = j;
					}
				}
			}
			
			if(bestGain < 0) {
				//System.out.println("Gain is " + bestGain);
				exchange(bestI, bestJ);
			}
			else {
				mark = false;
			}
		}
	}
	
	private int computeGain(int cityIndex1, int cityIndex2) {
		int firstDiff = 0;
		int secondDiff = 0;
		
		if(cityIndex1 == 0) {
			firstDiff = 0;
		}
		else {
			int src1 = path[cityIndex1 - 1];
			firstDiff = distanceMatrix[src1][path[cityIndex2]] - distanceMatrix[src1][path[cityIndex1]];
		}
		
		if(cityIndex2 == path.length - 1) {
			secondDiff = 0;
		}
		else {
			int dest1 = path[cityIndex2 + 1];
			secondDiff = distanceMatrix[path[cityIndex1]][dest1] - distanceMatrix[path[cityIndex2]][dest1];
		}
		
		return firstDiff + secondDiff;
		
	}
	
	private void exchange(int cityIndex1, int cityIndex2) {
		//System.out.println("exchange " + cityIndex1 + " and " + cityIndex2);
		/*int src1 = path[cityIndex1 - 1];
		int dest1 = path[cityIndex2 + 1];*/
		//System.out.println("src1 is " + src1 + " dest1 is " + dest1);
		int[] newPath = new int[path.length];
		/*newPath[0] = path[0];
		newPath[path.length - 1] = path[path.length - 1];*/
		int indexOfNewPath = 0;
		int i = 0;
		while(i <= cityIndex1 - 1) {
			newPath[indexOfNewPath] = path[i];
			i++;
			indexOfNewPath++;
		}
		i = cityIndex2;
		while(i >= cityIndex1) {
			//System.out.println("i is " + i + " " + "cityIndex1 " + cityIndex1);
			newPath[indexOfNewPath] = path[i];
			i--;
			indexOfNewPath++;
		}
		i = cityIndex2 + 1;
		while(i < path.length) {
			newPath[indexOfNewPath] = path[i];
			indexOfNewPath++;
			i++;
		}
		
		for(i = 0; i < path.length; i++) {
			path[i] = newPath[i];
			//System.out.print(path[i] + " ");
		}
		//System.out.println();
				
	}
	
	public int[] getPath() {
		return path;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
