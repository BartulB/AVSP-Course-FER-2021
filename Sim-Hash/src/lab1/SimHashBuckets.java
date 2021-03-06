package lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

public class SimHashBuckets {
	
	private static int N;
	private static int Q;
	private static int I;
	private static int K;
	private static String[] hashes;
	private static Map<Integer, Set<Integer>> candidates = new HashMap<>();
	private static int b = 8;
	private static int r = 16;

	public static void main(String[] args) {
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			
			N = Integer.parseInt(reader.readLine());
			
			hashes = new String[N];
			
			for (int i=0; i<N; i++) {
				
				hashes[i] = simhash(reader.readLine());
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
				
		if(possibleCandidates != null) {
			
			for (Integer candidat : possibleCandidates) {
				
				String hash = hashes[candidat];
				
				int hammingDistance = getHammingDistance(hashInPositionI, hash);
				
				if (hammingDistance <= distance) {
					
					count++;
				}
				
			}
		}
		
		return count;
	}
	
	/**
	* Returns Hamming distance between two binary numbers.  
	*
	* @param  s1   hash in position I in hashes
	* @param  s2   next hash in hashes
	*/
	public static int getHammingDistance(String s1, String s2) {
		
		int i = 0, count = 0;
		
	    while (i < s1.length()) {
	    	
	        if (s1.charAt(i) != s2.charAt(i)) {
	            
	        	count++;
	        }
	        i++;
	    }
	    return count;
	}

	/**
	* Returns compressed text using SimHash algorithm.  
	*
	* @param  text   a string that is going to be compressed
	*/
	public static String simhash(String text) {
		
		int[] sh = new int[128];
		
		String[] individuals = text.split(" ");
		
		for (String individual : individuals) {
			
			byte[] hash = DigestUtils.md5(individual);
			
			for (int i=0; i<hash.length; i++) {
				
				for (int j=0; j<8; j++) {
					
					int index = i * 8 + j;

					int bit = (hash[i] >> (8 - (j + 1))) & 1;
									
					if (bit == 1) {
						
						sh[index] += 1;
					}
					else {
						
						sh[index] -= 1;
					}
				}
			}
		}

		for (int i=0; i<sh.length; i++) {
			
			if (sh[i] >= 0) {
				
				sh[i] = 1;
			}
			else {
				
				sh[i] = 0;
			}
		}
				
		String binaryStr = formatString(Arrays.toString(sh));
		
		String hexStr = new BigInteger(binaryStr, 2).toString(16);
		
//		System.out.println(hexStr);
		
		return binaryStr;
	}
	
	/**
	* Returns formatted string.  
	*
	* @param  string	string that is going to be formatted
	*/
	private static String formatString(String string) {
		
		String formattedString = string
			    .replace(",", "")  //remove the commas
			    .replace("[", "")  //remove the right bracket
			    .replace("]", "")  //remove the left bracket
			    .replace(" ", "")  //remove white spaces
			    .trim();   		   //remove trailing spaces from partially initialized arrays
		
		return formattedString;
	}
}
