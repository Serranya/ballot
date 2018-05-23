package de.serra.ballot.frontend.dashboard;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.google.common.collect.ImmutableList;
import de.serra.ballot.frontend.BasePage;
import de.serra.ballot.frontend.createballot.SelectTemplatePage;
import de.serra.ballot.frontend.domain.FrontendBallot;
import de.serra.ballot.frontend.voteballot.VoteBallotPage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.devutils.stateless.StatelessComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WicketHomePage
@MountPath("/ballots")
@StatelessComponent
public class HomePage extends BasePage<Void> {
	@SpringBean
	private DasboardService dasboardService;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		var create = newCreate("createBallot");
		var list = newBallots("ballots");

		add(create, list);
	}

	protected AbstractLink newCreate(String id) {
		return new StatelessLink<Void>(id) {
			@Override
			public void onClick() {
				setResponsePage(SelectTemplatePage.class);

			}
		};
	}

	protected ListView<FrontendBallot> newBallots(String id) {
		return new ListView<FrontendBallot>(id, this::getActiveBallots) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(!ObjectUtils.isEmpty(getModelObject()));
			}

			@Override
			protected void populateItem(ListItem<FrontendBallot> item) {
				var container = new WebMarkupContainer("ballot");
				container.add(new StatelessAjaxEventBehavior("click") {
					@Override
					protected void onEvent(AjaxRequestTarget target) {
						var params = new PageParameters();
						params.add("id", item.getModelObject().getId());
						setResponsePage(VoteBallotPage.class, params);
					}
				});
				item.add(container.setOutputMarkupId(true));
				container.add(new Label("name", LambdaModel.of(item.getModel(), FrontendBallot::getName)));
				container.add(new Label("remaining",
						new StringResourceModel("remaining").setParameters(LambdaModel.of(item.getModel(),
								b -> b.getRemainingTime().toMinutes()))));
				container.add(new Label("createdBefore",
						new StringResourceModel("createdBefore").setParameters(LambdaModel.of(item.getModel(),
								b -> Duration.ofMinutes(ChronoUnit.MINUTES.between(b.getCreated(), LocalDateTime.now()))
										.toMinutes()))));
				item.add(new Label("answers", LambdaModel.of(item.getModel(), ob -> ob.getBallot().getNumberOfVotes())));
			}
		};
	}

	protected List<FrontendBallot> getActiveBallots() {
		return dasboardService.getBallots().stream().filter(b -> !b.hasEnded())
				.collect(ImmutableList.toImmutableList());
	}

	private abstract static class StatelessAjaxEventBehavior extends AjaxEventBehavior {
		StatelessAjaxEventBehavior(String event) {
			super(event);
		}

		/**
		 * @return always {@code true}
		 */
		@Override
		public boolean getStatelessHint(final Component component) {
			return true;
		}
	}
}
