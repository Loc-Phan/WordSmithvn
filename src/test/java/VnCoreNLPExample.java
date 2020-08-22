import vn.wordsmith.wordlist.WordList;

import java.io.IOException;

import static vn.wordsmith.wordlist.WordListWriter.wordListWriter;

public class VnCoreNLPExample {
    public static void main(String[] args) throws IOException {
    
        String fileName = "./data";
        String[] options = {"case", "num", "punc", "pos"};

        WordList wordList = new WordList(fileName, options);
        wordList.process();

        String outfile = "output.txt";
        wordListWriter(wordList, outfile);
    }
}