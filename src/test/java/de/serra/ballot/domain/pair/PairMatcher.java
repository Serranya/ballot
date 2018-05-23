package de.serra.ballot.domain.pair;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Objects;

public class PairMatcher<F, S> extends TypeSafeDiagnosingMatcher<Pair<F, S>> {
	private final Pair<F, S> pair;

	public PairMatcher(Pair<F, S> pair) {
		this.pair = pair;
	}

	@Override
	public void describeTo(Description description) {}

	@Override
	protected boolean matchesSafely(Pair<F, S> item, Description mismatchDescription) {
		if (Objects.equals(item.getFirst(), pair.getFirst()) && Objects.equals(item.getSecond(), pair.getSecond())) {
			return true;
		}
		if (Objects.equals(item.getFirst(), pair.getSecond()) && Objects.equals(item.getSecond(), pair.getFirst())) {
			return true;
		}
		return false;
	}
}
