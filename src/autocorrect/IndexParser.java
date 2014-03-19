package autocorrect;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mjhart
 * 
 * This class can be used to import text from a file and filter
 * it into lowercase only words. It can also be used without a 
 * file to perform the filter tasks on Strings
 *
 */
public class IndexParser {
	private BufferedReader br;
	private Iterator<String> words; // holds words in line currently being read
	private BufferedReader _file;
	private int _name;
	private int _nodes;
	private Trie _trie;
	
	/**
	 * Creates a TextImporter reading from the file specified. The caller
	 * must call closeFile() on this instance to close the file after the 
	 * caller is finished with it.
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public IndexParser(String filename, Trie trie) throws FileNotFoundException {
		
		_trie = trie;
		
		_file = new BufferedReader(new FileReader(filename));
		
		// set up indices
		_name = -1;
		_nodes = -1;
		
		String[] header = null;
		try {
			header = _file.readLine().split("\t");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i=0; i<header.length; i++) {
			if(header[i].equals("name")) {
				_name = i;
			}
			if(header[i].equals("nodes")) {
				_nodes = i;
			}
		}

		if(_name == -1) {
			System.err.println("ERROR: No id field in " + filename);
			System.exit(1);
		}
		if(_nodes == -1) {
			System.err.println("ERROR: No longitude field in " + filename);
			System.exit(1);
		}
		
		buildTrie();
	}
	
	private void buildTrie() {
		String line;
		String[] data;
		try {
			while((line = _file.readLine()) != null) {
				data = line.split("\t");
				TrieNode node = _trie.insertOrGet(data[0]);
				node.incrementCount();
				node.concatData(data[1]);
			}
		}
		catch(IOException e) {
			
		}
	}
		
		
	
}
