package com.blueapps.egyptianwriter;

import android.text.Editable;
import android.text.TextWatcher;

// this is a utility class, so the error unused doesn't make sense here
@SuppressWarnings("unused")
public class TranskriptionManager {

    public static String convertTranscription(String input){

        StringBuilder stringBuilder = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            char newChar = c;

            if (!escape){
                switch (c) {

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

                    case 'j':
                        newChar = 'ỉ';
                        break;

                    case 'Y':
                        newChar = 'ï';
                        break;

                    case 'z':
                        newChar = 'ś';
                        break;

                    case 'q':
                        newChar = 'ḳ';
                        break;

                    case '^':
                        escape = true;
                        break;

                }
            } else {
                escape = false;

                switch (c) {

                    case '.':
                        newChar = '·';
                        break;

                    case 'A':
                        newChar = 'Ꜣ';
                        break;

                    case 'a':
                        newChar = 'Ꜥ';
                        break;

                    case 'j':
                        newChar = 'Ỉ';
                        break;

                    case 'y':
                        newChar = 'Y';
                        break;

                    case 'Y':
                        newChar = 'Ï';
                        break;

                    case 'w':
                        newChar = 'W';
                        break;

                    case 'b':
                        newChar = 'B';
                        break;

                    case 'p':
                        newChar = 'P';
                        break;

                    case 'f':
                        newChar = 'F';
                        break;

                    case 'm':
                        newChar = 'M';
                        break;

                    case 'n':
                        newChar = 'N';
                        break;

                    case 'r':
                        newChar = 'R';
                        break;

                    case 'h':
                        newChar = 'H';
                        break;

                    case 'H':
                        newChar = 'Ḥ';
                        break;

                    case 'x':
                        newChar = 'Ḫ';
                        break;

                    case 's':
                        newChar = 'S';
                        break;

                    case 'z':
                        newChar = 'Ś';
                        break;

                    case 'S':
                        newChar = 'Š';
                        break;

                    case 'q':
                        newChar = 'Ḳ';
                        break;

                    case 'k':
                        newChar = 'K';
                        break;

                    case 'g':
                        newChar = 'G';
                        break;

                    case 't':
                        newChar = 'T';
                        break;

                    case 'd':
                        newChar = 'D';
                        break;

                    case 'T':
                        newChar = 'Ṯ';
                        break;

                    case 'D':
                        newChar = 'Ḏ';
                        break;

                    case '^':
                        escape = true;
                        break;

                }
            }

            if (newChar == 'X'){
                stringBuilder.append("H̱");
            } else {
                if (newChar != '^') stringBuilder.append(newChar);
            }
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
        input = convertTranscription(input);

        return " " + input + " ";
    }


    /**
     * Add this class as TextChangedListener to autoformat the Transcription in an EditText.
     */
    public static class TranscriptionTextWatcher implements TextWatcher {

        private boolean textChanged = false;
        private int textStart = 0;
        private int textEnd = 0;
        private int textBefore = 0;
        private String text = "";

        private String userInput = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!textChanged) {
                textStart = start;
                textEnd = start + count;
                textBefore = start + before;
                text = s.subSequence(textStart, textEnd).toString();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!textChanged){
                textChanged = true;

                s.replace(textStart, textEnd, TranskriptionManager.convertTranscription(text));

                // Keep track on the unformatted code
                userInput = userInput.substring(0, textStart) + text
                        + userInput.substring(textBefore);

                textChanged = false;
            }
        }

        /**
         * This function return the String actually typed in by the user without any conversion.
         *
         * @return Unformatted Code
         */
        public String getUnformattedCode(){
            return userInput;
        }

    }

}
