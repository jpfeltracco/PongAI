package com.jeremyfeltracco.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Paddle {
	private Sprite sprite;
	private Vector2 velocity;
	private Position pos;
	
	public Paddle(Position pos) {
		this.pos = pos;
		sprite = new Sprite(Textures.paddle);
		velocity = new Vector2(0, 0);
		
		switch(pos) {
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
	
	private void setOriginPosition(float x, float y) {
		sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void update(float delta) {
		// Get ball position and velocity vector, give to controller
		// Controller outputs velocities
		
		// Assume vel = controller output
		
		Vector2 velocity = new Vector2(0, 0);
		float vel = 4.1820f;
		
		vel = MathUtils.clamp(vel, -5, 5);
		
		switch(pos) {
		
		case TOP:
			break;
		case BOTTOM:
			velocity.x = vel;
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
		}
		
		
//		Vector2 pos = new Vector2(getX(), getY());
//		pos = pos.add(velocity.scl(delta));
//		this.setPosition(pos.x, pos.y);
	}
}
