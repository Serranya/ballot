package de.serra.ballot.frontend.createballot;

import de.serra.ballot.domain.CondorcetBallot;

public interface BallotCreationService {

	void createBallot(CondorcetBallot ballot, String name);
}
