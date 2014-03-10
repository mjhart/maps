package newGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Parser {
	
	private RandomAccessFile _raf;
	private long _len;
	
	public Parser(String path, boolean node) throws FileNotFoundException{
		File f = new File(path);
		_len = f.length();
		_raf = new RandomAccessFile(f,"r");
	}

}
