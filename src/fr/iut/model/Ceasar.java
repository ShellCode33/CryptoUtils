package fr.iut.model;

/**
 * Created by shellcode on 3/2/17.
 */
public class Ceasar {

    String cipher;
    String [] results = new String[26];

    public Ceasar(String cipher) {
        this.cipher = cipher;
        bruteforce();
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
        float lowestScore = Float.MAX_VALUE;

        for(String result : results) {
            float score = new FrequentialAnalysis(result).matchScore("FR");

            if(lowestScore > score) {
                bestScoreStr = result;
                lowestScore = score;
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
