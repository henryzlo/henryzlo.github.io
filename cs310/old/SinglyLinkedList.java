
/*
 *
 * SinglyLinkedList.java
 */

public class SinglyLinkedList<T> 
{
  private Node<T> head; // the head reference
  private int size;  // list size (# elements)

  public SinglyLinkedList()
  {
      head = new Node<T>(null, null); // dummy header node!
  }
  
  // helper method
  // insert a new node with given element after a given node in list
  private void insertAfter(Node<T> node, T element)
  {
      // create the new node
      Node<T> n = new Node<T>(element, node.next);
      // insert the node in the list
      node.next = n;
      // update the list size
      size++;
  }
  // add o to end of list
  public boolean add(T o)
  {
      // we move to the last node
      Node<T> current = head;
      while (current.next != null)
        current = current.next;

      insertAfter(current, o);
      current = null;
      return true;
  }
  // get the ith element, or throw if beyond end of list
  public T get(int index) {
	  // we move to the ith node, if possible
      Node<T> current = head.next; // start after dummy
      int i = 0;
      while (current.next != null && i < index) {
        current = current.next;
        i++;
      }
      if (i == index)
    	  return current.data;
      else 
    	  throw new IndexOutOfBoundsException();
  }
  public int size()
  {
      return size;
  }

  public boolean isEmpty()
  {
      return size == 0;
  }
  // quick test of this code: list of Strings, list of Integers
  // 
  public static void main(String[] args) {
		SinglyLinkedList<String> list = new SinglyLinkedList<String>();
		list.add("foo");
		list.add("bar");
		list.add("bar"); 
		System.out.println("list size = " + list.size());
		System.out.println("#0 element = " + list.get(0));
		System.out.println("#1 element = " + list.get(1));
		System.out.println("#2 element = " + list.get(2));
		SinglyLinkedList<Integer> list1 = new SinglyLinkedList<Integer>();
		list1.add(6);
		System.out.println("list1 size = " + list1.size());
		System.out.println("#0 element = " + list1.get(0));
		// this will throw an IndexOutOfBoundsException--
		System.out.println("#1 element = " + list1.get(1));
	}

}
// this is our node class, a singly linked node
class Node<T>
{
  T data;
  Node<T> next;

  Node(T data, Node<T> next)
  {
    this.data = data;
    this.next = next;
  }
}

