package de.serra.ballot.domain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Map;
import java.util.stream.IntStream;

public final class VoteHelper {

	private VoteHelper() {}

	public static CondorcetVote singleVote(ImmutableChoice choice, Integer rank) {
		return vote(Map.of(choice, rank));
	}

	public static Collection<CondorcetVote> votes(Map<ImmutableChoice, Integer> ranking, int amount) {
		return IntStream.range(0, amount).mapToObj(i -> vote(ranking)).collect(ImmutableList.toImmutableList());
	}

	public static CondorcetVote vote(Map<ImmutableChoice, Integer> ranking) {
		return ImmutableCondorcetVote.builder().candidatePreferences(ImmutableMap.copyOf(ranking)).build();
	}
}
