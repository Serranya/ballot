package de.serra.ballot.frontend.createballot;

import de.serra.ballot.domain.Choice;
import de.serra.ballot.frontend.BasePage;
import org.apache.wicket.model.IModel;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@MountPath("/ballots/new")
public class CreateBallotPage extends BasePage<Void> {
	private static final String CONTENT_ID = "content";

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new SelectTemplatePanel(CONTENT_ID) {
			@Override
			protected void onTemplateSelected(IModel<String> name, IModel<List<Choice>> choices) {
				replaceWith(new ConfigureBallotPanel(CONTENT_ID, name, choices));
			}
		});
	}
}
