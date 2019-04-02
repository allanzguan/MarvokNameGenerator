import java.io.*;
import java.util.*;

/*
 * 
 * Novel name generator that use Markov model to generate names
 * User inpute gender, min name length, max name length, Markov model and number of names to generate
 * 
 */
public class MarvokNameGenerator {
	int order =2;
	int min = 0;
	int max = 0;
	int numName = 0;
	String filler = "";
	File file = null;
	static List<String> checkList = new ArrayList<String>();
	HashMap<String, HashMap<String, Integer>> countList = new HashMap<String, HashMap<String, Integer>>(); 
	HashMap<String, HashMap<String, Double>> probList = new HashMap<String, HashMap<String, Double>>(); 
	
	
	public MarvokNameGenerator() {}
	
	public MarvokNameGenerator(int inMin, int inMax, int inOrder, int inName, File inFile) throws FileNotFoundException {
		order = inOrder;
		min = inMin;
		max = inMax;
		numName = inName;
		file = inFile;
		for(int i = 0; i < order; i++) {
			filler = filler + "-";
		}
		readFile(file);
		calProb();
	}
	
	
	/*
	 *  readFile is used to read the names off the text file.
	 */
	public void readFile(File f) throws FileNotFoundException {
		Scanner sc = new Scanner(f);
		while(sc.hasNextLine()) {
			String noFiller = sc.nextLine();
			String line = filler + noFiller + filler;
			checkList.add(noFiller);
			parseName(line.toLowerCase());
		}
		sc.close();
	}

	
	/*
	 * parseName takes one name as argument and parse it according to the Markov model.
	 */
	public void parseName(String n) {
		for (int i = 0; i < n.length()-order ; i++) {
			String k = n.substring(i, i+order);
			String v = n.substring(i+order, i+(order+1));		
			HashMap<String, Integer> valueHash = new HashMap<String, Integer>();
			if( !countList.containsKey(k)) {
				valueHash.put(v, 1);
				countList.put(k, valueHash);
			}
			else if (countList.containsKey(k)) {
				List<HashMap> keyList = new ArrayList<HashMap>();
				keyList.add(countList.get(k));
				for(HashMap h : keyList) {
					if( ! h.containsKey(v)) {
						countList.get(k).put(v, 1);
					}
					else if( h.containsKey(v)) {
						countList.get(k).put(v, countList.get(k).get(v)+1);
					}
				}
			}			
		}
	}
	
	/*
	 * calProb calculates the probability after all the names are added to the heap.
	 */
	public void calProb() {
		for(String k : countList.keySet()) {
			int count = 0;
			for(String vk : countList.get(k).keySet() ) {
				count = count + countList.get(k).get(vk);				
			}			
			HashMap<String, Double> probValue = new HashMap<String, Double>();
			for(String pv : countList.get(k).keySet() ) {
				probValue.put(pv, Double.valueOf(countList.get(k).get(pv)) / Double.valueOf(count));
			}
			probList.put(k, probValue);
		}
	}
	
	/*
	 * generateName generates the number of names according to the user input.
	 */
	public void generateName() {
		List<String> nameList = new ArrayList<String>();		
		while(nameList.size() < numName) {
			String currChar = "";
			String genName = "";
			String nameHeader = filler;
			Random rand = new Random();
			double randProb = rand.nextDouble() * 1.0;
			while ( !currChar.equals("-")) {
				for(String s : probList.get(nameHeader).keySet()) {
					double p = probList.get(nameHeader).get(s);
					if( randProb < p) {
						currChar = s;
						genName = genName + s;
						nameHeader = nameHeader + s;
						nameHeader = nameHeader.substring(nameHeader.length() - order);
						randProb = rand.nextDouble() * 1.0;
						break;
					}
					else if( randProb > p) {
						randProb = randProb - p;
					}
				}
			}
			genName = genName.substring(0, genName.length()-1);
			genName = genName.substring(0,1).toUpperCase() + genName.substring(1);		
			
			if( genName.length() > min && genName.length() < max && !nameList.contains(genName) && !checkList.contains(genName)) {
				nameList.add(genName);
			}
		}	
		Collections.sort(nameList, String.CASE_INSENSITIVE_ORDER);
		for(String n: nameList) {
			System.out.println(n);
		}

	}
	
	public static void main(String[] args) throws IOException{			
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter male or female: ");
		String inGender = sc.nextLine();
		String fileURL = "";
		if (inGender.equalsIgnoreCase("male")) {
			fileURL = "Resources/namesBoys.txt";
		}
		else if (inGender.equalsIgnoreCase("female")) {
			fileURL = "Resources/namesGirls.txt";
		}		
		ClassLoader classLoader = new MarvokNameGenerator().getClass().getClassLoader();
		File inFile = new File(classLoader.getResource(fileURL).getFile());
		
		System.out.println("Enter Min Length: ");
		int min = sc.nextInt();
		System.out.println("Enter Max Length: ");
		int max = sc.nextInt();
		System.out.println("Enter Number of Order: ");
		int order = sc.nextInt();
		System.out.println("Enter Number of Name: ");
		int num = sc.nextInt();
		sc.close();
		
		MarvokNameGenerator gn = new MarvokNameGenerator(min, max, order, num, inFile);		
		System.out.println("------------------------------");
		gn.generateName();
	}		
}
