package com.blueapps.egyptianwriter.learning;

import com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class TrainAlgorithm {

    private final VocabFileMaster vocabFileMaster;
    private final int maxCardCount;

    // Constants
    public static final int MAX_SCORE = 100;

    public TrainAlgorithm(VocabFileMaster vocabFileMaster, int maxCardCount){

        this.vocabFileMaster = vocabFileMaster;
        this.maxCardCount = maxCardCount;

    }

    public ArrayList<Card> getTrainingCards(){
        ArrayList<Card> cards = vocabFileMaster.getCards();

        // Sort out cards with learn expire date in future
        // Because they do not have to be learned today
        cards = removeFutureCards(cards);

        // Sort out cards which finished learning (they have the maximum score)
        // The user knows them by heart and doesn't have to learn them anymore
        cards = removeFinishedCards(cards);

        if(cards.size() > maxCardCount){
            // There are too many cards left, so the Algorithm has to choose which cards to abandon

        }

        Collections.shuffle(cards);
        return cards;
    }

    public boolean isSomethingToLearn(){
        ArrayList<Card> cards = vocabFileMaster.getCards();

        // Sort out cards with learn expire date in future
        // Because they do not have to be learned today
        cards = removeFutureCards(cards);

        // Sort out cards which finished learning (they have the maximum score)
        // The user knows them by heart and doesn't have to learn them anymore
        cards = removeFinishedCards(cards);

        return !cards.isEmpty();
    }

    public static Date cutOutTime(Date date){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static ArrayList<Card> removeFutureCards(ArrayList<Card> cards){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        ArrayList<Card> cards2 = new ArrayList<>();
        for (Card card: cards){
            long longDate = card.getLearnExpireDate();
            Date dateDate = new Date(longDate);
            Date currentDate = calendar.getTime();

            // reset hours, minutes, seconds and milliseconds to only compare the day/week/month/year
            dateDate = cutOutTime(dateDate);
            currentDate = cutOutTime(currentDate);

            if (!dateDate.after(currentDate)){
                cards2.add(card);
            }
        }
        return cards2;
    }

    public static ArrayList<Card> removeFinishedCards(ArrayList<Card> cards){
        ArrayList<Card> cards2 = new ArrayList<>();
        for (Card card: cards){
            if (card.getScore() < MAX_SCORE){
                cards2.add(card);
            }
        }
        return cards2;
    }

}
