package de.serra.ballot.domain;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import de.serra.ballot.domain.pair.ImmutablePair;
import de.serra.ballot.domain.pair.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

public interface CondorcetMatrix {

	ImmutableCollection<ImmutableChoice> getChoices();

	/**
	 * Maps Pair&lt;C1,C2&gt; to number of votes who prefer C1 over C2.
	 */
	ImmutableMap<Pair<ImmutableChoice, ImmutableChoice>, Integer> getComparisons();

	default Optional<ImmutableChoice> getBeatpathWinner() {
		var winners = getWinnerOrdering().entrySet().stream()
				.max((e1, e2) -> Integer.compare(e1.getKey(), e2.getKey())).map(Entry::getValue).get();
		if (winners.size() > 1) {
			return Optional.empty();
		}
		return Optional.of(winners.get(0));
	}

	default ImmutableMap<Integer, List<ImmutableChoice>> getWinnerOrdering() {
		var choices = getChoices();
		var strongestPaths = getStrongestPaths();
		var wins = new HashMap<ImmutableChoice, Integer>();
		for (var c1 : choices) {
			for (var c2 : choices) {
				if (!c1.equals(c2)) {
					var c1Toc2 = strongestPaths.get(ImmutablePair.of(c1, c2));
					var c2Toc1 = strongestPaths.get(ImmutablePair.of(c2, c1));
					if (c1Toc2 > c2Toc1) {
						wins.put(c1, wins.getOrDefault(c1, 0) + 1);
					}
				}
			}
		}

		if (wins.isEmpty()) {
			return ImmutableMap.of(1, getChoices().asList());
		}

		var orderedWins = new TreeMap<Integer, List<ImmutableChoice>>(Integer::compare);
		for (var entry : wins.entrySet()) {
			var list = orderedWins.getOrDefault(entry.getValue(), new ArrayList<ImmutableChoice>());
			list.add(entry.getKey());
			orderedWins.put(entry.getValue(), list);
		}

		return ImmutableMap.copyOf(orderedWins);
	}

	default ImmutableMap<Pair<ImmutableChoice, ImmutableChoice>, Integer> getStrongestPaths() {
		// Floyd–Warshall algorithm
		//# Input: d[i,j], the number of voters who prefer candidate i to candidate j.
		//# Output: p[i,j], the strength of the strongest path from candidate i to candidate j.
		//
		//for i from 1 to C
		//   for j from 1 to C
		//      if (i ≠ j) then
		//         if (d[i,j] > d[j,i]) then
		//            p[i,j] := d[i,j]
		//         else
		//            p[i,j] := 0
		//
		//for i from 1 to C
		//   for j from 1 to C
		//      if (i ≠ j) then
		//         for k from 1 to C
		//            if (i ≠ k and j ≠ k) then
		//               p[j,k] := max ( p[j,k], min ( p[j,i], p[i,k] ) )
		var comparions = getComparisons();
		var paths = new HashMap<Pair<ImmutableChoice, ImmutableChoice>, Integer>();

		for (var pair : comparions.keySet()) {
			var oppositePair = ImmutablePair.of(pair.getSecond(), pair.getFirst());
			var pairVotes = comparions.get(pair);
			var oppositePairVotes = comparions.get(oppositePair);
			if (pairVotes > oppositePairVotes) {
				paths.put(pair, pairVotes);
			} else {
				paths.put(pair, 0);
			}
		}

		for (var pair : comparions.keySet()) {
			for (ImmutableChoice k : getChoices()) {
				var i = pair.getFirst();
				var j = pair.getSecond();
				if (!(k.equals(i) || k.equals(j))) {
					paths.put(ImmutablePair.of(j, k), Math.max(paths.get(ImmutablePair.of(j, k)),
							Math.min(paths.get(ImmutablePair.of(j, i)), paths.get(ImmutablePair.of(i, k)))));
				}
			}
		}
		return ImmutableMap.copyOf(paths);
	}
}
