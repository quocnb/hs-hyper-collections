package collections;
import java.util.Objects;

public class Range<T extends Comparable> {
    private final T lowerEndpoint;
    private final T upperEndpoint;
    private final boolean lowerInclusive;
    private final boolean upperInclusive;

    private Range() {
        lowerEndpoint = null;
        upperEndpoint = null;
        lowerInclusive = false;
        upperInclusive = false;
    }

    private Range(T lowerEndpoint, T upperEndpoint, boolean lowerInclusive, boolean upperInclusive) {
        this.lowerEndpoint = lowerEndpoint;
        this.upperEndpoint = upperEndpoint;
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
    }

    static <T extends Comparable<T>> void checkNull(T value1, T value2) {
        if (value1 == null || value2 == null) {
            throw new NullPointerException();
        }
    }

    static <T extends Comparable<T>> void checkNull(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
    }

    public static <T extends Comparable<T>> Range<T> open(T lower, T upper) {
        checkNull(lower, upper);
        if (lower.compareTo(upper) >= 0) {
            throw new IllegalArgumentException();
        }
        return new Range<>(lower, upper, false, false);
    }

    public static <T extends Comparable<T>> Range<T> closed(T lower, T upper) {
        checkNull(lower, upper);
        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException();
        }
        return new Range<>(lower, upper, true, true);
    }

    public static <T extends Comparable<T>> Range<T> openClosed(T lower, T upper) {
        checkNull(lower, upper);
        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException();
        }
        return new Range<>(lower, upper, false, true);
    }

    public static <T extends Comparable<T>> Range<T> closedOpen(T lower, T upper) {
        checkNull(lower, upper);
        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException();
        }
        return new Range<>(lower, upper, true, false);
    }

    public static <T extends Comparable<T>> Range<T> greaterThan(T lower) {
        checkNull(lower);
        return new Range<>(lower, null, false, false);
    }

    public static <T extends Comparable<T>> Range<T> atLeast(T lower) {
        checkNull(lower);
        return new Range<>(lower, null, true, false);
    }

    public static <T extends Comparable<T>> Range<T> lessThan(T upper) {
        checkNull(upper);
        return new Range<>(null, upper, false, false);
    }

    public static <T extends Comparable<T>> Range<T> atMost(T upper) {
        checkNull(upper);
        return new Range<>(null, upper, false, true);
    }

    public static <T extends Comparable<T>> Range<T> all() {
        return new Range<>(null, null, false, false);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "EMPTY";
        }
        StringBuilder sb = new StringBuilder();
        if (lowerInclusive) {
            sb.append('[');
        } else {
            sb.append('(');
        }
        if (lowerEndpoint != null) {
            sb.append(lowerEndpoint);
        } else {
            sb.append("-INF");
        }
        sb.append(", ");
        if (upperEndpoint != null) {
            sb.append(upperEndpoint);
        } else {
            sb.append("INF");
        }
        if (upperInclusive) {
            sb.append(']');
        } else {
            sb.append(')');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Range<?> range = (Range<?>) obj;
        return lowerInclusive == range.lowerInclusive &&
                upperInclusive == range.upperInclusive &&
                Objects.equals(lowerEndpoint, range.lowerEndpoint) &&
                Objects.equals(upperEndpoint, range.upperEndpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerEndpoint, upperEndpoint, lowerInclusive, upperInclusive);
    }

    public boolean contains(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        int compareLower = compareLowerPoint(value, true);
        if (compareLower > 0) {
            return false;
        }
        int compareUpper = compareUpperPoint(value, true);
        if (compareUpper < 0) {
            return false;
        }
        return true;
    }

    public boolean encloses(Range<T> other) {
        if (isEmpty()) {
            return false;
        }
        if (other.isEmpty()) {
            return true;
        }
        int compareLower = compareLowerPoint(other.lowerEndpoint, other.lowerInclusive);
        if (compareLower > 0) {
            return false;
        }
        int compareUpper = compareUpperPoint(other.upperEndpoint, other.upperInclusive);
        if (compareUpper < 0) {
            return false;
        }
        return true;
    }

    public Range<T> intersection(Range<T> connectedRange) {
        if (isEmpty()) {
            return this;
        }
        if (connectedRange.isEmpty()) {
            return connectedRange;
        }
        int compareLowerWithUpper = compareLowerWithUpper(connectedRange.upperEndpoint, connectedRange.upperInclusive);
        if (compareLowerWithUpper > 0) {
            return empty(lowerEndpoint);
        } else if (compareLowerWithUpper == 0) {
            return lowerInclusive ? oneValueRange(lowerEndpoint) : empty(lowerEndpoint);
        }
        int compareUpperWithLower = compareUpperWithLower(connectedRange.lowerEndpoint, connectedRange.lowerInclusive);
        if (compareUpperWithLower < 0) {
            return empty(upperEndpoint);
        } else if (compareUpperWithLower == 0) {
            return upperInclusive ? oneValueRange(upperEndpoint) : empty(upperEndpoint);
        }

        T newLower;
        boolean newLowerInclusive;
        int compareLowerWithLower = compareLowerPoint(connectedRange.lowerEndpoint, connectedRange.lowerInclusive);
        if (compareLowerWithLower >= 0) {
            newLower = lowerEndpoint;
            newLowerInclusive = lowerInclusive;
        } else {
            newLower = connectedRange.lowerEndpoint;
            newLowerInclusive = connectedRange.lowerInclusive;
        }

        T newUpper;
        boolean newUpperInclusive;
        int compareUpperWithUpper = compareUpperPoint(connectedRange.upperEndpoint, connectedRange.upperInclusive);
        if (compareUpperWithUpper <= 0) {
            newUpper = upperEndpoint;
            newUpperInclusive = upperInclusive;
        } else {
            newUpper = connectedRange.upperEndpoint;
            newUpperInclusive = connectedRange.upperInclusive;
        }
        return new Range<>(newLower, newUpper, newLowerInclusive, newUpperInclusive);
    }

    public Range<T> span(Range<T> other) {
        if (other.isEmpty()) {
            return this;
        }
        if (isEmpty()) {
            return other;
        }
        T newLower;
        boolean newLowerInclusive;
        int compareLower = compareLowerPoint(other.lowerEndpoint, other.lowerInclusive);
        if (compareLower <= 0) {
            newLower = lowerEndpoint;
            newLowerInclusive = lowerInclusive;
        } else {
            newLower = other.lowerEndpoint;
            newLowerInclusive = other.lowerInclusive;
        }
        T newUpper;
        boolean newUpperInclusive;
        int compareUpper = compareUpperPoint(other.upperEndpoint, other.upperInclusive);
        if (compareUpper >= 0) {
            newUpper = upperEndpoint;
            newUpperInclusive = upperInclusive;
        } else {
            newUpper = other.upperEndpoint;
            newUpperInclusive = other.upperInclusive;
        }
        return new Range<>(newLower, newUpper, newLowerInclusive, newUpperInclusive);
    }

    private int compareLowerPoint(T compareOne, boolean inclusive) {
        if (lowerEndpoint == null) {
            return compareOne == null ? 0 : -1;
        }
        if (compareOne == null) {
            return 1;
        }
        int compareResult = lowerEndpoint.compareTo(compareOne);
        if (compareResult == 0) {
            if (lowerInclusive) {
                return inclusive ? 0 : -1;
            } else {
                return inclusive ? 1 : 0;
            }
        }
        return compareResult;
    }

    private int compareLowerWithUpper(T compareOne, boolean inclusive) {
        if (lowerEndpoint == null) {
            return -1;
        }
        if (compareOne == null) {
            return -1;
        }
        int compareResult = lowerEndpoint.compareTo(compareOne);
        if (compareResult == 0) {
            if (lowerInclusive) {
                return inclusive ? 0 : -1;
            } else {
                return inclusive ? 1 : 0;
            }
        }
        return compareResult;
    }

    private int compareUpperPoint(T compareOne, boolean inclusive) {
        if (upperEndpoint == null) {
            return compareOne == null ? 0 : 1;
        }
        if (compareOne == null) {
            return -1;
        }
        int compareResult = upperEndpoint.compareTo(compareOne);
        if (compareResult == 0) {
            if (upperInclusive) {
                return inclusive ? 0 : 1;
            } else {
                return inclusive ? -1 : 0;
            }
        }
        return compareResult;
    }

    private int compareUpperWithLower(T compareOne, boolean inclusive) {
        if (upperEndpoint == null) {
            return 1;
        }
        if (compareOne == null) {
            return 1;
        }
        int compareResult = upperEndpoint.compareTo(compareOne);
        if (compareResult == 0) {
            if (upperInclusive) {
                return inclusive ? 0 : 1;
            } else {
                return inclusive ? -1 : 0;
            }
        }
        return compareResult;
    }

    public boolean isEmpty() {
        if (lowerInclusive == upperInclusive) {
            return false;
        }
        if (lowerEndpoint == null || upperEndpoint == null) {
            return false;
        }
        return lowerEndpoint.compareTo(upperEndpoint) == 0;
    }

    private Range<T> empty(T value) {
        return new Range<>(value, value, false, true);
    }

    private Range<T> oneValueRange(T value) {
        return new Range<>(value, value, true, true);
    }
}
