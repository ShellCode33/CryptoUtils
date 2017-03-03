package fr.iut.model;

import java.util.*;

/**
 * Created by shellcode on 2/3/17.
 */
public class FrequentialAnalysis {

    private String text;

    Map<Character, Integer> frequences = new LinkedHashMap<>();
    int nb_letters = 0;

    public FrequentialAnalysis(String text) {
        this.text = text;
        clearText();
        compute();
    }

    private void clearText() {
        //On enlève la ponctuation
        String to_remove_chars[] = new String[] {".", ",", "!", "?", ";", ":", "-", "'"};
        for(String to_remove : to_remove_chars)
            text = text.replace(to_remove, "");

        boolean hasNonAlpha = text.matches("^.*[^a-zA-Z0-9 ].*$");

        if(hasNonAlpha)
            throw new IllegalArgumentException("Special chars are not allowed ! Please remove all the '\"éàè@ etc...");

        text = text.toUpperCase();
        text = text.trim();
        text = text.replaceAll(" +", " "); //enlève les espaces multiples
    }

    private void compute() {
        //On compte la fréquence d'apparition de toutes les lettres
        for(char c : text.toCharArray()) {
            if(c >= 'A' && c <= 'Z') {
                Integer freq = frequences.get(c);
                frequences.put(c, freq == null ? 1 : freq+1);
                nb_letters++;
            }
        }

        //On trie la map
        Map<Character, Integer> sorted_frequences = new LinkedHashMap<>();

        while(frequences.size() > 0) { //On va supprimer la frequence la plus grande à chaque fois afin de trier


            Character highest_character = null;
            Integer highest_integer = -1;

            //On trouve la plus grande fréquence des lettres qui restent
            for(Map.Entry<Character, Integer> entry : frequences.entrySet()) {
                if(entry.getValue() > highest_integer) {
                    highest_integer = entry.getValue();
                    highest_character = entry.getKey();
                }
            }

            sorted_frequences.put(highest_character, highest_integer);
            frequences.remove(highest_character);
        }

        frequences = sorted_frequences;
    }

    public int getNbOccurrencesOf(char c) {
        c = ("" + c).toUpperCase().charAt(0);

        if(c < 'A' || c > 'Z')
            throw new IllegalArgumentException("The letter must be between A and Z");

        Integer freq = frequences.get(c);

        if(freq == null)
            return 0;

        return freq;
    }

    public float getFrequenceOf(char c) {
        return (float)getNbOccurrencesOf(c) / nb_letters;
    }

    @SuppressWarnings("unchecked")
    //retourne la nième lettre avec la plus grande frequence d'apparation
    public char getNthLetterWithHighestFrequency(int n) {

        if(n >= frequences.size() || n < 0)
            throw new IndexOutOfBoundsException();

        return ((Map.Entry<Character, Integer>)frequences.entrySet().toArray()[n]).getKey();
    }

    public void printFrequences() {
        System.out.println("---- FREQUENCIES ----");
        for(Map.Entry<Character, Integer> entry : frequences.entrySet()) {
            System.out.println(entry.getKey() + " : " + (float)entry.getValue() / nb_letters);
        }
        System.out.println("---------------------");
    }

    public String getText() {
        return text;
    }

    public int getNbLetters() {
        return nb_letters;
    }

    public int getNbDifferentLetters() {
        return frequences.size();
    }

    //établie un score, plus celui-ci est bas, plus le texte suit les fréquences d'apparition par défaut de la langue donnée
    //On utilise les statistiques Chi Squared : http://practicalcryptography.com/cryptanalysis/text-characterisation/chi-squared-statistic/
    public float matchScore(String country) {

        float reference[];

        switch(country) {
            case "FR":
                reference = new float[] {0.0942f, 0.0102f, 0.0264f, 0.0339f, 0.1587f, 0.0095f, 0.0104f, 0.0077f, 0.0841f, 0.0089f, 0, 0.0534f, 0.0324f, 0.0715f, 0.0514f, 0.0286f, 0.0106f, 0.0646f, 0.0790f, 0.0726f, 0.0624f, 0.0215f, 0, 0.0030f, 0.0024f, 0.0032f};
                break;

            case "EN":
                reference = new float[] {0.0808f, 0.0167f, 0.0318f, 0.0399f, 0.1256f, 0.0217f, 0.0180f, 0.0527f, 0.0724f, 0.0014f, 0.0063f, 0.0404f, 0.0260f, 0.0738f, 0.0747f, 0.0191f, 0.0009f, 0.0642f, 0.0659f, 0.0915f, 0.0279f, 0.01f, 0.0189f, 0.0021f, 0.0165f, 0.0007f};
                break;

            default:
                throw new IllegalArgumentException("Language not supported");
        }

        float score = 0;

        for(char c = 'A'; c <= 'Z'; c++) {
            float expected_letter_count = (float)nb_letters*reference[c-'A'];
            int nb_occurrences = getNbOccurrencesOf(c);

            if(expected_letter_count > 0)
                score += ((nb_occurrences - expected_letter_count) * (nb_occurrences - expected_letter_count)) / expected_letter_count;
        }

        return score;
    }
}
