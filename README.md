# MarvokNameGenerator
This [Java](https://www.java.com/en/) Program implements Markov model to generate novel names that follow common letter squences.

The program takes the following parameter:
- "Male" or "Female"
- The minimum name length
- The maximum name length
- The order of the model
- The number of names to generate

## Example Run
```
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
```
