package ec3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*  
        Copyright (C) Leonardo Leite
        
        This code is free software: you can
        redistribute it and/or modify it under the terms of the GNU
        General Public License (GNU GPL) as published by the Free Software
        Foundation, either version 3 of the License, or
        any later version.  The code is distributed WITHOUT ANY WARRANTY;
        without even the implied warranty of MERCHANTABILITY or FITNESS
        FOR A PARTICULAR PURPOSE.  See the GNU GPL for more details.
*/

public class Formatter {
        
        public static String noTags(String arg) {
                
                //String result = arg.replaceAll("\\<.*?\\>", "");
        		String result = arg.replaceAll("<[^>]*>", "<>");
        	
                result = result.replaceAll("&.*?;", "");
                return result;
                
        }

        public static String noScript(String arg) {
                
                String result = arg.replaceAll("\\<script*>.*?\\</script>", "");
                return result;
                
        }

        public static String noHead(String arg) {
                
                String result = arg.replaceAll("\\<head>.*?\\</head>", "");
                return result;
                
        }

        public static String breakeLines(String arg) {
                
                String result = arg.replaceAll("#", "\n\r\n\r\n\r#");
                return result;
                
        }

        static String separateComments(String code){
        	String t = null;
        	String result="";
        	
        	//Pattern pattern = Pattern.compile("<div class=\\\\\\\"comment-box.*? <\\/div>\\\\n");
        	Pattern pattern = Pattern.compile("<div class=\\\\\\\\\\\\\"comment-box.*?<\\\\\\/div>");
            Matcher matcher = pattern.matcher(code);


            boolean found = false;
            while (matcher.find()) {
            	t = matcher.group();
            	result+=t;
            	found=true;
            }
            if(!found){
            	System.out.println("No match found.%n");
            } else{

            	result = result.replaceAll("<[^>]*>", "\n");
            	result = result.replaceAll("\\\\\\\\n", "");
            	result = result.replaceAll("\n\n", "\n");
            }
            //System.out.println(result);
        
            return result;
        }
        static String separateComments2(String code){
        	String t = null;
        	String result="";
        	
        	//Pattern pattern = Pattern.compile("<div class=\\\\\\\"comment-box.*? <\\/div>\\\\n");
        	Pattern pattern = Pattern.compile("<div class=\"comment-box.*?</div>");
            Matcher matcher = pattern.matcher(code);
        	
            boolean found = false;
            while (matcher.find()) {
            	t = matcher.group();
            	result+=t;
            	found=true;
            }
            if(!found){
            	System.out.println("No match found.%n");
            } else{

            	result = result.replaceAll("<[^>]*>", "\n");
            	result = result.replaceAll("\n\n", "\n");
            }
            //System.out.println(result);
        
            return result;
        }
}