package de.serra.ballot.frontend.showballot;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.List;

public abstract class MatrixTablePanel<T> extends GenericPanel<List<T>> {
	public MatrixTablePanel(String id, IModel<List<T>> model) {
		super(id, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(newTableCaption("caption"));
		add(newTopLabelRowName("topLabel"), newLeftLabelColumnName("leftLabel"));
		add(newTopLabels("topLabels"));
		add(newRows("rows"));
	}

	protected Component newTableCaption(String id) {
		return new EmptyPanel(id);
	}

	protected Component newTopLabelRowName(String id) {
		return new Label(id, new ResourceModel("topLabel", "A"));
	}

	protected Component newLeftLabelColumnName(String id) {
		return new Label(id, new ResourceModel("leftLabel", "B"));
	}

	protected Component newTopLabels(String id) {
		return new ListView<T>(id, getModel()) {
			@Override
			protected void populateItem(ListItem<T> item) {
				item.add(new Label("topLabel", getLabelForItem(item.getModel())));
			}
		};
	}

	protected Component newRows(String id) {
		return new ListView<T>(id, getModel()) {
			@Override
			protected void populateItem(ListItem<T> item) {
				item.add(new Label("topLabel", getLabelForItem(item.getModel())));
				item.add(newColumns("data", item.getModel()));
			}
		};
	}

	protected IModel<?> getLabelForItem(IModel<T> item) {
		return item;
	}

	protected Component newColumns(String id, IModel<T> columnModel) {
		return new ListView<T>(id, getModel()) {
			@Override
			protected void populateItem(ListItem<T> item) {
				populateCell("content", item, item.getModel(), columnModel);
			}
		};
	}

	protected abstract void populateCell(String id, WebMarkupContainer cell, IModel<T> rowModel, IModel<T> columnModel);
}
