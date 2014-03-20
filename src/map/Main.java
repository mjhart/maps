package map;

public class Main {

	public static void main(String[] args) {
		if(args.length==4){
			if(args[0].equals("--gui")) {
				new Controller(args[1], args[2], args[3], true);
			}
			else {
				System.out.println("ERROR: Usage: maps [--gui] <ways> <ndoes> <index>");
			}
		}
		else if(args.length==3) {
			new Controller(args[0], args[1], args[2], false);
		}
		else {
			System.out.println("ERROR: Usage: maps [--gui] <ways> <ndoes> <index>");
		}
		//new Controller("/course/cs032/data/maps/ways.tsv", "/course/cs032/data/maps/nodes.tsv", "/course/cs032/data/maps/index.tsv", true);
		//new Controller("small_ways.tsv", "small_nodes.tsv", "small_nodes.tsv", true);
	}

}
