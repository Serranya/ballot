package de.serra.ballot.domain;

import com.google.common.collect.ImmutableMap;
import org.immutables.value.Value.Immutable;

import java.io.Serializable;

@Immutable
public interface CondorcetVote extends Serializable {

	ImmutableMap<ImmutableChoice, Integer> getCandidatePreferences();
}
