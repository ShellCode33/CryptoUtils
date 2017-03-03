package fr.iut.model;

import java.util.*;

/**
 * Created by shellcode on 2/3/17.
 */
public class Vigenere {

    FrequentialAnalysis frequentialAnalysis;
    String original_text, text; //Ce texte peut très bien être du plaintext comme le cipher

    public Vigenere(FrequentialAnalysis frequentialAnalysis) {
        this.frequentialAnalysis = frequentialAnalysis;
        original_text = frequentialAnalysis.getText();
        text = original_text.replaceAll(" +", "");
    }

    public String decode(String key) {
        return decode(original_text, key);
    }

    public String decode(String cipher, String key) {

        boolean hasNonAlpha = key.matches("^.*[^a-zA-Z0-9 ].*$");

        if(hasNonAlpha)
            throw new IllegalArgumentException("Special chars are not allowed ! Please remove all the '\"éàè@ etc...");

        key = key.toUpperCase();
        String plaintext = "";

        int nb_spaces = 0;
        for(int i = 0; i < cipher.length(); i++) {
            char text_letter = cipher.charAt(i);

            if(text_letter != ' ') {
                int key_index = (i - nb_spaces) % key.length();
                char key_letter = key.charAt(key_index);
                int shift = (text_letter - key_letter);

                if(shift < 0)
                    shift += 26;

                plaintext += (char) (shift % 26 + 'A');
            }

            else {
                nb_spaces++; //On compte les espaces pour gérer correctement le décallage de la clé
                plaintext += " ";
            }
        }

        return plaintext;
    }

    public String encode(String key) {

        boolean hasNonAlpha = key.matches("^.*[^a-zA-Z0-9 ].*$");

        if(hasNonAlpha)
            throw new IllegalArgumentException("Special chars are not allowed ! Please remove all the '\"éàè@ etc...");

        key = key.toUpperCase();
        String cipher = "";

        int nb_spaces = 0;
        for(int i = 0; i < text.length(); i++) {
            char text_letter = text.charAt(i);

            if(text_letter != ' ') {
                int key_index = (i - nb_spaces) % key.length();
                char key_letter = key.charAt(key_index);
                int shift = text_letter + key_letter;
                cipher += (char) (shift % 26 + 'A');
            }

            else {
                nb_spaces++; //On compte les espaces pour gérer correctement le décallage de la clé
                cipher += " ";
            }
        }

        return cipher;
    }

