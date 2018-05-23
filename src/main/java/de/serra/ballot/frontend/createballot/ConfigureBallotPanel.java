package de.serra.ballot.frontend.createballot;

import de.serra.ballot.domain.Choice;
import de.serra.ballot.domain.CondorcetBallotImpl;
import de.serra.ballot.domain.ModifiableChoice;
import de.serra.ballot.frontend.BallotService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfigureBallotPanel extends Panel {
	private final FeedbackPanel feedback = new FeedbackPanel("feedback");
	private final IModel<List<ModifiableChoice>> choices;
	private final WebMarkupContainer choicesContainer = new WebMarkupContainer("choicesContainer");
	private final IModel<String> name;
	@SpringBean
	private BallotService ballotService;

	public ConfigureBallotPanel(String id, IModel<String> name, IModel<? extends List<? extends Choice>> choices) {
		super(id);

		this.name = name;
		this.choices = new ListModel<>(choices.getObject().stream()
				.map(c -> ModifiableChoice.create(c.getDisplayValue()))
				.collect(Collectors.toList()));
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(feedback.setOutputMarkupId(true));
		add(new Form<>("form") {
			@Override
			protected void onInitialize() {
				super.onInitialize();
				this.setOutputMarkupId(true);
				add(new AjaxEditableLabel<>("name", name).setRequired(true));
				ConfigureBallotPanel.this.addNewChoiceComponents(this);
			}

			protected void onError() {
				ConfigureBallotPanel.this.onError();
			};
		});
		add(newDone("done"));
		add(choicesContainer.add(new ListView<>("choices", choices) {
			@Override
			protected void populateItem(ListItem<ModifiableChoice> item) {
				item.add(new AjaxEditableLabel<>("choice",
						LambdaModel.of(item.getModel(), Choice::getDisplayValue, ModifiableChoice::setDisplayValue))
								.setRequired(true));
				item.add(new AjaxLink<ModifiableChoice>("deleteChoice", item.getModel()) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						choices.getObject().remove(getModelObject());
						target.add(choicesContainer);
					}
				});
			}
		}).setOutputMarkupId(true));
	}

	protected void addNewChoiceComponents(Form<?> form) {
		var newChoiceModel = Model.of((String) null);
		var newChoice = new TextField<>("newChoice", newChoiceModel).setRequired(true).setOutputMarkupId(true);
		form.add(newChoice);
		form.add(new AjaxFallbackButton("addNewChoice", form) {
			@Override
			protected void onSubmit(Optional<AjaxRequestTarget> target) {
				choices.getObject().add(ModifiableChoice.create(newChoiceModel.getObject()));
				newChoiceModel.setObject(null);
				target.ifPresent(t -> {
					t.add(feedback, this.getForm(), choicesContainer);
					t.focusComponent(newChoice);
				});
			};
		});
	}

	protected AjaxLink<?> newDone(String id) {
		return new AjaxLink<>(id) {
			public void onClick(AjaxRequestTarget target) {
				if (choices.getObject().size() < 2) {
					error(getString("minimumTwoChoicesNeeded"));
					onError();
				} else {
					ConfigureBallotPanel.this.onDone(name, choices);
				}
			};
		};
	};

	protected void onDone(IModel<String> name, IModel<? extends Collection<? extends Choice>> choices) {
		var ballot = new CondorcetBallotImpl(choices.getObject());
		ballotService.createBallot(ballot, name.getObject());
		setResponsePage(getApplication().getHomePage());
	}

	protected void onError() {
		var target = RequestCycle.get().find(AjaxRequestTarget.class);
		target.ifPresent(t -> t.add(feedback));
	}

	@Override
	protected void onDetach() {
		name.detach();
		choices.detach();
		super.onDetach();
	}
}
