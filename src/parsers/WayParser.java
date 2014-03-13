package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WayParser {
	
	private RandomAccessFile _raf;
	private long _len;
	private int _idcol;
	private int _namecol;
	private int _srccol;
	private int _dstcol;
	
	public WayParser(String path) throws Exception{
		File f = new File(path);
		_len = f.length();
		_raf = new RandomAccessFile(f,"r");
		/*_idcol = this.findCols(1);
		_namecol = this.findCols(2);
		_srccol = this.findCols(3);
		_dstcol = this.findCols(4);*/
		_raf.seek(0);
		String[] header = _raf.readLine().split("\t");
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
	}
	
	public String[] search(String id) throws IOException{
		_raf.seek(0);
		long min = 0;
		long max = _len;
		while(max>min){
			long mid = (long) Math.floor(max-(max-min)/2.0);
			if(max==min+1){
				break;
			}
			_raf.seek(mid);
			int ch = _raf.read();
			if(ch > 128){
				ch = _raf.read();
			}// seek to first relevant byte
			while(_raf.getFilePointer()>min){
				if(ch == 10){
					break;
				}
				_raf.seek(_raf.getFilePointer()-2);
				ch = _raf.read();
			}
			if(_raf.getFilePointer()==min){
				while(true){
					if(ch == 10){
						break;
					}
					ch = _raf.read();
				}
			}// seek to first newline
			String l = _raf.readLine();
			if(l!=null){
				String[] line = l.split("\t");
				if(line[_idcol].compareTo(id) < 0){
					min = mid;
				}
				else if(line[_idcol].compareTo(id) > 0){
					max = mid - 1;
				}
				else{
					String[] temp = {line[_namecol], line[_srccol], line[_dstcol]};
					return temp;
				}
			}
			else{
				break;
			}
		}
		return null;
	}
	
	public int findCols(int col) throws IOException{
		_raf.seek(0);
		String header = _raf.readLine();
		String split[] = header.split("\t");
		int i = 0;
		if(col==1){
			for(String s : split){
				if(s.equals("id")){
					return i;
				}
				i++;
			}
		}
		else if(col==2){
			for(String s : split){
				if(s.equals("name")){
					return i;
				}
				i++;
			}
		}
		else if(col==3){
			for(String s : split){
				if(s.equals("start")){
					return i;
				}
				i++;
			}
		}
		else if(col==4){
			for(String s : split){
				if(s.equals("end")){
					return i;
				}
			}
			i++;
		}
		return Integer.MAX_VALUE;
	}
	
	public long getLength(){
		return _len;
	}

}
