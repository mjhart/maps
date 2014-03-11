package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class NodeParser {
	
	private RandomAccessFile _raf;
	private long _len;
	private int _idcol;
	private int _wayscol;
	private int _latcol;
	private int _loncol;
	
	public NodeParser(String path) throws Exception{
		File f = new File(path);
		_len = f.length();
		_raf = new RandomAccessFile(f,"r");
		_idcol = this.findCols(1);
		_wayscol = this.findCols(2);
		_latcol = this.findCols(3);
		_loncol = this.findCols(4);
		if(_idcol==Integer.MAX_VALUE || _wayscol==Integer.MAX_VALUE || _latcol==Integer.MAX_VALUE || _loncol==Integer.MAX_VALUE){
			throw new RuntimeException("ERROR: Improper Nodes file");
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
					return line[_wayscol].split(",");
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
				if(s.equals("ways")){
					return i;
				}
				i++;
			}
		}
		else if(col==3){
			for(String s : split){
				if(s.equals("latitude")){
					return i;
				}
				i++;
			}
		}
		else if(col==4){
			for(String s : split){
				if(s.equals("longitude")){
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
