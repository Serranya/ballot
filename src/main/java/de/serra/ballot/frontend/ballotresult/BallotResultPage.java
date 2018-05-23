package de.serra.ballot.frontend.ballotresult;

import com.google.common.collect.ImmutableList;
import de.serra.ballot.domain.Choice;
import de.serra.ballot.domain.ImmutableChoice;
import de.serra.ballot.domain.pair.ImmutablePair;
import de.serra.ballot.domain.pair.Pair;
import de.serra.ballot.frontend.FrontenedBallotPage;
import de.serra.ballot.frontend.domain.FrontendBallot;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@MountPath("/ballots/${id}")
public class BallotResultPage extends FrontenedBallotPage {
	private WebMarkupContainer statsForNerds;

	public BallotResultPage() {
		throw new RestartResponseException(getApplication().getHomePage());
	}

	public BallotResultPage(PageParameters params) {
		super(params);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Label("header", LambdaModel.of(getModel(), FrontendBallot::getName)));
		add(newWinners("winners"));

		add(new AjaxLink<Void>("showStatsForNerds") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				statsForNerds.setVisible(!statsForNerds.isVisible());
				target.add(statsForNerds);
			}
		});
		var comparisonsModel = new LoadableDetachableModel<Map<Pair<ImmutableChoice, ImmutableChoice>, Integer>>() {
			@Override
			protected Map<Pair<ImmutableChoice, ImmutableChoice>, Integer> load() {
				var m = BallotResultPage.this.getModelObject().getBallot().getCondorcetMatrix();
				return m.getComparisons();
			}
		};
		var strongestPathModel = new LoadableDetachableModel<Map<Pair<ImmutableChoice, ImmutableChoice>, Integer>>() {
			@Override
			protected Map<Pair<ImmutableChoice, ImmutableChoice>, Integer> load() {
				var m = BallotResultPage.this.getModelObject().getBallot().getCondorcetMatrix();
				return m.getStrongestPaths();
			}
		};
		add(statsForNerds = new WebMarkupContainer("statsForNerds"));
		statsForNerds.setVisible(false).setOutputMarkupPlaceholderTag(true);
		statsForNerds.add(newMatrix("preferenceMatrix", comparisonsModel, new ResourceModel("pairwise_preferences")));
		statsForNerds
				.add(newMatrix("strongestPathMatrix", strongestPathModel, new ResourceModel("pairwise_strongest_path")));
	}

	protected WebMarkupContainer newWinners(String markupId) {
		return new ListView<List<ImmutableChoice>>("winners",
				LambdaModel.of(getModel(),
						fb -> fb.getBallot().getCondorcetMatrix().getWinnerOrdering().entrySet().stream()
								.sorted((e1, e2) -> Integer.compare(e2.getKey(), e1.getKey())).map(Entry::getValue)
								.collect(ImmutableList.toImmutableList()))) {

			@Override
			protected void populateItem(ListItem<List<ImmutableChoice>> item) {
				var repeater = new RepeatingView("winner");
				item.add(repeater);

				for (var c : item.getModelObject()) {
					repeater.add(new Label(repeater.newChildId(), c.getDisplayValue()));
				}
			}
		};
	}

	protected Component newMatrix(String id, IModel<Map<Pair<ImmutableChoice, ImmutableChoice>, Integer>> model,
			IModel<String> caption)
	{
		return new MatrixTablePanel<Choice>(id,
				LambdaModel.of(getModel(), b -> ImmutableList.copyOf(b.getBallot().getChoices()))) {
			@Override
			protected Component newTableCaption(String id) {
				return new Label(id, caption);
			}

			@Override
			protected void populateCell(String id, WebMarkupContainer cell, IModel<Choice> rowModel,
					IModel<Choice> columnModel)
			{
				var content = new Fragment(id, "cellContent", BallotResultPage.this);
				cell.add(content);

				var rowChoice = rowModel.getObject();
				var columnChoice = columnModel.getObject();
				var isSameChoice = rowChoice.equals(columnChoice);
				var map = model.getObject();
				var rowWins = map.get(ImmutablePair.of(rowChoice, columnChoice));
				var columnWins = map.get(ImmutablePair.of(columnChoice, rowChoice));

				content.add(new Label("columnVotes", () -> "A: " + rowWins).setVisible(!isSameChoice));
				content.add(new Label("rowVotes", () -> "B: " + columnWins).setVisible(!isSameChoice));
				if (!isSameChoice) {
					var winner = rowWins > columnWins ? "a" : rowWins < columnWins ? "b" : "";
					cell.add(AttributeModifier.replace("data-winner", winner));
				}
			}

			@Override
			protected IModel<?> getLabelForItem(IModel<Choice> item) {
				return LambdaModel.of(item, Choice::getDisplayValue);
			}

			@Override
			protected void onDetach() {
				model.detach();
				super.onDetach();
			}
		};
	}

	@Override
	protected CharSequence getTitle() {
		return getModelObject().getName();
	}
}
