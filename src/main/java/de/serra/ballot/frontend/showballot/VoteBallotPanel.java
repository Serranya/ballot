package de.serra.ballot.frontend.showballot;

import de.serra.ballot.domain.Choice;
import de.serra.ballot.domain.CondorcetVote;
import de.serra.ballot.domain.ImmutableChoice;
import de.serra.ballot.domain.ImmutableCondorcetVote;
import de.serra.ballot.frontend.BaseGenericPanel;
import de.serra.ballot.frontend.FrontendBallot;
import de.serra.ballot.frontend.ImmutableFrontendChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.collections.MultiMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public abstract class VoteBallotPanel extends BaseGenericPanel<FrontendBallot> {
	private IModel<MultiMap<Integer, ImmutableFrontendChoice>> voteModel;

	public VoteBallotPanel(String id, IModel<FrontendBallot> model) {
		super(id, model);

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
				var prefs = new HashMap<ImmutableChoice, Integer>();
				for (Entry<Integer, List<ImmutableFrontendChoice>> entry : voteModel.getObject().entrySet()) {
					for (ImmutableFrontendChoice c : entry.getValue()) {
						prefs.put(ImmutableChoice.copyOf(c.getChoice()), entry.getKey());
					}
				}
				var vote = ImmutableCondorcetVote.builder().candidatePreferences(prefs).build();
				onVote(Model.of(vote));
			}
		});
	}

	protected abstract void onVote(IModel<CondorcetVote> voteModel);

	@Override
	protected void onDetach() {
		voteModel.detach();
		super.onDetach();
	}
}
