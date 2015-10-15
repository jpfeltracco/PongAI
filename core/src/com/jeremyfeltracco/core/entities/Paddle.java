package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Paddle extends Entity{
	public enum State {
		STOP, LEFT, RIGHT
	}
	private float vel;

	private float boundY;
	private float boundX;

	private Side side;
	private State state;
	
	
	public Paddle(Side side) {
		super(Textures.paddle);
		this.side = side;
		this.state = State.STOP;
		
		switch(side) {
		case UP:
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
		float size = Textures.corner.getTexture().getHeight()/2;
		boundX = Sim.maxX - sprite.getWidth() / 2 - size * 2;
		boundY = Sim.maxY - sprite.getWidth() / 2 - size * 2;
		
	}
	
	@Override
	public void update(float delta) {
		Vector2 pos = getOriginPosition();
		
		switch(state) {
		case STOP:
			vel = 0;
			break;
		case LEFT:
			vel = -100;
			break;
		case RIGHT:
			vel = 100;
			break;
		}
		
		switch(side) {
		case UP:
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
	
		//Check whether the position is in valid
		if (side == Side.UP || side == Side.BOTTOM) {
			if (pos.x >= boundX)
				pos.x = boundX;
			if (pos.x < -boundX)
				pos.x = -boundX;
		}
		if (side == Side.LEFT || side == Side.RIGHT) {
			if (pos.y > boundY)
				pos.y = boundY;
			if (pos.y < -boundY)
				pos.y = -boundY;
		}
		
		setOriginPosition(pos.x, pos.y);
		
	}
	
	public float getVel() {
		return vel;
	}
	
	public void setState(State s) {
		this.state = s;
	}
	
	public State getState() {
		return this.state;
	}
	
	public String toString(){
		return side.name();
	}
}
