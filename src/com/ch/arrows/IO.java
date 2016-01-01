package com.ch.arrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.ch.arrows.program.Line;
import com.ch.arrows.program.Program;

public class IO {
	
	public static Program loadFile(String fileName) throws IOException {
		
		if (!fileName.endsWith(".arw")) throw new IOException("Invalid file format: expected .arw");
		
		Program p = new Program(fileName.replace(".arw", ""));
		
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File(fileName)));
			String line = "";
			int pos = 1;
			while((line = r.readLine()) != null) {
				p.addLine(new Line(line, pos++));
			}
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return p;
		
	}

}
