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
	private int _cols;
	
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
		//System.out.println("lat col "+_latcol);
		//System.out.println("lon col "+_loncol);
	}

	
	public String[] search(String id, boolean latlon) throws IOException{
		_raf.seek(0);
		long min = 0;
		long max = _len;
		long end = 0;
		long start = 0;
		long oldmax = 0;
		long oldmin = _len;
		while(max>min){//oldmax!=max && oldmin!=min){
			//System.out.println("new max "+max);
			//System.out.println("new min "+min);
			long mid = (long) Math.floor(max-(max-min)/2.0);
			//System.out.println("Mid:   "+mid);
			_raf.seek(mid);
			
			/*int ch = _raf.read();
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
				while(true && _raf.getFilePointer() < _raf.length()){
					if(ch == 10){
						break;
					}
					ch = _raf.read();
					//System.out.println(_raf.getFilePointer());
				}
			}// seek to first newline
			*/
			byte[] b = new byte[10000];
			_raf.read(b);
			int index = 0;
			while(b[index] > 128){
				//System.out.println("Should only happen once");
				index++;
			}//seek to first real byte
			while(index>=0){
				if(b[index]==10){
					break;
				}
				index-=2;
			}
			if(index<0){
				index = 0;
				while(index < 10000){
					if(b[index]==10){
						break;
					}
					index+=2;
				}
			}//see to first newline
			index++;
			int endofbatchline = 0;
			String[] firstline = new String[_cols];
			String[] lastline = new String[_cols];
			byte[] lines = new byte[10000-index];
			for(int i = index; i < 10000; i++){
				lines[i-index] = b[i];
			}
			String[] linearray = (new String(lines, "UTF-8")).split("\n");
			for(String s : linearray){
				//System.out.println(s.split("\t")[_idcol]+", "+s.split("\t").length);
				if(s.split("\t").length==_cols){
					lastline = s.split("\t");
				}
			}
			firstline = linearray[0].split("\t");
			//System.out.println("first line:  "+firstline[_idcol]);
			//System.out.println("looking for: "+id);
			//System.out.println("last line:   "+lastline[_idcol]);
			if(lastline==null || lastline[_idcol]==null){
				return null;
			}
			if(firstline[_idcol].compareTo(id)<=0 && lastline[_idcol].compareTo(id)>=0){
				for(String line : linearray){
					String[] split = line.split("\t");
					if(split[_idcol].compareTo(id)==0){
						if(latlon){
							String[] temp = {split[_latcol], split[_loncol]};
							return temp;
						}
						else{
							if(split.length==_cols){
								return split[_wayscol].split(",");
							}
							else{
								return null;
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
			
			
			/*start = _raf.getFilePointer();
			String[] batch = new String[100];
			int batchcount = 0;
			for(batchcount = 0; batchcount < 100; batchcount++){
				String l = _raf.readLine();
				if(l!=null){
					batch[batchcount] = l;
				}
				else{
					break;
				}
			}
			end = _raf.getFilePointer();
			if(batchcount > 0){
				String startid = batch[0].split("\t")[_idcol];
				String endid = batch[batchcount - 1].split("\t")[_idcol];
				System.out.println("Start: "+start+", "+startid);
				System.out.println(startid.compareTo(id));
				System.out.println("End:   "+end+", "+endid);
				System.out.println(endid.compareTo(id));
				System.out.println("       "+id);
				
				if(startid.compareTo(id)<=0 && endid.compareTo(id)>=0){
					//System.out.println("ID in Batch");
					for(int j = 0; j < batchcount; j++){
						String[] line = batch[j].split("\t");
						if(line[_idcol].compareTo(id)==0){
							//System.out.println("ID found, ways: "+line[_wayscol]);
							if(latlon){
								//System.out.println("Should be here....");
								String[] temp = {line[_latcol], line[_loncol]};
								temp[0] = line[_latcol];
								temp[1] = line[_loncol];
								//System.out.println("and here...");
								for(String s : temp){
									System.out.println(s);
								}
								return temp;
							}
							else{
								return line[_wayscol].split(",");
							}
						}
					}
				}
				else if(startid.compareTo(id)>0 && endid.compareTo(id)>=0){
					oldmax = max;
					max = start-1;
				}
				else if(startid.compareTo(id)<=0 && endid.compareTo(id)<0){
					//System.out.println("here");
					oldmin = min;
					min = end;
				}
				
			}*/
			
			
			/*String l = _raf.readLine();
			if(l!=null){
				String[] line = l.split("\t");
				//System.out.println("currently on line "+line[_idcol]+" and latlon: "+latlon);
				//System.out.println("looking for       "+id);
				//System.out.println(line[_idcol].compareTo(id));
				if(line[_idcol].compareTo(id) < 0){
					min = mid;
				}
				else if(line[_idcol].compareTo(id) > 0){
					max = mid - 1;
				}
				else{// if(line[_idcol].compareTo(id) == 0){
					if(latlon){
						//System.out.println("Should be here....");
						String[] temp = {line[_latcol], line[_loncol]};
						temp[0] = line[_latcol];
						temp[1] = line[_loncol];
						//System.out.println("and here...");
						for(String s : temp){
							System.out.println(s);
						}
						return temp;// WHY NO RETURN???????????
					}
					else{
						return line[_wayscol].split(",");
					}
				}
			}
			else{
				break;
			}*/
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


	public int getCols() {
		// TODO Auto-generated method stub
		return _cols;
	}

}
