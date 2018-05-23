package de.serra.ballot.frontend.domain;

import de.serra.ballot.domain.Choice;
import de.serra.ballot.domain.CondorcetBallot;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Immutable
public interface FrontendBallot extends HasId, Serializable {
	Duration VALID_AFTER_CREATION = Duration.ofHours(5);

	LocalDateTime getCreated();

	default boolean hasEnded() {
		var remainingTime = getRemainingTime();
		return remainingTime.isZero() || remainingTime.isNegative();
	}

	default Duration getRemainingTime() {
		var created = getCreated();
		var timePassed = Duration.ofMinutes(ChronoUnit.MINUTES.between(created, LocalDateTime.now()));
		return VALID_AFTER_CREATION.minus(timePassed);
	}

	String getName();

	CondorcetBallot getBallot();

	@Derived
	default List<ImmutableFrontendChoice> getFrontendChoices() {
		var id = 0L;
		var ret = new ArrayList<ImmutableFrontendChoice>();
		for (Choice c : getBallot().getChoices()) {
			ret.add(ImmutableFrontendChoice.builder().choice(c).id(id).build());
			id++;
		}

		return ret;
	};
}
