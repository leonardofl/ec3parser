package ec3;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class Main {
	
	public static void main(String[] args) {
		
		List<String> links = new ArrayList<String>();
		links.add("http://www8.poli.usp.br/index.php?option=com_content&view=article&id=324:definir-perfil-do-egresso-desejado&catid=82:ec-3&Itemid=265");
		
		
		// baixa as páginas
		System.out.println("Downloading");
		String result = null;
		try {
			for (String s: links)
				result = Http.httpRequisition(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// formata (remove tags)
		System.out.println("Formatting");
		result = Formatter.noHead(result);
		result = Formatter.noScript(result);
		result = Formatter.noTags(result);
		result = Formatter.breakeLines(result);
		
		// salva no arquivo
		System.out.println("Recording");
		File file = new File("ec3.txt");
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
