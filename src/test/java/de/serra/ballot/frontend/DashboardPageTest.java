package de.serra.ballot.frontend;

import com.google.common.collect.ImmutableList;
import de.serra.ballot.domain.CondorcetBallotImpl;
import de.serra.ballot.domain.ImmutableChoice;
import de.serra.ballot.frontend.dashboard.DashboardPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class DashboardPageTest {
	private WicketTester tester;
	private List<FrontendBallot> testBallots;

	@Before
	public void setUp() {
		tester = new WicketTester(new BallotMockApplication());
		var choice1 = ImmutableChoice.builder().displayValue("Choice1").build();
		var choice2 = ImmutableChoice.builder().displayValue("Choice1").build();
		var choices = ImmutableList.of(choice1, choice2);
		var ballot1 = ImmutableFrontendBallot.builder()
				.created(LocalDateTime.now())
				.id(1L)
				.name("TestBallot1")
				.ballot(new CondorcetBallotImpl(choices))
				.build();
		var ballot2 = ImmutableFrontendBallot.builder()
				.created(LocalDateTime.now())
				.id(2L)
				.name("TestBallot2")
				.ballot(new CondorcetBallotImpl(choices))
				.build();
		testBallots = ImmutableList.of(ballot1, ballot2);
	}

	@Test
	public void pageRendersSuccessfully() {
		tester.startPage(new DashboardPage() {
			@Override
			protected List<FrontendBallot> getActiveBallots() {
				return Collections.emptyList();
			}
		});
		tester.assertRenderedPage(DashboardPage.class);
	}

	@Test
	public void pageRendersWithBallotsSuccessfully() {
		tester.startPage(new DashboardPage() {
			@Override
			protected List<FrontendBallot> getActiveBallots() {
				return testBallots;
			}
		});

		tester.assertRenderedPage(DashboardPage.class);
	}
}
