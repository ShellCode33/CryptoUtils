package fr.iut.model;

/**
 * Created by shellcode on 3/2/17.
 */
public class Ceasar {

    String cipher;
    String [] results = new String[26];

    public Ceasar(String cipher) {
        this.cipher = cipher;
    }

    public void bruteforce() {
        for(int i = 0; i < 26; i++) {

            results[i] = "";

            for(char c : cipher.toCharArray()) {

            }
        }
    }

    public String getMostProbableAnswer() {
        return null;
    }
}
