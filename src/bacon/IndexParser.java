package bacon;

/****
 * This is the fileparser I wrote to parse the index file.
 * It performs a binary search on the UTF-8 encoded TSV file and returns
 * the UID associated with the given actor name.
 */

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class IndexParser implements AutoCloseable{
	
	private RandomAccessFile _raf;
	
	public IndexParser(String path) throws FileNotFoundException{
		File f = new File(path);
		_raf = new RandomAccessFile(f,"r");
	}
	
	public String search(String actor, long min, long max) throws IOException{
		//int z = 0;
		while(max >= min){
			long mid = min+(max-min)/2;
			_raf.seek(mid);
			int ch = _raf.read();
			if(ch > 128){
				ch = _raf.read();
			}// seek to first relevant byte
			while(true){
				if(ch == 10){
					break;
				}
				ch = _raf.read();
			}// seek to first newline
			List<byte[]> bytes = new ArrayList<byte[]>();
			byte b = 0;
			while(b!=9){
				try{
					b = _raf.readByte();
				}
				catch(EOFException e){
					break;
				}
				byte temp[] = {b};
				bytes.add(temp);
			}// read bytes of name from file
			StringBuilder sb = new StringBuilder();
			int c = 0;
			for(byte[] i : bytes){
				String s = new String(i,"UTF-8");
				if(c==bytes.size()-1){
					break;
				}
				else{
					sb.append(s);
				}
				c++;
			}// convert UTF-8 bytes to String\
			if(sb.toString().compareTo(actor)<0){
					min = mid + 1;
			}
			else if(sb.toString().compareTo(actor)>0){
					max = mid -1;
			}
			else{
				b = 33;
				bytes = new ArrayList<byte[]>();
				while(32<b && b<127){
					b = _raf.readByte();
					byte temp[] = {b};
					bytes.add(temp);
				}// read bytes of uid from file
				sb = new StringBuilder();
				c = 0;
				for(byte[] i : bytes){
					String s = new String(i,"UTF-8");
					if(c==bytes.size()-1){
						break;
					}
					else{
						sb.append(s);
					}
					c++;
				}// convert UTF-8 bytes to String
				return sb.toString();
			}//found actor
		}
		return null;
	}
	
	public long getLength() throws IOException{
		return _raf.length();
	}

	@Override
	public void close() throws Exception {
		_raf.close();
	}

}
