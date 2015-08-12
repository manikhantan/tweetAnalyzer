package core;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import utils.ReadAdjectives;
import utils.Segment;
import utils.Stemmer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

/**
 * Created by mani on 8/9/15.
 */

public class Application {

    private InstanceList instances;
    
    public void importData(String path) throws FileNotFoundException, UnsupportedEncodingException {

        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        pipeList.add( new CharSequenceLowercase());
        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("[#@\\p{L}][\\p{L}\\p{P}]+\\p{L}")) );
        pipeList.add( new TokenSequenceRemoveNonAlpha());
        pipeList.add( new TokenSequenceRemoveStopwords());
        pipeList.add( new Segment());
        pipeList.add( new Stemmer());
        pipeList.add( new TokenSequence2FeatureSequence());
        pipeList.add( new FeatureSequence2FeatureVector());

        instances =new InstanceList(new SerialPipes(pipeList));

        Reader fileReader = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");
        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(.*)$"),1,0,0));
    }
    
    public static List<String> topNKeys(final HashMap<String,Integer> map, int n) {
        PriorityQueue<String> topN = new PriorityQueue<String>(n, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return Integer.compare(map.get(s1), map.get(s2));
            }
        });

        for(String key:map.keySet()){
            if (topN.size() < n)
                topN.add(key);
            else if (map.get(topN.peek()) < map.get(key)) {
                topN.poll();
                topN.add(key);
            }
        }        
        return (List)Arrays.asList(topN.toArray());
    }

    public static void main(String[] args) throws IOException {

        Application app = new Application();
        app.importData("input/tweets.txt");
        HashMap<String,Integer> siennaMiller = new HashMap<String,Integer>();
        HashMap<String,Integer> clintEastwood = new HashMap<String,Integer>();
        HashMap<String,Integer> bradleyCooper = new HashMap<String,Integer>();
        boolean sienna, clint, bradley;
        HashMap<String,Integer> tokensMap = new HashMap<String,Integer>();
        
        String[] wordTokens = new String[25];
        for(Instance instance:app.instances) {  
        	sienna=false;
        	clint=false;
        	bradley=false;
        	tokensMap.clear();
        	wordTokens = instance.getData().toString().split("\n");
        	
        	for(String tokens:wordTokens) {
        		String[] wordCount = tokens.split("(\\(\\d+\\)=)");            
        		String word = wordCount[0];
        		int count = wordCount.length==2?Math.round(Float.parseFloat(wordCount[1])):1;
        		if(ReadAdjectives.getInstance().contains(word))
        			tokensMap.put(word,count);
        		if(word.equals("sienna")||word.equals("miller")||word.equals("siennamiller"))
            		sienna = true;
            	if(word.equals("clint")||word.equals("eastwood")||word.equals("clinteastwood"))
            		clint = true;
               	if(word.equals("bradley")||word.equals("cooper")||word.equals("bradleycooper"))
               		bradley = true;
        	}
        	
        	for(String word:tokensMap.keySet()) {
        		if(sienna) {
        			if(siennaMiller.containsKey(word)) 
        				siennaMiller.put(word,siennaMiller.get(word)+tokensMap.get(word));
        			else
        				siennaMiller.put(word,tokensMap.get(word));
        		}
        		if(clint) {
        			if(clintEastwood.containsKey(word)) 
        				clintEastwood.put(word,clintEastwood.get(word)+tokensMap.get(word));
        			else
        				clintEastwood.put(word,tokensMap.get(word));
        		}
        		if(bradley) {
        			if(bradleyCooper.containsKey(word)) 
        				bradleyCooper.put(word,bradleyCooper.get(word)+tokensMap.get(word));
        			else
        				bradleyCooper.put(word,tokensMap.get(word));
        		}
        	}
        }
        
        System.out.println("Sienna Miller:");
        
        for(String trend: topNKeys(siennaMiller,5))
        	System.out.println(trend);
        System.out.println();
        
        System.out.println("Clint Eastwood:");
        
        for(String trend: topNKeys(clintEastwood,5))
        	System.out.println(trend);
        System.out.println();
        
        System.out.println("Bradley Cooper:");
        
        for(String trend: topNKeys(bradleyCooper,5))
        	System.out.println(trend);
    }
}