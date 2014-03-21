package parsers;

/*****
 * This class is for binary search of the nodes tsv.
 * 
 * you can search by node id to get:
 * 		Ways associated with the node
 * 		Lat/Lon	associated with the node
 * 
 * it reads in buffers of 200kB and binary searches within the buffer as well
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class NodeParser {
	
	private RandomAccessFile _raf;
	private long _len;
	private int _idcol;
	private int _wayscol;
	private int _latcol;
	private int _loncol;
	private int _cols;
	private final int _buf = 200000;
	String[] _lastRec;
	
	public NodeParser(String path) throws Exception{
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
			if(header[i].equals("latitude")) {
				_latcol = i;
			}
			if(header[i].equals("longitude")) {
				_loncol = i;
			}
			if(header[i].equals("ways")) {
				_wayscol = i;
			}
		}
		if(_idcol==Integer.MAX_VALUE || _wayscol==Integer.MAX_VALUE || _latcol==Integer.MAX_VALUE || _loncol==Integer.MAX_VALUE){
			System.err.println("ERROR: Improper Columns in Nodes file");
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

	
	public String[] search(String id, boolean latlon) throws IOException{
		_raf.seek(0);
		long min = 0;
		long max = _len;
		long end = 0;
		long start = 0;
		long oldmax = 0;
		long oldmin = _len;
		while(max>min){
			long mid = (long) Math.floor(max-(max-min)/2.0);
			_raf.seek(mid);
			
			byte[] b = new byte[_buf];
			_raf.read(b);
			int index = 0;
			while(b[index] > 128){
				index++;
			}//seek to first real byte
			int tindex = index;
			while(index>=0){
				if(b[index]==10){
					break;
				}
				index-=2;
			}
			if(index<0){
				index = tindex;
				while(index < _buf){
					if(b[index]==10){
						break;
					}
					
					index+=2;
				}
			}//see to first newline
			index++;
			String[] firstline = new String[_cols];
			String[] lastline = new String[_cols];
			if(index >= _buf && _raf.getFilePointer()==_raf.length()){
				break;
			}
			byte[] lines = new byte[_buf-index];
			for(int i = index; i < _buf; i++){
				lines[i-index] = b[i];
			}
			String[] linearray = (new String(lines, "UTF-8")).split("[\\n]");
			String[] fulllinearray = new String[linearray.length-1];
			for(int i = 0; i < fulllinearray.length; i++){
				fulllinearray[i] = linearray[i];
			}
			for(String s : fulllinearray){
				if(s.split("[\\t]").length>=_cols-1){
					lastline = s.split("[\\t]");
				}
			}
			firstline = linearray[0].split("[\\t]");
			if(lastline[_idcol]==null){
				if(firstline[_idcol].compareTo(id)==0){
					if(latlon){
						String[] temp = {firstline[_latcol], firstline[_loncol]};
						return temp;
					}
					else{
						if(firstline.length==_cols){
							return firstline[_wayscol].split(",");
						}
						else{
							String[] here = {""};
							return here;
						}
					}
				}
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
						if(latlon){
							String[] temp = {split[_latcol], split[_loncol]};
							return temp;
						}
						else{
							if(split.length==_cols){
								return split[_wayscol].split(",");
							}
							else{
								String[] here = {""};
								return here;
							}
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
			if(latlon){
				String[] temp = {_lastRec[_latcol], _lastRec[_loncol]};
				return temp;
			}
			else{
				if(_lastRec.length==_cols){
					return _lastRec[_wayscol].split(",");
				}
				else{
					String[] here = {""};
					return here;
				}
			}
		}
		return null;
	}
	
	public long getLength(){
		return _len;
	}


	public int getCols() {
		return _cols;
	}

}
