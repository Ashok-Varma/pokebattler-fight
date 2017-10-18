package com.pokebattler.fight.ranking.filter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.pokebattler.fight.data.proto.Ranking.FilterType;

@Repository
public class FilterRegistry {
    @Resource
    List<RankingsFilter> rankingsFilters;
	
    
    private final static Map<FilterType,RankingsFilter> filters = new EnumMap<>(FilterType.class);
    
    @PostConstruct
    public void init() {
    	rankingsFilters.forEach(filter -> registerFilter(filter));
    }    
    public boolean registerFilter(RankingsFilter filter) {
        return filters.put(filter.getType(), filter) != null;
    }
    public RankingsFilter getFilter(FilterType filterType, String filterValue) {
        return filters.get(filterType).forValue(filterValue);
    }

}
