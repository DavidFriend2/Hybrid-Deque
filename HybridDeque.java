import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Doubly-linked-list implementation of the java.util.Deque interface. This
 * implementation is more space-efficient than Java's LinkedList class for large
 * collections because each node contains a block of elements instead of only
 * one. This reduces the overhead required for next and previous node
 * references.This implementation does not allow null's to be added to the collection.
 * Adding a null will result in a NullPointerException.
 *
 * @author David Friend
 * @version 10/04/24
 */
public class HybridDeque<E> extends AbstractDeque<E> {

  /*
   * IMPLEMENTATION NOTES ----------------------------------
   *
   * The list of blocks is never empty, so leftBlock and rightBlock are never
   * equal to null. The list is not circular.
   *
   * A deque's first element is at leftBlock.elements[leftIndex]
   * 
   * and its last element is at rightBlock.elements[rightIndex].
   * 
   * The indices, leftIndex and rightIndex are always in the range:
   * 
   * 0 <= index < BLOCK_SIZE
   *
   * And their exact relationship is:
   * 
   * (leftIndex + size - 1) % BLOCK_SIZE == rightIndex
   *
   * Whenever leftBlock == rightBlock, then:
   * 
   * leftIndex + size - 1 == rightIndex
   *
   * However, when leftBlock != rightBlock, the leftIndex and rightIndex become
   * indices into distinct blocks and either may be larger than the other.
   *
   * Empty deques have:
   * 
   * size == 0
   * 
   * leftBlock == rightBlock
   * 
   * leftIndex == CENTER + 1
   * 
   * rightIndex == CENTER
   * 
   * Checking for size == 0 is the intended way to see whether the Deque is empty.
   * 
   * 
   * (Comments above are a lightly modified version of comments in Python's deque
   * implementation:
   * https://github.com/python/cpython/blob/v3.11.2/Modules/_collectionsmodule.c
   * https://docs.python.org/3.11/license.html)
   * 
   */

  private static int BLOCK_SIZE = 64;
  private static int CENTER = (BLOCK_SIZE - 1) / 2;

  private Cursor leftCursor;
  private Cursor rightCursor;
  private int size;


  /**
   * DO NOT MODIFY THIS METHOD. This will be used in grading/testing to modify the
   * default block size..
   *
   * @param blockSize The new block size
   */
  protected static void setBlockSize(int blockSize) {
    HybridDeque.BLOCK_SIZE = blockSize;
    HybridDeque.CENTER = (blockSize - 1) / 2;
  }


  /**
   * Doubly linked list node (or block) containing an array with space for
   * multiple elements.
   */
  private class Block {
    private E[] elements;
    private Block next;
    private Block prev;

    /**
     * Block Constructor.
     *
     * @param prev Reference to previous block, or null if this is the first
     * @param next Reference to next block, or null if this is the last
     */
    @SuppressWarnings("unchecked")
    public Block(Block prev, Block next) {
      this.elements = (E[]) (new Object[BLOCK_SIZE]);
      this.next = next;
      this.prev = prev;
    }

  }

  /**
   * Many of the complications of implementing this Deque class are related to the
   * fact that there are two pieces of information that need to be maintained to
   * track a position in the deque: a block reference and the index within that
   * block. This class combines those two pieces of information and provides the
   * logic for moving forward and backward through the deque structure.
   * 
   * <p>NOTE: The provided cursor class is *immutable*: once a Cursor object is
   * created, it cannot be modified. Incrementing forward or backward involves
   * creating new Cursor objects at the required location. Immutable objects can
   * be cumbersome to work with, but they prevent coding errors caused by
   * accidentally aliasing mutable objects.
   */
  private class Cursor {
    private final Block block;
    private final int index;

    public Cursor(HybridDeque<E>.Block block, int index) {
      this.block = block;
      this.index = index;
    }

    /**
     * Increment the cursor, crossing a block boundary if necessary.
     *
     * @return A new cursor at the next position, or null if there are no more valid
     *         positions.
     */
    private Cursor next() {

      if (index == BLOCK_SIZE - 1) { // We need to cross a block boundary
        if (block.next == null) {
          return null;
        } else {
          return new Cursor(block.next, 0);
        }
      } else { // Just move one spot forward in the current block
        return new Cursor(block, index + 1);
      }
    }

