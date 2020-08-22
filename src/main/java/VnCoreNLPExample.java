import marmot.util.Sys;
import vn.pipeline.VnCoreNLP;
import vn.wordsmith.concord.Concord;
import vn.wordsmith.keyword.KeyWord;
import vn.wordsmith.keyword.KeyWordReader;
import vn.wordsmith.wordlist.WordList;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static vn.wordsmith.concord.ConcordWriter.concordWriter;
import static vn.wordsmith.keyword.KeyWordReader.keyWordReader;
import static vn.wordsmith.keyword.KeyWordWriter.keyWordWriter;
import static vn.wordsmith.wordlist.WordListWriter.wordListWriter;

public class VnCoreNLPExample {
    public static void main(String[] args) throws IOException {
/*
        String fileName = "C:\\Users\\Zane\\Desktop\\data\\viwiki";
        List<String> options = new ArrayList<>();
        options.add("case");    // không phân biệt chữ hoa chữ thường
        options.add("num");     // không lấy số
        options.add("punc");    // không lấy dấu câu
        options.add("pos");     // không tính pos tag

        WordList wordList = new WordList(fileName, options);
        wordList.process();

        String outfile = "corpus.lst";
        wordListWriter(wordList, outfile);
*/

//        String corpusName = "corpus.txt";
//        String studyName = "output.txt";
//        Integer num_keywords = 5;
//
//        WordList corpus = keyWordReader(corpusName);
//        WordList study = keyWordReader(studyName);
//
//        KeyWord keyWord = new KeyWord(study, corpus, num_keywords);
//        keyWord.findKeyWord();
//
//        String outfile2 = "keyword.txt";
//        keyWordWriter(keyWord, outfile2);


        // Nếu file không thay đổi thì k cần làm mới lại việc xử lý dữ liệu đầu vào
        String fileName = "./data";
        Concord concord = new Concord(fileName);
        concord.process();

        // Chỉ việc tra từ
        String word = "bệnh_nhân";
        boolean case_sensitive = false;
        concord.concordance(word, case_sensitive);

        String outfile3 = "concordance.txt";
        concordWriter(concord, outfile3);

    }
}