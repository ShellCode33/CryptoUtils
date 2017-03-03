package fr.iut.model;

/**
 * Created by shellcode on 3/2/17.
 */
public class Ceasar {

    String cipher;
    String [] results = new String[26];

    public Ceasar(String cipher) {
        this.cipher = cipher;
        clearText();
        bruteforce();
    }

    private void clearText() {
        //On enlève la ponctuation
        String to_remove_chars[] = new String[] {".", ",", "!", "?", ";", ":", "-", "'"};
        for(String to_remove : to_remove_chars)
            cipher = cipher.replace(to_remove, "");

        boolean hasNonAlpha = cipher.matches("^.*[^a-zA-Z0-9 ].*$");

        if(hasNonAlpha)
            throw new IllegalArgumentException("Special chars are not allowed ! Please remove all the '\"éàè@ etc...");

        cipher = cipher.toUpperCase();
        cipher = cipher.trim();
        cipher = cipher.replaceAll(" +", " "); //enlève les espaces multiples
    }

    private void bruteforce() {
        for(int i = 0; i < 26; i++) {
            results[i] = Ceasar.encode(cipher, i);
        }
    }

    public String[] getResults() {
        return results;
    }

    public String getMostProbableAnswer() {

        String bestScoreStr = "";
        float highestScore = -1;

        for(String result : results) {
            float score = new FrequentialAnalysis(result).matchScore("FR");

            if(highestScore < score) {
                bestScoreStr = result;
                highestScore = score;
            }
        }

        return bestScoreStr;
    }

    public static String encode(String text, int shift) {
        char [] result = text.toUpperCase().toCharArray();

        for(int i = 0; i < result.length; i++) {
            if(result[i] != ' ') {
                if(result[i]+shift > 'Z')
                    result[i] -= 26 - shift;
                else
                    result[i] += shift;
            }
        }

        return new String(result);
    }
}
