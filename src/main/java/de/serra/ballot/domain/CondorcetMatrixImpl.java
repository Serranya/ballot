package de.serra.ballot.domain;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.serra.ballot.domain.pair.ImmutablePair;
import de.serra.ballot.domain.pair.Pair;
import de.serra.ballot.domain.pair.Pairs;

import java.util.Collection;

public class CondorcetMatrixImpl implements CondorcetMatrix {
	private final ImmutableCollection<ImmutableChoice> choices;
	private final ImmutableMap<Pair<ImmutableChoice, ImmutableChoice>, Integer> comparisons;

	public CondorcetMatrixImpl(Collection<? extends Choice> choices, Iterable<? extends CondorcetVote> votes) {
		this.choices = choices.stream().map(ImmutableChoice::copyOf).collect(ImmutableList.toImmutableList());
		var pairs = Pairs.createUniquePairs(choices);
		var builder = ImmutableMap.<Pair<ImmutableChoice, ImmutableChoice>, Integer>builder();

		for (var pair : pairs) {
			var comparison = compare(pair, votes);
			var first = ImmutableChoice.copyOf(pair.getFirst());
			var second = ImmutableChoice.copyOf(pair.getSecond());
			builder.put(ImmutablePair.of(first, second), comparison.getFirst());
			builder.put(ImmutablePair.of(second, first), comparison.getSecond());
		}
		comparisons = builder.build();
	}

	private Pair<Integer, Integer> compare(Pair<? extends Choice, ? extends Choice> pair,
			Iterable<? extends CondorcetVote> votes)
	{
		var first = pair.getFirst();
		var second = pair.getSecond();

		var firstWins = 0;
		var secondWins = 0;

		for (var v : votes) {
			var firstRank = v.getCandidatePreferences().getOrDefault(first, Integer.MIN_VALUE);
			var secondRank = v.getCandidatePreferences().getOrDefault(second, Integer.MIN_VALUE);
			if (firstRank < secondRank) {
				firstWins++;
			} else if (firstRank > secondRank) {
				secondWins++;
			}
		}
		return ImmutablePair.of(firstWins, secondWins);
	}

	@Override
	public ImmutableCollection<ImmutableChoice> getChoices() {
		return choices;
	}

	@Override
	public ImmutableMap<Pair<ImmutableChoice, ImmutableChoice>, Integer> getComparisons() {
		return comparisons;
	}
}
