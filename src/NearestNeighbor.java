
public class NearestNeighbor {
	private int[] path;
	
	public NearestNeighbor(int[][] distanceMatrix, int startCity) {
		path = new int[distanceMatrix[0].length];
		for(int i = 0; i < path.length; i++)
			path[i] = -1;
		path[0] = startCity;
		//path[distanceMatrix[0].length] = startCity;
		int currentCity = startCity;
		int i = 1;
		while(i < path.length) {
			int nextCity = findMin(distanceMatrix[currentCity]);
			if(nextCity != -1) {
				path[i] = nextCity;
				currentCity = nextCity;
				i++;
			}
		}
	}
	
	public int[] getPath() {
		return path;
	}
	
	private int findMin(int[] row) {
		int nextCity = -1;
		int i = 0;
		int min = Integer.MAX_VALUE;
		while(i < row.length) {
			if((isCityInPath(path, i) == false) && (row[i] < min)) {
				nextCity = i;
				min = row[i];
			}
			i++;
		}
		
		return nextCity;
	}
	
	private boolean isCityInPath(int[] path, int city) {
		for(int i = 0; i < path.length; i++) {
			if(path[i] == city)
				return true;
		}
		return false;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
