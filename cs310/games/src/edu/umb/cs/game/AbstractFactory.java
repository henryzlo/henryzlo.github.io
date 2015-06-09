// AbstractFactory.java
//
// Ethan Bolker, February 2003

package edu.umb.cs.game;

/**
 * Factory class for constructing Object instances dynamically.
 */
public abstract class AbstractFactory {
	/**
	 * Create an Object given the name of its class.
	 * 
	 * @param classname
	 *            the name of the class.
	 * @return an instance of the requested class.
	 * @throws Exception
	 *             if class not found.
	 */
	protected static Object createObject(String classname) throws Exception {
		return (Class.forName(classname).newInstance());
	}
}
