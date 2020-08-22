package vn.wordsmith.wordlist;

import org.apache.log4j.Logger;
import vn.pipeline.Annotation;
import vn.pipeline.Sentence;
import vn.pipeline.VnCoreNLP;
import vn.pipeline.Word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordList {

    public final static Logger LOGGER = Logger.getLogger(WordList.class);

    private List<List<Sentence>> sentences;
    public Dictionary<Integer, Dictionary> statistics;
    public int words_count;
    public List<String> words;
    public List<String> tags;
    public List<Integer> freqs;
    public List<Double> dispersion;
    public List<Integer> freqs_file;
    private String fileName;
    public List<String> options;
    public List<String> fileIndex;

    public WordList(String fileName, List<String> options) {
        sentences = new ArrayList<>();
        statistics = new Hashtable();
        words_count = 0;
        words = new ArrayList<>();
        tags = new ArrayList<>();
        freqs = new ArrayList<>();
        dispersion = new ArrayList<>();
        freqs_file = new ArrayList<>();
        this.fileName = fileName;
        this.options = options;
        fileIndex = new ArrayList<>();
    }

    public void process() throws IOException {
        String[] annotators = {"wseg", "pos"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);

        int file_size = 0, tokens_in_text = 0, words_in_text = 0, words_used_for_word_list = 0, sentences_count = 0,
            punctuations_removed = 0, numbers_removed = 0;

        if (Files.isDirectory(Paths.get(fileName))) {
            int i = 1;
            for (Path child : Files.newDirectoryStream(Paths.get(fileName), "*.txt")) {

                if (i % 1000 == 1 && i != 1) {
                    LOGGER.info("============ Processed " + (i - 1) + " files ============");
                }

                LOGGER.info("Start processing file " + i + " " + child.toString());
                fileIndex.add(child.toString());

                StringBuilder sb = new StringBuilder();
                for (String line : Files.readAllLines(child)) {
                    sb.append(line.trim()).append("\n");
                }

                Annotation annotation = new Annotation(sb.toString());
                pipeline.annotate(annotation);

                SingleWordList swl = new SingleWordList(annotation);

                swl.statistics.put("words_in_text", getWordsInText(swl));
                initOptions(options, swl);
                sentences.add(swl.annotation.getSentences());
                makeWordList(swl);
                makeStatistics(swl);
                statistics.put(i, swl.statistics);

                file_size += (Integer) swl.statistics.get("file_size");
                tokens_in_text += (Integer) swl.statistics.get("tokens_in_text");
                words_in_text += (Integer) swl.statistics.get("words_in_text");
                words_used_for_word_list += (Integer) swl.statistics.get("words_used_for_word_list");
                sentences_count += (Integer) swl.statistics.get("sentences");
                punctuations_removed += (Integer) swl.statistics.get("punctuations_removed");
                numbers_removed += (Integer) swl.statistics.get("numbers_removed");

                i++;
            }

            LOGGER.info("Summarizing");
            // Overall
            makeWordList();
            makeDispersion();

            words_count = words_used_for_word_list;

            Dictionary sttAll = new Hashtable();
            sttAll.put("file_size", file_size);
            sttAll.put("tokens_in_text", tokens_in_text);
            sttAll.put("words_in_text", words_in_text);
            sttAll.put("words_used_for_word_list", words_used_for_word_list);
            sttAll.put("sentences", sentences_count);
            sttAll.put("punctuations_removed", punctuations_removed);
            sttAll.put("numbers_removed", numbers_removed);
            makeStatistics(sttAll);

            statistics.put(0, sttAll);
        }
    }

    private void initOptions(List<String> options, SingleWordList swl) {
    	
    	
    	for (String option : options) {
			
				if (option.equals("case"))
					capitalize(swl);
				else if (option.equals("num"))
					removeNumber(swl);
				else if (option.equals("punc"))
					removePunctuation(swl);
				else if (option.equals("pos"))
					removePostag(swl);
				
			
        }
    }

    private void removePostag(SingleWordList swl) {
        for (Sentence sentence : swl.annotation.getSentences()) {
            for (Word word : sentence.getWords()) {
                word.setPosTag("_");
            }
        }
    }

    private void capitalize(SingleWordList swl) {
        for (Sentence sentence : swl.annotation.getSentences()) {
            for (Word word : sentence.getWords()) {
                word.setForm(word.getForm().toUpperCase());
            }
        }
    }

    private void removePunctuation(SingleWordList swl) {
        int count = 0;
        for (Sentence sentence : swl.annotation.getSentences()) {
            for (int i = 0; i < sentence.getWords().size(); i++) {
                Word word = sentence.getWords().get(i);
                if (word.getPosTag().equals("CH")) {
                    sentence.removeWord(i);
                    count ++;
                    i--;
                }
            }
        }
        swl.statistics.put("punctuations_removed", count);
    }

    private void removeNumber(SingleWordList swl) {
        int count = 0;
        for (Sentence sentence : swl.annotation.getSentences()) {
            for (int i = 0; i < sentence.getWords().size(); i++) {
                Word word = sentence.getWords().get(i);
                if (word.getPosTag().equals("M")) {
                    sentence.removeWord(i);
                    count ++;
                    i--;
                }
            }
        }
        swl.statistics.put("numbers_removed", count);
    }

    private void makeDispersion() {
        int division = 8;
        int division_length = words_count / division;
        for (int i = 0; i < words.size(); i++) {
            List<Integer> freq_division = new ArrayList<>();
            int freq = 0;
            int count = 0;
            for (List<Sentence> file : sentences) {
                for (Sentence sentence : file) {
                    for (Word word : sentence.getWords()) {
                        count++;
                        if (word.getForm().equals(words.get(i)) && word.getPosTag().equals(tags.get(i))) {
                            freq++;
                        }
                        if (freq_division.size() + 1 < division) {
                            if (count == division_length) {
                                freq_division.add(freq);
                                freq = 0;
                                count = 0;
                            }
                        } else {
                            if (count == division_length + words_count % division) {
                                freq_division.add(freq);
                            }
                        }
                    }
                }
            }

            double mean = (double) freqs.get(i) / division;
            double sum = 0;
            for (Integer fr : freq_division) {
                sum += Math.pow((fr - mean), 2);
            }
            double std = Math.sqrt(sum / (division - 1));

            dispersion.add(1.0 - (std / mean) / Math.sqrt(division));
        }
    }

    private void makeWordList(SingleWordList swl) {
        for (Sentence sentence : swl.annotation.getSentences()) {
            for (Word word : sentence.getWords()) {
                boolean exist = false;
                for (int i = 0; i < swl.words.size(); i++) {
                    if (swl.words.get(i).equals(word.getForm()) && swl.tags.get(i).equals(word.getPosTag())) {
                        swl.freqs.set(i, swl.freqs.get(i) + 1);
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    swl.words.add(word.getForm());
                    swl.tags.add(word.getPosTag());
                    swl.freqs.add(1);
                }
            }
        }
    }

    private void makeWordList() {
        List<List<Integer>> fileList = new ArrayList<>();

        for (int k = 0; k < sentences.size(); k++) {
            for (Sentence sentence : sentences.get(k)) {
                for (Word word : sentence.getWords()) {
                    boolean exist = false;
                    for (int i = 0; i < words.size(); i++) {
                        if (words.get(i).equals(word.getForm()) && tags.get(i).equals(word.getPosTag())) {
                            freqs.set(i, freqs.get(i) + 1);
                            fileList.get(i).add(k);
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        words.add(word.getForm());
                        tags.add(word.getPosTag());
                        freqs.add(1);
                        List<Integer> files = new ArrayList<>();
                        files.add(k);
                        fileList.add(files);
                    }
                }
            }
        }
        for (List<Integer> l : fileList) {
            Set<Integer> file = new HashSet<>(l);
            freqs_file.add(file.size());
        }
    }

    private void makeStatistics(SingleWordList swl) {
        swl.statistics.put("file_size", swl.annotation.getRawText().length()); // in characters
        swl.statistics.put("tokens_in_text", swl.annotation.getTokens().size());
        swl.words_count = getWordsInText(swl);
        swl.statistics.put("words_used_for_word_list", swl.words_count);
        int types = swl.words.size();
        swl.statistics.put("types", types); // distinct words
        swl.statistics.put("TWR", (double) types / swl.words_count * 100); //types/words ratio
        double mean_word_length = getMeanWordLength(swl);
        swl.statistics.put("mean_word_length", mean_word_length); // in tokens
        swl.statistics.put("std_word_length", getStdWordLength(mean_word_length, swl));
        double mean_token_length = getMeanTokenLength(swl);
        swl.statistics.put("mean_token_length", mean_token_length); // in characters
        swl.statistics.put("std_token_length", getStdTokenLength(mean_token_length, swl));
        int sentences_count = swl.annotation.getSentences().size();
        swl.statistics.put("sentences", sentences_count);
        double mean_sentence_length = getMeanSentenceLength(sentences_count, swl);
        swl.statistics.put("mean_sentence_length", mean_sentence_length); // in words
        swl.statistics.put("std_sentence_length", getStdSentenceLength(mean_sentence_length, sentences_count, swl));
        List<Integer> ngram_word = getNgramWord(swl);
        swl.statistics.put("_1gram_word", ngram_word.get(0));
        swl.statistics.put("_2gram_word", ngram_word.get(1));
        swl.statistics.put("_3gram_word", ngram_word.get(2));
        swl.statistics.put("_4gram_word", ngram_word.get(3));
        swl.statistics.put("_5gram_word", ngram_word.get(4));
        swl.statistics.put("ngram_word", ngram_word.get(5));
    }

    private void makeStatistics(Dictionary sttAll) {
        int types = words.size();
        sttAll.put("types", types); // distinct words
        sttAll.put("TWR", (double) types / words_count * 100); //types/words ratio
        double mean_word_length = getMeanWordLength();
        sttAll.put("mean_word_length", mean_word_length); // in tokens
        sttAll.put("std_word_length", getStdWordLength(mean_word_length));
        double mean_token_length = getMeanTokenLength();
        sttAll.put("mean_token_length", mean_token_length); // in characters
        sttAll.put("std_token_length", getStdTokenLength(mean_token_length));
        int sentences_count = (Integer) sttAll.get("sentences");
        double mean_sentence_length = getMeanSentenceLength(sentences_count);
        sttAll.put("mean_sentence_length", mean_sentence_length); // in words
        sttAll.put("std_sentence_length", getStdSentenceLength(mean_sentence_length, sentences_count));
        List<Integer> ngram_word = getNgramWord();
        sttAll.put("_1gram_word", ngram_word.get(0));
        sttAll.put("_2gram_word", ngram_word.get(1));
        sttAll.put("_3gram_word", ngram_word.get(2));
        sttAll.put("_4gram_word", ngram_word.get(3));
        sttAll.put("_5gram_word", ngram_word.get(4));
        sttAll.put("ngram_word", ngram_word.get(5));
    }

    private List<Integer> getNgramWord(SingleWordList swl) {
        List<Integer> result = new ArrayList<>();
        int one_gram = 0, two_gram = 0, three_gram = 0, four_gram = 0, five_gram = 0, over = 0;
        for (int i = 0; i < swl.words.size(); i++) {
            int len = swl.words.get(i).split("_").length;
            if (len == 1) one_gram += swl.freqs.get(i);
            else if (len == 2) two_gram += swl.freqs.get(i);
            else if (len == 3) three_gram += swl.freqs.get(i);
            else if (len == 4) four_gram += swl.freqs.get(i);
            else if (len == 5) five_gram += swl.freqs.get(i);
            else over += swl.freqs.get(i);
        }
        result.add(one_gram);
        result.add(two_gram);
        result.add(three_gram);
        result.add(four_gram);
        result.add(five_gram);
        result.add(over);
        return result;
    }

    private double getStdSentenceLength(double mean_sentence_length, int sentences_count, SingleWordList swl) {
        double sum = 0;
        for (Sentence sentence : swl.annotation.getSentences()) {
            sum += Math.pow((sentence.getWords().size() - mean_sentence_length), 2);
        }
        return Math.sqrt(sum / (sentences_count - 1));
    }

    private double getMeanSentenceLength(int sentences, SingleWordList swl) {
        int sum = 0;
        for (Sentence sentence : swl.annotation.getSentences()) {
            sum += sentence.getWords().size();
        }
        return (double) sum / sentences;
    }

    private double getStdTokenLength(double mean_token_length, SingleWordList swl) {
        double sum = 0, count = 0;
        for (int i = 0; i < swl.words.size(); i++) {
            String tokens[] = swl.words.get(i).split("_");
            for (String token : tokens) {
                sum += Math.pow((token.length() - mean_token_length), 2) * swl.freqs.get(i);
            }
            count += tokens.length * swl.freqs.get(i);
        }
        return Math.sqrt(sum / (count - 1));

    }

    private double getStdWordLength(double mean_word_length, SingleWordList swl) {
        double sum = 0;
        for (int i = 0; i < swl.words.size(); i++) {
            String tokens[] = swl.words.get(i).split("_");
            sum += Math.pow((tokens.length - mean_word_length), 2) * swl.freqs.get(i);
        }
        return Math.sqrt(sum / (swl.words_count - 1));
    }

    private double getMeanTokenLength(SingleWordList swl) {
        int sum = 0, count = 0;
        for (int i = 0; i < swl.words.size(); i++) {
            String tokens[] = swl.words.get(i).split("_");
            for (String token : tokens) {
                sum += token.length() * swl.freqs.get(i);
            }
            count += tokens.length * swl.freqs.get(i);
        }
        return (double) sum / count;
    }

    private double getMeanWordLength(SingleWordList swl) {
        int sum = 0;
        for (int i = 0; i < swl.words.size(); i++) {
            String tokens[] = swl.words.get(i).split("_");
            sum += tokens.length * swl.freqs.get(i);
        }
        return (double) sum / swl.words_count;
    }

    private int getWordsInText(SingleWordList swl) {
        int count = 0;
        for (Sentence sentence : swl.annotation.getSentences()) {
            count += sentence.getWords().size();
        }
        return count;
    }

    // Overall
    private List<Integer> getNgramWord() {
        List<Integer> result = new ArrayList<>();
        int one_gram = 0, two_gram = 0, three_gram = 0, four_gram = 0, five_gram = 0, over = 0;
        for (int i = 0; i < words.size(); i++) {
            int len = words.get(i).split("_").length;
            if (len == 1) one_gram += freqs.get(i);
            else if (len == 2) two_gram += freqs.get(i);
            else if (len == 3) three_gram += freqs.get(i);
            else if (len == 4) four_gram += freqs.get(i);
            else if (len == 5) five_gram += freqs.get(i);
            else over += freqs.get(i);
        }
        result.add(one_gram);
        result.add(two_gram);
        result.add(three_gram);
        result.add(four_gram);
        result.add(five_gram);
        result.add(over);
        return result;
    }

    private double getStdSentenceLength(double mean_sentence_length, int sentences_count) {
        double sum = 0;
        for (List<Sentence> file : sentences){
            for (Sentence sentence : file) {
                sum += Math.pow((sentence.getWords().size() - mean_sentence_length), 2);
            }
        }
        return Math.sqrt(sum / (sentences_count - 1));
    }

    private double getMeanSentenceLength(int sentences_count) {
        int sum = 0;
        for (List<Sentence> file : sentences) {
            for (Sentence sentence : file) {
                sum += sentence.getWords().size();
            }
        }
        return (double) sum / sentences_count;
    }

    private double getStdTokenLength(double mean_token_length) {
        double sum = 0, count = 0;
        for (int i = 0; i < words.size(); i++) {
            String tokens[] = words.get(i).split("_");
            for (String token : tokens) {
                sum += Math.pow((token.length() - mean_token_length), 2) * freqs.get(i);
            }
            count += tokens.length * freqs.get(i);
        }
        return Math.sqrt(sum / (count - 1));

    }

    private double getStdWordLength(double mean_word_length) {
        double sum = 0;
        for (int i = 0; i < words.size(); i++) {
            String tokens[] = words.get(i).split("_");
            sum += Math.pow((tokens.length - mean_word_length), 2) * freqs.get(i);
        }
        return Math.sqrt(sum / (words_count - 1));
    }

    private double getMeanTokenLength() {
        int sum = 0, count = 0;
        for (int i = 0; i < words.size(); i++) {
            String tokens[] = words.get(i).split("_");
            for (String token : tokens) {
                sum += token.length() * freqs.get(i);
            }
            count += tokens.length * freqs.get(i);
        }
        return (double) sum / count;
    }

    private double getMeanWordLength() {
        int sum = 0;
        for (int i = 0; i < words.size(); i++) {
            String tokens[] = words.get(i).split("_");
            sum += tokens.length * freqs.get(i);
        }
        return (double) sum / words_count;
    }
}



class SingleWordList {
    public List<String> words;
    public List<String> tags;
    public List<Integer> freqs;
    public Annotation annotation;
    public Dictionary statistics;
    public Integer words_count;

    public SingleWordList(Annotation annotation) {
        words = new ArrayList<>();
        tags = new ArrayList<>();
        freqs = new ArrayList<>();
        statistics = new Hashtable();
        this.annotation = annotation;
    }
}
