package de.serra.ballot.frontend;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BallotRepositoryImpl implements BallotRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(BallotRepositoryImpl.class);
	private final ConcurrentHashMap<Long, FrontendBallot> ballots = new ConcurrentHashMap<>();

	@Override
	public void add(FrontendBallot ballot) {
		ballots.put(ballot.getId(), ballot);
	}

	@Override
	public Collection<FrontendBallot> findAll() {
		return ImmutableList.copyOf(ballots.values());
	}

	@Override
	public Optional<FrontendBallot> findById(Long id) {
		return Optional.ofNullable(ballots.get(id));
	}

	@Scheduled(fixedDelayString = "${BallotRepository.fixedDelay:30000}")
	private void cleanupBallots() {
		LOGGER.trace("Running cleanupJob");
		for (var entry : ballots.entrySet()) {
			var ballot = entry.getValue();
			if (ballot.hasEnded()) {
				LOGGER.info("Removing {}", ballot);
				ballots.remove(entry.getKey());
			}
		}
	}
}
