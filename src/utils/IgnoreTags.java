package utils;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by mani on 8/9/15.
 */
public class IgnoreTags extends Pipe implements Serializable{

	private static final long serialVersionUID = 1L;

	public Instance pipe(Instance carrier) {
        TokenSequence ts = (TokenSequence)carrier.getData();
        TokenSequence ret = new TokenSequence();

        for(int i = 0; i < ts.size(); ++i) {
            Token t = (Token)ts.get(i);
            String token = t.getText();
            if(!Pattern.matches("[#@]+[\\p{L}\\p{P}]+",token))
                ret.add(t);
        }
        carrier.setData(ret);
        return carrier;
    }
}
