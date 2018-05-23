package de.serra.ballot.frontend;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class BaseGenericPanel<T> extends GenericPanel<T> {

	public BaseGenericPanel(String id) {
		super(id);
	}

	public BaseGenericPanel(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public BallotWebSession getSession() {
		return BallotWebSession.get();
	}
}