    /**
     * Decrement the cursor, crossing a block boundary if necessary.
     *
     * @return A new cursor at the previous position, or null if there is no
     *         previous position.
     */
    private Cursor prev() {
      if (index == 0) { // We need to cross a block boundary
        if (block.prev == null) {
          return null;
        } else {
          return new Cursor(block.prev, BLOCK_SIZE - 1);
        }
      } else { // Just move one spot back in the current block.
        return new Cursor(block, index - 1);
      }
    }

    /**
     * Two cursors are equal if they refer to the same index in the same block.
     */
    public boolean equals(Object other) {
      if (!(other instanceof HybridDeque.Cursor)) {
        return false;
      }
      @SuppressWarnings("unchecked")
      Cursor otherCursor = (Cursor) other;
      return otherCursor.block == block && otherCursor.index == index;
    }

    /**
     * Return the element stored at this cursor.
     */
    public E get() {
      return block.elements[index];
    }

    /**
     * Set the element at this cursor.
     */
    public void set(E item) {
      block.elements[index] = item;
    }

  }

  /**
   * HybridDeuque Constructor.
   */
  public HybridDeque() {
    Block block = new Block(null, null);
    rightCursor = new Cursor(block, CENTER);
    leftCursor = new Cursor(block, CENTER + 1);
    size = 0;
  }
  
  @Override
  public boolean offerFirst(E e) {
    if (e == null) {
      throw new NullPointerException();
    }
    if (leftCursor.prev() == null) {
      Block newBlock = new Block(null, leftCursor.block);
      leftCursor.block.prev = newBlock;
    }
    leftCursor = leftCursor.prev();
    leftCursor.set(e);
    size++;
    return true;
  }

  @Override
  public boolean offerLast(E e) {
    if (e == null) {
      throw new NullPointerException();
    }
    
    if (rightCursor.next() == null) {
      Block newBlock = new Block(rightCursor.block, null);
      rightCursor.block.next = newBlock;
    }
    rightCursor = rightCursor.next();
    rightCursor.set(e);
    size++;
    return true;
  }

  @Override
  public E pollFirst() {
    E result = peekFirst();
    if (leftCursor.index == BLOCK_SIZE - 1 && leftCursor.next() == null) {
      leftCursor.block.elements[leftCursor.index] = null;
      clear();
      return result;
    } 
    leftCursor.block.elements[leftCursor.index] = null;
    leftCursor = leftCursor.next();
    if (leftCursor.index == 0) {
      leftCursor.block.prev = null;
    }
    size--;
    return result;
  }

  @Override
  public E pollLast() {
    E result = peekLast();
    if (rightCursor.index == 0 && rightCursor.prev() == null) {
      rightCursor.block.elements[rightCursor.index] = null;
      clear();
      return result;
    } 
    rightCursor.block.elements[rightCursor.index] = null;
    rightCursor = rightCursor.prev();
    if (rightCursor.index == BLOCK_SIZE - 1) {
      rightCursor.block.next = null;
    }
    size--;
    return result;
  }

  @Override
  public E peekFirst() {
    return leftCursor.get();
  }

  @Override
  public E peekLast() {
    return rightCursor.get();
  }

  @Override
  public boolean removeFirstOccurrence(Object o) {
    return iteratorRemoveHelper(iterator(), o);
  }

  @Override
  public boolean removeLastOccurrence(Object o) {
    return iteratorRemoveHelper(descendingIterator(), o);
  }
  
