package de.serra.ballot.domain;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.serra.ballot.domain.pair.ImmutablePair;
import de.serra.ballot.domain.pair.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class CondorcetMatrixImplTest {
	private static final ImmutableChoice A = ImmutableChoice.of("a");
	private static final ImmutableChoice B = ImmutableChoice.of("b");
	private static final ImmutableChoice C = ImmutableChoice.of("c");
	private static final ImmutableChoice D = ImmutableChoice.of("d");
	private static final ImmutableChoice E = ImmutableChoice.of("e");
	private static final ImmutableCollection<ImmutableChoice> CHOICES_3 = ImmutableList.of(A, B, C);
	private static final ImmutableCollection<ImmutableChoice> CHOICES_4 = ImmutableList.of(A, B, C, D);
	private static final ImmutableCollection<ImmutableChoice> CHOICES_5 = ImmutableList.of(A, B, C, D, E);

	@Test
	public void ballotDeterminsCorrectWinner1() {
		var votes = new ArrayList<CondorcetVote>();
		for (var v : VoteHelper.votes(Map.of(A, 1, B, 1, C, 2), 60)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(C, 1, B, 2, A, 3), 39)) {
			votes.add(v);
		}
		votes.add(VoteHelper.vote(Map.of(A, 1, C, 2, B, 3)));

		var m = new CondorcetMatrixImpl(CHOICES_3, votes);

		assertEquals("Expected B to win", Optional.of(B), m.getBeatpathWinner());
	}

	@Test
	public void ballotDeterminsCorrectWinner2() {
		var votes = new ArrayList<CondorcetVote>();
		for (var v : VoteHelper.votes(Map.of(A, 1, C, 2, B, 3, E, 4, D, 5), 5)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(A, 1, D, 2, E, 3, C, 4, B, 5), 5)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(B, 1, E, 2, D, 3, A, 4, C, 5), 8)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(C, 1, A, 2, B, 3, E, 4, D, 5), 3)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(C, 1, A, 2, E, 3, B, 4, D, 5), 7)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(C, 1, B, 2, A, 3, D, 4, E, 5), 2)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(D, 1, C, 2, E, 3, B, 4, A, 5), 7)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(E, 1, B, 2, A, 3, D, 4, C, 5), 8)) {
			votes.add(v);
		}

		var m = new CondorcetMatrixImpl(CHOICES_5, votes);

		assertEquals("Expected E to win", Optional.of(E), m.getBeatpathWinner());
	}

	// The examples from:
	// Markus Schulze, “A new monotonic, clone-independent, reversal symmetric, and
	// Condorcet-consistent single -winner election method”
	// http://m-schulze.9mail.de/schulze1.pdf
	@Test
	public void ballotDeterminsCorrectWinnerSchulzeExample1() {
		var votes = new ArrayList<CondorcetVote>();

		for (var v : VoteHelper.votes(Map.of(A, 1, C, 2, D, 3, B, 4), 8)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(B, 1, A, 2, D, 3, C, 4), 2)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(C, 1, D, 2, B, 3, A, 4), 4)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(D, 1, B, 2, A, 3, C, 4), 4)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(D, 1, C, 2, B, 3, A, 4), 3)) {
			votes.add(v);
		}

		var m = new CondorcetMatrixImpl(CHOICES_4, votes);

		var comparisons = m.getComparisons();
		assertEquals("Size should be a matrix without Choices compared to themself",
				CHOICES_4.size() * (CHOICES_4.size() - 1), comparisons.size());
		var comparisonsBuilder = ImmutableMap.<Pair<ImmutableChoice, ImmutableChoice>, Integer>builder();
		comparisonsBuilder.put(ImmutablePair.of(A, B), 8);
		comparisonsBuilder.put(ImmutablePair.of(A, C), 14);
		comparisonsBuilder.put(ImmutablePair.of(A, D), 10);
		comparisonsBuilder.put(ImmutablePair.of(B, A), 13);
		comparisonsBuilder.put(ImmutablePair.of(B, C), 6);
		comparisonsBuilder.put(ImmutablePair.of(B, D), 2);
		comparisonsBuilder.put(ImmutablePair.of(C, A), 7);
		comparisonsBuilder.put(ImmutablePair.of(C, B), 15);
		comparisonsBuilder.put(ImmutablePair.of(C, D), 12);
		comparisonsBuilder.put(ImmutablePair.of(D, A), 11);
		comparisonsBuilder.put(ImmutablePair.of(D, B), 19);
		comparisonsBuilder.put(ImmutablePair.of(D, C), 9);
		assertEquals("comparisons contains wrong entries ", comparisonsBuilder.build(), comparisons);
		var pathStrengths = m.getStrongestPaths();
		var pathStrengthsBuilder = ImmutableMap.<Pair<ImmutableChoice, ImmutableChoice>, Integer>builder();
		pathStrengthsBuilder.put(ImmutablePair.of(A, B), 14);
		pathStrengthsBuilder.put(ImmutablePair.of(A, C), 14);
		pathStrengthsBuilder.put(ImmutablePair.of(A, D), 12);
		pathStrengthsBuilder.put(ImmutablePair.of(B, A), 13);
		pathStrengthsBuilder.put(ImmutablePair.of(B, C), 13);
		pathStrengthsBuilder.put(ImmutablePair.of(B, D), 12);
		pathStrengthsBuilder.put(ImmutablePair.of(C, A), 13);
		pathStrengthsBuilder.put(ImmutablePair.of(C, B), 15);
		pathStrengthsBuilder.put(ImmutablePair.of(C, D), 12);
		pathStrengthsBuilder.put(ImmutablePair.of(D, A), 13);
		pathStrengthsBuilder.put(ImmutablePair.of(D, B), 19);
		pathStrengthsBuilder.put(ImmutablePair.of(D, C), 13);
		assertEquals("Calculated wrong strengths for strongestPaths", pathStrengthsBuilder.build(), pathStrengths);
		assertEquals("Expected d to win", Optional.of(D), m.getBeatpathWinner());
	}

	@Test
	public void ballotDeterminsCorrectWinnerSchulzeExample2() {
		var votes = new ArrayList<CondorcetVote>();

		for (var v : VoteHelper.votes(Map.of(A, 1, B, 2, C, 3, D, 4), 3)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(C, 1, B, 2, D, 3, A, 4), 2)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(D, 1, A, 2, B, 3, C, 4), 2)) {
			votes.add(v);
		}
		for (var v : VoteHelper.votes(Map.of(D, 1, A, 2, B, 3, C, 4), 2)) {
			votes.add(v);
		}

		var m = new CondorcetMatrixImpl(CHOICES_4, votes);

		assertEquals("Expected d to win", Optional.of(D), m.getBeatpathWinner());
	}

	@Test
	public void ballotHandlesAllSameVote() {
		var m = new CondorcetMatrixImpl(CHOICES_5, Arrays.asList(VoteHelper.vote(Map.of(A, 1, B, 1, C, 1, D, 1, E, 1))));

		var o = m.getWinnerOrdering();
		var expected = ImmutableMap.of(1, CHOICES_5);

		assertEquals("All Choices should get the first place.", expected, o);
	}
}
