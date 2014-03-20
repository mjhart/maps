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
	private int _cols;
	private final int _buf = 200000;
	String[] _lastRec;
	
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
		/*while(_raf.getFilePointer() < _raf.length()){
			String line = _raf.readLine();
			String[] toKeep = line.split("\t");
			if(toKeep.length>=3){
				_lastRec = toKeep;
			}
		}*/
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
			//System.out.println("new max "+max);
			//System.out.println("new min "+min);
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
					//System.out.println(b[index]);
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
				//System.out.println("null 1");
				//return null;
				break;
			}
			//System.out.println(index);
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
			/*System.out.println("W first line:  "+firstline[_idcol]);
			System.out.println("W looking for: "+id);
			System.out.println("W last line:   "+lastline[_idcol]);*/
			if(lastline==null || lastline[_idcol]==null){
				for(String s : lastline){
					//System.out.println(s);
				}
				//System.out.println("null 2");
				return null;
			}
			if(firstline[_idcol].compareTo(id)<=0 && lastline[_idcol].compareTo(id)>=0){
				/*for(String line : linearray){
					String[] split = line.split("\t");
					if(split[_idcol].compareTo(id)==0){
						if(split.length==_cols){
							String[] temp = {split[_namecol], split[_srccol], split[_dstcol]};
							return temp;
						}
						else{
							String[] here = {""};
							return here;
						}
					}
				}*/
				//System.out.println("Here");
				int mindex = 0;
				int maxdex = linearray.length-1;
				while(maxdex>=mindex){
					//System.out.println("Mindex: "+ mindex);
					//System.out.println("Maxdex: "+ maxdex);
					int middex = maxdex-(maxdex-mindex)/2;
					String[] split = linearray[middex].split("\t");
					//System.out.println(split[_idcol].compareTo(id));
					if(split[_idcol].compareTo(id)<0){
						mindex = middex;
					}
					else if(split[_idcol].compareTo(id)>0){
						maxdex = middex-1;
					}
					else{
						//System.out.println("DAFUQ___________________________________");
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
				//System.out.println("here");
				max = mid;
			}
			else if(firstline[_idcol].compareTo(id)<=0 && lastline[_idcol].compareTo(id)<0){//batch isn't deep enough in file 
				//System.out.println(new String(b,"UTF-8"));
				min = mid;
			}
			/*if(max==min+1){
				break;
			}
			_raf.seek(mid);
			int ch = _raf.read();
			if(ch > 128){
				ch = _raf.read();
			}// seek to first relevant byte
			while(_raf.getFilePointer()>min+1){
				if(ch == 10){
					break;
				}
				_raf.seek(_raf.getFilePointer()-2);
				ch = _raf.read();
			}
			if(_raf.getFilePointer()==min+1){
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
			}*/
		}
		//System.out.println(_lastRec[_idcol]);
		if(_lastRec[_idcol].compareTo(id)==0){
			String[] temp = {_lastRec[_namecol], _lastRec[_srccol], _lastRec[_dstcol]};
			return temp;
		}
		//System.out.println("null 3");
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
