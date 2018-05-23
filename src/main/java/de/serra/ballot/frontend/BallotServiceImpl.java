package de.serra.ballot.frontend;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import de.serra.ballot.domain.CondorcetBallot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BallotServiceImpl implements BallotService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BallotServiceImpl.class);
	private final AtomicLong ballotIds = new AtomicLong(0);
	private final BallotRepository ballotRepository;

	public BallotServiceImpl(BallotRepository ballotRepository) {
		this.ballotRepository = ballotRepository;
	}

	@Override
	public ImmutableCollection<ImmutableFrontendBallot> getAllActiveBallots() {
		return ballotRepository.findAll().stream().filter(fb -> !fb.hasEnded()).map(ImmutableFrontendBallot::copyOf)
				.collect(ImmutableList.toImmutableList());
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
