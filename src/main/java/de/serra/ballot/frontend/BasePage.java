package de.serra.ballot.frontend;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Collections;

public abstract class BasePage<T> extends GenericWebPage<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);

	protected BasePage() {
		super();
	}

	protected BasePage(final IModel<T> model) {
		super(model);
	}

	protected BasePage(final PageParameters parameters) {
		super(parameters);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(new TitleHeaderItem(getTitle()));
		response.render(CssHeaderItem.forReference(new CssResourceReference(BasePage.class, "style.css")));
	}

	protected CharSequence getTitle() {
		return getString("title");
	}

	@Override
	public BallotWebSession getSession() {
		return BallotWebSession.get();
	}

	private static class TitleHeaderItem extends HeaderItem {
		private final CharSequence title;

		TitleHeaderItem(CharSequence title) {
			if (StringUtils.isEmpty(title)) {
				LOGGER.warn("Not title provided");
			}
			this.title = title;
		}

		@Override
		public Iterable<?> getRenderTokens() {
			return Collections.singleton("title");
		}

		@Override
		public void render(Response response) {
			response.write("<title>");
			response.write(title);
			response.write("</title>");
		}
	}
}
