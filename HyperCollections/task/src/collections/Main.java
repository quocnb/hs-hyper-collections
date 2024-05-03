package collections;

public class Main {
  public static void main(String[] args) {
     Range<Integer> range1 = Range.closedOpen(10, 20);
     Range<Integer> range2 = Range.greaterThan(20);
     System.out.println(range1.intersection(range2));
  }
}