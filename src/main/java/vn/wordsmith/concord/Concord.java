package vn.wordsmith.concord;

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
import java.util.stream.Collectors;

public class Concord {
    public final static Logger LOGGER = Logger.getLogger(Concord.class);

    private String fileName;
    public List<String> fileIndex;
    private List<List<Sentence>> sentences;
    public List<List<String>> contexts;
    public List<String> tags;
    public List<Integer> word_index;
    public List<Integer> sentence_index;
    public List<Integer> file_index;

    public List<Integer> num_sentences;
    public List<Integer> num_words;
    public List<Integer> freqs;
    public List<Double> dispersions;

    public String word_search;
    public boolean case_sensitive;

    public HashMap<String, List<Integer>> collocate;

    public Concord(String fileName) {
        this.fileName = fileName;
        fileIndex = new ArrayList<>();
        sentences = new ArrayList<>();
        freqs = new ArrayList<>();
        dispersions = new ArrayList<>();
        collocate = new HashMap<>();
    }

    public void process() throws IOException {
        String[] annotators = {"wseg", "pos"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);


        if (Files.isDirectory(Paths.get(fileName))) {
            int i = 1;
            for (Path child : Files.newDirectoryStream(Paths.get(fileName), "*.txt")) {
                LOGGER.info("Start processing " + child.toString());
                fileIndex.add(child.toString());

                StringBuilder sb = new StringBuilder();
                for (String line : Files.readAllLines(child)) {
                    sb.append(line.trim()).append("\n");
                }

                Annotation annotation = new Annotation(sb.toString());
                pipeline.annotate(annotation);

                List<Sentence> sents = annotation.getSentences();
                sentences.add(sents);
            }
        }
    }

    public void concordance(String w, boolean case_sensitive) {
        LOGGER.info("Finding word " + w);
        word_search = w;
        this.case_sensitive = case_sensitive;
        contexts = new ArrayList<>();

        tags = new ArrayList<>();
        word_index = new ArrayList<>();
        sentence_index = new ArrayList<>();
        file_index = new ArrayList<>();
        num_words = new ArrayList<>();
        num_sentences = new ArrayList<>();

        for (List<Sentence> file : sentences) {

            int pos = 0;
            int x = 0;  // dòng đầu tiên
            for (Sentence sentence : file) {
                int y = 0;
                for (Word word : sentence.getWords()) {
                    String word_ = word.getForm();
                    if (!case_sensitive) {
                        word_ = word_.toUpperCase();
                        w = w.toUpperCase();
                    }

                    String context_right = "", context_left = "";

                    if (word_.equals(w)) {
                        if (y < 7 && x == 0) {
                            for (int i = 0; i < y; i++) {
                                context_left += sentence.getWords().get(i).getForm() + " ";
                            }
                        }

                        else if (y < 7) {
                            int t = 7 - y;
                            for (int i = file.get(x-1).getWords().size() - t; i < file.get(x-1).getWords().size(); i++) {
                                context_left += file.get(x-1).getWords().get(i).getForm() + " ";
                            }
                            for (int i = 0; i < y; i++) {
                                context_left += sentence.getWords().get(i).getForm() + " ";
                            }
                        }

                        else {
                            for (int i = y - 7; i < y; i++) {
                                context_left += sentence.getWords().get(i).getForm() + " ";
                            }
                        }

                        if (sentence.getWords().size() - y < 7 && x == file.size() - 1) {
                            for (int i = y + 1; i < sentence.getWords().size(); i++) {
                                context_right += sentence.getWords().get(i).getForm() + " ";
                            }
                        }

                        else if (sentence.getWords().size() - 1 - y < 7) {
                            int t = 7 - (sentence.getWords().size() - 1 - y);
                            for (int i = y + 1; i < sentence.getWords().size(); i++) {
                                context_right += sentence.getWords().get(i).getForm() + " ";
                            }
                            for (int i = 0; i < t; i++) {
                                context_right += file.get(x + 1).getWords().get(i).getForm() + " ";
                            }
                        }

                        else {
                            for (int i = y + 1; i < y + 8; i++) {
                                context_right += sentence.getWords().get(i).getForm() + " ";
                            }
                        }

                        List<String> ctx = new ArrayList<>();
                        ctx.add(context_left.trim());
                        ctx.add(word.getForm());
                        ctx.add(context_right.trim());
                        contexts.add(ctx);

                        word_index.add(pos + y);
                        sentence_index.add(x);
                        tags.add(word.getPosTag());
                        file_index.add(sentences.indexOf(file));

                    }
                    y++;
                }
                x++;
                pos += sentence.getWords().size();
            }
            num_words.add(pos);
            num_sentences.add(file.size());

        }
        statistics();
        collocates(w);
    }

    public void statistics() {
        for (int i = 0; i < num_words.size(); i++) {
            dispersions.add(makeDispersion(i, num_words.get(i)));
        }
    }

