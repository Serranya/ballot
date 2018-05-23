package de.serra.ballot.frontend;

import de.serra.ballot.domain.Choice;
import org.immutables.value.Value.Immutable;

import java.io.Serializable;

@Immutable
public interface FrontendChoice extends HasId, Serializable {
	Choice getChoice();
}
