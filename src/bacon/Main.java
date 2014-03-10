package bacon;

import java.io.IOException;


public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){
		//System.out.println("WORKS");
		while(true){
			App app = new App();
			try{
				app.init(args);
			}
			catch(Exception e){
				System.out.println(e.toString().substring(e.toString().indexOf("ERROR")));
				break;
			}
			try {
				app.search();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("DONE");
			break;
		}
	}

}