    public void crack() {

        ArrayList<Integer> repeatedPatternsDistances = findDistancesBetweenPatterns();

        if(repeatedPatternsDistances.size() == 0) {
            System.out.println("No pattern found, can't crack this sorry...");
            return;
        }

        System.out.print("Distances are :");
        for(Integer i : repeatedPatternsDistances)
            System.out.print(" " + i);
        System.out.println();


        //Maintenant on fait un genre de pgcd entre toutes les distances trouvées : on choisit en fait le diviseur qui est commun à un maximum de distances

        Map<Integer, Integer> dividersOccurrences = new HashMap<>(); //Permet de stocker les diviseurs possibles, celui qui apparait le plus de fois devrait être la longueur de la clé

        //On récupère tous les diviseurs possibles de chacune des distances
        for(Integer distance : repeatedPatternsDistances) {
            for(int i = 2; i < distance; i++) {
                if (distance%i == 0) {
                    Integer d = dividersOccurrences.get(i);

                    if(d == null)
                        dividersOccurrences.put(i, 1);
                    else
                        dividersOccurrences.put(i, d+1);
                }
            }
        }

        int highest_occurence = 0;

        //On trouve le diviseur le plus présent
        for(Map.Entry<Integer, Integer> entry : dividersOccurrences.entrySet()) {
            if(entry.getValue() > highest_occurence) {
                highest_occurence = entry.getValue();
            }
        }

        ArrayList<Integer> possible_keys_length = new ArrayList<>();

        //On récupère toutes les clés possible avec une tolerence de +ou- highest_occurence/2
        for(Map.Entry<Integer, Integer> entry : dividersOccurrences.entrySet()) {
            if((float)highest_occurence-(float)highest_occurence/2 < entry.getValue() && entry.getValue() < (float)highest_occurence+(float)highest_occurence/2)
                possible_keys_length.add(entry.getKey());
        }

        System.out.print("Possible keys length are :");

        for(Integer key_length : possible_keys_length)
            System.out.print(" " + key_length);

        System.out.println("\n");

        Map<String, Float> best_keys_score = new LinkedHashMap<>();

        for(int k = possible_keys_length.size()-1; k >= 0; k--) {
            int key_length = possible_keys_length.get(k);
            System.out.println("Attempting with key length of " + key_length + "...");
            String encrypted_with_same_letter_of_key[] = new String[key_length];

            for(int i = 0; i < key_length; i++) {

                encrypted_with_same_letter_of_key[i] = "";

                for(int j = i; j < text.length(); j+=key_length) {
                    encrypted_with_same_letter_of_key[i] += text.charAt(j);
                }
            }

            List<List<Character>> letters_to_bruteforce = new ArrayList<>(key_length); //regroupe toutes les lettres POSSIBLES pour chaque lettre de la clé (qu'on va bruteforce donc...)

            for(int i = 0; i < encrypted_with_same_letter_of_key.length; i++) {

                System.out.println("Analyzing " + encrypted_with_same_letter_of_key[i] + "...");
                letters_to_bruteforce.add(new ArrayList<>());


                ArrayList<Float> scores = new ArrayList<>();

                System.out.print("Trying : ");

                for(char c = 'A'; c <= 'Z'; c++) {
                    String decoded = decode(encrypted_with_same_letter_of_key[i], "" + c);
                    System.out.print(" " + c);

                    float score = new FrequentialAnalysis(decoded).matchScore("FR");
                    scores.add(score);
                }

                System.out.println();

                float lowest_value = Float.MAX_VALUE;
                char best_letter = 0;
                int loop_index = 0;

                //On trouve la valeur la plus basse
                for(Float f : scores) {

                    if(f < lowest_value) {
                        lowest_value = f;
                        best_letter = (char)('A'+loop_index);
                    }

                    loop_index++;
                }

                loop_index = 0;

                //On prend touts les scores qui sont au maximum 1.25 fois plus grand que le plus petite valeur
                for(Float f : scores) {
                    if(f <= lowest_value*1.25 && !letters_to_bruteforce.get(i).contains((char)('A'+loop_index)))
                        letters_to_bruteforce.get(i).add((char)('A'+loop_index));

                    loop_index++;
                }

            }

            int nb_possible_keys = 1;
            System.out.println("\nEach lines are possible letters for the N-th letter of the key : ");

            for(List<Character> letters : letters_to_bruteforce) {
                System.out.print('\t');

                for(Character letter : letters)
                    System.out.print(letter);

                System.out.println();
                nb_possible_keys *= letters.size();
            }

            if(nb_possible_keys == 1) {

                String key = "";

                for(List<Character> letters : letters_to_bruteforce) {
                    key += letters.get(0);
                }

                String decoded = decode(key);

                System.out.println("Looks like the key is : " + key);
                System.out.println("Decoded message : " + decoded);
                System.out.print("Does the text makes sense ? Do you want to continue searching (Y/N) ? ");

                Scanner scanner = new Scanner(System.in);

                String read = "Dummy";
                while(read.charAt(0) != 'Y' && read.charAt(0) != 'N') {
                    read = scanner.nextLine();
                }

                if(read.charAt(0) == 'N')
                    System.exit(0);

                best_keys_score.put(key, findCoincidenceIndex(decoded));
            }

            else {
                System.out.println("\n" + nb_possible_keys + " keys to bruteforce ! Much better than " + Math.pow(26, key_length) + "\n");

                List<String> keys = new ArrayList<>();
                bruteforce(letters_to_bruteforce, keys, 0, "");

                for(String key : keys) { //On essaye de décoder le texte avec toutes les clés probables et on regarde l'indice de coincidence sur le texte
                    String decoded_texte = decode(key);
                    float score = findCoincidenceIndex(decoded_texte);
                    best_keys_score.put(key, score);
                }
            }
        }

        Map<String, Float> best_keys_score_sorted = new LinkedHashMap<>();

        System.out.println("There is a total of " + best_keys_score.size() + " keys, here is the list from the less probable to the most :");

        while(best_keys_score.size() > 0) {
            float lowest_score = Float.MAX_VALUE;
            String worst_key = "";

            for(Map.Entry<String, Float> entry : best_keys_score.entrySet()) {
                if(entry.getValue() < lowest_score) {
                    lowest_score = entry.getValue();
                    worst_key = entry.getKey();
                }
            }

            best_keys_score_sorted.put(worst_key, lowest_score);
            best_keys_score.remove(worst_key);
        }

        String best_key = "";

        for(Map.Entry<String, Float> key_score : best_keys_score_sorted.entrySet()) {
            System.out.println(key_score.getKey() + " :");
            System.out.println("\tscore : " + key_score.getValue());
            System.out.println("\ttext : " + decode(key_score.getKey()) + "\n");
            best_key = key_score.getKey();
        }

        System.out.println("\nSo it looks like the good key is : " + best_key);
        System.out.println("But be careful because it could be wrong... So check the decoded text if it makes sense.");
    }

