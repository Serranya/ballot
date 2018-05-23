package de.serra.ballot.frontend;

import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

public class BallotMockApplication extends MockApplication {

	@Override
	public BallotWebSession newSession(Request request, Response response) {
		return new BallotWebSession(request);
	}
}
