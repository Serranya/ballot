package de.serra.ballot.frontend.voteballot;

import de.serra.ballot.frontend.domain.ImmutableFrontendChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.collections.MultiMap;
import org.danekja.java.util.function.serializable.SerializableSupplier;

import java.util.List;
import java.util.stream.Collectors;

class AllChoicesReadOnlyModel implements IModel<List<ImmutableFrontendChoice>> {
	private final SerializableSupplier<IModel<MultiMap<Integer, ImmutableFrontendChoice>>> inner;

	AllChoicesReadOnlyModel(SerializableSupplier<IModel<MultiMap<Integer, ImmutableFrontendChoice>>> inner) {
		this.inner = inner;
	}

	@Override
	public List<ImmutableFrontendChoice> getObject() {
		return inner.get().getObject().values().stream().flatMap(List::stream).distinct().collect(Collectors.toList());
	}
}
