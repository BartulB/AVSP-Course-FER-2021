import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PCY {
	
	private static int numOfBaskets, numOfCases, threshold;
	
	private static float support;

	private static List<Integer[]> buckets = new ArrayList<Integer[]>();;
	
	private static Map<Integer, Integer> items = new HashMap<>(), cases = new HashMap<>();
	
	private static Map<String, Integer> pairs = new HashMap<>();

	public static void main(String[] args) {
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

        //try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/bartu/Downloads/test2/R.in"))) {
        	
        	numOfBaskets = Integer.parseInt(reader.readLine());
        	
        	support = Float.parseFloat(reader.readLine());
        	
        	threshold = (int) (support * numOfBaskets);
        	
        	numOfCases = Integer.parseInt(reader.readLine());
        	
        	String line;
        	
        	while ((line = reader.readLine()) != null) {
				
				if(line.isEmpty()) break;
				
				buckets.add(
						Stream.of(line.split(" "))
							  .mapToInt(Integer::parseInt)
							  .boxed()
							  .toArray(Integer[]::new));
        	}
        	
        	firstPass();
        	        	        	
        	secondPass();
        	        	
        	thirdPass();
        	        	
        	Print();
        	
        } 
        catch (FileNotFoundException e) {
			
        	e.printStackTrace();
		} 
        catch (IOException e) {

        	e.printStackTrace();
		}
	}
	
	/**
	* Returns number of each item in all buckets
	* 
	*/
	private static void firstPass() {
				
		for (Integer[] bucket : buckets) {
			
			for (Integer item : bucket) {
				
				items.putIfAbsent(item, 0);
				
				items.put(item, items.get(item) + 1);
			}
		}		
	}
	
	/**
	* Returns compressed cases and number of each case
	*
	*/
	private static void secondPass() {
						
		for (Integer[] bucket : buckets) {
						
			for (int i=0; i<bucket.length; i++) {
				
				for (int j=i+1; j<bucket.length; j++) {
					
					if (passesTheThreshold(bucket[i], bucket[j])) {
						
						Integer k = getHash(bucket[i], bucket[j]);
												
						cases.putIfAbsent(k, 0);
						
						cases.put(k, cases.get(k) + 1);
					}
				}
			}
		}		
	}

	/**
	* Returns all pairs of items and number of pair repeat
	*  
	*/
	private static void thirdPass() {
				
		for (Integer[] bucket : buckets) {
			
			for (int i=0; i<bucket.length; i++) {
				
				for (int j=i+1; j<bucket.length; j++) {
										
					String pair = bucket[i] + "," + bucket[j];
					
					if (passesTheThreshold(bucket[i], bucket[j])) {
						
						Integer k = getHash(bucket[i], bucket[j]);
						
						if (cases.get(k) >= threshold) {
							
							pairs.putIfAbsent(pair, 0);
							
							pairs.put(pair, pairs.get(pair) + 1);
						}
					}
				}
			}
		}
	}
	
	/**
	* Prints result in given form   
	*
	*/
	private static void Print() {
		
		int m = 0;
		
		for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
			
			if ( entry.getValue() >= threshold) {
				
				m++;
			}
		}
		
		System.out.println((m * (m - 1)) / 2);
		
		System.out.println(pairs.size());
		
		List<Integer> valueList = new ArrayList<Integer>(pairs.values());
		
		Collections.sort(valueList, Collections.reverseOrder());
		
		for (Integer value : valueList) {
			
			System.out.println(value);
		}
		
	}
	
	/**
	* Returns	hash number (compressed pair of items) 
	*
	* @param item1	item in bucket
	* @param item2	item in bucket
	*/
	private static Integer getHash(Integer item1, Integer item2) {
		
		return ((item1 * items.size()) + item2) % numOfCases;
	}
	
	/**
	* Returns	true if both items are frequent items
	*
	* @param item1	item in bucket
	* @param item2	item in bucket
	*/
	private static boolean passesTheThreshold(Integer item1, Integer item2) {
		
		return (items.get(item1) >= threshold) && (items.get(item2) >= threshold);
	}
}
