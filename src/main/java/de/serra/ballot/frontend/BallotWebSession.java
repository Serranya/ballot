package de.serra.ballot.frontend;

import de.serra.ballot.frontend.domain.FrontendBallot;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class BallotWebSession extends WebSession {
	private final Set<FrontendBallot> votedBallots = Collections.newSetFromMap(new WeakHashMap<>());

	public BallotWebSession(Request request) {
		super(request);
	}

	public static BallotWebSession get() {
		return (BallotWebSession) WebSession.get();
	}

	public void markBallotVoted(FrontendBallot ballot) {
		votedBallots.add(ballot);
	}

	public boolean hasVotedForBallot(FrontendBallot ballot) {
		return votedBallots.contains(ballot);
	}
}
