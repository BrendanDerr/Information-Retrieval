/**
 * @author Brendan Derr
 */
package info_assg1;

	import java.util.*;
import java.util.Map.Entry;
import java.io.*;    
	 
	public class assg1 {  			
		//tested works
		/**
		 * The removeNums method removes numbers from a given string.
		 * @param line the given string.
		 * @return the string with no numbers
		 */
		public static String removeNums(String line) 
	    { 
	        char[] charArray = line.toCharArray(); 
	        String space = ""; 
	        for (int i = 0; i < charArray.length; i++) {  
	            if (!Character.isDigit(charArray[i])) { 
	                space = space + charArray[i]; 
	            } 
	        }  
	        return space; 
	    } 
		/**
		 * The uniqueWord method returns the number of unique words needed to account for 15% of the total number of words.
		 * @param hash the linked list of hashtable values, sorted high to low by value
		 * @param num the total number of unique words for the hash table.
		 * @return
		 */
		public static int uniqueWord(List<Map.Entry<String, Integer>> hash, int num) {
			double numNeeded = num*.15;
			double currentNum = 0;
			int totalNums = 0;
			int count = 1;
			while(numNeeded > currentNum) {
				currentNum = currentNum + hash.get(hash.size()-count).getValue();
				totalNums++;
				count++;
			}
			return totalNums;
		}
		
		
	   public static void main(String[ ] args)  
	   {  		   
		   // creating the files into a string array, works. 		   
		   File fileN = new File("citeseer");
		   //creating the hashtable for the stop words.
		   File stop = new File("stopwords");
		   Scanner stopper = null;
		   Hashtable<String,Integer> stopword = new Hashtable<String,Integer>();
			try {
				stopper = new Scanner(stop);
			}
			catch(FileNotFoundException e) {
				System.out.println("error file not found.");
				System.exit(1);
			}
			while(stopper.hasNextLine()) {
				String line = stopper.nextLine().trim();
				stopword.put(line, 0);
			}
						
		   //running a test to removing punctuation and numbers from text
		   File[] files = fileN.listFiles(); 
		   Scanner inputStream = null;
		   Hashtable<String,Integer> tokens = new Hashtable<String, Integer>();
		   int numOfWords = 0;
		   for(int i =0; i < files.length; i++) {
			try {
				inputStream = new Scanner(files[i]);
			}
			catch(FileNotFoundException e) {
				System.out.println("error file not found.");
				System.exit(1);
			}									
			while(inputStream.hasNextLine()) {
				//preprocessing the file
				String line = inputStream.nextLine();
				line = removeNums(line);
				StringTokenizer tokenizer = new StringTokenizer(line, "[!._,'@?()#-`/ ]\"");

				while(tokenizer.hasMoreTokens()) {
					String tk = tokenizer.nextToken();
					numOfWords++;
					if(tokens.containsKey(tk)) {
						tokens.compute(tk, (key,val)-> val + 1);
					}
					else {
						tokens.put(tk, 1);
					}
				}
			}
		   }
		   inputStream.close();
					   
		   System.out.println("Number of words processed: " + numOfWords);
		   System.out.println("Size of vocabulary: " + tokens.size());		   
		   //prints the 20 most common words by converting into linked list, sorting it by value, then printing first 2 values. WORKS!!!!
		   System.out.println("Top twenty words:");
		   List<Map.Entry<String, Integer> > hashList = new LinkedList<Map.Entry<String, Integer> >(tokens.entrySet()); 	   
	       Collections.sort(hashList, new Comparator<Map.Entry<String, Integer> >() { 
	            public int compare(Map.Entry<String, Integer> h1, Map.Entry<String, Integer> h2) 
	            { 
	                return (h1.getValue()).compareTo(h2.getValue()); 
	            } 
	        });
	       	int size = hashList.size();
	       	int ct = 0;
	        for(int i =size+20; i > size; i--) {
	        	ct++;
	        	System.out.println(ct + ": " + ((LinkedList<Entry<String, Integer>>) hashList).get(size-ct));    	
	        }
	        
	        //getting total number of unique words needed to account for 15% of total words
	        int total = uniqueWord(hashList, numOfWords);
	        System.out.println("Minumum number of unique words accounting for 15% total words: " + total);	        
	        //using the porter Stemmer testing!
	        System.out.println("With the porter stemmer active");
			   Porter Stemmer = new Porter();
			   Scanner inputStream2 = null;
			   Hashtable<String,Integer> tokens2 = new Hashtable<String, Integer>();
			   int numOfWords2 = 0;
			   for(int i =0; i < files.length; i++) {
				try {
					inputStream2 = new Scanner(files[i]);
				}
				catch(FileNotFoundException e) {
					System.out.println("error file not found.");
					System.exit(1);
				}									
				while(inputStream2.hasNextLine()) {
					//preprocessing the file
					String line2 = inputStream2.nextLine();
					line2 = removeNums(line2);
					line2 = line2.toLowerCase();
					StringTokenizer tokenizer = new StringTokenizer(line2, "[!._,'@?()#-`/ ]\"");
					while(tokenizer.hasMoreTokens()) {
						String tk = tokenizer.nextToken();
						if(stopword.containsKey(tk)) {
							continue;
						}
						else {	           

							tk = Stemmer.stripAffixes(tk);
							numOfWords2++;
							if(tokens2.containsKey(tk)) {
								tokens2.compute(tk, (key,val)-> val + 1);
							}
							else {
								tokens2.put(tk, 1);
							}	
						}
					}
				}
			   }
			   inputStream2.close();
			   //printing out the stuff for tokens2 hashtable which has had stopwords removed and porter stemmer used
			   System.out.println("Number of words processed: " + numOfWords2);
			   System.out.println("Size of vocabulary: " + tokens2.size());
			   
			   //prints the 20 most common words by converting into linked list, sorting it by value, then printing first 2 values. WORKS!!!!
			   System.out.println("Top twenty words:");
			   List<Map.Entry<String, Integer>> hashList2 = new LinkedList<Map.Entry<String, Integer> >(tokens2.entrySet()); 	   
		       Collections.sort(hashList2, new Comparator<Map.Entry<String, Integer> >() { 
		            public int compare(Map.Entry<String, Integer> h1, Map.Entry<String, Integer> h2) 
		            { 
		                return (h1.getValue()).compareTo(h2.getValue()); 
		            } 
		        });
		       	int size2 = hashList2.size();
		       	int ct2 = 0;
		        for(int i =size+20; i > size; i--) {
		        	ct2++;
		        	System.out.println(ct2 + ": " + ((LinkedList<Entry<String, Integer>>) hashList2).get(size2-ct2));    	
		        }		
		        int total2 = uniqueWord(hashList2, numOfWords2);
		        System.out.println("Minumum number of unique words accounting for 15% total words: " + total2);
			
			
			
			
			
	   }
}
