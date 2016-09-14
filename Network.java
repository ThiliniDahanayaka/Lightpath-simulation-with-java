import java.io.*;
import java.util.*;
import java.lang.*;

public class Network{
	//private int capacity;
	private int tot = 0;
	private  int done = 0;
	private int wn;
	private String []snodes;
	private HashMap<String, Node> nodes;
	private int nlinks;

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
			tot++;
			System.out.println("Please enter the source and destination seperated by a '-' :");
			String []start = console.nextLine().split("-");
			int ans=-1;
			String path;
			path = this.findPath(start[0], start[1]);
			if(!path.equals(start[0]+"-"+null)){
				System.out.printf("Path assigned is %s\n",path);
				ans = this.getLightpath(path);
				done++;
			}
			if (ans<0)System.out.println("No route available.");
			else System.out.printf("assigned lambda%d for the route.\n",ans);
			System.out.println("Have another route?");
			if(console.nextLine().equals("N")) break;
		}

		System.out.printf("Percentage of blocked routes is %.2f\n",(float)(this.tot-this.done)/this.tot*100);

	}

	private int getLightpath(String route){
		boolean []used = new boolean[this.wn];
		String []temp = route.split("-");
		int lamda = -1;
		for(int i=0;i<(temp.length-2);i++){ //for each step
			String dest,source;
			dest = temp[i+1]; source = temp[i];
			int []list = null;
			HashMap<String, Link> paths = this.nodes.get(source).links;
			//if (paths!=null) return lamda;
			list = paths.get(dest).list;
			if(list == null) return lamda; //if list is not null proceed, else no link

			for(int j=0;j<(this.wn);j++){//find out occupied wavelengths
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

		for (int i = 0; i < (temp.length - 2); i++) {
			HashMap<String, Link> paths = this.nodes.get(temp[i]).links;
			String s = temp[i + 1];
			if(paths.containsKey(s)){
				paths.get(s).list[lamda] = 1;
				boolean full = true;
				for(int m =0;m<this.wn;m++){
					if(paths.get(s).list[m]==0)full =false;
				}
				if(full) paths.remove(s);
			}
		}

		return lamda;
	}

	private void addlinks(String [] links){
		Node n1 = null;
		for(int i=0;i<this.nlinks;i++){
			String [] temp = links[i].split("-");

			//add links in both directions
			int []t = new int[this.wn];
			for(int k=0;k<this.wn;k++)t[k] = 0;
			Link l = new Link(temp[1],t);
			n1 = this.nodes.get(temp[0]);
			n1.links.put(temp[1],l);

			int []t2 = new int[this.wn];
			for(int k=0;k<this.wn;k++)t2[k] = 0;
			l = new Link(temp[0],t2);
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

	//uses breadth first search
	private String findPath(String s, String d){
		Queue<Node> queue = new LinkedList<Node>();
		Node n = null;
		queue.add(this.nodes.get(s));
		while (!queue.isEmpty()){
			n = queue.remove();
			if(n.name.equals(d))break;
			for (Map.Entry<String, Link> entry : n.links.entrySet()) {
				Node dest = this.nodes.get(entry.getValue().dest);
				if(dest.visited == 0){
					dest.visited = 1;
					dest.prev = n.name;
					queue.add(dest);
				}
			}
			n.visited = 2;
		}
		String path=null;
		n = this.nodes.get(d);
		while(n.prev!=null){
			path = n.name + "-" + path;
			n= this.nodes.get(n.prev);
		}
		path = s + "-" + path;
		this.clearNodes();
		return path;
	}

	private void printNodes(){
		for (Map.Entry<String, Node> entry : this.nodes.entrySet()) {
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