import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Traveller {
	Maze m;

	public Traveller(Maze m) throws Exception {
		this.m = m;
		m.go(m.getNode(0, 0));
	}
	
	public void travel() throws Exception {
		rTravel(new HashSet<Maze.Node>());
	}
	
	public boolean rTravel(Set<Maze.Node> s) throws Exception {
		Iterator <Maze.Node> i = m.getMoves();
		m.print();
		Maze.Node n;
		Maze.Node here = m.currentNode();
		if (m.atEnd()) {
			return true;
		} else {
			while(i.hasNext()) {
				n = i.next();
				if (!s.contains(n)) {
					s.add(n);
					m.go(n);
					m.print();
					if(rTravel(s)) {
						return true;
					}
					m.go(here);
					m.print();
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		Maze m = new Maze(12, 5);
		Traveller t = new Traveller(m);
		t.travel();
	}
}
