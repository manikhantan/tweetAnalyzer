package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class ReadAdjectives {
	
	private static HashSet<String> adjectives;
	
	private ReadAdjectives() {}
	
	public static HashSet<String> getInstance() throws FileNotFoundException, IOException {
		adjectives = new HashSet<String>();
		File file = new File("resources/adjectives.txt");
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			String[] tokens = scanner.nextLine().split("\\s+");
			for(String token:tokens)
				adjectives.add(token);
		}
		scanner.close();
		return adjectives;
	}
}