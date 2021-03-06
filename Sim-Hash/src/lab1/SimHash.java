package lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

public class SimHash {
	
	private static int N;
	private static int Q;
	private static int I;
	private static int K;
	private static String[] hashes;

	public static void main(String[] args) {
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			
			N = Integer.parseInt(reader.readLine());
			hashes = new String[N];
			
			for (int i=0; i<N; i++) {
				
				hashes[i] = simhash(reader.readLine());
			}
			
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
	* Returns number of similar texts with text in position I.  
	*
	* @param  position   position of text in document that is going to be used to find similar
	* @param  distance	 maximum distance texts may vary
	*/
	private static int getNumberOfSimilar(int position, int distance) {
		
		int count = 0;
		
		String hashInPositionI = hashes[position];
		
		for (String hash : hashes) {
			
			int hammingDistance = getHammingDistance(hashInPositionI, hash);
			
			if (hammingDistance <= distance) {
				
				count++;
			}
		}
		
		return count - 1;
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
		
		//System.out.println(hexStr);
		
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
