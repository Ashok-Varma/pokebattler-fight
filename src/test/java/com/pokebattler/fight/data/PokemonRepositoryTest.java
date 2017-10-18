package com.pokebattler.fight.data;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.pokebattler.fight.calculator.Formulas;
import com.pokebattler.fight.data.proto.PokemonMoveOuterClass.PokemonMove;
import com.pokebattler.fight.data.proto.PokemonOuterClass.Pokemon;
import com.pokebattler.fight.data.proto.PokemonOuterClass.Pokemons;
import com.pokebattler.fight.data.raw.RawData;
import com.pokebattler.fight.data.proto.PokemonDataOuterClass.PokemonData;

public class PokemonRepositoryTest {
	IndividualPokemonRepository p;

	@Before
	public void setUp() throws Exception {
		p = new IndividualPokemonRepository() {

			@Override
			public Pokemons createPokemons(RawData rawData, String legacy) {
				return Pokemons.newBuilder().build();
			}

		};
	}


	@Test
	public void testGetAll() throws Exception {
		// Make sure to update the client if these change
		p = new IndividualPokemonRepository();
		CpMRepository cpmRepository = new CpMRepository();
		Formulas f = new Formulas();
		f.setCpmRepository(cpmRepository);
		assertEquals(330, p.getAll().getPokemonList().stream().mapToInt(pokemon -> pokemon.getStats().getBaseAttack())
				.max().getAsInt());
		assertEquals(396, p.getAll().getPokemonList().stream().mapToInt(pokemon -> pokemon.getStats().getBaseDefense())
				.max().getAsInt());
		assertEquals(510, p.getAll().getPokemonList().stream().mapToInt(pokemon -> pokemon.getStats().getBaseStamina())
				.max().getAsInt());
		assertEquals(4760,
				p.getAll().getPokemonList().stream()
						.mapToInt(pokemon -> f.calculateCp("40", pokemon.getStats().getBaseAttack(), 15,
								pokemon.getStats().getBaseDefense(), 15, pokemon.getStats().getBaseStamina(), 15))
						.max().getAsInt());
	}

	@Test
	public void testGetDefenders() throws Exception {
		p = new IndividualPokemonRepository();
		final CpMRepository cpmRepository = new CpMRepository();
		final Formulas f = new Formulas();
		final PokemonData data = PokemonData.newBuilder().setIndividualAttack(15).setIndividualDefense(15)
				.setIndividualStamina(15).setLevel("40").build();
		f.setCpmRepository(cpmRepository);
		//top 30 end game
		List<Pokemon> sorted = p.getAll().getPokemonList().stream()
				.filter(pokemon -> PokemonRepository.END_GAME_POKEMONS.contains(pokemon.getPokemonId()))
				.sorted(Comparator.<Pokemon>comparingInt(pokemon -> -f.calculateCp(data, pokemon)))
				.collect(Collectors.toList());
		for (int i = 0; i < 30; i++) {
			assertTrue("Should have contained " + sorted.get(i).getPokemonId(), 
					PokemonRepository.END_GAME_DEFENDER_POKEMONS.contains(sorted.get(i).getPokemonId()));
		}
		
		assertEquals(127, PokemonRepository.END_GAME_POKEMONS.size());
		assertEquals(45, PokemonRepository.END_GAME_ATTACKER_POKEMONS.size());
		assertEquals(58, PokemonRepository.END_GAME_GOOD_DEFENDER_POKEMONS.size());

	}

}
