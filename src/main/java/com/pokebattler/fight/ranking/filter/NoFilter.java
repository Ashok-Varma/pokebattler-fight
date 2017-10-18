package com.pokebattler.fight.ranking.filter;

import org.springframework.stereotype.Component;

import com.pokebattler.fight.data.proto.Ranking.FilterType;
import com.pokebattler.fight.ranking.RankingParams;

@Component
public class NoFilter implements RankingsFilter {
	private FilterType filterType = FilterType.NO_FILTER;

    @Override
    public FilterType getType() {
        return filterType;
    }

    @Override
    public RankingsFilter forValue(String filterValue) {
        return this;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filterType == null) ? 0 : filterType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoFilter other = (NoFilter) obj;
		if (filterType != other.filterType)
			return false;
		return true;
	}

	@Override
	public String getValue() {
		return "NONE";
	}

	@Override
	public RankingsFilter getOptimizer(RankingParams params) {
		// TODO Auto-generated method stub
		return new NoFilter() {
			@Override
		    public int getNumBestAttackerToKeep() {
		        return (int)(super.getNumBestAttackerToKeep() * 1.25);
		    }		
			@Override
			public boolean compressResults() {
				return false;
			}

		};
	}



}
