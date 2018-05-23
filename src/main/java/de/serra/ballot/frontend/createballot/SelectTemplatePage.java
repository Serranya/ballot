package de.serra.ballot.frontend.createballot;

import com.google.common.collect.ImmutableList;
import de.serra.ballot.domain.ImmutableChoice;
import de.serra.ballot.frontend.BasePage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/ballots/new/selectTemplate")
public class SelectTemplatePage extends BasePage<Void> {
	private final FeedbackPanel feedback = new FeedbackPanel("feedback");

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(feedback);
		add(new Form<Void>("nameForm") {
			private final TextField<String> name = new TextField<>("name", Model.of((String) null));

			@Override
			protected void onInitialize() {
				super.onInitialize();
				add(name.setRequired(true));
			}

			@Override
			protected void onSubmit() {
				setResponsePage(new CreateBallotPage(name.getModel()));
			}
		});

		add(new Link<Void>("eatingTemplate") {
			ImmutableList<ImmutableChoice> choices = ImmutableList.of(ImmutableChoice.of("Inder"),
					ImmutableChoice.of("Mauel"), ImmutableChoice.of("Vapiano"), ImmutableChoice.of("Döner links"),
					ImmutableChoice.of("Döner rechts"), ImmutableChoice.of("Thai links"),
					ImmutableChoice.of("Thai rechts"));

			@Override
			public void onClick() {
				setResponsePage(
						new CreateBallotPage(Model.of(getString("template.eating.label")), new ListModel<>(choices)));
			}
		});
	}
}
