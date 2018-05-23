package de.serra.ballot.domain.pair;

import org.hamcrest.Matcher;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItems;

public class PairMatchers {
	private PairMatchers() {
	}

	public static <F, S> Matcher<Pair<F, S>> equalsPair(Pair<F, S> expected) {
		return new PairMatcher<F, S>(expected);
	}

	public static <F, S> Matcher<Iterable<Pair<F, S>>> hasPairs(Pair<F, S>[] pairs) {
		@SuppressWarnings("unchecked")
		Matcher<Pair<F, S>>[] array = Arrays.asList(pairs).stream().map(PairMatcher::new).toArray(Matcher[]::new);
		return hasPairs(array);
	}

	public static <F, S> Matcher<Iterable<Pair<F, S>>> hasPairs(Matcher<Pair<F, S>>[] matchers) {
		return hasItems(matchers);
	}
}
