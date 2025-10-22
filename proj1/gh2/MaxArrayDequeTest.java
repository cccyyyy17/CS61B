package gh2;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class MaxArrayDequeTest {

    @Test
    public void testIntegerMax() {
        Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return a - b;
            }
        };

        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(comp);
        deque.addLast(5);
        deque.addLast(10);
        deque.addLast(3);
        deque.addLast(8);

        Integer result = deque.max();
        assertEquals("整数最大值应该是10", Integer.valueOf(10), result);
    }

    @Test
    public void testStringLengthMax() {
        Comparator<String> comp = new Comparator<String>() {
            public int compare(String a, String b) {
                return a.length() - b.length();
            }
        };

        MaxArrayDeque<String> deque = new MaxArrayDeque<>(comp);
        deque.addLast("apple");
        deque.addLast("banana");
        deque.addLast("cat");
        deque.addLast("elephant");

        String result = deque.max();
        assertEquals("最长的字符串应该是elephant", "elephant", result);
    }

    @Test
    public void testDifferentComparator() {
        Comparator<String> lengthComp = new Comparator<String>() {
            public int compare(String a, String b) {
                return a.length() - b.length();
            }
        };

        Comparator<String> alphaComp = new Comparator<String>() {
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        };

        MaxArrayDeque<String> deque = new MaxArrayDeque<>(lengthComp);
        deque.addLast("apple");
        deque.addLast("banana");
        deque.addLast("cat");

        String result = deque.max(alphaComp);
        assertEquals("按字母顺序最大应该是cat", "cat", result);
    }

    @Test
    public void testEmptyDeque() {
        Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return a - b;
            }
        };

        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(comp);
        Integer result = deque.max();

        assertNull("空队列应该返回null", result);
    }

    @Test
    public void testSingleElement() {
        Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return a - b;
            }
        };

        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(comp);
        deque.addLast(42);

        Integer result = deque.max();
        assertEquals("单元素应该返回自身", Integer.valueOf(42), result);
    }

    @Test
    public void testNegativeNumbers() {
        Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return a - b;
            }
        };

        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(comp);
        deque.addLast(-5);
        deque.addLast(-10);
        deque.addLast(-1);
        deque.addLast(-8);

        Integer result = deque.max();
        assertEquals("负数中的最大值应该是-1", Integer.valueOf(-1), result);
    }

    @Test
    public void testAllEqualElements() {
        Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return a - b;
            }
        };

        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(comp);
        deque.addLast(7);
        deque.addLast(7);
        deque.addLast(7);
        deque.addLast(7);

        Integer result = deque.max();
        assertEquals("所有元素相等时应该返回任意一个7", Integer.valueOf(7), result);
    }
}