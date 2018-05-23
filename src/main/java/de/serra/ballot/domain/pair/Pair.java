package de.serra.ballot.domain.pair;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Modifiable;
import org.immutables.value.Value.Parameter;

import javax.annotation.Nullable;

@Immutable
@Modifiable
public interface Pair<F, S> {

	@Parameter(order = 1)
	@Nullable
	F getFirst();

	@Parameter(order = 2)
	@Nullable
	S getSecond();
}
