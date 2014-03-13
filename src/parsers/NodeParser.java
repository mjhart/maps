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
		/*_idcol = this.findCols(1);
		_wayscol = this.findCols(2);
		_latcol = this.findCols(3);
		_loncol = this.findCols(4);*/
		_raf.seek(0);
		String[] header = _raf.readLine().split("\t");
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
		//System.out.println("lat col "+_latcol);
		//System.out.println("lon col "+_loncol);
	}
	
	public String[] search(String id, boolean latlon) throws IOException{
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
				System.out.println("currently on line "+line[_idcol]);
				System.out.println(line[_idcol].compareTo(id));
				if(line[_idcol].compareTo(id) < 0){
					min = mid;
				}
				else if(line[_idcol].compareTo(id) > 0){
					max = mid - 1;
				}
				else{// if(line[_idcol].compareTo(id) == 0){
					if(latlon){
						System.out.println("Should be here....");
						String[] temp = new String[2];//{line[_latcol], line[_loncol]};
						temp[0] = line[_latcol];
						temp[1] = line[_loncol];
						System.out.println("and here...");
						for(String s : temp){
							System.out.println(s);
						}
						String[] s = temp;
						return s;// WHY NO RETURN???????????
					}
					else{
						return line[_wayscol].split(",");
					}
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
		for(int i = 0; i < split.length; i++){
			
		}
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
