package de.serra.ballot.frontend.showballot;

import de.serra.ballot.domain.CondorcetVote;
import de.serra.ballot.frontend.FrontendBallot;
import de.serra.ballot.frontend.FrontenedBallotPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/ballots/${id}")
public class ShowBallotPage extends FrontenedBallotPage {
	public static final String ID_PARAM = "id";
	private static final String CONTENT_ID = "content";

	public ShowBallotPage() {
		super();
	}

	public ShowBallotPage(PageParameters params) {
		super(params);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Label("header", LambdaModel.of(getModel(), FrontendBallot::getName)));
		add(new VoteBallotPanel("content", getModel()) {
			@Override
			protected void onBeforeRender() {
				if (getModelObject().hasEnded()) {
					replaceWith(new BallotResultPanel(CONTENT_ID, getModel()));
				}
				super.onBeforeRender();
			}

			@Override
			protected void onVote(IModel<CondorcetVote> voteModel) {
				var ballot = getModelObject();
				if (ballot.hasEnded()) {
					return;
				}
				ballot.getBallot().vote(voteModel.getObject());
				replaceWith(new BallotResultPanel(CONTENT_ID, getModel()));
			}
		});
	}

	protected boolean canVote(IModel<FrontendBallot> ballotModel) {
		var ballot = ballotModel.getObject();
		return !(getSession().hasVotedForBallot(ballot) || ballot.hasEnded());
	}

	@Override
	protected CharSequence getTitle() {
		return getModelObject().getName();
	}
}
