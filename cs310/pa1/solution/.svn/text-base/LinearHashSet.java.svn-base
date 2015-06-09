import java.util.ArrayList;

public class LinearHashSet<E> extends AbstractHashSet<E> {
	private ArrayList<E> entries;

	public LinearHashSet(int size) {
		entries = new ArrayList<E>(size);
		for (int i = 0; i < size; i++)
			entries.add(i, null);
		collisions = 0;
	}

	public int size() {
		return entries.size();
	}

	public boolean add(E e) {
		int index;
		index = findIndex(e);
		entries.set(index, e);
		return true;
	}

	private int findIndex(E e) {
		int index = Math.abs(e.hashCode()) % size();
		int step = 3;
		if (size()!=1000)
		    System.out.println(size());
		while (entries.get(index) != null) {
			index = (index + step) % size();
			collisions++;
		}
		return index;
	}

	public E get(Object o) {
		E entry = (E) o;
		return entries.get(findIndex(entry));
	}
	
	public boolean remove(E e) {
		return false;
	}
}
