package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Paddle extends Entity{
	private float vel;
	private Position side;
	
	public Paddle(Position side) {
		super(Textures.paddle);
		this.side = side;
		
		switch(side) {
		case TOP:
			setOriginPosition(0, Sim.maxY - sprite.getOriginY());
			break;
		case BOTTOM:
			setOriginPosition(0, -Sim.maxY + sprite.getOriginY());
			break;
		case LEFT:
			sprite.rotate(90);
			setOriginPosition(-Sim.maxX + sprite.getOriginY(), 0);
			break;
		case RIGHT:
			sprite.rotate(90);
			setOriginPosition(Sim.maxX - sprite.getOriginY(), 0);
			break;
		}
	}
	
	@Override
	public void update(float delta) {
		// Get ball position and velocity vector, give to controller
		// Controller outputs velocities
		
		Vector2 pos = getOriginPosition();
		
		// Assume vel = controller output
		vel = -100;
		
		vel = MathUtils.clamp(vel, -100, 100);
		
		switch(side) {
		case TOP:
			pos.x += -vel * delta;
			break;
		case BOTTOM:
			pos.x += vel * delta;
			break;
		case LEFT:
			pos.y += -vel * delta;
			break;
		case RIGHT:
			pos.y += vel * delta;
			break;
		}
		
		float boundX = Sim.maxX - sprite.getWidth() / 2;
		float boundY = Sim.maxY - sprite.getWidth() / 2;
		
		if (side == Position.TOP || side == Position.BOTTOM) {
			if (pos.x >= boundX)
				pos.x = boundX;
			if (pos.x <= -boundX)
				pos.x = -boundX;
		}
		if (side == Position.LEFT || side == Position.RIGHT) {
			if (pos.y >= boundY)
				pos.y = boundY;
			if (pos.y <= -boundY)
				pos.y = -boundY;
		}
		setOriginPosition(pos.x, pos.y);
	}
}
