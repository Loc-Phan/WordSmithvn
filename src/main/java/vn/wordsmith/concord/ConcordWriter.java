package vn.wordsmith.concord;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class ConcordWriter {
    public final static Logger LOGGER = Logger.getLogger(ConcordWriter.class);

    public static void concordWriter(Concord concord, String outfile) {
        LOGGER.info("Writing to file " + outfile);
        DecimalFormat df = new DecimalFormat("###.##");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("word", concord.word_search);
        jsonObject.put("case", concord.case_sensitive);
        jsonObject.put("files", concord.fileIndex);

        JSONArray list = new JSONArray();
        for (int i = 0; i < concord.word_index.size(); i++) {
            JSONObject word = new JSONObject();
            word.put("context", concord.contexts.get(i));
            word.put("pos", concord.tags.get(i));
            word.put("word_index", concord.word_index.get(i));
            word.put("percent_word_index", Double.parseDouble(df.format(((double) concord.word_index.get(i) + 1) / concord.num_words.get(concord.file_index.get(i)) * 100)));
            word.put("sentence_index", concord.sentence_index.get(i));
            word.put("percent_sentence_index", Double.parseDouble(df.format(((double) concord.sentence_index.get(i) + 1) / concord.num_sentences.get(concord.file_index.get(i)) * 100)));
            word.put("file_index", concord.file_index.get(i));
            list.add(word);
        }
        jsonObject.put("concordance", list);

        JSONArray statistics = new JSONArray();
        for (int i = 0; i < concord.fileIndex.size(); i++) {
            JSONObject plot_file = new JSONObject();
            plot_file.put("words", concord.num_words.get(i));
            plot_file.put("hits", concord.freqs.get(i));
            plot_file.put("dispersion", Double.parseDouble(df.format(concord.dispersions.get(i))));
            statistics.add(plot_file);
        }
        jsonObject.put("statistics", statistics);

        JSONArray collo = new JSONArray();
        for (String key : concord.collocate.keySet()) {
            JSONObject cll = new JSONObject();
            cll.put(key, concord.collocate.get(key));
            collo.add(cll);
        }

        jsonObject.put("collocates", collo);

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