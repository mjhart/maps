package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/*****
 * This class is for binary search of the wasy tsv.
 * 
 * you can search by way id to get the start and end nodes as well as the name
 * 
 * it reads in buffers of 200kB and binary searches within the buffer as well
 * 
 */

public class WayParser {
	
	private RandomAccessFile _raf;
	private long _len;
	private int _idcol;
	private int _namecol;
	private int _srccol;
	private int _dstcol;
	private int _cols;
	private final int _buf = 200000;
	String[] _lastRec;
	
	public WayParser(String path) throws Exception{
		File f = new File(path);
		_len = f.length();
		_raf = new RandomAccessFile(f,"r");
		_raf.seek(0);
		String[] header = _raf.readLine().split("\t");
		_cols = header.length;
		for(int i=0; i<header.length; i++) {
			if(header[i].equals("id")) {
				_idcol = i;
			}
			if(header[i].equals("start")) {
				_srccol = i;
			}
			if(header[i].equals("end")) {
				_dstcol = i;
			}
			if(header[i].equals("name")){
				_namecol = i;
			}
		}
		if(_idcol==Integer.MAX_VALUE || _namecol==Integer.MAX_VALUE || _srccol==Integer.MAX_VALUE || _dstcol==Integer.MAX_VALUE){
			System.err.println("ERROR: Improper Columns in Ways file");
			System.exit(1);
		}
		_raf.seek(_raf.length()-2);
		byte b = 33;
		while(b!=10){
			b = _raf.readByte();
			_raf.seek(_raf.getFilePointer()-2);
		}
		_raf.seek(_raf.getFilePointer()-1);
		String test = _raf.readLine();
		test = _raf.readLine();
		_lastRec = test.split("\t");
	}
	
	public String[] search(String id) throws IOException{
		_raf.seek(0);
		long min = 0;
		long max = _len;
		while(max>min){
			long mid = (long) Math.floor(max-(max-min)/2.0);
			_raf.seek(mid);
			byte[] b = new byte[_buf];
			_raf.read(b);
			int index = 0;
			while(b[index] > 128){
				index++;
			}//seek to first relevant byte
			int tindex = index;//remember first relevant byte in case we need to go back
			while(index >= 0){
				if(b[index]==10){
					break;
				}
				index -=2;
			}
			if(index < 0){
				index = tindex;
				while(index < _buf){
					if(b[index]==10){
						break;
					}
					index += 2;
				}
			}
			index++;
			String[] firstline = new String[_cols];
			String[] lastline = new String[_cols];
			if(index>=_buf && _raf.getFilePointer()==_raf.length()){
				break;
			}
			byte[] lines = new byte[_buf-index];
			for(int i = index; i < _buf; i++){
				lines[i-index] = b[i];
			}
			String[] linearray = (new String(lines, "UTF-8")).split("\n");
			String[] fulllinearray = new String[linearray.length-1];
			for(int i = 0; i < fulllinearray.length; i++){
				fulllinearray[i] = linearray[i];
			}
			boolean ll = false;
			for(String s : fulllinearray){
				if(s.split("\t").length>=_cols){
					lastline = s.split("\t");
					ll = true;
				}
				if(ll==false){
					System.out.println(s);
				}
			}
			firstline = linearray[0].split("\t");
			if(lastline==null || lastline[_idcol]==null){
				for(String s : lastline){
				}
				return null;
			}
			if(firstline[_idcol].compareTo(id)<=0 && lastline[_idcol].compareTo(id)>=0){
				int mindex = 0;
				int maxdex = linearray.length-1;
				while(maxdex>=mindex){
					int middex = maxdex-(maxdex-mindex)/2;
					String[] split = linearray[middex].split("\t");
					if(split[_idcol].compareTo(id)<0){
						mindex = middex;
					}
					else if(split[_idcol].compareTo(id)>0){
						maxdex = middex-1;
					}
					else{
						if(split.length>=_cols){
							String[] temp = {split[_namecol], split[_srccol], split[_dstcol]};
							return temp;
						}
						else{
							String[] here = {""};
							return here;
						}
					}
				}
			}
			else if(firstline[_idcol].compareTo(id)>0 && lastline[_idcol].compareTo(id)>=0){//batch is too far into file
				max = mid;
			}
			else if(firstline[_idcol].compareTo(id)<=0 && lastline[_idcol].compareTo(id)<0){//batch isn't deep enough in file
				min = mid;
			}
		}
		if(_lastRec[_idcol].compareTo(id)==0){
			String[] temp = {_lastRec[_namecol], _lastRec[_srccol], _lastRec[_dstcol]};
			return temp;
		}
		return null;
	}
	
	public long getLength(){
		return _len;
	}

}
