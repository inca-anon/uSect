package org.unbiquitous.games.uSect.environment;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.unbiquitous.games.uSect.DeviceStats;
import org.unbiquitous.games.uSect.Nutrient;
import org.unbiquitous.games.uSect.Sect;
import org.unbiquitous.uImpala.engine.core.GameComponents;
import org.unbiquitous.uImpala.engine.core.GameObject;
import org.unbiquitous.uImpala.engine.core.GameRenderers;
import org.unbiquitous.uImpala.engine.io.Screen;
import org.unbiquitous.uImpala.jse.util.shapes.Rectangle;
import org.unbiquitous.uos.core.InitialProperties;

public class Environment extends GameObject {

	private Screen screen;
	//TODO: fix this
	public RandomGenerator random =  new RandomGenerator();
	Rectangle background;
	
	Map<UUID,Point> positionMap = new HashMap<UUID,Point>();
	Map<UUID,Long> energyMap = new HashMap<UUID,Long>();
	private NutrientManager nutrients;
	private SectManager sects;
	private MovementManager mover;

	public Environment(InitialProperties props) {
		this(new DeviceStats(),props);
	}
	
	public Environment(DeviceStats deviceStats,InitialProperties props) {
		nutrients = new NutrientManager(this, random, deviceStats, positionMap);
		sects = new SectManager(this, positionMap, energyMap);
		mover = new MovementManager(this, positionMap, random);
		createBackground();
	}

	private void createBackground() {
		screen = GameComponents.get(Screen.class);
		Point center = new Point(screen.getWidth()/2, screen.getHeight()/2);
		background = new Rectangle(center, Color.WHITE, screen.getWidth(), screen.getHeight());
	}

	public void update() {
		nutrients.update();
		sects.update();
	}

	public Point position(UUID objectId){
		if(!positionMap.containsKey(objectId)){
			return null;
		}
		return (Point) positionMap.get(objectId).clone();
	}
	
	public Nutrient addNutrient() {
		int x = (int) (Math.random()*screen.getWidth());
		int y = (int) (Math.random()*screen.getHeight());
		return addNutrient(new Point(x, y));
	}

	public Nutrient addNutrient(Point position) {
		return nutrients.addNutrient(position);
	}
	
	public Sect addSect(Sect s, Point position) {
		return sects.addSect(s, position);
	}

	public void moveTo(Sect sect, Point dir) {
		mover.moveTo(sect, dir);
	}

	public List<Sect> sects() {
		return sects.sects();
	}
	
	public List<Nutrient> nutrients(){
		return nutrients.nutrients();
	}
	
	protected void render(GameRenderers renderers) {
		background.render();
		renderNutrients();
		renderSects();
	}

	private void renderSects() {
		for(Sect s : sects()){
			s.render(null);
		}
	}

	private void renderNutrients() {
		for(Nutrient n : nutrients()){
			n.render(null);
		}
	}

	protected void wakeup(Object... args) {}
	protected void destroy() {}
}