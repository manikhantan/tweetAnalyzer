package utils;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.types.InstanceList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by mani on 8/9/15.
 */

public class Dictionary implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException {

        HashMap<String,Integer> dictionary = new HashMap<String, Integer>();
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        pipeList.add( new Input2CharSequence());
        pipeList.add( new CharSequenceLowercase());
        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
        pipeList.add( new IgnoreTags());
        pipeList.add( new TokenSequence2FeatureSequence());
        pipeList.add(new FeatureSequence2FeatureVector());

        InstanceList instances =new InstanceList(new SerialPipes(pipeList));
        FileIterator iterator = new FileIterator(new File("input"), new TxtFilter(), FileIterator.LAST_DIRECTORY);
        instances.addThruPipe(iterator);

        String[] dictTokens = instances.get(0).getData().toString().split("\n");
        for(String tokens : dictTokens) {
            String[] wordCount = tokens.split("(\\(\\d+\\)=)");
            String word = wordCount[0];
            int count = Math.round(Float.parseFloat(wordCount[1]));
            dictionary.put(word,count);
        }

        ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("resources/dictionary.ser"));
        o.writeObject(dictionary);
        o.flush();
        o.close();
    }
}

class TxtFilter implements FileFilter {
    public boolean accept(File file) {
        return file.toString().endsWith(".txt");
    }
}