    private static void bruteforce(List<List<Character>> letters_to_bruteforce, List<String> keys, int depth, String current)
    {
        if(depth == letters_to_bruteforce.size())
        {
            keys.add(current);
            return;
        }

        for(int i = 0; i < letters_to_bruteforce.get(depth).size(); ++i)
        {
            bruteforce(letters_to_bruteforce, keys, depth + 1, current + letters_to_bruteforce.get(depth).get(i));
        }
    }

    private float findCoincidenceIndex(String text) {

        FrequentialAnalysis fa = new FrequentialAnalysis(text);
        float coincidenceIndex = 0;

        for(char c = 'A'; c <= 'Z'; c++) {
            int nb_occurrences = fa.getNbOccurrencesOf(c);
            coincidenceIndex += nb_occurrences*(nb_occurrences-1);
        }

        int nb_letters = fa.getNbLetters();
        coincidenceIndex /= nb_letters*(nb_letters-1);

        return coincidenceIndex;
    }

    public FrequentialAnalysis getFrequentialAnalysis() {
        return frequentialAnalysis;
    }

    private ArrayList<Integer> findDistancesBetweenPatterns() {
        ArrayList<Integer> repeatedPatternsDistances = new ArrayList<>();

        System.out.println("PATTERNS : ");

        //On cherche pour les groupes de lettres à 4...
        for(int i = 0; i < text.length()-4; i++) {
            String pattern = "" + text.charAt(i) + text.charAt(i+1) + text.charAt(i+2) + text.charAt(i+3);
            int index_other = text.indexOf(pattern, i+4);

            if(index_other > -1) {
                System.out.print(" " + pattern);
                repeatedPatternsDistances.add(index_other - i);
            }
        }

        //...puis avec 3...
        for(int i = 0; i < text.length()-3; i++) {
            String pattern = "" + text.charAt(i) + text.charAt(i+1) + text.charAt(i+2);
            int index_other = text.indexOf(pattern, i+3);

            if(index_other > -1) {
                System.out.print(" " + pattern);
                repeatedPatternsDistances.add(index_other - i);
            }
        }

        //s'il n'y en a aucun ou qu'un on cherche avec 2
        if(repeatedPatternsDistances.size() <= 1) {
            for(int i = 0; i < text.length()-2; i++) {
                String pattern = "" + text.charAt(i) + text.charAt(i+1);
                int index_other = text.indexOf(pattern, i+2);

                if(index_other > -1) {
                    System.out.print(" " + pattern);
                    repeatedPatternsDistances.add(index_other - i);
                }
            }
        }

        System.out.println();
        return repeatedPatternsDistances;
    }
}
