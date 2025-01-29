import java.util.AbstractCollection;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * The Deque Interface includes several methods that have different names but
 * identical or very similar functionality. For example, add, addLast, offer and
 * offerLast all add a single element to the end of the Deque. The purpose of
 * this class is to simplify the development of new Deque implementations.
 * Concrete subclasses only need implement one of the related methods. This
 * class provides the others by calling the method defined in the subclass.
 *
 * @author CS240 Instructors
 *
 * @version V1.1, 3/2023
 */
public abstract class AbstractDeque<E> extends AbstractCollection<E> implements Deque<E> {

  // -----------------------------------------------
  // METHODS THAT ADD TO THE RIGHT...
  // Concrete subclasses must implement offerLast.
  // -----------------------------------------------

  @Override
  public boolean add(E element) {
    return offerLast(element);
  }

  @Override
  public void addLast(E element) {
    offerLast(element);
  }

  @Override
  public boolean offer(E element) {
    return offerLast(element);
  }

  // -----------------------------------------------
  // METHODS THAT ADD TO THE LEFT...
  // Concrete subclasses must implement offerFirst.
  // -----------------------------------------------

  @Override
  public void addFirst(E element) {
    offerFirst(element);
  }

  @Override
  public void push(E element) {
    offerFirst(element);
  }

  // -----------------------------------------------
  // METHODS THAT RETRIEVE THE LEFTMOST ELEMENT WITH NO MODIFICATION...
  // Concrete subclasses must implement peekFirst.
  // -----------------------------------------------

  @Override
  public E element() {
    return peekFirst();
  }

  @Override
  public E getFirst() {
    if (size() == 0) {
      throw new NoSuchElementException();
    }
    return peekFirst();

  }

  @Override
  public E peek() {
    return peekFirst();
  }

  // -----------------------------------------------
  // METHODS THAT RETRIEVE THE RIGHTMOST ELEMENT WITH NO MODIFICATION...
  // Concrete subclasses must implement peekLast.
  // -----------------------------------------------

  @Override
  public E getLast() {
    if (size() == 0) {
      throw new NoSuchElementException();
    }
    return peekLast();

  }

  // -----------------------------------------------
  // METHODS THAT REMOVE AN ELEMENT FROM THE LEFT...
  // Concrete subclasses must implement pollFirst.
  // -----------------------------------------------

  @Override
  public E poll() {
    return pollFirst();
  }

  @Override
  public E pop() {
    E item = pollFirst();
    if (item == null) {
      throw new NoSuchElementException();
    }
    return item;
  }

  @Override
  public E remove() {
    return pollFirst();
  }

  @Override
  public E removeFirst() {
    return pollFirst();
  }

  // -----------------------------------------------
  // METHODS THAT REMOVE AN ELEMENT FROM THE RIGHT...
  // Concrete subclasses must implement pollLast.
  // -----------------------------------------------

  @Override
  public E removeLast() {
    E item = pollLast();
    if (item == null) {
      throw new NoSuchElementException();
    }
    return item;
  }
}
