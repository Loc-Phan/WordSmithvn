package vn.pipeline;

import java.io.*;
import java.util.*;

public class Annotation {
    private String rawText;
    private List<String> tokens;
    private List<Sentence> sentences;

    public Annotation(String rawText) {
        this.rawText = rawText.trim();
        this.tokens = new ArrayList<>();
    }

    public String detectLanguage() {
        try {
            return Utils.detectLanguage(rawText);
        } catch (IOException e) {
            System.err.println("Cannot detect language!");
        }
        // Can't detect language
        return "N/A";
    }

    public static boolean isAlphabetic(String str) {
        char[] chars = str.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if(sentences != null)
            for(Sentence sentence : sentences) {
                sb.append(sentence.toString() + "\n\n");
            }
        else return rawText;
        return sb.toString();
    }

    public LinkedHashMap<String, Integer> ngrams(int n, boolean isWordLevel) {
        if (!isWordLevel) return ngramAtCharacterLevel(n);
        return ngramAtWordLevel(n);
    }

    private LinkedHashMap<String, Integer> ngramAtCharacterLevel(int n) {
        LinkedHashMap<String, Integer> output = new LinkedHashMap<>();
        for (int i = 0; i < this.rawText.length(); i++) {
            StringBuffer sb = new StringBuffer();
            if (i + n <= this.rawText.length()) {
                for (int j = i; j < i + n; j++)
                    sb.append(this.rawText.charAt(j));
                String ngram = sb.toString();
                if (!output.containsKey(ngram)) output.put(ngram, 1);
                else output.put(ngram, output.get(ngram) + 1);
            }
        }
        return output;
    }

    private LinkedHashMap<String, Integer> ngramAtWordLevel(int n) {
        LinkedHashMap<String, Integer> output = new LinkedHashMap<>();
        for (int i = 0; i < this.tokens.size(); i++) {
            StringBuffer sb = new StringBuffer();
            if (i + n <= this.tokens.size()) {
                for (int j = i; j < i + n; j++)
                    sb.append(this.tokens.get(j) + " ");
                String ngram = sb.toString();
                if (!output.containsKey(ngram)) output.put(ngram, 1);
                else output.put(ngram, output.get(ngram) + 1);
            }
        }
        return output;
    }

    public String getRawText() {
        return rawText;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

}
