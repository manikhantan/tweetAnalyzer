package utils;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by mani on 8/9/15.
 */
public class Segment extends Pipe implements Serializable{

	private static final long serialVersionUID = 1L;
    private HashMap<String,Integer> dictionary;
    private String output="";
    private int mostProbableValue=0;
    public Instance pipe(Instance carrier) {

    	dictionary = ReadDictionary.getInstance();
        TokenSequence ts = (TokenSequence) carrier.getData();
        TokenSequence ret = new TokenSequence();

        for (int i = 0; i < ts.size(); ++i) {
        	output="";
        	mostProbableValue=0;
            Token t = (Token) ts.get(i);
            String token = t.getText();
            if (Pattern.matches("#[\\p{L}\\p{P}]+", token)) {
            	segmentWords(0,"",token.substring(1));
            	if(output.isEmpty())
            		ret.add(token);
            	else {
            		for(String str:output.split(" "))
            			ret.add(str);
            	}
            }
            else
            	ret.add(token);
        }
        carrier.setData(ret);
        return carrier;
    }
 
    public void segmentWords(int val, String prefix, String input) {
		if(dictionary.containsKey(input) && val+dictionary.get(input)>=mostProbableValue) {
			int value = val+dictionary.get(input);
			output = prefix+" "+input;
			mostProbableValue = value;
		}
		if(input.equals(""))
			return;
		int len = input.length();
		for(int i=1; i<len; i++) {
			String pre = input.substring(0,i);
			if(dictionary.containsKey(pre))
				segmentWords(val+dictionary.get(pre),prefix+pre+" ", input.substring(i,len));
		}
	}
}