package com.alwayswantedtoplay.scramble;
public class ScrambleHelper {

    private static final int NUMBER_OF_LETTERS = 16;
    private static final int[] LETTER_PROBABILITIES = {
            43, 55, 82,101,165, 171,186,200,256,257, 266,296,313,
            343,382, 400,401,440,496,536,567,572,575, 576,588
    };

    public static String makeLetters() {
        StringBuffer s = new StringBuffer();
        char c = ' ';
        int iVow, iCon;
        do {
            s = new StringBuffer();
            c = ' '; iVow = 0; iCon = 0;
            for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
                if (c != 'q') c = getRandomLetter();
                else c = 'u';
                s.append(c);

                if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') iVow++;
                else  iCon++;
            }
        } while (iVow < 3 || iCon < 9);
        String sLetters;
        sLetters = s.toString().toUpperCase();
        //sLetters = "EEAGXSACPVGKCRUI";
        return sLetters;
    }
    // each letter gets a weighted probability
    static char getRandomLetter() {
        int rnd = myRandom(592);
        for (int i = 0; i < 25; i++) {
            int num = LETTER_PROBABILITIES[i];
            if (rnd < num) return (char)('a' + i);
        }
        return 'z';
    }
    public static int myRandom(int i) {
        int iReturn =  (int) Math.ceil(Math.random()*i);
        iReturn = 0;
        return iReturn;
    }

}
