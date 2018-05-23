package de.serra.ballot.frontend;

import com.google.common.collect.ImmutableCollection;
import de.serra.ballot.domain.CondorcetBallot;

public interface BallotService {
	ImmutableCollection<ImmutableFrontendBallot> getAllActiveBallots();

	void createBallot(CondorcetBallot ballot, String name);
}
