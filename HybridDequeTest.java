import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class HybridDequeTest<E> {
  
  
  @Test
  void testClass() {
    HybridDeque.setBlockSize(4);
    HybridDeque<String> hd = new HybridDeque<>();
    try {
      hd.offerFirst(null);
      fail("Exception not thrown");
    } catch (NullPointerException e) {
      //Element was thrown as expected
    }
    try {
      hd.offerLast(null);
      fail("Exception not thrown");
    } catch (NullPointerException e) {
      //Element was thrown as expected
    }
    hd.offerFirst("Hello");
    hd.offerFirst("A");
    hd.offerLast("V");
    hd.offerLast("E");
    hd.offerLast("Dave");
    hd.offerFirst("Him");
    hd.offerFirst("Him");
    hd.offerFirst("Him");
    hd.offerFirst("Him");

    assertFalse(hd.removeFirstOccurrence("be"));
    assertFalse(hd.removeLastOccurrence("be"));
    
    hd.removeLastOccurrence("A");
    hd.removeLastOccurrence("Him");
    hd.removeLastOccurrence("Him");
    
    hd.clear();
    assertNull(hd.peekFirst());
    assertNull(hd.peekLast());
    hd.offerFirst("v");
    hd.offerFirst("Hello");
    hd.offerFirst("A");
    hd.offerLast("V");
    hd.offerLast("E");
    hd.offerLast("Dave");
    hd.offerFirst("Him");
    hd.offerFirst("two");
    hd.offerFirst("Him");
    hd.offerFirst("Him");
    hd.pollFirst();
    hd.pollLast();
    hd.pollFirst();
    hd.pollLast();
    assertEquals(hd.peekFirst(), "two");
    assertEquals(hd.peekLast(), "V");
    hd.removeFirstOccurrence("A");
    hd.removeLastOccurrence("Him");
    assertEquals(hd.peekFirst(), "two");
    assertEquals(hd.peekLast(), "V");
    HybridDeque.setBlockSize(64);
    HybridDeque<Integer> hd2 = new HybridDeque<>(); 
    for (int i = 32; i >= 0; i--) {
      hd2.offerFirst(i);
      
    }
    for (int i = 32; i <= 64; i++) {
      hd2.offerLast(i);
    }
    assertEquals(64, hd2.pollLast());
    assertEquals(0, hd2.pollFirst());
    hd2.removeFirstOccurrence(63);
    assertEquals(62, hd2.pollLast());
    Iterator iterator = hd2.iterator();
    for (int i = 0; i <= 61; i++) {
      iterator.next();
      iterator.remove();
    }
    HybridDeque.setBlockSize(4);
    HybridDeque<Integer> hd3 = new HybridDeque<>();
    for (int i = 32; i >= 0; i--) {
      hd3.offerFirst(i);
      
    }
    for (int i = 33; i <= 64; i++) {
      hd3.offerLast(i);
    }
    //Iterator iterator2 = hd3.descendingIterator();
    for (int i = 0; i <= 64; i++) {
      //iterator2.next();
      //iterator2.remove();
      hd3.removeLastOccurrence(i);
    }
    HybridDeque.setBlockSize(4);
    HybridDeque<Integer> hd4 = new HybridDeque<>();
    for (int i = 32; i >= 0; i--) {
      hd4.offerFirst(i);
      
    }
    for (int i = 33; i <= 64; i++) {
      hd4.offerLast(i);
    }
    Iterator iterator3 = hd4.descendingIterator();
    for (int i = 0; i <= 32; i++) {
      iterator3.next();
      iterator3.remove();
      iterator3.next();
    }
    
    HybridDeque<Integer> hd5 = new HybridDeque<>();
    for (int i = 32; i >= 0; i--) {
      hd5.offerFirst(i);
      
    }
    for (int i = 33; i <= 64; i++) {
      hd5.offerLast(i);
    }
    Iterator iterator4 = hd5.iterator();
    for (int i = 0; i <= 64; i++) {
      iterator4.next();
      iterator4.remove();
    }
    
    HybridDeque.setBlockSize(4);
    HybridDeque<Integer> hd6 = new HybridDeque<>();
    for (int i = 2; i >= 0; i--) {
      hd6.offerFirst(i);
      
    }
    for (int i = 3; i <= 6; i++) {
      hd6.offerLast(i);
    } 
    Iterator iterator5 = hd6.descendingIterator();
    for (int i = 0; i <= 6; i++) {
      iterator5.next();
      iterator5.remove();
    }    
  }

  @Test
  void testEquals() {
    HybridDeque.setBlockSize(4);
    HybridDeque<String> hd = new HybridDeque<>();
    assertFalse(hd.equals("dave"));
    HybridDeque<String> hd2 = new HybridDeque<>();
    assertTrue(hd.equals(hd2));
    
    hd.offerFirst("dave");
    assertFalse(hd.equals(hd2));
    
    hd.pollFirst();
    hd2.offerLast("d");
    assertFalse(hd.equals(hd2));
    
    hd.offerLast("d");
    hd2.offerFirst("a");
    assertFalse(hd.equals(hd2));
    
    hd.offerFirst("a");
    assertTrue(hd.equals(hd2));
    
    
    hd2.offerLast("l");
    assertFalse(hd.equals(hd2));
    
    hd.offerLast("l");
    hd.offerFirst("hello");
    assertFalse(hd.equals(hd2));
    
    hd.pollFirst();
    assertTrue(hd.equals(hd2));
    
    hd.offerFirst("dave");
    hd.offerFirst("comp");
    assertFalse(hd.equals(hd2));
    
    hd.clear();
    hd2.clear();
    assertTrue(hd.equals(hd2));
  }

  @Test
  void testIterators() {
    HybridDeque.setBlockSize(4);
    HybridDeque<String> hd = new HybridDeque<>();
    Iterator<String> iterator = hd.iterator();
    Iterator<String> descIterator = hd.descendingIterator();
    try {
      iterator.next();
      fail("There was no exception thrown");
    } catch (NoSuchElementException e) {
      //Exception was thrown as expected
    }
    try {
      descIterator.next();
      fail("There was no exception thrown");
    } catch (NoSuchElementException e) {
      //Exception was thrown as expected
    }
    assertFalse(iterator.hasNext());
    assertFalse(descIterator.hasNext());
    hd.offerFirst("Hello");
    hd.offerFirst("A");
    hd.offerLast("V");
    hd.offerLast("E");
    hd.offerLast("Dave");
    hd.offerFirst("Him");
    hd.offerLast("M");
    hd.offerLast("M");
    iterator = hd.iterator();
    descIterator = hd.descendingIterator();
    try {
      iterator.remove();
      fail("Exception was not thrown");
    } catch (IllegalStateException e) {
      //Exception was thrown as expected
    }
    try {
      descIterator.remove();
      fail("Exception was not thrown");
    } catch (IllegalStateException e) {
      //Exception was thrown as expected
    }
    //descIterator.next();
    hd.offerFirst("Hello");
    hd.offerFirst("A");
    hd.offerLast("V");
    hd.offerLast("E");
    hd.offerLast("Dave");
    hd.offerFirst("Him");
    hd.offerLast("M");
    hd.offerLast("M");
  }
  
  @Test
  void testAllMethodsBlockSize64() {
    HybridDeque.setBlockSize(64);
    HybridDeque<Integer> deque = new HybridDeque<>();
    int num = 500;
    for (int i = 0; i < num; i++) {
      deque.offerLast(i);
    }
    for (int i = 0; i < num; i++) {
      deque.offerFirst(i);
    }
    for (int i = 0; i < num; i++) {
      assertEquals(num - i - 1, deque.pollLast());
    }
    for (int i = 0; i < num; i++) {
      assertEquals(num - i - 1, deque.pollFirst());
    }
    for (int i = 0; i < num; i++) {
      deque.offerFirst(i);
      assertEquals(deque.peekFirst(), i);
      deque.offerLast(i);
      assertEquals(deque.peekLast(), i);
    }
    Iterator<Integer> it = deque.iterator();
    for (int i = 0; i < num; i++) {
      assertEquals(num - i - 1, it.next());
    }
    for (int i = 0; i < num; i++) {
      assertEquals(i, it.next());
    }
    it = deque.descendingIterator();
    for (int i = 0; i < num; i++) {
      assertEquals(num - i - 1, it.next());
    }
    for (int i = 0; i < num; i++) {
      assertEquals(i, it.next());
    }
    HybridDeque<Integer> deque1 = new HybridDeque<>();
    deque.clear();
    for (int i = 0; i < num; i++) {
      deque.offerFirst(i);
      deque1.offerFirst(i);
    }
    assertTrue(deque1.equals(deque));
    assertTrue(deque.equals(deque1));
    deque1.pollFirst();
    deque1.offerFirst(712);
    assertFalse(deque1.equals(deque));
    assertFalse(deque.equals(deque1));
  }
  
  @Test
  public void testAllMethodsAlternateBlockSizes() {
    HybridDeque.setBlockSize(8);
    HybridDeque<Integer> deque = new HybridDeque<>();
    int num = 500;
    for (int i = 0; i < num; i++) {
      deque.offerLast(i);
    }
    for (int i = 0; i < num; i++) {
      deque.offerFirst(i);
    }
    for (int i = 0; i < num; i++) {
      assertEquals(num - i - 1, deque.pollLast());
    }
    for (int i = 0; i < num; i++) {
      assertEquals(num - i - 1, deque.pollFirst());
    }
    for (int i = 0; i < num; i++) {
      deque.offerFirst(i);
      assertEquals(deque.peekFirst(), i);
      deque.offerLast(i);
      assertEquals(deque.peekLast(), i);
    }
    Iterator<Integer> it = deque.iterator();
    for (int i = 0; i < num; i++) {
      assertEquals(num - i - 1, it.next());
    }
    for (int i = 0; i < num; i++) {
      assertEquals(i, it.next());
    }
    it = deque.descendingIterator();
    for (int i = 0; i < num; i++) {
      assertEquals(num - i - 1, it.next());
    }
    for (int i = 0; i < num; i++) {
      assertEquals(i, it.next());
    }
    HybridDeque.setBlockSize(128);
    HybridDeque<Integer> deque1 = new HybridDeque<>();
    deque.clear();
    for (int i = 0; i < num; i++) {
      deque.offerFirst(i);
      deque1.offerFirst(i);
    }
    assertTrue(deque1.equals(deque));
    assertTrue(deque.equals(deque1));
    deque1.pollFirst();
    deque1.offerFirst(712);
    assertFalse(deque1.equals(deque));
    assertFalse(deque.equals(deque1));
  }

}
