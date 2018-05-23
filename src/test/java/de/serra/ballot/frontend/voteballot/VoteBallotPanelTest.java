package de.serra.ballot.frontend.voteballot;

import de.serra.ballot.domain.Choice;
import de.serra.ballot.domain.CondorcetBallot;
import de.serra.ballot.domain.CondorcetBallotImpl;
import de.serra.ballot.domain.CondorcetVote;
import de.serra.ballot.domain.ModifiableChoice;
import de.serra.ballot.frontend.BallotMockApplication;
import de.serra.ballot.frontend.FrontendBallot;
import de.serra.ballot.frontend.ImmutableFrontendBallot;
import de.serra.ballot.frontend.showballot.VoteBallotPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

@RunWith(SpringRunner.class)
public class VoteBallotPanelTest {
	private WicketTester tester;
	private Choice c1;
	private Choice c2;
	private Choice c3;
	private Choice c4;
	private Choice c5;
	private CondorcetBallot b1;
	private FrontendBallot fb1;

	@Before
	public void setUp() {
		tester = new WicketTester(new BallotMockApplication());
		c1 = ModifiableChoice.create("Choice1");
		c2 = ModifiableChoice.create("Choice2");
		c3 = ModifiableChoice.create("Choice3");
		c4 = ModifiableChoice.create("Choice4");
		c5 = ModifiableChoice.create("Choice5");
		b1 = new CondorcetBallotImpl(Arrays.asList(c1, c2, c3, c4, c5));
		fb1 = ImmutableFrontendBallot.builder()
				.ballot(b1)
				.created(LocalDateTime.now())
				.id(1L)
				.name("TestBallot1")
				.build();
	}

	@Test
	public void pageRendersSuccessfully() {
		tester.startComponentInPage(new VoteBallotPanel("id", Model.of(fb1)) {
			@Override
			protected void onVote(IModel<CondorcetVote> voteModel) {
			}
		});
	}
}
