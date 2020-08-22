package vn.pipeline;

import vn.corenlp.postagger.PosTagger;
import vn.corenlp.tokenizer.Tokenizer;
import vn.corenlp.wordsegmenter.WordSegmenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sentence {
    private String rawSentence;
    private List<String> tokens;
    private String wordSegmentedSentence;

    private List<Word> words;

    private WordSegmenter wordSegmenter ;
    private PosTagger posTagger;

    public Sentence(String rawSentence, WordSegmenter wordSegmenter, PosTagger tagger) throws IOException {
        this.posTagger = tagger;
        this.wordSegmenter = wordSegmenter;
        init(rawSentence.trim());
    }

    public String detectLanguage() {
        try {
            return Utils.detectLanguage(rawSentence);
        } catch (IOException e) {
            System.err.println("Cannot detect language!");
        }
        // Can't detect language
        return "N/A";
    }

    private void init(String rawSentence) throws IOException {
        this.rawSentence = rawSentence;
        this.tokens = Tokenizer.tokenize(this.rawSentence);

        if(this.wordSegmenter != null) {
            this.wordSegmentedSentence = this.wordSegmenter.segmentTokenizedString(this.rawSentence);
        }
        else this.wordSegmentedSentence = String.join(" ", this.tokens);

        this.createWords();

    }

    private void createWords() throws IOException {

        if (this.posTagger != null)
            this.words = posTagger.tagSentence(this.wordSegmentedSentence);
        else {
            this.words = new ArrayList<>();
            String[] segmentedTokens = this.wordSegmentedSentence.split(" ");
            for (int i = 0; i < segmentedTokens.length; i++) {
                Word word = new Word(segmentedTokens[i]);
                this.words.add(word);
            }
        }

    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Word word : words) {
            sb.append(word.toString() + "\n");
        }
        return sb.toString().trim();
    }

    public String getRawSentence() {
        return rawSentence;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public String getWordSegmentedSentence() {
        return wordSegmentedSentence;
    }

    public List<Word> getWords() {
        return words;
    }

    public String getWordSegmentedTaggedSentence() {
        StringBuffer wordSegmentedTaggedSentence = new StringBuffer();
        for(Word word : this.words) {
            wordSegmentedTaggedSentence.append(word.toString() + " ");
        }
        return wordSegmentedTaggedSentence.toString().trim();
    }

    public void removeWord(int idx) {
        this.getWords().remove(idx);
    }

}
