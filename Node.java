import java.io.*;
import java.util.*;
import java.lang.*;

public class Node{
    String name;
    int visited;
    String prev;
    HashMap<String, Link> links;

    public Node(String name){
        this.name = name;
        this.visited = 0;
        this.prev = null;
        this.links = new HashMap<String, Link>();
    }
}