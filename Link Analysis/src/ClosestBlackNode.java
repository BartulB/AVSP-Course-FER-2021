import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClosestBlackNode {
	
	private static int N, e;
	
	private static List<Node> nodeList = new ArrayList<>();
	
	private static LinkedList<Node> queue = new LinkedList<>();
	
	private static int maxDistance = Integer.MAX_VALUE;
	
	private static int undetermined = -1;

	public static void main(String[] args) {

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

		//try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/bartu/Downloads/btest2/R.in"))) {
	    	
	    	String[] firstLine = reader.readLine().split(" ");
	    	
	    	N = Integer.parseInt(firstLine[0]);
	    	
	    	e = Integer.parseInt(firstLine[1]);
	    	
	    	for (int i=0; i<N; i++) {
	    		
	    		int color = Integer.parseInt(reader.readLine());
	    		
	    		int distance = maxDistance;
	    		
	    		int closestBlack = undetermined;
	    		
	    		if (color == 1) {
	    			
	    			distance = 0;
	    			
	    			closestBlack = i;
	    		}
	    		
	    		//Node node = new Node(i, color, distance, new ArrayList<Node>(), closestBlack);
	    		
	    		Node node = new Node();
	    		
	    		node.index = i;
	    		node.color = color;
	    		node.distanceToBlack = distance;
	    		node.neighbours = new ArrayList<Node>();
	    		node.closestBlack = closestBlack;
	    		
	    		nodeList.add(node);
	    	}
	    	
	    	String line;
	    	
	    	while ((line = reader.readLine()) != null) {
				
				if(line.isEmpty()) break;
				
				String[] edge = line.split(" ");
				
				int nodeIndex1 = Integer.parseInt(edge[0]);
				
				int nodeIndex2 = Integer.parseInt(edge[1]);
				
				Node node1 = nodeList.get(nodeIndex1);
				
				Node node2 = nodeList.get(nodeIndex2);
				
				addNodeNeighbour(node1, node2);
				
				addNodeNeighbour(node2, node1);
	    	}
	    	
	    }
	    catch (IOException e) {

			e.printStackTrace();
		}
	    
	    BFS();
	    
	    print();
	}
	
	private static void BFS() {
		
		boolean visited[] = new boolean[N];
		
		for (Node node : nodeList) {
			
			if (node.color == 1) {
				
				queue.add(node);
			}
		}
		
		Node currentNode = queue.peek();
		
		visited[currentNode.index] = true;
		
		while (queue.size() != 0) {
			
			currentNode = queue.poll();
			
			List<Node> neighbours = currentNode.neighbours;
			
			Iterator<Node> iterator = neighbours.listIterator();
			
			while (iterator.hasNext()) {
				
				Node node = iterator.next();
				
				if (!visited[node.index] && node.color == 0) {
					
					visited[node.index] = true;
					
					queue.add(node);
					
					if (node.distanceToBlack > currentNode.distanceToBlack + 1) {
						
						node.closestBlack = currentNode.closestBlack;
						
						node.distanceToBlack = currentNode.distanceToBlack + 1;
					}
				}
			}
		}
	}
	
	private static void print() {
		
		for (Node node : nodeList) {
			
			if (node.distanceToBlack > 10) {
				
				node.distanceToBlack = -1;
				
				node.closestBlack = -1;
			}
			
			System.out.println(node.closestBlack + " " + node.distanceToBlack);
		}
	}
	
	private static void addNodeNeighbour(Node node1, Node node2) {
		
		if (!node1.neighbours.contains(node2)) {
			
			nodeList.get(node1.index).neighbours.add(node2);
		}
	}
	
	private static class Node {
		
		private int index;
		private int distanceToBlack;
		private int color;
		private List<Node> neighbours = new ArrayList<>();
		private int closestBlack;
		
		public Node() {}
		
		public Node(int index, int color, int distance, List<Node> neighbours, int closestBlack) {
			
			index = this.index;
			distance = this.distanceToBlack;
			color = this.color;
			neighbours = this.neighbours;
			closestBlack = this.closestBlack;
		}
	}
}
