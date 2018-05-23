package de.serra.ballot.domain.pair;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static de.serra.ballot.domain.pair.PairMatchers.equalsPair;
import static de.serra.ballot.domain.pair.PairMatchers.hasPairs;
import static de.serra.ballot.domain.pair.Pairs.createUniquePairs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PairsTest {

	@Test
	public void handelsNull() {
		var actual = createUniquePairs(null);

		assertTrue("Pairs#createUniquePairs should gracefully handel null", actual.isEmpty());
	}

	@Test
	public void handelsEmpty() {
		var actual = createUniquePairs(Collections.emptyList());

		assertTrue("Pairs#createUniquePairs should gracefully handel empty inputs", actual.isEmpty());
	}

	@Test
	public void handelsNullInCollection() {
		var actual = createUniquePairs(Arrays.asList(null, null));

		assertEquals("Pairs#createUniquePairs should gracefully handel null values", 1, actual.size());
	}

	@Test
	public void createsRightNumberOfPairs() {
		var actual = createUniquePairs(Arrays.asList(null, null));
		assertEquals("should be n over k with n 2 and k 2", 1, actual.size());

		actual = createUniquePairs(Arrays.asList(null, null, null));
		assertEquals("should be n over k with n 3 and k 2", 3, actual.size());

		actual = createUniquePairs(Arrays.asList(null, null, null, null));
		assertEquals("should be n over k with n 4 and k 2", 6, actual.size());

		actual = createUniquePairs(Arrays.asList(null, null, null, null, null));
		assertEquals("should be n over k with n 5 and k 2", 10, actual.size());
	}

	@Test
	public void createsUniquePair() {
		var expected = buildPair(1, 2);
		var actual = createUniquePairs(Arrays.asList(1, 2));

		assertEquals("should be n over k with n 2 and k 2", 1, actual.size());
		assertThat(actual.stream().findAny().get(), equalsPair(expected));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createsThreeUniquePairs() {
		var actual = createUniquePairs(Arrays.asList(1, 2, 3));

		assertEquals("should be n over k with n 2 and k 2", 3, actual.size());
		assertThat(actual, hasPairs(new Pair[] {buildPair(1, 2), buildPair(1, 3), buildPair(2, 3)}));
	}

	private static <F, S> Pair<F, S> buildPair(F first, S second) {
		return ImmutablePair.<F, S>builder().first(first).second(second).build();
	}
}
