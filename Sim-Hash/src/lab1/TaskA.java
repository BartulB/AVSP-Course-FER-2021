package lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TaskA {
	
	private static int N;
	private static int Q;
	private static int I;
	private static int K;
	private static String[] hashes;

	public static void main(String[] args) {
		
		//try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/bartu/Downloads/lab1A_primjer/test2/R.in"))) {
			
			N = Integer.parseInt(reader.readLine());
			hashes = new String[N];
			
			for (int i=0; i<N; i++) {
				
				hashes[i] = Main.simhash(reader.readLine());
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
}
