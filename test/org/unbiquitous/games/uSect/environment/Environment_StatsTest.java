package org.unbiquitous.games.uSect.environment;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.unbiquitous.games.uSect.TestUtils.setUpEnvironment;

import org.junit.Before;
import org.junit.Test;
import org.unbiquitous.games.uSect.DeviceStats;
import org.unbiquitous.uImpala.engine.core.GameObject;

public class Environment_StatsTest {

	Environment e;
	
	@Before public void setUp(){
		e = setUpEnvironment();
	}
	
	@Test public void environmentIsAGameObject(){
		assertThat(new Environment(null)).isInstanceOf(GameObject.class);
	}
	
	@Test public void minimunChanceOfApearingNutrientsIs10percent(){
		e = new Environment(createStastWithMemory(0));
		testNutrientsProbability(0.01);
		
		e = new Environment(createStastWithMemory(512));
		testNutrientsProbability(0.01);
	}

	@Test public void maximunChanceOfApearingNutrientsIs50percent(){
		e = new Environment(createStastWithMemory(Integer.MAX_VALUE));
		testNutrientsProbability(0.05);
		
		e = new Environment(createStastWithMemory(16*1024));
		testNutrientsProbability(0.05);
	}
	
	@Test public void chanceOfApearingNutrientsIsProportionalToMemory(){
		e = new Environment(createStastWithMemory(1024));
		testNutrientsProbability(0.0125);
		e = new Environment(createStastWithMemory(2*1024));
		testNutrientsProbability(0.015);
	}
	
	@Test public void nutrientCreationCanBeDisabled(){
		e = new Environment(createStastWithMemory(2*1024));
		e.disableNutrientsCreation();
		Random.setvalue(1);
		e.update();
		assertThat(e.nutrients()).isEmpty();
		Random.setvalue(0);
		e.update();
		assertThat(e.nutrients()).isEmpty();
	}
	
	
	private void testNutrientsProbability(double chances) {
		double value = 1-chances;
		Random.setvalue(value-0.0001);
		e.update();
		assertThat(e.nutrients()).isEmpty();
		Random.setvalue(value);
		e.update();
		assertThat(e.nutrients()).hasSize(1);
	}
	
	private DeviceStats createStastWithMemory(final int memoryInMB) {
		return new DeviceStats(){
			public long totalMemory(){
				return memoryInMB;
			}
			public long cpuSpeed() {
				return 0;
			}
		};
	}
	
}
