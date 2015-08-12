package utils;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * Created by mani on 8/9/15.
 */

public class ReadDictionary {

	private static HashMap<String,Integer> dictionary;
	
	private ReadDictionary() {}
	
	public static HashMap<String,Integer> getInstance() {
		if(dictionary==null) {
			  try {
		            ObjectInputStream o = new ObjectInputStream(new FileInputStream("resources/dictionary.ser"));
		            dictionary = (HashMap<String,Integer>) o.readObject();
		            o.close();
		            return dictionary;

		        } catch (Exception e) {
		            e.printStackTrace();
		            return null;
		        }
		}
		else {
			return dictionary;
		}		
	}
}
