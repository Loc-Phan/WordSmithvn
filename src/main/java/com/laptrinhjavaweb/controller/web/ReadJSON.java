package com.laptrinhjavaweb.controller.web;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Chen-Yang
 */
public class ReadJSON {

    /**
     * @param args the command line arguments
     */
   public JSONObject parseData(String fileName) throws ParseException, IOException {
       FileInputStream fileInputStream = null;
        String data = "";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try{
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis,StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            data = br.readLine();
//        StringBuffer stringBuffer = new StringBuffer("");
//        try {
//            fileInputStream = new FileInputStream(fileName);
//            int i;
//            while ((i = fileInputStream.read()) != -1) {
//                stringBuffer.append((char) i);
//            }
//            data = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {                
                fileInputStream.close();
            }
        }
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser.parse(data);
        return jsonObject;
   }   

}


