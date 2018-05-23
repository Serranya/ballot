package de.serra.ballot.frontend.showballot;

import de.serra.ballot.frontend.ImmutableFrontendChoice;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.collections.MultiMap;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.util.List;
import java.util.Map.Entry;

/**
 * Use multimple hidden select tags with multi selection to track votes.
 */
public class VoteBallotFormComponentPanel extends FormComponentPanel<MultiMap<Integer, ImmutableFrontendChoice>> {
	private IModel<List<ImmutableFrontendChoice>> allChoicesModel;
	private WebMarkupContainer selectsContainer;
	private Component voteList;
	// private WebMarkupContainer container;

	public VoteBallotFormComponentPanel(String id) {
		this(id, Model.of(new MultiMap<>()));
	}

	public VoteBallotFormComponentPanel(String id, IModel<MultiMap<Integer, ImmutableFrontendChoice>> model) {
		super(id, model);
		allChoicesModel = new AllChoicesReadOnlyModel(this::getModel);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(voteList = new WebMarkupContainer("voteList").setOutputMarkupId(true));
		add(selectsContainer = new WebMarkupContainer("selectsContainer"));
		selectsContainer.setOutputMarkupId(true);
		selectsContainer.add(newSelectList("selects"));
	}

	protected Component newSelectList(String id) {
		var list = new RepeatingView(id);
		var modelObject = getModelObject();
		if (modelObject != null) {
			for (Entry<Integer, List<ImmutableFrontendChoice>> entries : modelObject.entrySet()) {
				var select = new VoteListMultipleChoice(list.newChildId(),
						new MapValueByKeyModel<>(modelObject, entries.getKey()), allChoicesModel,
						new ChoiceRenderer<>("choice.displayValue", "id"), entries.getKey());
				list.add(select);
			}
		}
		return list;
	}

	@Override
	public void convertInput() {
		super.convertInput();
		var votes = new MultiMap<Integer, ImmutableFrontendChoice>();
		selectsContainer.visitChildren(VoteListMultipleChoice.class, new IVisitor<VoteListMultipleChoice, Void>() {

			@Override
			public void component(VoteListMultipleChoice component, IVisit<Void> visit) {
				var choices = component.getConvertedInput();
				var rank = component.getRank();
				if (choices != null && !choices.isEmpty()) {
					for (ImmutableFrontendChoice c : choices) {
						votes.addValue(rank, c);
					}
				}
			}
		});

		setConvertedInput(votes);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(VoteBallotFormComponentPanel.class, "dragList.js")));
		String voteListId = voteList.getMarkupId();
		response.render(OnLoadHeaderItem.forScript(
				String.format("DragAndDrop.initDragList('%s', '%s')", selectsContainer.getMarkupId(), voteListId)));
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		allChoicesModel.detach();
	}
}
