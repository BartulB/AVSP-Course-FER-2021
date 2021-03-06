import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeRank {
	
	private static int N, Q;
	
	public static void main(String[] args) {
		
		//try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

	    try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/bartu/Downloads/myTest.txt"))) {
	    	
	    	String[] firstLine = reader.readLine().split(" ");
	    	
	    	N = Integer.parseInt(firstLine[0]);
	    	
	    	double beta = Double.parseDouble(firstLine[1]);
	    	
	    	Map<Integer, List<Integer>> linkedNodes = new HashMap<>();
	    	
	    	for (int i=0; i<N; i++) {
	    		
	    		List<Integer> nodes = new ArrayList<>();
	    		
	    		String[] line = reader.readLine().split(" ");
	    		
	    		for (int j=0; j<line.length; j++) {
	    			
	    			Integer node = Integer.parseInt(line[j]);
	    			
	    			nodes.add(node);
	    		}
	    		
	    		linkedNodes.put(i, nodes);
	    	}
	    	
	    	Q = Integer.parseInt(reader.readLine());
	    	
	    	String line;
	    	
	    	List<Double[]> listOfCurrentRanks = new ArrayList<>();
	    	
	    	Double[] currentRanks = new Double[N];
	    	
	    	double preIterationRank = 1.0 / N;
	    	
	    	for (int i=0; i<N; i++) {
				
				currentRanks[i] = preIterationRank;			
			}
	    	
	    	listOfCurrentRanks.add(currentRanks);
	    	
	    	while ((line = reader.readLine()) != null) {
				
				if(line.isEmpty()) break;
								
				int node = Integer.parseInt(line.split(" ")[0]);
				
				int iteration =  Integer.parseInt(line.split(" ")[1]);
				
				List<Double[]> ranks = calculateRankIteration(linkedNodes, listOfCurrentRanks, iteration, beta);
				
				Double[] ranksOfIteration = ranks.get(iteration);
				
				double result = ranksOfIteration[node];
				
		    	//print
		    	DecimalFormat df = new DecimalFormat("0.0000000000");
				
				BigDecimal bd = new BigDecimal(result);
				
				BigDecimal res = bd.setScale(10, RoundingMode.HALF_UP);
				
				System.out.println(df.format(res));
	    	}
	    }
	    catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	
	private static List<Double[]> calculateRankIteration(Map<Integer, List<Integer>> linkedNodes, List<Double[]> ranks, int iteration, double beta) {
	
		double preIterationRank = 1.0 / N;
	
		double teleportRank = (1 - beta) / N;
		
		Double[] currentRanks = new Double[N];
		Double[] listOfNexts = new Double[N];
		
		for (int i=0; i<N; i++) {
			
			listOfNexts[i] = teleportRank;
		}
		
		
		
		while (ranks.size() <= iteration) {
			
			currentRanks = ranks.get(ranks.size() - 1);
		
			for (int i=0; i<N; i++) {
				
				List<Integer> nextNodes = linkedNodes.get(i);
								
				for (Integer next : nextNodes) {
					
					double nextRank = listOfNexts[next];
										
					nextRank += (beta * currentRanks[i] / nextNodes.size());
					
					listOfNexts[next] = nextRank;	
				}
			}
			
			ranks.add(listOfNexts);	
			
			listOfNexts = new Double[N];
			
			for (int i=0; i<N; i++) {
				
				listOfNexts[i] = teleportRank;
			}
		}
		return ranks;
	}
}
