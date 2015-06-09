import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Maze {
	private Node[][] cells;
	private HashMap<Node, LinkedList<Node>> adjacencyList;
	private int width;
	private int height;
	private int markX;
	private int markY;

	public Maze(int width, int height) {
		this.cells = new Node[width][height];
		this.adjacencyList = new HashMap<Node, LinkedList<Node>>();
		this.width = width;
		this.height = height;
		this.markX = -1;
		this.markY = -1;

		Node n;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				n = new Node(i, j);
				this.cells[i][j] = n;
				this.adjacencyList.put(n, new LinkedList<Node>());
			}
		}
		makeEdges();
	}
	
	public void makeEdges() {
		HashSet<Node> visited = new HashSet<Node>();
		HashSet<Node> notVisited = new HashSet<Node>();
		ArrayList<Wall> walls = new ArrayList<Wall>();
		Wall w;
		int i, startY, startX;
		Iterator<Wall> itr;
		Node n1, n2;

		startY = (int)(Math.random()*height);
		startX = (int)(Math.random()*width);
		
		for (i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i==startX && j==startY) {
					visited.add(getNode(i,j));
					for (Wall thisWall : getNode(i,j).walls())
						walls.add(thisWall);
				} else {
					notVisited.add(getNode(i,j));
				}
			}
		}

		while (!notVisited.isEmpty()) {	
			i = (int)(Math.random() * walls.size());
			w = walls.get(i);
			n1 = w.getStartNode();
			n2 = w.getEndNode();
			if ((visited.contains(n1) && notVisited.contains(n2)) || 
				(visited.contains(n2) && notVisited.contains(n1))) {
				notVisited.remove(n2);
				visited.add(n2);
				addEdge(n1, n2);
				print();
				itr = n2.walls().iterator();
				while(itr.hasNext())
					walls.add((Wall)itr.next());
			} else {
				walls.remove(i);
			}
		}
	}
	
	public void print() {
		try {	
			Thread.sleep(100);
		} catch(Exception e) {}
		
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				System.out.print("\u2588");
				if (j==0 || !hasEdge(getNode(i, j-1), getNode(i, j)))
					System.out.print("\u2588");
				else
					System.out.print(" ");
				if (i==width-1) 
					System.out.print("\u2588");
			}
			System.out.println();
			
			for (int i = 0; i < width; i++) {
				if (i==0 || !hasEdge(getNode(i-1,j), getNode(i,j)))
					System.out.print("\u2588");
				else
					System.out.print(" ");
				if (i==markX && j==markY)
					System.out.print("x");
				else
					System.out.print(" ");
				if (i==width-1) 
					System.out.print("\u2588");
			}
			
			if (j==height-1) {
				System.out.println();
				for (int i = 0; i < 2*width+1; i++)
					System.out.print("\u2588");	
			}
			System.out.println();
		}

		System.out.println();
	}

	public Node getNode(int i, int j) {
		if (j < 0 || j >= height || i < 0 || i >= width)
			return null;
		else
			return cells[i][j];
	}

	public void addEdge(Node n1, Node n2) {
		LinkedList <Node> list = adjacencyList.get(n1);
		if (list==null)
			adjacencyList.put(n1, new LinkedList<Node>());
		list.add(n2);
		
		list = adjacencyList.get(n2);
		if (list==null)
			adjacencyList.put(n2, new LinkedList<Node>());
		list.add(n1);
	}

	public boolean hasEdge(Node n1, Node n2) {
		LinkedList <Node> list = adjacencyList.get(n1);
		if (list==null)
			return false;
		else
			return list.contains(n2);
	}
	
	// for traveller
	public Node currentNode() {
		return getNode(markX,markY);
	}
	
	public void go(Node n) throws Exception {
		int x = n.getX();
		int y = n.getY();
		if (!(x==0 && y==0) && !hasEdge(currentNode(), n))
			throw new Exception("Illegal move");
		else {
			markX = n.getX();
			markY = n.getY();
		}
	}
	
	public Iterator<Node> getMoves() {
		Node n = cells[markX][markY];
		HashSet<Node> s = new HashSet<Node>();
		for (Wall w: n.walls()) {
			if (hasEdge(w.getStartNode(), w.getEndNode()))
				s.add(w.getEndNode());
		}
		return s.iterator();
	}
	
	public boolean atEnd() {
		return markX == (width-1) && markY==(height-1);
	}

	public class Node {
		private int x, y;

		public Node(int i, int j) {
			x = i;
			y = j;
		}
		
		public Collection<Wall> walls() {
			ArrayList <Wall> l = new ArrayList <Wall>();
			if (x < width-1)
				l.add(new Wall(this, Maze.this.getNode(x+1,y)));
			if (y < height-1)
				l.add(new Wall(this, Maze.this.getNode(x,y+1)));
			if (x > 0)
				l.add(new Wall(this, Maze.this.getNode(x-1,y)));
			if (y > 0)
				l.add(new Wall(this, Maze.this.getNode(x,y-1)));
			
			for (Wall w:l)
				if (w.n2==null)
					System.out.println("ohno");
			
			return l;
		}
		
		public String toString() {
			return "Node " + x + "," + y;
		}
		
		public int getX() {return x;}
		public int getY() {return y;}
	}
	
	public class Wall {
		private Node n1, n2;

		public Wall(Node n1, Node n2) {
			this.n1 = n1;
			this.n2 = n2;
		}
		
		public Node getStartNode() {
			return n1;
		}
		
		public Node getEndNode() {
			return n2;
		}
		
		public boolean equals(Object obj) {
			Wall other = (Wall) obj;
			return ((other.getStartNode() == n1 && other.getEndNode()==n2) ||
					(other.getStartNode() == n2 && other.getEndNode()==n1));			
		}

		public String toString() {
			return n1.toString() + " -> " + n2.toString();
		}
	}
	
	public static void main(String[] args) {
		Maze m = new Maze(12, 4);
		m.print();
	}
}
