package lab1;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;

public class Main {

	public static void main(String[] args) {

		System.out.println(simhash("fakultet elektrotehnike i racunarstva"));
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
		
		System.out.println(hexStr);
						
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
