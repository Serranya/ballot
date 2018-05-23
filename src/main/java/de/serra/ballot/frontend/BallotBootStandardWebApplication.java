package de.serra.ballot.frontend;

import com.giffing.wicket.spring.boot.starter.app.WicketBootStandardWebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.springframework.stereotype.Component;

@Component
public class BallotBootStandardWebApplication extends WicketBootStandardWebApplication {

	@Override
	public BallotWebSession newSession(Request request, Response response) {
		return new BallotWebSession(request);
	}
}
