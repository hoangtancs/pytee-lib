package vn.pytee.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {
	public String joinListInt(String delimiter, List<Integer> iList) {
		List<String> sList = iList.stream().map(i -> String.valueOf(i)).collect(Collectors.toList());

		return String.join(delimiter, sList);
	}

	public <T, U> List<U> transform(List<T> tList, Function<T, U> function) {
		// function. eg: String::valueOf
		return tList.stream().map(function).collect(Collectors.toList());
	}
	
	public static void main(String[] args) {
		CollectionUtils l = new CollectionUtils();
		List<Integer> iList = Arrays.asList(1, 2, 3, 4, 5);
		Function<Integer, String> function = (Integer i) -> {
			return String.valueOf(i);
		};
		
		List<String> sList = l.transform(iList, function);
		System.out.println(sList);
	}
}