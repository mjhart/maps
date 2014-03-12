package parsers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;

public class FileSearcher {

	private RandomAccessFile file;
	private Comparator<String> comp;
	
	public FileSearcher(RandomAccessFile file, Comparator<String> comp) {
		this.file = file;
		this.comp = comp;
	}
	
	public long search(String query) {
		try {

			// set top
			long t = file.length();

			// set bottom
			file.seek(0);
			file.readLine();
			long b = file.getFilePointer();

			while(t > b) {

				long mid = (t-b)/2 + b;

				// move to midpoint
				file.seek(mid);

				// move to beginning of line
				while(file.readByte() != '\n') {
					file.seek(file.getFilePointer()-2);
					mid--;
				}
				mid++;

				// read line
				String line = file.readLine();
				
				int compVal = comp.compare(line, query);
				
				// found film
				if(compVal == 0) {
					while(file.readByte() != '\n') {
						file.seek(file.getFilePointer()-2);
					}
					return file.getFilePointer();
				}
				
				// upper half
				if(compVal >  0) {
					b = file.getFilePointer();
				}
				
				// lower half
				else {
					t = mid;
				}
			}


		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return -1;
	}

}
