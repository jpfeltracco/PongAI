package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Corner extends Entity {
	
	public Corner(float x, float y, Vector2 vec, float rotation){
		super(Textures.corner, vec, rotation);
		setOriginPosition(x, y);
		this.updateSides();
		setInitPos();
	}
	
	public Corner(float x, float y, Vector2 vec) {
		this(x,y, vec, 0);
	}
	
	public Corner(float x, float y) {
		this(x,y, new Vector2(0,0));
	}

	@Override
	public void update(float delta) {
		if(velocity.x > 0 && velocity.y > 0){
			Vector2 pos = getOriginPosition();
			pos.add(velocity.cpy().scl(delta));
			this.setOriginPosition(pos.x, pos.y);
		}
	}
}
