package utils;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;

import java.io.Serializable;

/**
 * Created by mani on 8/9/15.
 */
public class Stemmer extends Pipe implements Serializable{

	private static final long serialVersionUID = 1L;

    public Instance pipe(Instance carrier) {
        SnowballStemmer stemmer = new englishStemmer();
        TokenSequence in = (TokenSequence) carrier.getData();

        for (Token token : in) {
            stemmer.setCurrent(token.getText());
            stemmer.stem();
            token.setText(stemmer.getCurrent());
        }

        return carrier;
    }
}
