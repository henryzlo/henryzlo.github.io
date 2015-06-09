import java.util.ArrayList;
import java.util.LinkedList;

public class ChainingHashSet<E> extends AbstractHashSet<E> {
	private ArrayList<LinkedList<E>> entries;
	private int count;

	public ChainingHashSet(int size){
		entries = new ArrayList<LinkedList<E>>(size);
		for (int i=0; i<size; i++)
			entries.add(new LinkedList<E>());
		collisions = 0;
	}
	
	public int size() {
		return count;
	}

	public boolean add(E e) {
		collisions += entries.get(findIndex(e)).size();
		entries.get(findIndex(e)).add(e);
		count += 1;
		return true;
	}

	private int findIndex(E e){
		return Math.abs(e.hashCode()) % entries.size();
	}
	
	public E get(Object o) {
		E e = (E) o;
		LinkedList<E> list = entries.get(findIndex(e));
		return (E) ((list.indexOf(e) == -1) ? null : o);
	}

	public boolean remove(E e) {
		return false;
	}
}