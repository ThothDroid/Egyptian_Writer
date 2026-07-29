package com.blueapps.egyptianwriter.learning;

public class ResultCalculator {

    private int[] result;
    private long time;
    private float quickestAnswer;

    // Values
    private int allCards;
    private int correctCards = 0;
    private int incorrectCards = 0;
    private int skippedCards = 0;

    private float successRate;
    private float errorRate;
    private float skipRate;

    private float averageTimePerCard;

    private int score;


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
            score = 4;
        } else if (successRate > 0.6){
            score = 3;
        } else if (successRate > 0.3) {
            score = 2;
        } else {
            score = 1;
        }
    }

}
