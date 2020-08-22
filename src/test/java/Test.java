

import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
    
    	FileInputStream fileInputStream = null;
    	String data="";
    	StringBuffer stringBuffer = new StringBuffer("");
    	try{
    	    fileInputStream=new FileInputStream("output.JSON");
    	    int i;
    	    while((i=fileInputStream.read())!=-1)
    	    {
    	        stringBuffer.append((char)i);
    	    }
    	    data = stringBuffer.toString();
    	}
    	catch(Exception e){
    	        e.printStackTrace();
    	}
    	finally{
    	    if(fileInputStream!=null){  
    	        fileInputStream.close();
    	    }
    	}
    	JSONParser parser = new JSONParser();
    	org.json.simple.JSONObject jsonObject= (org.json.simple.JSONObject) parser.parse(data);
    	//System.out.println((JSONArray)jsonObject.get("statistics"));
    	JSONArray tempArray = (JSONArray)jsonObject.get("statistics");
    	JSONObject tempOb = (JSONObject) tempArray.get(0);
    	System.out.println(tempOb.get("std_word_length"));
    }
}