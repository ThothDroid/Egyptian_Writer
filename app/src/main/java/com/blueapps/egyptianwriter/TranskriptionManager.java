package com.blueapps.egyptianwriter;

public class TranskriptionManager {

    public static String convertTranskription(String input){

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            char newChar = c;

            switch (c){

                case 'A':
                    newChar = 'Ꜣ';
                    break;

                case 'a':
                    newChar = 'Ꜥ';
                    break;

                case 'H':
                    newChar = 'ḥ';
                    break;

                case 'x':
                    newChar = 'ḫ';
                    break;

                case 'X':
                    newChar = 'ẖ';
                    break;

                case 'i':
                    newChar = 'j';
                    break;

                case 'S':
                    newChar = 'š';
                    break;

                case 'T':
                    newChar = 'ṯ';
                    break;

                case 'D':
                    newChar = 'ḏ';
                    break;

            }

            stringBuilder.append(newChar);
        }

        return stringBuilder.toString();
    }

}
