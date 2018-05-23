package de.serra.ballot.frontend.createballot;

import com.google.common.collect.ImmutableList;
import de.serra.ballot.domain.Choice;
import de.serra.ballot.domain.ImmutableChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectTemplatePanel extends Panel {
	private final FeedbackPanel feedback = new FeedbackPanel("feedback");

	public SelectTemplatePanel(String id) {
		super(id);
	}

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
				onTemplateSelected(name.getModel(), new ListModel<>(new ArrayList<>(0)));
			}
		});

		add(new Link<Void>("eatingTemplate") {
			ImmutableList<Choice> choices = ImmutableList.of(ImmutableChoice.of("Inder"),
					ImmutableChoice.of("Mauel"), ImmutableChoice.of("Vapiano"), ImmutableChoice.of("Döner links"),
					ImmutableChoice.of("Döner rechts"), ImmutableChoice.of("Thai links"),
					ImmutableChoice.of("Thai rechts"));

			@Override
			public void onClick() {
				onTemplateSelected(Model.of(getString("template.eating.label")), new ListModel<>(choices));
			}
		});
	}

	protected abstract void onTemplateSelected(IModel<String> name, IModel<List<Choice>> choices);
}
