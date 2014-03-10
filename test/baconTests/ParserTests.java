package baconTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import bacon.DataParser;
import bacon.IndexParser;

public class ParserTests {
	
	@Test
	public void indexTestName() throws Exception{
		try (IndexParser ip = new IndexParser("/course/cs032/data/bacon/smallIndex.tsv")){
			String uid = "NOT A UID";
			uid = ip.search("Michael Shalhoub", 0, ip.getLength());
			assertTrue(uid.equals("/m/0gm17q1"));
			uid = ip.search("Not a Name", 0, ip.getLength());
			assertNull(uid);
			uid = ip.search("Amy Stiller", 0, ip.getLength());
			assertTrue(uid.equals("/m/0194r1"));
			uid = ip.search("Willow Smith", 0, ip.getLength());
			assertTrue(uid.equals("/m/03gq433"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void actorTestUID() throws Exception{
		try(DataParser dp = new DataParser("/course/cs032/data/bacon/smallActors.tsv", true)){
			String name = "NOT A NAME";
			name = dp.searchUID("/m/03gq433", 0, dp.getLength());
			assertTrue(name.equals("Willow Smith"));
			name = dp.searchUID("NOT A UID", 0, dp.getLength());
			assertNull(name);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void actorTest() throws Exception{
		try(DataParser dp = new DataParser("/course/cs032/data/bacon/smallActors.tsv", true)){
			String test[] = dp.search("/m/0309lm",0, dp.getLength());
			//System.out.println("here");
			assertTrue(test[0].equals("/m/01xrr2_"));
			assertTrue(test[1].equals("/m/03hp2y1"));
			test = dp.search("/m/01nfys",0, dp.getLength());
			assertTrue(test[0].equals("/m/08k40m"));
			assertTrue(test[1].equals("/m/0crzbrv"));
			test = dp.search("not a uid", 0, dp.getLength());
			assertNull(test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void filmTest() throws Exception{
		try(DataParser dp = new DataParser("/course/cs032/data/bacon/smallFilms.tsv", false)){
			String test[] = dp.search("/m/02q3fdr",0, dp.getLength());
			assertTrue(test[0].equals("/m/0154qm"));
			assertTrue(test[1].equals("/m/025mb_"));
			test = dp.search("/m/0c1jc8s",0, dp.getLength());
			assertTrue(test[0].equals("/m/0g4jm8q"));
			assertTrue(test[1].equals("/m/02mjf2"));
			test = dp.search("not a uid", 0, dp.getLength());
			assertNull(test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void comboTest() throws Exception {
		IndexParser ip = new IndexParser("/course/cs032/data/bacon/smallIndex.tsv");
		DataParser ap = new DataParser("/course/cs032/data/bacon/smallActors.tsv", true);
		DataParser fp = new DataParser("/course/cs032/data/bacon/smallFilms.tsv", false);
		String aid = ip.search("Willow Smith",0,ip.getLength());
		assertNotNull(aid);
		String[] test = ap.search(aid, 0, ap.getLength());
		assertTrue(test[0].equals("/m/07k9wxd"));
		for(String s : test){
			String[] test2 = fp.search(s, 0, fp.getLength());
			assertTrue(test2[0].equals("/m/025mb_"));
			assertTrue(test2[1].equals("/m/03gq433"));
		}
		ip.close();
		ap.close();
		fp.close();
	}

}
