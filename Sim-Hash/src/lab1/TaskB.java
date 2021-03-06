package lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TaskB {
	
	private static int N;
	private static int Q;
	private static int I;
	private static int K;
	private static String[] hashes;
	private static Map<Integer, Set<Integer>> candidates = new HashMap<>();
	private static int b = 8;
	private static int r = 16;

	public static void main(String[] args) {
		
		//try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/bartu/Downloads/lab1B_primjer/test0/R.in"))) {
			
			N = Integer.parseInt(reader.readLine());
			
			hashes = new String[N];
			
			for (int i=0; i<N; i++) {
				
				hashes[i] = Main.simhash(reader.readLine());
			}
			
			algorithmLSH();
			
			Q = Integer.parseInt(reader.readLine());
			
			String line;
			String[] lineArray;
			
			while ((line = reader.readLine()) != null) {
				
				if(line.isEmpty()) break;
				
				lineArray = line.split(" ");
				
				I = Integer.parseInt(lineArray[0]);
				
				K = Integer.parseInt(lineArray[1]);
				
				int numberOfSimilar = getNumberOfSimilar(I, K);
				
				System.out.println(numberOfSimilar);
			}
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Gets all possible candidates for similarity.  
	*
	*/
	private static void algorithmLSH(){
		
		for (int i=1; i<=b; i++) {
			
			Map<Integer, Set<Integer>> buckets = new HashMap<>();
			
			for (int j=0; j<N; j++) {
				
				String hash = hashes[j];
				
				int val = hashToint(i, hash);
				
				Set<Integer> textsInBucket = new HashSet<>();
				
				if (buckets.get(val) != null) {
					
					textsInBucket = buckets.get(val);
					
					for (Integer textId : textsInBucket) {
						
						if(candidates.get(j) == null) {
							
							candidates.put(j, new HashSet<>());
						}
						candidates.get(j).add(textId);
						
						if(candidates.get(textId) == null) {
							
							candidates.put(textId, new HashSet<>());
						}
						candidates.get(textId).add(j);
					}
				}
				else {
					
					textsInBucket.clear();
				}
				
				textsInBucket.add(j);
				
				buckets.put(val, textsInBucket);
			}
		}
		
	}
	
	/**
	* Returns decimal number that is representation of 16 bit binary number.  
	*
	* @param  band   indicates number of band
	* @param  hash	 hash that contains 128 bits
	*/
	private static int hashToint(int band, String hash) {
		
		int lowerBoundary = 0 + band*r - r;
		
		int upperBoundary = lowerBoundary + r;
		
		hash = hash.substring(lowerBoundary, upperBoundary);
				
		return Integer.parseInt(hash, 2);
	}
	
	/**
	* Returns number of similar texts with text in position I.  
	*
	* @param  position   position of text in document that is going to be used to find similar
	* @param  distance	 maximum distance texts may vary
	*/
	private static int getNumberOfSimilar(int position, int distance) {
		
		int count = 0;
		
		String hashInPositionI = hashes[position];
		
		Set<Integer> possibleCandidates = candidates.get(position);
		
		//System.out.println(possibleCandidates);
		
		if(possibleCandidates != null) {
			
			for (Integer candidat : possibleCandidates) {
				
				String hash = hashes[candidat];
				
				int hammingDistance = TaskA.getHammingDistance(hashInPositionI, hash);
				
				if (hammingDistance <= distance) {
					
					count++;
				}
				
			}
		}
		
		return count;
	}
}
