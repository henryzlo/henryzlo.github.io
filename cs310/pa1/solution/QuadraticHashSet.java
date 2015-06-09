import java.util.ArrayList;

public class QuadraticHashSet<E> extends AbstractHashSet<E> {
	private ArrayList<E> entries;

	public QuadraticHashSet(int size){
		entries = new ArrayList<E>(size);
		for (int i=0; i<size; i++)
			entries.add(i, null);
		collisions = 0;
	}
	
	public int size() {
		return entries.size();
	}

	public boolean add(E e) {
		int index;
		index = findIndex(e);
		if (index==-1) 
		    return false;
		else {
		    entries.set(index, e);
		    return true;
		}
	}
	
	private int findIndex(E e) {
		int index = Math.abs(e.hashCode()) % size();
		int newindex = index;
		int step = 3;
		int i = 0;
		while (entries.get(index) != null) {
		    newindex = (index + (i*step)*(i*step)) % size();
		    collisions++;
		    
		    if (i++==size())
			return -1;
		}
		return newindex;
	}

	public E get(Object o) {
		E entry = (E) o;
		return entries.get(findIndex(entry));
	}

	public boolean remove(E e) {
		return false;
	}
}
