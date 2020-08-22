package vn.wordsmith.keyword;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vn.wordsmith.wordlist.WordList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class KeyWordReader {
    public final static Logger LOGGER = Logger.getLogger(KeyWordReader.class);

    public static WordList keyWordReader(String infile) {
        LOGGER.info("Reading file " + infile);

        JSONParser parser = new JSONParser();
        Object object = null;
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(infile), "UTF-8"));
            String line = null;

            while ((line = buff.readLine()) != null) {
                object = parser.parse(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = (JSONObject) object;

        WordList wordList = new WordList(null, null);
        List<String> words = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        List<Integer> freqs = new ArrayList<>();
        List<Integer> freqs_file = new ArrayList<>();

        JSONArray file_index = (JSONArray) jsonObject.get("file_index");
        List<String> fileIndex = (List<String>) file_index;
        fileIndex.remove(0); //remove overall

        JSONArray jsonArray = (JSONArray) jsonObject.get("word_list");
        int types = jsonArray.size();
        for (int i = 0; i < types; i++) {
            JSONObject word = (JSONObject) jsonArray.get(i);
            words.add((String) word.get("word"));
            tags.add((String) word.get("pos"));
            freqs.add(((Long) word.get("frequency")).intValue());
            freqs_file.add(((Long) word.get("file")).intValue());
        }

        JSONArray stt = (JSONArray) jsonObject.get("statistics");
        Integer words_count = ((Long) (((JSONObject) stt.get(0)).get("words_used_for_word_list"))).intValue();

        wordList.words = words;
        wordList.tags = tags;
        wordList.freqs = freqs;
        wordList.freqs_file = freqs_file;
        wordList.words_count = words_count;
        wordList.fileIndex = fileIndex;

        return wordList;
    }
}
