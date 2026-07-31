package com.blueapps.egyptianwriter.learning;

import com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;

import java.util.ArrayList;
import java.util.Collections;

public class TrainAlgorithm {

    private final VocabFileMaster vocabFileMaster;
    private final int maxCardCount;

    public TrainAlgorithm(VocabFileMaster vocabFileMaster, int maxCardCount){

        this.vocabFileMaster = vocabFileMaster;
        this.maxCardCount = maxCardCount;

    }

    public ArrayList<Card> getTrainingCards(){
        ArrayList<Card> cards = vocabFileMaster.getCards();



        Collections.shuffle(cards);
        return cards;
    }

}
