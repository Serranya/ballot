package de.serra.ballot.domain;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;

public class CondorcetBallotImpl implements CondorcetBallot {
	private final ImmutableCollection<ImmutableChoice> choices;
	private final Collection<ImmutableCondorcetVote> votes = new ArrayList<>();

	/**
	 * @param choices
	 *            The different choices
	 * @throws IllegalArgumentException
	 *             <ul>
	 *             <li>when {@code choices} are null</li>
	 *             <li>when {@code choices} contains {@code null}</li>
	 *             <li>when {@code choices} contains less than 2 elements</li>
	 *             </ul>
	 */
	public CondorcetBallotImpl(Collection<? extends Choice> choices) {
		try {
			this.choices = choices.stream().map(ImmutableChoice::copyOf).collect(ImmutableList.toImmutableList());
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("choices must be neither null nor contain nulls", e);
		}

		if (this.choices.size() < 2) {
			throw new IllegalArgumentException(
					"choices has less than 2 values. You need to provide at least two choices");
		}
	}

	@Override
	public ImmutableCollection<ImmutableChoice> getChoices() {
		return choices;
	}

	/**
	 * @throws RankingDoesNotMatchChoicesException
	 *             when the rank of a choice is &lt; or bigger than the available
	 *             choices
	 * @throws IllegalArgumentException
	 *             when vote is {@code null}
	 */
	@Override
	public void vote(CondorcetVote vote) {
		ImmutableCondorcetVote immutableVote;
		try {
			immutableVote = ImmutableCondorcetVote.copyOf(vote);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("vote must be non null", e);
		}

		immutableVote.getCandidatePreferences().values().stream().forEach(i -> {
			if (i < 1 || i > getAmountOfChoices()) {
				throw new RankingDoesNotMatchChoicesException();
			}
		});

		votes.add(immutableVote);
	}

	@Override
	public CondorcetMatrix getCondorcetMatrix() {
		return new CondorcetMatrixImpl(choices, votes);
	}

	@Override
	public int getNumberOfVotes() {
		return votes.size();
	}
}
