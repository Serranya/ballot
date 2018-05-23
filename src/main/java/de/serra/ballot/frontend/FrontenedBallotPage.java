package de.serra.ballot.frontend;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;

import java.util.Optional;

public class FrontenedBallotPage extends BasePage<FrontendBallot> {
	public static final String ID_PARAM = "id";
	@SpringBean
	private BallotRepository ballotRepository;

	public FrontenedBallotPage() {
		throw new RestartResponseException(getApplication().getHomePage());
	}

	public FrontenedBallotPage(PageParameters params) {
		var ballot = getBallotSafe(params);
		ballot.ifPresentOrElse(ob -> setDefaultModel(new Model<FrontendBallot>(ob)),
				() -> {throw new RestartResponseException(getApplication().getHomePage());});
	}

	protected FrontenedBallotPage(IModel<FrontendBallot> model) {
		super(model);
	}

	protected Optional<FrontendBallot> getBallotSafe(PageParameters params) {
		if (params == null) {
			return Optional.empty();
		}
		var id = params.get(ID_PARAM);
		if (id == null) {
			return Optional.empty();
		}
		try {
			var longId = id.toLongObject();
			return ballotRepository.findById(longId);
		} catch (StringValueConversionException e) {
			return Optional.empty();
		}
	}
}