  private boolean iteratorRemoveHelper(Iterator<E> iterator, Object o) {
    while (iterator.hasNext()) {
      E element = iterator.next();
      if (element.equals(o)) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }
      

  @Override
  public int size() {
    return size;
  }
  
  /**
   * Clears HybridDeque back to the beginning.
   */
  public void clear() {
    Block block = new Block(null, null);
    leftCursor = new Cursor(block, CENTER + 1);
    rightCursor = new Cursor(block, CENTER);
    size = 0;
  }
  
  /**
   * Compares to HybridDeques and determines if they are equal.
   *
   * @param obj the HydridDeque to compare
   * @return true if they are equal false otherwise
   */
  public boolean equals(Object obj) {
    if (obj instanceof HybridDeque) {
      if (((AbstractCollection<E>) obj).size() == size()) {
        Iterator<E> objIterator = ((HybridDeque<E>) obj).iterator();
        Iterator<E> thisIterator = iterator();
        if (thisIterator.hasNext() && objIterator.hasNext()) {
          while (thisIterator.hasNext()) {
            if (!objIterator.next().equals(thisIterator.next())) {
              return false;
            }
          }
          return true;
        }
        if (size() == 0 && ((HybridDeque<E>) obj).size() == 0) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Iterator<E> iterator() {
    return new HybridDequeIterator();
  }
  
  private class HybridDequeIterator implements Iterator<E> {
    private int leftIndex;
    private Block block;
    private boolean removeCheck;
    
    private HybridDequeIterator() {
      leftIndex = leftCursor.index;
      block = leftCursor.block;
    }
    
    @Override
    public boolean hasNext() {
      if (block == null) {
        return false;
      }
      if (block.next == null) {
        return leftIndex < rightCursor.index + 1;
      }
      return true;
    }

    @Override
    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      E result = block.elements[leftIndex];
      if (leftIndex == BLOCK_SIZE - 1) {
        block = block.next;
        leftIndex = 0;
      } else {
        leftIndex++;
      }
      removeCheck = true;
      return result;
    }
    
    public void remove() {
      if (!removeCheck) {
        throw new IllegalStateException();
      }
      if (!hasNext()) {
        pollLast();
        removeCheck = false;
        return;
      }
      if (leftIndex == 0) {
        leftIndex = BLOCK_SIZE - 1;
        block = block.prev;
      } else {
        leftIndex--;
      }
      removeHelper(block, leftIndex);
      rightCursor = rightCursor.prev();
      if (rightCursor.index == BLOCK_SIZE - 1) {
        rightCursor.block.next = null;
      }
      removeCheck = false;
    }
  }

  @Override
  public Iterator<E> descendingIterator() {
    return new DescendingHybridDequeIterator();
  }
  
  private class DescendingHybridDequeIterator implements Iterator<E> {
    private int rightIndex;
    private Block block;
    private boolean removeCheck;
    
    private DescendingHybridDequeIterator() {
      rightIndex = rightCursor.index;
      block = rightCursor.block;
    }
    
    @Override
    public boolean hasNext() {
      if (block == null) {
        return false;
      }
      if (block.prev == null) {
        return rightIndex > leftCursor.index - 1;
      }
      return true;
    }

    @Override
    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      E result = block.elements[rightIndex];
      
      if (rightIndex == 0) {
        block = block.prev;
        rightIndex = BLOCK_SIZE - 1;
      } else {
        rightIndex--;
      }
      removeCheck = true;
      return result;
    }
    
    public void remove() {
      if (!removeCheck) {
        throw new IllegalStateException();
      }
      if (!hasNext()) {
        pollFirst();
        removeCheck = false;
        return;
      }
      if (rightIndex == BLOCK_SIZE - 1) {
        rightIndex = 0;
        block = block.next;
      } else {
        rightIndex++;
      }
      if (rightIndex == rightCursor.index) {
        pollLast();
        removeCheck = false;
        if (rightCursor.index == BLOCK_SIZE - 1) {
          block = block.prev;
        }
        rightIndex = rightCursor.index;
        return;
      }
      removeHelper(block, rightIndex);
      rightCursor = rightCursor.prev();
      if (rightCursor.index == BLOCK_SIZE - 1) {
        rightCursor.block.next = null;
      }
      removeCheck = false;
    }
  }
  
  private void removeHelper(Block temp, int index) {
    while (temp != null) {
      if (temp.next == null) {
        for (int i = index; i < rightCursor.index + 1; i++) {
          if (i == rightCursor.index) {
            temp.elements[i] = null;
          } else {
            temp.elements[i] = temp.elements[i + 1];
          }
        }
      } else {
        for (int i = index; i < BLOCK_SIZE; i++) {
          if (i + 1 == BLOCK_SIZE) {
            temp.elements[i] = temp.next.elements[0];
          } else {
            temp.elements[i] = temp.elements[i + 1];
          }
        }
      }
      temp = temp.next;
      index = 0;
    }
    size--;
  }
}
