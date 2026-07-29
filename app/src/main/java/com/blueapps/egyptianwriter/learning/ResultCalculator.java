package com.blueapps.egyptianwriter.learning;

public class ResultCalculator {

    private final int[] result;
    private final long time;
    private final float quickestAnswer;

    // Values
    private final int allCards;
    private int correctCards = 0;
    private int incorrectCards = 0;
    private int skippedCards = 0;

    private final float successRate;
    private final float errorRate;
    private final float skipRate;

    private final float averageTimePerCard;

    private final int score;
    
    // Constants
    public static final int SCORE_STUDENT = 1;
    private static final int SCORE_SCRIBE = 2;
    private static final int SCORE_PRIEST = 3;
    private static final int SCORE_WESIR = 4;


    public ResultCalculator(int[] result, long time, float quickestAnswer){
        this.result = result;
        this.time = time;
        this.quickestAnswer = quickestAnswer;

        // Calculate
        // cards
        allCards = result.length;
        for (int card: result){
            if (card == 2){
                correctCards++;
            } else if (card == 1) {
                incorrectCards++;
            } else if (card == 0) {
                skippedCards++;
            }
        }
        // rates
        successRate = ((float) allCards / 100) * correctCards;
        errorRate = ((float) allCards / 100) * incorrectCards;
        skipRate = ((float) allCards / 100) * skippedCards;
        // time
        averageTimePerCard = (float) time / allCards;
        // score
        if (successRate > 0.9){
            score = SCORE_WESIR;
        } else if (successRate > 0.6){
            score = SCORE_PRIEST;
        } else if (successRate > 0.3) {
            score = SCORE_SCRIBE;
        } else {
            score = SCORE_STUDENT;
        }
    }

    public int[] getResult() {
        return result;
    }

    public long getTime() {
        return time;
    }

    public float getQuickestAnswer() {
        return quickestAnswer;
    }

    public int getAllCards() {
        return allCards;
    }

    public int getCorrectCards() {
        return correctCards;
    }

    public int getIncorrectCards() {
        return incorrectCards;
    }

    public int getSkippedCards() {
        return skippedCards;
    }

    public float getSuccessRate() {
        return successRate;
    }

    public float getErrorRate() {
        return errorRate;
    }

    public float getSkipRate() {
        return skipRate;
    }

    public float getAverageTimePerCard() {
        return averageTimePerCard;
    }

    public int getScore() {
        return score;
    }
}
