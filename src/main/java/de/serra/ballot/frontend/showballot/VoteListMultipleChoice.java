package de.serra.ballot.frontend.showballot;

import de.serra.ballot.frontend.ImmutableFrontendChoice;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;

import java.util.List;

class VoteListMultipleChoice extends ListMultipleChoice<ImmutableFrontendChoice> {
	private final int rank;

	VoteListMultipleChoice(String id,
			IModel<List<ImmutableFrontendChoice>> model,
			IModel<List<ImmutableFrontendChoice>> choices,
			IChoiceRenderer<ImmutableFrontendChoice> renderer, int rank)
	{
		super(id, model, choices, renderer);
		this.rank = rank;
		add(AttributeModifier.append("data-rank", rank));
	}

	public int getRank() {
		return rank;
	}
}
