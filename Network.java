import java.io.*;
import java.util.*;
import java.lang.*;

public class Network{
	//private int capacity;
	private int wn;
	private String []snodes;
	private HashMap<String, Node> nodes;
	private int nlinks;
	private String route;

	public Network(){
		this.nodes = new HashMap<String, Node >();
	}

	public static void main(String []args){
		Network n = new Network();
		Scanner console = new Scanner(System.in);
		n.init(console);
	} 

	private void init(Scanner console){
		//System.out.println("System is ready to accept input, please enter capacity of a fiber : ");
		//this.capacity = Integer.parseInt(console.nextLine());
		System.out.println("Please enter no of wavelengths per fiber:");
		this.wn = Integer.parseInt(console.nextLine());
		System.out.println("Please enter nodes seperated by a '-':");
		this.snodes = (console.nextLine()).split("-");
		System.out.println("Please enter no of links:");
		this.nlinks = Integer.parseInt(console.nextLine());

		String []links = new String[this.nlinks];

		System.out.println("Please enter the links:");

		for(int i=0;i<this.nlinks;i++) links[i] = console.nextLine();
		this.addNodes();
		addlinks(links);



		while(true){
			System.out.println("Please enter the source and destination seperated by a '-' :");
			String []start = console.nextLine().split("-");
			System.out.println("Please wait");
			String ans = this.findPath(start[0], start[1]);
			System.out.printf("%s",ans);
			/*int ans = this.getLightpath();
			if (ans<0)System.out.println("No route available.");
			else System.out.printf("assigned lambda%d for the route.\n",ans);*/
			System.out.println("Have another route?");
			if(console.nextLine().equals("N")) break;
		}


	}

	/*private int [] getlist(String a,ArrayList<Link> paths ){
		int []list ;
		for (int x=0; x<paths.size(); x++){//for each link originating from source
			if(paths.get(x).dest.equals(a)){
				list = paths.get(x).list;
				return list;
			}
		}
		return null;
	}*/

	/*private int getLightpath(){
		boolean []used = new boolean[this.wn];
		String []temp = this.route.split("-");
		int lamda = -1;
		for(int i=0;i<(temp.length-1);i++){ //for each step
			String dest,source;
			dest = temp[i+1]; source = temp[i];
			int []list = null;
			ArrayList<Link> paths = this.nodes.get(source);
			if (paths!=null) list = getlist(dest,paths);

			if(paths==null || list == null){
					paths = this.nodes.get(dest);
					if (paths==null) return  lamda;
					list = getlist(source,paths);
			}

			if(list == null) return lamda; //if list is not null proceed, else no link

			for(int j=0;j<(this.wn);j++){
				if(list[j]==1) used[j]=true;
			}
		}

		for(int j=0;j<(this.wn);j++){
			if(!used[j]){
				lamda = j;
				break;
			}
		}

		if(lamda == -1)			return lamda; //if no free lamda, return

		for (int i = 0; i < (temp.length - 1); i++) {
			ArrayList<Link> paths = this.nodes.get(temp[i]);
			String s = temp[i + 1];
			if (paths==null){
				paths = this.nodes.get(temp[i+1]);
				s = temp[i];
			}
			for (int x = 0; x < paths.size(); x++) {
				if (paths.get(x).dest.equals(s)) {
					paths.get(x).list[lamda] = 1;
					break;
				}
			}
		}

		return lamda;
	}*/

	private void addlinks(String [] links){
		Node n1 = null;
		for(int i=0;i<this.nlinks;i++){
			String [] temp = links[i].split("-");

			//add links in both directions
			Link l = new Link(temp[1]);
			n1 = this.nodes.get(temp[0]);
			n1.links.put(temp[1],l);

			l = new Link(temp[0]);
			n1 = this.nodes.get(temp[1]);
			n1.links.put(temp[0],l);
		}
	}

	private void addNodes(){
		for(int i=0;i<this.snodes.length;i++){
			Node temp = new Node(this.snodes[i]);
			this.nodes.put(this.snodes[i],temp);
		}
	}

	private String findPath(String s, String d){
		Queue<Node> queue = new LinkedList<Node>();
		Node n = null;
		queue.add(this.nodes.get(s));
		while (!queue.isEmpty()){
			System.out.println("whhile..");
			n = queue.remove();
			if(n.name.equals(d)){System.out.println("break..");break;}
			for (Map.Entry<String, Link> entry : n.links.entrySet()) {
				//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				Node dest = this.nodes.get(entry.getValue().dest);
				if(dest.visited == 0){
					dest.visited = 1;
					dest.prev = n.name;
					queue.add(dest);
				}
			}
			n.visited = 2;
		}
		System.out.println("out of whhile..");
		String path=null;
		n = this.nodes.get(d);
		while(n.prev!=null){
			//System.out.println( n.name);
			path = n.name + "-" + path;
			n= this.nodes.get(n.prev);
		}
		path = s + "-" + path;
		this.clearNodes();
		return path;
	}

	private void printNodes(){
		for (Map.Entry<String, Node> entry : this.nodes.entrySet()) {
			//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			Node f = entry.getValue();
			for (Map.Entry<String, Link> entry1 : f.links.entrySet()) {
				System.out.println(entry1.getKey() );
			}
			System.out.println();
		}
	}

	private void clearNodes(){
		for (Map.Entry<String, Node> entry : this.nodes.entrySet()) {
			Node f = entry.getValue();
			f.visited = 0;
			f.prev = null;
		}
	}
}