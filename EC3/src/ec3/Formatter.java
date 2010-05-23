package ec3;

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
		
		String result = arg.replaceAll("\\<.*?\\>", "");
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
		
		String result = arg.replaceAll("#", "\n\n#");
		return result;
		
	}
}
