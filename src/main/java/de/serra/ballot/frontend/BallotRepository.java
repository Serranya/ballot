package de.serra.ballot.frontend;

import de.serra.ballot.frontend.domain.FrontendBallot;

import java.util.Collection;
import java.util.Optional;

public interface BallotRepository {
	void add(FrontendBallot ballot);

	Collection<FrontendBallot> findAll();

	Optional<FrontendBallot> findById(Long id);
}
