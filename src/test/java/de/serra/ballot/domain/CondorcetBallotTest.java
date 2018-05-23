package de.serra.ballot.domain;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CondorcetBallotTest {
	private static final ImmutableChoice A = ImmutableChoice.of("a");
	private static final ImmutableChoice B = ImmutableChoice.of("b");
	private static final ImmutableChoice C = ImmutableChoice.of("C");

	@Test
	public void ballotThrowsWhenNullChoices() {
		try {
			new CondorcetBallotImpl(null);
			fail("Ballot#new did NOT throw");
		} catch (IllegalArgumentException expected) {}
	}

	@Test
	public void ballotThrowsWhenChoicesContainNull() {
		try {
			new CondorcetBallotImpl(Arrays.asList(A, null, B));
			fail("Ballot#new did NOT throw");
		} catch (IllegalArgumentException expected) {}
	}

	@Test
	public void ballotThrowsWhenChoicesAreLessThanTwo() {
		try {
			new CondorcetBallotImpl(Collections.emptyList());
			fail("Ballot#new did NOT throw");
		} catch (IllegalArgumentException expected) {}
		try {
			new CondorcetBallotImpl(Arrays.asList(A));
			fail("Ballot#new did NOT throw");
		} catch (IllegalArgumentException expected) {}
		new CondorcetBallotImpl(Arrays.asList(A,B));
	}

	@Test
	public void ballotReturnsAllChoices() {
		var ballot = new CondorcetBallotImpl(Arrays.asList(A, B, C));

		assertThat("Ballot#getChoices did not return inserted choices", ballot.getChoices(), hasItems(A, B, C));
	}

	@Test
	public void ballotThrowsOnNullVote() {
		var ballot = new CondorcetBallotImpl(Arrays.asList(A, B));

		try {
			ballot.vote(null);
			fail("Ballot#vote did NOT throw");
		} catch (IllegalArgumentException expected) {}
	}

	@Test
	public void ballotThrowsOnRankingLowerOne() {
		var ballot = new CondorcetBallotImpl(Arrays.asList(A, B));

		try {
			ballot.vote(VoteHelper.singleVote(A, 0));
			fail("Ballot#vote did NOT throw");
		} catch (RankingDoesNotMatchChoicesException expected) {}

		try {
			ballot.vote(VoteHelper.singleVote(A, -1));
			fail("Ballot#vote did NOT throw");
		} catch (RankingDoesNotMatchChoicesException expected) {}
	}

	@Test
	public void ballotThrowsOnRankingGreaterThanChoices() {
		var ballot = new CondorcetBallotImpl(Arrays.asList(A, B, C));

		ballot.vote(VoteHelper.singleVote(A, 3));

		try {
			ballot.vote(ImmutableCondorcetVote.builder().candidatePreferences(ImmutableMap.of(A, 4)).build());
			fail("Ballot#vote did NOT throw");
		} catch (RankingDoesNotMatchChoicesException expected) {}
	}
}
