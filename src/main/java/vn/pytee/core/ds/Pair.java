package vn.pytee.core.ds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Pair<U, V> {
	public final U first;
	public final V second;

	// Constructs a new Pair with specified values
	private Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}

	@Override
	// Checks specified object is "equal to" current object or not
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Pair<?, ?> pair = (Pair<?, ?>) o;

		// call equals() method of the underlying objects
		if (!first.equals(pair.first))
			return false;
		return second.equals(pair.second);
	}

	@Override
	// Computes hash code for an object to support hash tables
	public int hashCode() {
		// use hash codes of the underlying objects
		return 31 * first.hashCode() + second.hashCode();
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	// Factory method for creating a Typed Pair immutable instance
	public static <U, V> Pair<U, V> of(U a, V b) {
		// calls private constructor
		return new Pair<>(a, b);
	}

	public static void main(String[] args) {
		Pair<String, Integer> p1 = Pair.of("John", 26);
		Pair<String, Integer> p2 = Pair.of("Tom", 30);
		Pair<String, Integer> p3 = Pair.of("John", 26);

		List<Pair<String, Integer>> pairs = new ArrayList<>();
		pairs.add(p1);
		pairs.add(p2);
		pairs.add(p3);

		System.out.println(pairs);

		Set<Pair<String, Integer>> distinctPairs = new HashSet<>(pairs);
		System.out.println(distinctPairs);
	}
}