package bacon;
/****
 * This is the fileparser I wrote to parse the actor/film files.
 * It first finds the necesssary columns (if they exist) and has
 * the ability to perform a binary search on a UTF-8 encoded file
 * to return:
 * 		1) a list of films/actors associated with a given unique actor/film ID
 * 		OR
 * 		2) a name of a film/actor associated with a given unique actor/film ID
 */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;

public class DataParser implements AutoCloseable{
	
	private RandomAccessFile _raf;
	private boolean _act;
	private int _namecol;
	private int _idcol;
	private int _listcol;
	private long _len;
	private int _cols;
	
	/****
	 * Constructs parser (only if file at path is formatted properly)
	 * @param path
	 * @param actor
	 * @throws Exception
	 */
	public DataParser(String path, boolean actor) throws Exception{
		File f = new File(path);
		_len = f.length();
		//System.out.println(_len);
		_raf = new RandomAccessFile(f,"r");
		_act = actor;
		_namecol = this.findCols(1);
		_idcol = this.findCols(2);
		_listcol = this.findCols(3);
		if(_namecol==Integer.MAX_VALUE || _idcol==Integer.MAX_VALUE || _listcol==Integer.MAX_VALUE){
			throw new RuntimeException();
		}
		_cols = this.countCols();
	}
	
	private int countCols() throws IOException {
		_raf.seek(0);
		String header = _raf.readLine();
		String split[] = header.split("\t");
		return split.length;
	}

	
	/***
	 * this method returns the name associated with a given UID via Binary Search
	 * @param uid
	 * @param min
	 * @param max
	 * @return
	 * @throws IOException
	 */
	public String searchUID(String uid, long min, long max) throws IOException{
		//TRY HASHSET OF UIDs, SKIP TO NEXT LINE IF UID ALREADY IN SET?
		_raf.seek(0);
		//System.out.println(max);
		
		//HashSet<String> mid_uids = new HashSet<String>();
		
		while(max > min){
			long mid = (long) Math.floor(max-(max-min)/2.0);//(max+min)/2;//max-(max-min)/2;
			/*System.out.println("min: "+min);
			System.out.println("mid: "+mid);
			System.out.println("max: "+max);*/
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
			}// seek to first newline
			//System.out.println(min + " "+ max);
			/*if(_raf.getFilePointer()==max){
				ch = Integer.MAX_VALUE;
				while(ch!=10){
					_raf.seek(_raf.getFilePointer()-2);
					ch = _raf.read();
				}
			}*/
			if(_raf.getFilePointer()==min){
				while(true){
					if(ch == 10){
						break;
					}
					ch = _raf.read();
				}
			}
			//System.out.println(min);
			//System.out.println(_raf.getFilePointer());
			//System.out.println(max);
			if(_raf.getFilePointer()>=min){
				//System.out.println("here");
				String l = _raf.readLine();
				//System.out.println(l);
				
				if(l!=null){
					String[] line = l.split("\t");
					//System.out.println("cur line id: "+line[_idcol]);
					//System.out.println("uid: "+uid);
					//System.out.println("compare: "+line[_idcol].compareTo(uid));*/
					line = l.split("\t");
					if(line[_idcol].compareTo(uid) < 0){
						min = mid;// + 1;
						//System.out.println("min is now: "+min);
						//System.out.println(min + " "+ max);
					}
					else if(line[_idcol].compareTo(uid) > 0){
						max = mid - 1;
						//System.out.println("max is now: "+max);
						//System.out.println("here");
					}
					else{
						return line[_namecol];
					}
				}
				else{
					//System.out.println("here");
					break;
				}
			}
			else{
				//System.out.println("here 1");
				break;
			}
		}
		return null;
	}
	
	/**
	 * this method returns a list of films/actors associated with a given UID
	 * via Binary Search
	 * @param uid
	 * @param min
	 * @param max
	 * @return
	 * @throws IOException
	 */
	public String[] search(String uid, long min, long max) throws IOException{
		_raf.seek(0);
		
		//HashSet<String> mid_uids = new HashSet<String>();
		
		while(max > min){
			//System.out.println("min: "+min);
			//System.out.println("max: "+max);
			long mid = (long) Math.floor(max-(max-min)/2.0);//(max+min)/2;//= max-(max-min)/2;
			//System.out.println("mid: "+mid);
			if(max==min+1){
				break;
			}
			_raf.seek(mid);
			int ch = _raf.read();
			//byte[] t = {(byte)ch};
			//System.out.println("ch: "+new String(t, "UTF-8")+", intch: "+ch);
			if(ch > 128){
				ch = _raf.read();
				//byte[] t1 = {(byte)ch};
				//System.out.println("ch: "+new String(t1, "UTF-8"));
			}// seek to first relevant byte
			while(_raf.getFilePointer()>min){
				if(ch == 10){
					break;
				}
				_raf.seek(_raf.getFilePointer()-2);
				ch = _raf.read();
			}// seek to first newline
			//System.out.println(min + " "+ max);
			/*if(_raf.getFilePointer()==max){
				ch = Integer.MAX_VALUE;
				while(ch!=10){
					_raf.seek(_raf.getFilePointer()-2);
					ch = _raf.read();
				}
			}*/
			if(_raf.getFilePointer()==min){
				while(true){
					if(ch == 10){
						break;
					}
					ch = _raf.read();
				}
			}
			
			if(_raf.getFilePointer()>=min){
				String l = _raf.readLine();
				if(l!=null){
					//System.out.println(_idcol);
					String[] line = l.split("[\t]");
					line = l.split("\t");
					//System.out.println(line[_idcol]);
					if(line[_idcol].compareTo(uid) < 0){
						min = mid;// + 1;
					}
					else if(line[_idcol].compareTo(uid) > 0){
						max = mid - 1;
					}
					else{
						//System.out.println(l);
						if(line.length<=_listcol){
							break;
						}
						return line[_listcol].split(",");
					}
				}
				else{
					break;
				}
			}
			else{
				break;
			}
		}
		return null;
	}
	
	/**
	 * This method parses the first line of the input file to determine the
	 * appropriate indices of the relvant columns
	 * @param col
	 * @return
	 * @throws IOException
	 */
	public int findCols(int col) throws IOException{
		_raf.seek(0);
		String header = _raf.readLine();
		String split[] = header.split("\t");
		int i = 0;
		if(col==1){
			for(String s : split){
				if(s.equals("name")){
					return i;
				}
				i++;
			}
		}
		else if(col==2){
			for(String s : split){
				if(s.equals("id")){
					return i;
				}
				i++;
			}
		}
		else{
			for(String s : split){
				if(_act){
					if(s.equals("film")){
						return i;
					}
				}
				else{
					if(s.equals("starring")){
						return i;
					}
				}
				i++;
			}
		}
		return Integer.MAX_VALUE;
	}
	
	public long getLength() throws IOException{
		return _len;//return _raf.length();
	}
	
	@Override
	public void close() throws Exception {
		_raf.close();
	}

}
