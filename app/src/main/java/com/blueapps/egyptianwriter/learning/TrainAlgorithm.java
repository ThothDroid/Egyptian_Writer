package com.blueapps.egyptianwriter.learning;

import com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;

import java.util.ArrayList;
import java.util.Collections;

public class TrainAlgorithm {

    private final VocabFileMaster vocabFileMaster;

    public TrainAlgorithm(VocabFileMaster vocabFileMaster){

        this.vocabFileMaster = vocabFileMaster;

    }

    public ArrayList<Card> getTrainingCards(){
        ArrayList<Card> cards = vocabFileMaster.getCards();
        Collections.shuffle(cards);
        return cards;
    }

}
