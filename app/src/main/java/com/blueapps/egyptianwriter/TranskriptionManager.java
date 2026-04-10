package com.blueapps.egyptianwriter;

// this is a utility class, so the error unused doesn't make sense here
@SuppressWarnings("unused")
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

    /**
     * Use this method to append a space before and after the converted Transcription.
     * This is useful when using TextView with italic textStyle. Then characters like j will not be cut.
     *
     * @return String
     */
    public static String convertTranscriptionItalic(String input){
        input = convertTranskription(input);

        return " " + input + " ";
    }

}
