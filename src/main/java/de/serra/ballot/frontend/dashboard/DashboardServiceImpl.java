package de.serra.ballot.frontend.dashboard;

import com.google.common.collect.ImmutableList;
import de.serra.ballot.frontend.BallotRepository;
import de.serra.ballot.frontend.domain.ImmutableFrontendBallot;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DashboardServiceImpl implements DasboardService {
	private final BallotRepository ballotRepository;

	public DashboardServiceImpl(BallotRepository ballotRepository) {
		this.ballotRepository = ballotRepository;
	}

	@Override
	public Collection<ImmutableFrontendBallot> getBallots() {
		return ballotRepository.findAll().stream().map(ImmutableFrontendBallot::copyOf)
				.collect(ImmutableList.toImmutableList());
	}
}
