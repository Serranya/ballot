package de.serra.ballot.domain;

import com.google.common.collect.ImmutableCollection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

public interface CondorcetBallot extends Serializable {
	ImmutableCollection<ImmutableChoice> getChoices();

	default int getAmountOfChoices() {
		Collection<ImmutableChoice> choices = getChoices();
		return choices.size();
	}

	int getNumberOfVotes();

	void vote(CondorcetVote vote);

	CondorcetMatrix getCondorcetMatrix();

	default Optional<ImmutableChoice> getBeatpathWinner() {
		return getCondorcetMatrix().getBeatpathWinner();
	}
}
