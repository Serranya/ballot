package de.serra.ballot.domain;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Modifiable;
import org.immutables.value.Value.Parameter;

import java.io.Serializable;

@Immutable
@Modifiable
public interface Choice extends Serializable {
	@Parameter
	String getDisplayValue();
}
