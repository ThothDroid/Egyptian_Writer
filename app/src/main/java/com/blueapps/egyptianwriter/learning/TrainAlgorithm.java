package com.blueapps.egyptianwriter.learning;

import com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class TrainAlgorithm {

    private final VocabFileMaster vocabFileMaster;
    private final int maxCardCount;

    // Constants
    public static final int MAX_SCORE = 100;
    public static final int QUARTER_1_SCORE = 25;
    public static final int QUARTER_2_SCORE = 50;
    public static final int QUARTER_3_SCORE = 75;

    public TrainAlgorithm(VocabFileMaster vocabFileMaster, int maxCardCount){

        this.vocabFileMaster = vocabFileMaster;
        this.maxCardCount = maxCardCount;

    }

    // The values before reassignment are important for the function to work!
    @SuppressWarnings("ReassignedVariable")
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

            // apportion cards among ArrayLists (Boxes) depending on their score
            // E.g. first box: score <= 25
            ArrayList<Card>[] boxes = getBoxes(cards);

            // Create sections where the cards can be placed into
            // Calculate "Seats" for each section by give ratio
            int remainingSeats = maxCardCount;

            int seatCount1 = remainingSeats / 2;
            remainingSeats = remainingSeats - seatCount1;
            int seatCount2 = remainingSeats / 2;
            remainingSeats = remainingSeats - seatCount2;
            int seatCount4 = remainingSeats / 2;
            int seatCount3 = remainingSeats - seatCount4;

            // Create sections
            Card[] section1 = new Card[seatCount1];
            Card[] section2 = new Card[seatCount2];
            Card[] section3 = new Card[seatCount3];
            Card[] section4 = new Card[seatCount4];

            // Fill sections
            section1 = fillSection(section1, boxes, new int[]{1,2,3,4});
            section2 = fillSection(section2, boxes, new int[]{2,3,4,1});
            section3 = fillSection(section3, boxes, new int[]{3,4,2,1});
            section4 = fillSection(section4, boxes, new int[]{4,3,2,1});

            // All Objects which do not belong to a Section  are abandoned
            cards = new ArrayList<>(Arrays.asList(section1));
            cards.addAll(Arrays.asList(section2));
            cards.addAll(Arrays.asList(section3));
            cards.addAll(Arrays.asList(section4));

        }

        // Set the define the card order

        // apportion cards among ArrayLists (Boxes) depending on their score
        // E.g. first box: score <= 25
        ArrayList<Card>[] boxes = getBoxes(cards);

        // shuffle all boxes independently
        Collections.shuffle(boxes[0]);
        Collections.shuffle(boxes[1]);
        Collections.shuffle(boxes[2]);
        Collections.shuffle(boxes[3]);

        // Add cards from boxes to ArrayList in a defined order
        // I do this, because if you are nearly at the end of your learning season, it is better
        // to learn cards, you already now better. Because of this it is easier at the end and
        // the motivation stays the same
        cards = new ArrayList<>(boxes[0]);
        cards.addAll(boxes[1]);
        cards.addAll(boxes[2]);
        cards.addAll(boxes[3]);

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

    public static ArrayList<Card>[] getBoxes(ArrayList<Card> cards){
        ArrayList<Card>[] returnArray = new ArrayList[4];

        returnArray[0] = new ArrayList<>();
        returnArray[1] = new ArrayList<>();
        returnArray[2] = new ArrayList<>();
        returnArray[3] = new ArrayList<>();

        for (Card card: cards){
            if (card.getScore() <= QUARTER_1_SCORE){
                returnArray[0].add(card);
            } else if (card.getScore() <= QUARTER_2_SCORE) {
                returnArray[1].add(card);
            } else if (card.getScore() <= QUARTER_3_SCORE) {
                returnArray[2].add(card);
            } else {
                returnArray[3].add(card);
            }
        }

        return returnArray;
    }

    public static Card[] fillSection(Card[] section, ArrayList<Card>[] boxes, int[] boxOrder){

        int selectedBoxOrder = 0;
        int selectedBox = boxOrder[selectedBoxOrder];

        for (int i = 0; i < section.length; i++){

            if (boxes[selectedBox].isEmpty()) {
                if (selectedBoxOrder > 3){
                    throw new RuntimeException("class: TrainAlgorithm.java \nReached end of boxes!");
                }
                selectedBoxOrder++;
                selectedBox = boxOrder[selectedBoxOrder];

                // loop through that item again
                i--;
            } else {

                int randomIndex = (int) (Math.random() * boxes[selectedBox].size());

                Card card = boxes[selectedBox].get(randomIndex);
                boxes[selectedBox].remove(randomIndex);

                section[i] = card;
            }
        }

        return section;
    }

}
