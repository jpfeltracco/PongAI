package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Position;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Paddle extends Entity{
	private Vector2 velocity;
	private Position pos;
	float vel;
	
	public Paddle(Position pos) {
		super(Textures.paddle);
		this.pos = pos;
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
		
		vel = -1000;
	}
	
	@Override
	public void update(float delta) {
		// Get ball position and velocity vector, give to controller
		// Controller outputs velocities
		
		// Assume vel = controller output
		
		Vector2 velocity = new Vector2(0, 0);
		
		
		vel = MathUtils.clamp(vel, -100, 100);
		
		switch(pos) {
		
		case TOP:
			velocity.x = -vel;
			break;
		case BOTTOM:
			velocity.x = vel;
			break;
		case LEFT:
			velocity.y = -vel;
			break;
		case RIGHT:
			velocity.y = vel;
			break;
		}
		
		//System.out.println(velocity);
		Vector2 position = getOriginPosition();
		position = position.add(velocity.scl(delta));
		
		//Check whether the position is in valid
		if(validPos(position)){
			setOriginPosition(position.x, position.y);
		}else{
			
			//Make more realistic for testing
			vel *= -1;
		}
		
		bounceBall();
	}
	
	private void bounceBall(){
		Vector2 balPos = Sim.ball.getOriginPosition();
		//Deal with ball bounces!
	}
	
	/**
	 * Checks if the proposed position is within the scope of the screen for each possible paddle type
	 * @param p the position to be checked
	 * @return whether that position is valid
	 */
	private boolean validPos(Vector2 p){
		boolean out = true;
		switch(pos){
		case TOP:
		case BOTTOM:
			out = (p.x - this.sprite.getWidth()/2 > -Sim.maxX && p.x + this.sprite.getWidth()/2 < Sim.maxX);
			break;
		case LEFT:
		case RIGHT:
			out = (p.y - this.sprite.getWidth()/2 > -Sim.maxY && p.y + this.sprite.getWidth()/2 < Sim.maxY);
			break;
		}
		return out;
	}
	
	public String toString(){
		return pos.name();
	}
}
