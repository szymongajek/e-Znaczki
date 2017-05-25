package com.sz.znaczki;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

	
	/**
	 * Return string content of src test resources file
	 * 
	 * @param fileName
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String readFileTestResources (String fileName) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get("src","test","resources",fileName));
			  return new String(encoded, Charset.defaultCharset());
			}
}
