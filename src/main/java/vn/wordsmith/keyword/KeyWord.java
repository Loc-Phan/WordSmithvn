package vn.wordsmith.keyword;

import org.apache.log4j.Logger;
import vn.wordsmith.wordlist.WordList;

import java.util.*;
import java.util.stream.Collectors;

public class KeyWord {

    public final static Logger LOGGER = Logger.getLogger(KeyWord.class);

    public WordList text;
    public WordList reference_corpus;
    public int number_keyword;
    public List<String> words;
    public List<String> tags;
    public List<Integer> freqs;
    public List<Integer> freqs_corpus;
    public List<Integer> files;
    public List<Double> scores;

    public KeyWord(WordList text, WordList reference_corpus, int number_keyword) {
        LOGGER.info("Finding keywords");
        this.text = text;
        this.reference_corpus = reference_corpus;
        this.number_keyword = number_keyword;
        words = new ArrayList<>();
        tags = new ArrayList<>();
        freqs = new ArrayList<>();
        freqs_corpus = new ArrayList<>();
        files = new ArrayList<>();
        scores = new ArrayList<>();
    }

    private double tf(String word, String tag) {
        for (int i = 0; i < text.words.size(); i++) {
            if (text.words.get(i).equals(word) && text.tags.get(i).equals(tag)) {
                return (double) text.freqs.get(i) / text.words_count;
            }
        }
        return 0;
    }

    private double idf(String word, String tag) {
        for (int i = 0; i < reference_corpus.words.size(); i++) {
            if (reference_corpus.words.get(i).equals(word) && reference_corpus.tags.get(i).equals(tag)) {
                return Math.log((double) reference_corpus.fileIndex.size() / (0.01 + reference_corpus.freqs_file.get(i)));
            }
        }
        return 0;
    }

    private double tfidf(String word, String tag) {
        return tf(word, tag) * idf(word, tag);
    }

    public void findKeyWord() {
        Map<Integer, Double> scoresMap = new LinkedHashMap<>();
        for (int i = 0; i < text.words.size(); i++) {
            scoresMap.put(i, tfidf(text.words.get(i), text.tags.get(i)));
        }

        Map<Integer, Double> sorted = scoresMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(number_keyword).collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<Integer, Double> word : sorted.entrySet()) {
            int idx = word.getKey();
            String w = text.words.get(idx);
            words.add(w);
            tags.add(text.tags.get(idx));
            freqs.add(text.freqs.get(idx));
            files.add(text.freqs_file.get(idx));
            boolean exist = false;
            for (int j = 0; j < reference_corpus.words.size(); j++) {
                if (reference_corpus.words.get(j).equals(w)) {
                    freqs_corpus.add(reference_corpus.freqs.get(j));
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                freqs_corpus.add(0);
            }
            scores.add(word.getValue());
        }
    }
}


