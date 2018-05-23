package de.serra.ballot.domain.pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class Pairs {

	private Pairs() {
	}

	public static <T> Collection<Pair<T, T>> createUniquePairs(Collection<T> values) {
		if (values == null || values.isEmpty()) {
			return Collections.emptyList();
		}

		var valuesList = new ArrayList<T>(values);

		var ret = new ArrayList<Pair<T, T>>();
		var size = valuesList.size();
		for (int i = 0; i < size; ++i) {
			for (int j = i + 1; j < size; ++j) {
				ret.add(ImmutablePair.of(valuesList.get(i), valuesList.get(j)));
			}
		}

		return ret;
	}
}
