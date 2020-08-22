package vn.wordsmith.keyword;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class KeyWordWriter {
    public final static Logger LOGGER = Logger.getLogger(KeyWordWriter.class);

    public static void keyWordWriter(KeyWord keyWords, String outfile) {
        LOGGER.info("Writing to file " + outfile);
        DecimalFormat df = new DecimalFormat("###.##");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("number_of_keywords", keyWords.number_keyword);

        JSONArray list = new JSONArray();
        for (int i = 0; i < keyWords.number_keyword; i++) {
            JSONObject word = new JSONObject();
            word.put("word", keyWords.words.get(i));
            word.put("pos", keyWords.tags.get(i));
            word.put("file", keyWords.files.get(i));
            word.put("frequency", keyWords.freqs.get(i));
            word.put("percent", Double.parseDouble(df.format((double) keyWords.freqs.get(i) / keyWords.text.words_count * 100)));
            word.put("frequency_corpus", keyWords.freqs_corpus.get(i));
            word.put("percent_corpus", Double.parseDouble(df.format((double) keyWords.freqs_corpus.get(i) / keyWords.reference_corpus.words_count * 100)));
            word.put("score", Double.parseDouble(df.format(keyWords.scores.get(i))));
            list.add(word);
        }
        jsonObject.put("keywords", list);

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