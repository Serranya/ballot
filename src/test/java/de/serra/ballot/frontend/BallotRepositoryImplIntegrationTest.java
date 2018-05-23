package de.serra.ballot.frontend;

import de.serra.ballot.domain.CondorcetBallot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "BallotRepository.fixedDelay=100")
public class BallotRepositoryImplIntegrationTest {

	@Autowired
	private BallotRepositoryImpl repo;

	@Test
	public void repoCleansUpOldBallots() throws InterruptedException {
		var oldDate = LocalDateTime.now().minusDays(1);
		var oldBallot = new FrontendBallot() {
			@Override
			public Long getId() {
				return 1L;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public LocalDateTime getCreated() {
				return oldDate;
			}

			@Override
			public CondorcetBallot getBallot() {
				return null;
			}
		};
		repo.add(oldBallot);

		var newDate = LocalDateTime.now();
		var newBallot = new FrontendBallot() {
			@Override
			public Long getId() {
				return 2L;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public LocalDateTime getCreated() {
				return newDate;
			}

			@Override
			public CondorcetBallot getBallot() {
				return null;
			}
		};
		repo.add(newBallot);

		Thread.sleep(1000L);

		assertTrue("The ballot should still be retained by the repo", repo.findById(2L).isPresent());
		assertFalse("The ballot should have been removed by the repo", repo.findById(1L).isPresent());
	}
}