    private double makeDispersion(int file, int words_count) {
        List<Integer> word_index_file = new ArrayList<>();
        for (int k = 0; k < file_index.size(); k++) {
            if (file_index.get(k) == file) word_index_file.add(word_index.get(k));
        }
        freqs.add(word_index_file.size());
        int division = 8;
        int division_length = words_count / division;
        List<Integer> freq_division = new ArrayList<>();
        freq_division.add(0);
        freq_division.add(0);
        freq_division.add(0);
        freq_division.add(0);
        freq_division.add(0);
        freq_division.add(0);
        freq_division.add(0);
        freq_division.add(0);

        int j = 0;
        for (int i = 0; i < word_index_file.size(); i++) {
            j = word_index_file.get(i) / division_length;
            
            freq_division.set(j, freq_division.get(j) + 1);
        }
        double mean = (double) word_index_file.size() / division;
        double sum = 0;
        for (Integer fr : freq_division) {
            sum += Math.pow((fr - mean), 2);
        }
        double std = Math.sqrt(sum / (division - 1));

        return (1.0 - (std / mean) / Math.sqrt(division));
    }

    public void collocates(String w) {

        List<List<String>> word_rows = new ArrayList<>();
        for (List<Sentence> file : sentences) {

            int pos = 0;
            int x = 0;  // dòng đầu tiên
            for (Sentence sentence : file) {
                int y = 0;
                for (Word word : sentence.getWords()) {

                    if (word.getForm().equalsIgnoreCase(w)) {
                        List<String> row = new ArrayList<>();
                        if (y < 5 && x == 0) {
                            int t = 5 - y;
                            for (int i = 0; i < t; i++) {
                                row.add("");
                            }
                            for (int i = t; i < y; i++) {
                                row.add(sentence.getWords().get(i).getForm());
                            }
                        } else if (y < 5) {
                            int t = 5 - y;
                            for (int i = file.get(x - 1).getWords().size() - t; i < file.get(x - 1).getWords().size(); i++) {
                                row.add(file.get(x - 1).getWords().get(i).getForm());
                            }
                            for (int i = 0; i < y; i++) {
                                row.add(sentence.getWords().get(i).getForm());
                            }
                        } else {
                            for (int i = y - 5; i < y; i++) {
                                row.add(sentence.getWords().get(i).getForm());
                            }
                        }

                        if (sentence.getWords().size() - y < 5 && x == file.size() - 1) {
                            int t = 5 - (sentence.getWords().size() - y);
                            for (int i = y + 1; i < sentence.getWords().size(); i++) {
                                row.add(sentence.getWords().get(i).getForm());
                            }
                            for (int i = 0; i < t; i++) {
                                row.add("");
                            }
                        } else if (sentence.getWords().size() - 1 - y < 5) {
                            int t = 7 - (sentence.getWords().size() - 1 - y);
                            for (int i = y + 1; i < sentence.getWords().size(); i++) {
                                row.add(sentence.getWords().get(i).getForm());
                            }
                            for (int i = 0; i < t; i++) {
                                row.add(file.get(x + 1).getWords().get(i).getForm());
                            }
                        } else {
                            for (int i = y + 1; i < y + 6; i++) {
                                row.add(sentence.getWords().get(i).getForm());
                            }
                        }
                        word_rows.add(row);
                    }
                    y++;
                }
                x++;
            }
        }

        List<List<String>> word_cols = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> col = new ArrayList<>();
            for (int j = 0; j < word_rows.size(); j++) {
                col.add(word_rows.get(j).get(i).toUpperCase());
            }
            word_cols.add(col);
        }

        HashMap<String, List<Integer>> dict = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < word_rows.size(); j++) {
                String wd = word_cols.get(i).get(j);
                if (wd != "" && dict.keySet().contains(wd)) {
                    dict.get(wd).set(i, dict.get(wd).get(i) + 1);
                }
                else  if (wd != "") {
                    List<Integer> new_freq = new ArrayList<>();
                    new_freq.add(0);new_freq.add(0);new_freq.add(0);new_freq.add(0);new_freq.add(0);
                    new_freq.add(0);new_freq.add(0);new_freq.add(0);new_freq.add(0);new_freq.add(0);
                    new_freq.set(i, 1);
                    dict.put(wd, new_freq);
                }
            }
        }

        Map<String, Integer> sum_freq = new LinkedHashMap<>();
        for (String entry : dict.keySet()) {
            int sum = 0;
            for (int j = 0; j < 10; j++) {
                sum += dict.get(entry).get(j);
            }
            sum_freq.put(entry, sum);
        }

        Map<String, Integer> sorted = sum_freq.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(5).collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<String, Integer> wd : sorted.entrySet()) {
            collocate.put(wd.getKey(), dict.get(wd.getKey()));
        }

    }
}
