package com.laptrinhjavaweb.controller.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.laptrinhjavaweb.model.*;

import vn.wordsmith.concord.Concord;
import vn.wordsmith.keyword.KeyWord;
import vn.wordsmith.wordlist.WordList;
import static vn.wordsmith.concord.ConcordWriter.concordWriter;

import java.io.IOException;

import static vn.wordsmith.keyword.KeyWordReader.keyWordReader;
import static vn.wordsmith.keyword.KeyWordWriter.keyWordWriter;
import static vn.wordsmith.wordlist.WordListWriter.wordListWriter;
import com.laptrinhjavaweb.controller.web.ReadJSON;

@Controller(value = "homeControllerOfWeb")
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/trang-chu", method = RequestMethod.GET)
	public ModelAndView homePage() {
		ModelAndView mav = new ModelAndView("web/home");
		return mav;
	}

	@RequestMapping(value = "/concord", method = RequestMethod.GET)
	public ModelAndView concord() {
		ModelAndView mav = new ModelAndView("web/concord");
		return mav;
	}

	@RequestMapping(value = "/wordlist", method = RequestMethod.GET)
	public String wordlist(Model model) {
		model.addAttribute("myFile", new MyFile());

		return "web/wordlist";
	}

	@RequestMapping(value = "/uploadMultiple", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("myFile", new MyFile());
		return "web/uploadMultiple";
	}

	@RequestMapping(value = "/uploadMultipleFiles", method = RequestMethod.POST)
	public String uploadFile(MyFile myFile, Model model) throws IOException, ParseException {

		try {
			MultipartFile multipartFile = myFile.getMultipartFile();
			String fileName = multipartFile.getOriginalFilename();
			// model.addAttribute("messages", fileName);
			String modelPath = System.getProperty("user.dir") + "/data";
			File file = new File(modelPath, fileName);
			multipartFile.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
			// model.addAttribute("message", "upload failed");
		}

		return "web/wordlist";
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String uploadFileKey(MyFile myFile, Model model) throws IOException, ParseException {

		try {
			MultipartFile multipartFile = myFile.getMultipartFile();
			String fileName = multipartFile.getOriginalFilename();
			// model.addAttribute("messages", fileName);
			String modelPath = System.getProperty("user.dir");
			File file = new File(modelPath, fileName);
			multipartFile.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
			// model.addAttribute("message", "upload failed");
		}

		return "web/keyword";
	}
	

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	public String process(@RequestParam(value = "case", required = false) String cases,
			@RequestParam(value = "punc", required = false) String punc,
			@RequestParam(value = "num", required = false) String num,
			@RequestParam(value = "pos", required = false) String pos, MyFile myFile, Model model)
			throws IOException, ParseException {
		
		//System.out.println(myFile);
		String fileNames = "./data";
		// String[] options = {"case", "num", "punc", "pos"};
		// String[] options = {checkboxValue, num, punc, pos};
		List<String> options = new ArrayList<>();
		if (cases!=null) {
			options.add("case"); // không phân biệt chữ hoa chữ thường
		} else {

		}
		if (punc!=null) {
			options.add("punc"); // không lấy số
		} else {

		}
		if (num!=null) {
			options.add("num"); // không lấy dấu câu
		} else {
			
		}
		if (pos!=null) {
			options.add("pos"); // không tính pos tag
		} else {
			
		}
		WordList wordList = new WordList(fileNames, options);
		//System.out.println(options.toString());
		try {
			wordList.process();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String outfile = "output.txt";
		wordListWriter(wordList, outfile);
		ReadJSON jsons = new ReadJSON();
		org.json.simple.JSONObject loadParse = jsons.parseData("output.txt");
		org.json.simple.JSONObject option = (org.json.simple.JSONObject) loadParse.get("options");
		List<Boolean> arrOption = new ArrayList<>(); 
		Boolean case_ =  (Boolean) option.get("case");
		arrOption.add(case_);
		Boolean punc_ = (Boolean) option.get("punc");
		arrOption.add(punc_);
		Boolean num_ = (Boolean) option.get("num");
		arrOption.add(num_);
		Boolean pos_ = (Boolean) option.get("pos");
		arrOption.add(pos_);
		
		model.addAttribute("option", arrOption);
		org.json.simple.JSONArray tempArray = (org.json.simple.JSONArray) loadParse.get("word_list");
		model.addAttribute("word_list", tempArray);
		org.json.simple.JSONArray tempStatistics = (org.json.simple.JSONArray) loadParse.get("statistics");
		model.addAttribute("statistics", tempStatistics);
		

		return "web/success";
	}
	
	@RequestMapping(value = "/uploadCon", method = RequestMethod.POST)
	public String uploadFileCon(MyFile myFile, Model model) throws IOException, ParseException {

		try {
			MultipartFile multipartFile = myFile.getMultipartFile();
			String fileName = multipartFile.getOriginalFilename();
			// model.addAttribute("messages", fileName);
			String modelPath = System.getProperty("user.dir") + "/data";
			File file = new File(modelPath, fileName);
			multipartFile.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
			// model.addAttribute("message", "upload failed");
		}

		return "web/concord";
	}
	
	@RequestMapping(value = "/processKey", method = RequestMethod.POST)
	public String processKey(@RequestParam(value = "num", required = false) String num,MyFile myFile, Model model)
			throws IOException, ParseException {
		

		String corpusName = "corpus.txt";
        String studyName = "output.txt";
        Integer num_keywords = 0;
        try {
        	num_keywords = Integer.parseInt(num);
        } catch(Exception e) {
        	num_keywords = 5;
        }
        WordList corpus = keyWordReader(corpusName);
        WordList study = keyWordReader(studyName);

        KeyWord keyWord = new KeyWord(study, corpus, num_keywords);
        keyWord.findKeyWord();

        String outfile2 = "keyword.txt";
        keyWordWriter(keyWord, outfile2);
        
        ReadJSON jsons = new ReadJSON();
		org.json.simple.JSONObject loadParse = jsons.parseData("keyword.txt");
		org.json.simple.JSONArray keywords = (org.json.simple.JSONArray) loadParse.get("keywords");
		model.addAttribute("keywords", keywords);

		return "web/successKey";
	}
	
	@RequestMapping(value = "/processCon", method = RequestMethod.POST)
	public String processCon(MyFile myFile, Model model)
			throws IOException, ParseException {
		 
		//System.out.println(myFile);
        // Nếu file không thay đổi thì k cần làm mới lại việc xử lý dữ liệu đầu vào
        String fileName = "./data";
        Concord concord = new Concord(fileName);
        System.out.println("Chay duoc");
        concord.process();
        System.out.println("Chay duoc 1");

        // Chỉ việc tra từ
        String word = "bệnh_nhân";
        boolean case_sensitive = false;
        concord.concordance(word, case_sensitive);
        System.out.println("Chay duoc 2");

        String outfile3 = "concordance.txt";
        concordWriter(concord, outfile3);
        System.out.println("Chay duoc 3");
		return "web/successCon";
	}

	@RequestMapping(value = "/keyword", method = RequestMethod.GET)
	public ModelAndView keyword() {
		ModelAndView mav = new ModelAndView("web/keyword");
		return mav;
	}

}
