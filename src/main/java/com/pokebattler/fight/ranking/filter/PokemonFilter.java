package com.pokebattler.fight.ranking.filter;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pokebattler.fight.data.PokemonRepository;
import com.pokebattler.fight.data.proto.PokemonIdOuterClass.PokemonId;
import com.pokebattler.fight.data.proto.PokemonOuterClass.Pokemon;
import com.pokebattler.fight.data.proto.Ranking.FilterType;

@Component
public class PokemonFilter implements RankingsFilter {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final FilterType filterType = FilterType.POKEMON;
	private final PokemonId pokemonId;

	public PokemonFilter() {
		this.pokemonId = PokemonId.UNRECOGNIZED;
	}

	public PokemonFilter(PokemonId pokemonId) {
		this.pokemonId = pokemonId;
	}

	@Override
	public FilterType getType() {
		return filterType;
	}

	@Override
	public RankingsFilter forValue(String filterValue) {
		try {
			return new PokemonFilter(PokemonId.valueOf(filterValue));
		} catch (Exception e) {
			log.error("Could not create filter", e);
			return this;
		}
	}

	@Override
	public Collection<Pokemon> getAttackers(PokemonRepository repository) {
		return Collections.singletonList(repository.getById(pokemonId));
	}

	@Override
	public Collection<Pokemon> getDefenders(PokemonRepository repository) {
		return repository.getAllEndGame().getPokemonList();
	}

	@Override
	public boolean compressResults() {
		return false;
	}

	@Override
	public int getNumWorstDefenderToKeep() {
		return RankingsFilter.TRIM_TO;
	}

	@Override
	public int getNumWorstSubDefenderToKeep() {
		return 1;
	}

	@Override
	public String getValue() {
		return pokemonId.name();
	}

	@Override
	public RankingsFilter getOptimizer() {
		return new PokemonFilter(pokemonId) {
			@Override
			public int getNumWorstDefenderToKeep() {
				return (int) (RankingsFilter.TRIM_TO * 1.5);
			}
			@Override
			public int getNumWorstSubDefenderToKeep() {
		        return 5;
			}

			@Override
			public boolean compressResults() {
				return false;
			}

		};
	}

}
