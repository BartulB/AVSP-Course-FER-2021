import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CF {
	
	private static int numOfItems, numOfUsers;
	
	private static double matrixItemItem[][], matrixUserUser[][];
	
	private static double matrixItemItemAvg[][], matrixUserUserAvg[][];
	
	private static final Integer ZERO = 0;	
	
	public static void main(String[] args) {
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
					
			String[] firstLine = reader.readLine().split(" ");
			
			numOfItems = Integer.parseInt(firstLine[0]);
			
			numOfUsers = Integer.parseInt(firstLine[1]);
			
			matrixItemItem = new double[numOfItems][numOfUsers];
			matrixUserUser = new double[numOfUsers][numOfItems];
			
			for (int i=0; i<numOfItems; i++) {
				
				String[] itemRatings = reader.readLine().split(" ");
				
				for (int j=0; j<numOfUsers; j++) {
					
					String itemRating = itemRatings[j];
					
					if (itemRating.equals("X")) {
						
						matrixItemItem[i][j] = ZERO;
						matrixUserUser[j][i] = ZERO;
					}
					else {
						
						matrixItemItem[i][j] = Double.parseDouble(itemRating);
						matrixUserUser[j][i] = Double.parseDouble(itemRating);
					}
				}
			}
			
			matrixItemItemAvg = normalizeMatrix(matrixItemItem, numOfItems, numOfUsers);
			
			matrixUserUserAvg = normalizeMatrix(matrixUserUser, numOfUsers, numOfItems);
			
			Integer Q = Integer.parseInt(reader.readLine());
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				
				if(line.isEmpty()) break;
				
				double result = 0;
				
				String[] lineArray = line.split(" ");
				
				int I = Integer.parseInt(lineArray[0]);
				int J = Integer.parseInt(lineArray[1]);
				int T = Integer.parseInt(lineArray[2]);
				int K = Integer.parseInt(lineArray[3]);
				
				if (T == 0) {
				
					result = evaluateSimilarity(matrixItemItem, matrixItemItemAvg, I-1, J-1, numOfItems, numOfUsers, K);
				}
				else {
					
					result = evaluateSimilarity(matrixUserUser, matrixUserUserAvg, J-1, I-1, numOfUsers, numOfItems, K);
				}
				
				DecimalFormat df = new DecimalFormat("#.000");
				
				BigDecimal bd = new BigDecimal(result);
				
				BigDecimal res = bd.setScale(3, RoundingMode.HALF_UP);
				
				System.out.println(df.format(res));
			}
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	/**
	* Returns	list of average rating for each row  
	*
	* @param matrix		matrix of item grades by users
	* @param rows		number of rows in matrix
	* @param columns	number of columns in matrix
	*/
	private static ArrayList<Double> findAverageRatings(double[][] matrix, int rows, int columns) {
		
		ArrayList<Double> avgRatings = new ArrayList<>();
		
		Double avgRating = (double) 0;
		
		for (int i=0; i<rows; i++) {
			
			double ratingSum = 0;
			
			int counter = 0;
			
			for (int j=0; j<columns; j++) {
				
				if (matrix[i][j] != ZERO) {
					
					ratingSum += matrix[i][j];
					
					counter++;
				}
			}
			
			if (counter != 0) {
				
				avgRating = ratingSum / counter;	
			}
			
			avgRatings.add(avgRating);	
		}
		
		return avgRatings;
	}
	
	/**
	* Returns	normalized matrix - from each grade averageRating is subtracted 
	*
	* @param matrix		matrix of item grades by users
	* @param rows		number of rows in matrix
	* @param columns	number of columns in matrix
	*/
	private static double[][] normalizeMatrix(double[][] matrix, int rows, int columns) {
		
		ArrayList<Double> avgRatings = findAverageRatings(matrix, rows, columns);
		
		double[][] matrixAvg = new double[rows][columns];
		
		for (int i=0; i<rows; i++) {
			
			Double avgRating = avgRatings.get(i);
			
			for (int j=0; j<columns; j++) {
				
				if (matrix[i][j] != ZERO) {
					
					matrixAvg[i][j] = matrix[i][j] - avgRating;
				}	
				else {
					
					matrixAvg[i][j] = ZERO;	
				}
			}
		}
		return matrixAvg;
	}

	/**
	* Returns	recommendation grade   
	*
	* @param matrix		matrix of item grades by users
	* @param matrixAvg	matrix of average item grades by users
	* @param I			item in matrix
	* @param J			user in matrix
	* @param rows		number of rows in matrix
	* @param columns	number of columns in matrix
	* @param K			cardinality number - how many grades will be taken to find recommendation
	*/
	private static Double evaluateSimilarity(double[][] matrix, double [][] matrixAvg, int I, int J, int rows, int columns, int K) {
		
		Map<Integer, Double> similarities = new HashMap<>();
		
		double[] row = matrixAvg[I];
		
		for (int i=0; i<rows; i++) {
			
			double cosineNumerator = 0, cosineDenominatorSum1 = 0, cosineDenominatorSum2 = 0, cosineDenominator;
			double cosine;
			
			for (int j=0; j<columns; j++) {
				
				cosineNumerator += matrixAvg[i][j] * row[j];
				
				cosineDenominatorSum1 += Math.pow(row[j], 2);
				
				cosineDenominatorSum2 += Math.pow(matrixAvg[i][j], 2);
			}
			
			cosineDenominator = Math.sqrt(cosineDenominatorSum1 * cosineDenominatorSum2);
			
			if (i == I) {
				
				cosine = 1;
			}
			else {
				
				cosine = cosineNumerator / cosineDenominator;
			}
		
			similarities.put(i, cosine);
		}
		
		Map<Integer, Double> similaritiesSorted = 
			     similarities.entrySet().stream()
			    .sorted(Map.Entry.<Integer, Double> comparingByValue().reversed())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		
		int counter = 0, i = 1;
		double grade = 0, similaritiesSum = 0;
		
		while (counter < K && i < similaritiesSorted.size()) {
			
			double similarity = (double) similaritiesSorted.values().toArray()[i];
			
			if (similarity > 0) {
				
				int position = (int) similaritiesSorted.keySet().toArray()[i];
				
				double r = matrix[position][J];
				
				if (r > 0) {
					
					grade += similarity * r;
					
					similaritiesSum += similarity;
					
					counter++;
				}
			}
			else {
				
				break;
			}
			
			i++;
		}

		double recommendation = grade / similaritiesSum;

		return recommendation;		
	}	
}
