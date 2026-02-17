package com.blueapps.egyptianwriter.dashboard.signlist;

import java.util.ArrayList;

public class SignData {

    private final int id;
    private String gardinerId;
    private ArrayList<String> phonetics = new ArrayList<>();
    private String phoneticString;

    public SignData(int id, String gardinerId){
        this.id = id;
        this.gardinerId = gardinerId;
    }

    public int getId() {
        return id;
    }

    public String getGardinerId() {
        return gardinerId;
    }

    public void setGardinerId(String gardinerId) {
        this.gardinerId = gardinerId;
    }

    public ArrayList<String> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(ArrayList<String> phonetics) {
        this.phonetics = phonetics;
        phoneticString = arrayListToString(phonetics);
    }

    public String getPhoneticString() {
        return phoneticString;
    }

    private String arrayListToString(ArrayList<String> list){
        StringBuilder builder = new StringBuilder();
        int counter = 1;
        for (String s: list){
            builder.append(s);
            if (counter == list.size() - 1) {
                builder.append(", ");
            }
            counter++;
        }
        return builder.toString();
    }
}
