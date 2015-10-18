package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Vector2;

public class VirtualShape extends Entity{
	VirtualShape(Vector2[] vec){
		super(vec);
		contact = true;
		contactPoints = 1;
		velocity = new Vector2(0,0);
	}

	@Override
	public void update(float delta) {}

}
