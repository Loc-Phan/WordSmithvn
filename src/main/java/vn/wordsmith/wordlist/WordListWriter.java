package vn.wordsmith.wordlist;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Dictionary;

public class WordListWriter {

    public final static Logger LOGGER = Logger.getLogger(WordListWriter.class);

    public static void wordListWriter(WordList wordList, String outfile) {
        LOGGER.info("Writing to file " + outfile);
        DecimalFormat df = new DecimalFormat("###.##");
        JSONObject jsonObject = new JSONObject();

        JSONObject options = new JSONObject();
        options.put("case", true);
        options.put("num", true);
        options.put("punc", true);
        options.put("pos", true);
        for (String option : wordList.options) {
            if (option.equals("case")) options.put("case", false);
            else if (option.equals("num")) options.put("num", false);
            else if (option.equals("punc")) options.put("punc", false);
            else if (option.equals("pos")) options.put("pos", false);
        }
        jsonObject.put("options", options);

        JSONArray fileIdx = new JSONArray();
        fileIdx.add("overall");
        for (int i = 0; i < wordList.fileIndex.size(); i++) {
            fileIdx.add(wordList.fileIndex.get(i));
        }
        jsonObject.put("file_index", fileIdx);

        JSONArray fileArray = new JSONArray();
        for (int i = 0; i < wordList.statistics.size(); i++) {
            JSONObject file = new JSONObject();
            Dictionary data = wordList.statistics.get(i);
            file.put("file_size", data.get("file_size"));
            file.put("tokens_in_text", data.get("tokens_in_text"));
            file.put("words_in_text", data.get("words_in_text"));
            file.put("words_used_for_word_list", data.get("words_used_for_word_list"));
            file.put("punctuations_removed", data.get("punctuations_removed"));
            file.put("numbers_removed", data.get("numbers_removed"));
            file.put("sentences", data.get("sentences"));
            file.put("types", data.get("types"));
            file.put("TWR", Double.parseDouble(df.format(data.get("TWR"))));
            file.put("mean_token_length", Double.parseDouble(df.format(data.get("mean_token_length"))));
            file.put("std_token_length", Double.parseDouble(df.format(data.get("std_token_length"))));
            file.put("mean_word_length", Double.parseDouble(df.format(data.get("mean_word_length"))));
            file.put("std_word_length", Double.parseDouble(df.format(data.get("std_word_length"))));
            file.put("mean_sentence_length", Double.parseDouble(df.format(data.get("mean_sentence_length"))));
            file.put("std_sentence_length", Double.parseDouble(df.format(data.get("std_sentence_length"))));
            file.put("_1gram_word", data.get("_1gram_word"));
            file.put("_2gram_word", data.get("_2gram_word"));
            file.put("_3gram_word", data.get("_3gram_word"));
            file.put("_4gram_word", data.get("_4gram_word"));
            file.put("_5gram_word", data.get("_5gram_word"));
            file.put("ngram_word", data.get("ngram_word"));

            fileArray.add(file);
        }
        jsonObject.put("statistics", fileArray);

        JSONArray allArray = new JSONArray();
        for (int i = 0; i  < wordList.words.size(); i++) {
            JSONObject word = new JSONObject();
            word.put("word", wordList.words.get(i));
            word.put("pos", wordList.tags.get(i));
            word.put("frequency", wordList.freqs.get(i));
            word.put("percent", Double.parseDouble(df.format((double) wordList.freqs.get(i) / wordList.words_count * 100)));
            word.put("file", wordList.freqs_file.get(i));
            word.put("percent_file", Double.parseDouble(df.format((double) wordList.freqs_file.get(i) / wordList.fileIndex.size() * 100)));
            allArray.add(word);
        }
        jsonObject.put("word_list", allArray);

        try {
            Writer file = new OutputStreamWriter(new FileOutputStream(outfile), StandardCharsets.UTF_8);
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LOGGER.info("Done!");
    }
}