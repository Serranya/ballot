package de.serra.ballot.frontend.voteballot;

import de.serra.ballot.domain.Choice;
import de.serra.ballot.domain.ImmutableChoice;
import de.serra.ballot.domain.ImmutableCondorcetVote;
import de.serra.ballot.frontend.FrontenedBallotPage;
import de.serra.ballot.frontend.ballotresult.BallotResultPage;
import de.serra.ballot.frontend.domain.ImmutableFrontendChoice;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.collections.MultiMap;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

@MountPath("/ballots/${id}/vote")
public class VoteBallotPage extends FrontenedBallotPage {
	private IModel<MultiMap<Integer, ImmutableFrontendChoice>> voteModel;

	public VoteBallotPage() {
		throw new RestartResponseException(getApplication().getHomePage());
	}

	public VoteBallotPage(PageParameters params) {
		super(params);
		if (getSession().hasVotedForBallot(getModelObject()) || getModelObject().hasEnded()) {
			var pageParams = new PageParameters();
			pageParams.add(ID_PARAM, getModelObject().getId());
			throw new RestartResponseException(BallotResultPage.class, pageParams);
		}
		var votes = new MultiMap<Integer, ImmutableFrontendChoice>();
		int voteRank = 1;
		for (var c : getModelObject().getFrontendChoices()) {
			votes.addValue(voteRank++, c);
		}
		voteModel = Model.of(votes);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Form<MultiMap<Integer, Choice>>("form") {
			@Override
			protected void onInitialize() {
				super.onInitialize();

				add(new VoteBallotFormComponentPanel("votePanel", voteModel));
				add(new SubmitLink("continue"));
			}

			@Override
			protected void onSubmit() {
				var ballot = VoteBallotPage.this.getModelObject();
				if (ballot.hasEnded()) {
					var pageParams = new PageParameters();
					pageParams.add(ID_PARAM, ballot.getId());
					throw new RestartResponseException(BallotResultPage.class, pageParams);
				}
				var vote = voteModel.getObject();
				var prefs = new HashMap<ImmutableChoice, Integer>();
				for (Entry<Integer, List<ImmutableFrontendChoice>> entry : vote.entrySet()) {
					for (ImmutableFrontendChoice c : entry.getValue()) {
						prefs.put(ImmutableChoice.copyOf(c.getChoice()), entry.getKey());
					}
				}
				var cVote = ImmutableCondorcetVote.builder().candidatePreferences(prefs).build();
				ballot.getBallot().vote(cVote);
				var page = VoteBallotPage.this;
				page.getSession().markBallotVoted(ballot);
				var pageParams = new PageParameters();
				pageParams.add(ID_PARAM, ballot.getId());
				setResponsePage(BallotResultPage.class, pageParams);
			}
		});
	}

	@Override
	protected CharSequence getTitle() {
		return getModelObject().getName();
	}

	@Override
	protected void onDetach() {
		voteModel.detach();
		super.onDetach();
	}
}
