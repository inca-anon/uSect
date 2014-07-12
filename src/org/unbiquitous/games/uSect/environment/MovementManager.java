package org.unbiquitous.games.uSect.environment;

import org.unbiquitous.games.uSect.environment.Environment.Stats;
import org.unbiquitous.games.uSect.objects.Sect;
import org.unbiquitous.uImpala.util.math.Point;

class MovementManager {
	private Environment env;
	
	public MovementManager(Environment env) {
		super();
		this.env = env;
	}

	public void moveTo(Sect sect, Point dir) {
		adjustDirection(dir);
		env.moveTo(sect.id, determineFinalPosition(sect, dir));
		env.changeStats(sect, Stats.n().energy(-1));
	}

	private void adjustDirection(Point dir) {
		double lottery = Random.v();
		if(lottery > 0.5 && dir.x != 0){
			dir.y = 0;
		}else if (lottery <= 0.5 && dir.y != 0){
			dir.x = 0;
		}
	}
	
	private Point determineFinalPosition(Sect sect, Point dir) {
		Point forwardPosition = new Point(sect.position().x + dir.x, sect.position().y + dir.y);
		if(!hasColided(sect, forwardPosition)){
			return forwardPosition;
		}else if (Random.v() > 0.5){
			Point backwardsPosition = new Point(sect.position().x - dir.x, sect.position().y - dir.y);
			return backwardsPosition;
		}
		return sect.position();
	}


	private boolean hasColided(Sect sect, Point newPos) {
		boolean hasColided = false;
		for(Sect s: env.sects()){
			if(!sect.equals(s) && s.position().distanceTo(newPos) < sect.radius()){
				hasColided = true;
			}
		}
		return hasColided;
	}
}