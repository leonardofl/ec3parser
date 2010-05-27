package ec3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
/*
        
        informações da interação:
        
        endereço:
        http://www8.poli.usp.br/index.php?option=com_jcomments&amp;tmpl=component
        
        requisição:
        jtxf=JCommentsShowPage&jtxa[]=324&jtxa[]=com_content&jtxa[]=3
        
        headers:
        "Content-Type", "application/x-www-form-urlencoded"
		'If-Modified-Since', 'Thu, 01 Jan 1970 00:00:00 GMT'
*/

public class Main {
        public static String baseaddr = new String("http://www8.poli.usp.br");
        
        public static void main(String[] args) {
        		boolean getfromnet = true;        	
        		String verb = getfromnet?new String("Downloading "):new String("Obtaining from disk ");
        		
        		String result = null;
                List<String> links = new ArrayList<String>();    
                                
                System.out.println(verb+baseaddr);
                result = getPageContents(getfromnet,baseaddr,"/index.html");//false = dont get from Internet, but from HD
                
                System.out.println("Extracting sessions");
                populateLinks(links, result);
                                
                for(int i=0; i<links.size();i++){
            	   int size = 1;
            	   
            	   //obtain page's id used through out ec3 site links
                   String session_id= links.get(i).substring(links.get(i).indexOf("id=")+3, links.get(i).indexOf(":"));
                   
            	   System.out.println(verb+"part 1 from session "+session_id+"\t"+baseaddr+links.get(i));
            	   result = getPageContents(getfromnet,baseaddr+links.get(i),"/" +session_id +".html");
            	   
            	   size = getMaxParts(result);
            	   
                   result = Formatter.separateComments2(result);
                   
                   
                   
                   for(int j=2;j<=size;j++){
                	   System.out.println("Downloading part "+j+" from session "+session_id);
                			   
                	   String sub_result=null;
                	   //PEGANDO SESSAO
                	   sub_result = doXhtRequisition(session_id,j);
                       sub_result = Formatter.separateComments(sub_result);
                       //System.out.println(sub_result);
                                   
                   		result += sub_result;
                	   
                   } 	   
            	   
            	   
                   System.out.println("Recording");
                   doRecord( (i+1)+".txt",result);
               }
                           
				               
                
                // formata (remove tags)
                /*System.out.println("Formatting");
                result = Formatter.noHead(result);
                result = Formatter.noScript(result);
                result = Formatter.noTags(result);
                result = Formatter.breakeLines(result);
                */
        }
        static void doRecord(String fname,String result){
            File file = new File(fname);
            FileWriter writer;
            try {
                    writer = new FileWriter(file);
                    writer.write(result);
                    writer.close();
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
        	
        }
        static int getMaxParts(String code){
        	//strings que iremos avaliar: "<span onclick=\"jcomments.showPage(324, 'com_content', 3);\" class=\"page\" onmouseo"
        	//a última string desse padrão que acharmos conterá, no lugar de ", 3" , ", x", com x sendo o número de 'partes'
        	
        	String t = null;        	
        	int maxparts = 1;
        	
        	//Pattern pattern = Pattern.compile("jcomments.showPage(324, 'com_content', 3)");
        	Pattern pattern = Pattern.compile("jcomments.showPage\\(\\d+, 'com_content', \\d\\);");
            Matcher matcher = pattern.matcher(code);

            boolean found = false;
        	//atualmente, cada link será encontrado 2 vezes:antes dos comentarios e após
            //anyways, o último encontrar é o que importa;p
            while (matcher.find()) {
            	t = matcher.group();
            	maxparts = Integer.parseInt(t.substring(t.indexOf("', ")+3, t.indexOf(");")));            	
            	found=true;
            }
            if(!found){
            	//System.out.println("Não rolou");
            } else{

            }
            return maxparts;
        
        }
        static void populateLinks(List<String> links, String code ){
        	/*t will denote all contents in the anchor tag ("<a>").
        	 * then it is trimmed to just the href param value.
        	 * this value is added to links list.
        	 * the value of the link parameter id is obtained.
        	 * if the link didnt have an id attribute, then that link was not from the menu of index page's main corpus.
        	 * therefore, must be removed from links list
        	
        	*/        	
        	String t = null;
        	
        	Pattern pattern = Pattern.compile("<a href=\"/index\\.php\\?.*?>");
            Matcher matcher = pattern.matcher(code);

            boolean found = false;
            while (matcher.find()) {
            	t = matcher.group().substring(9, matcher.group().length()-2);
            	links.add(t);
            	
            	//obtains index-id and also checks if link is valid for the purpose
            	t=t.substring(t.indexOf("id="));            	
            	if(t.indexOf(":") >0){//then this was a valid menu!
            		t=t.substring(3,t.indexOf(":"));//this is the ID!!!..but wont it will be got again later;p      	
            		found = true;
            	} else { //was not a valid link. Must be excluded
            		links.remove(links.size()-1);
            	}
            }
            if(!found){
            	System.out.println("No match found.%n");
            }
        }

        static String getPageContents(boolean fromInternet,String addr,String fname){
        	String result = null;
              	
        	if(fromInternet == false){
	            try {	
	            	File file = new File("./html"+fname);
	            	String str;
	            	FileReader reader = new FileReader(file);
	            	BufferedReader br = new BufferedReader(reader);
	            	
	            	while (null != ((str = br.readLine())))
	            		result += str;
	            } catch (FileNotFoundException e) {
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}
        	} else {
        		result = doHttpRequisition(addr,fname);
        	}		
        	return result;
        }
        static String getSessionSample(){
        	String result = null;
        	
        	
	            try {	
	            	File file = new File("session1.3.txt");
	            	String str;
	            	FileReader reader = new FileReader(file);
	            	BufferedReader br = new BufferedReader(reader);
	            	
	            	while (null != ((str = br.readLine())))
	            		result += str;
	            } catch (FileNotFoundException e) {
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}
        	return result;
        }

        static String doHttpRequisition(String address,String recordname){
        	String result=null;
        	try {
	                result = Http.httpRequisition(address);
	                doRecord("./html"+recordname, result);
	   		} catch (IOException e) {	   			
	               e.printStackTrace();
	   		}
        	return result;        	
        }

        static String doXhtRequisition(String session_id,int part){
        	String result=null;
        	try {
	                result = Http.xhtRequisition(session_id, part);
	                doRecord("./html"+session_id+"_"+part, result);
	   		} catch (IOException e) {
	               // TODO Auto-generated catch block
	               e.printStackTrace();
	   		}
        	return result;        	
        }
        
        
}