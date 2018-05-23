package de.serra.ballot.frontend.dashboard;

import de.serra.ballot.frontend.domain.ImmutableFrontendBallot;

import java.util.Collection;

public interface DasboardService {

	Collection<ImmutableFrontendBallot> getBallots();
}
