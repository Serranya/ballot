package de.serra.ballot.frontend.createballot;

import de.serra.ballot.domain.CondorcetBallot;
import de.serra.ballot.frontend.BallotRepository;
import de.serra.ballot.frontend.domain.ImmutableFrontendBallot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BallotCreationServiceImpl implements BallotCreationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BallotCreationServiceImpl.class);
	private final AtomicLong ballotIds = new AtomicLong(0);
	private final BallotRepository ballotRepository;

	public BallotCreationServiceImpl(BallotRepository ballotRepository) {
		this.ballotRepository = ballotRepository;
	}

	@Override
	public void createBallot(CondorcetBallot ballot, String name) {
		LOGGER.debug("Adding {} to ballots", ballot);
		var oBallot = ImmutableFrontendBallot.builder()
				.ballot(ballot)
				.created(LocalDateTime.now())
				.name(name)
				.id(ballotIds.getAndIncrement())
				.build();
		ballotRepository.add(oBallot);
	}
}
