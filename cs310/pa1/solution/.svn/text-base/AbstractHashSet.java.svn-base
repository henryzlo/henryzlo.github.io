/**
 * A minimal abstract class for hash sets. Supports adding, retrieval, and
 * removal. Does not support iteration or entry removal.
 * 
 * @param <E>
 *            the type of elements maintained by this set
 * @author Henry Lo
 */

abstract class AbstractHashSet<E> {

	/**
	 * Counter for number of total collisions encountered.
	 */
	protected int collisions;

	/**
	 * Returns the total number of collisions.
	 * 
	 * @return the number of collisions
	 */
	public int getCollisions() {
		return collisions;
	}

	/**
	 * Returns the number of elements stored in the hash set.
	 * 
	 * @return the number of elements
	 */
	public abstract int size();

	/**
	 * Adds an element to the hash set. Returns true if successful, false if
	 * failed.
	 * 
	 * @return true if successfully added e, false otherwise
	 */
	public abstract boolean add(E e);

	/**
	 * Removes an element from the hash set. Returns true if successful, false
	 * if failed.
	 * 
	 * @return true if successfully removed, false otherwise
	 */
	public abstract boolean remove(E e);

	/**
	 * Retrieves an element o. Returns null if it doesn't exist in the hash set.
	 * 
	 * @return the object if it exists in the hash set, null otherwise
	 */
	public abstract E get(Object o);
}