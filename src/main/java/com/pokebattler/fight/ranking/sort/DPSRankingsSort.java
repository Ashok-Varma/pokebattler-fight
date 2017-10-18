package com.pokebattler.fight.ranking.sort;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.pokebattler.fight.data.proto.FightOuterClass.FightResultOrBuilder;
import com.pokebattler.fight.data.proto.Ranking.SortType;
import com.pokebattler.fight.data.proto.Ranking.SubResultTotalOrBuilder;
import com.pokebattler.fight.ranking.RankingParams;

@Component
public class DPSRankingsSort implements RankingsSort {
	public Comparator<SubResultTotalOrBuilder> getSubResultComparator() {
		return Comparator
				.<SubResultTotalOrBuilder>comparingDouble(
						total -> -total.getDamageDealt() / (double) total.getCombatTime())
				.thenComparing(Comparator.comparingDouble(total -> -total.getPower()));
	}

	@Override
	public Comparator<FightResultOrBuilder> getFightResultComparator() {
		return Comparator.<FightResultOrBuilder>comparingDouble(result -> -result.getCombatantsOrBuilder(0).getDps())
				.thenComparing(Comparator.comparingDouble(result -> -result.getPower()));
	}

	@Override
	public SortType getType() {
		return SortType.DPS;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return getClass().equals(obj.getClass());
	}
	@Override
    public RankingsSort getRelativeSort(RankingParams params) {
		return new DPSRankingsSort();
	}

}