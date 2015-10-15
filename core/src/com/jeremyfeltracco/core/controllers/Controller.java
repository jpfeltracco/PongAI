package com.jeremyfeltracco.core.controllers;

import java.util.EnumMap;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Paddle.State;
import com.jeremyfeltracco.core.entities.Side;

/**
 * @author jpfel Parent class of specific controllers for the paddles. It
 *         handles all coordinate rotations and other changes in order to make
 *         each paddle "believe" it is the bottom paddle relative to all the
 *         others. Subclasses can make use of getter functions for positions and
 *         velocities/directions of other paddles and balls. These subclasses
 *         each need to implement a controlPaddle function that controller will
 *         then use to make appropriate changes.
 */
public abstract class Controller {
	// Maps each enum value to a corresponding rotation
	EnumMap<Side, Integer> rotMap = new EnumMap<Side, Integer>(Side.class);
	
	// Maps to convert global side to side relative to top, left, and right
	// Bottom is of course equal to global
	EnumMap<Side, Side> relTop = new EnumMap<Side, Side>(Side.class);
	EnumMap<Side, Side> relLeft = new EnumMap<Side, Side>(Side.class);
	EnumMap<Side, Side> relRight = new EnumMap<Side, Side>(Side.class);

	private Side controlledSide;
	private Paddle[] pads;
	private Ball ball;

	public Controller(Side side, Paddle[] pads, Ball ball) {
		this.controlledSide = side;
		this.pads = pads;
		this.ball = ball;

		rotMap.put(Side.UP, 180); // Top corresponds to 180 degree rot
		rotMap.put(Side.BOTTOM, 0); // Bottom corresponds to 0 degree rot
		rotMap.put(Side.LEFT, 90); // Left corresponds to 90 degree rot
		rotMap.put(Side.RIGHT, -90); // Right corresponds to -90 degree rot
		
		relTop.put(Side.UP, Side.BOTTOM);
		relTop.put(Side.BOTTOM, Side.UP);
		relTop.put(Side.LEFT, Side.RIGHT);
		relTop.put(Side.RIGHT, Side.LEFT);
		
		relLeft.put(Side.UP, Side.RIGHT);
		relLeft.put(Side.BOTTOM, Side.LEFT);
		relLeft.put(Side.LEFT, Side.UP);
		relLeft.put(Side.RIGHT, Side.BOTTOM);
		
		relRight.put(Side.UP, Side.LEFT);
		relRight.put(Side.BOTTOM, Side.RIGHT);
		relRight.put(Side.LEFT, Side.BOTTOM);
		relRight.put(Side.RIGHT, Side.UP);
	}

	/**
	 * Sets the pad state to the next computed state. Must be called once a
	 * render loop.
	 */
	public void update() {
		setPadState(controlPaddle());
	}
	
	/**
	 * Returns a new state for the controlled paddle. All controllers must
	 * implement this function.
	 * 
	 * @return A new state for the controlled paddle.
	 */
	public abstract State controlPaddle();

	/**
	 * Sets the state of the controlled paddle.
	 * 
	 * @param state
	 */
	protected void setPadState(State state) {
		pads[controlledSide.ordinal()].setState(state);
	}

	/**
	 * Gets the state of the controlled paddle.
	 * 
	 * @return State of controlled paddle.
	 */
	protected State getPadState() {
		return getOtherPadState(Side.BOTTOM);
	}

	/**
	 * Gets the state of a paddle specified by side RELATIVE TO THE CONTROLLED
	 * PADDLE.
	 * 
	 * @param side Side relative to the paddle.
	 * @return State of paddle specified by side.
	 */
	protected State getOtherPadState(Side side) {
		// This is the global index relative to the real bottom paddle
		int ind = 0;
		switch(controlledSide) {
		case UP:
			ind = relTop.get(side).ordinal();
			break;
		case BOTTOM:
			ind = side.ordinal();
			break;
		case LEFT:
			ind = relLeft.get(side).ordinal();
			break;
		case RIGHT:
			ind = relRight.get(side).ordinal();
			break;
		}
		return pads[ind].getState();
	}

	/**
	 * Gets the position of the controlled paddle.
	 * 
	 * @return A copy of the position vector of the paddle this controller is
	 *         controlling.
	 */
	protected Vector2 getPaddlePos() {
		return getOtherPaddlePos(controlledSide);
	}

	/**
	 * Gets the position of the specified paddle.
	 * 
	 * @param side
	 * @return A copy of the position vector of the paddle specified.
	 */
	protected Vector2 getOtherPaddlePos(Side side) {
		return pads[side.ordinal()].getOriginPosition().cpy().rotate(rotMap.get(side));
	}

	/**
	 * Gets the position of the ball.
	 * 
	 * @return A copy of the position vector of the ball.
	 */
	protected Vector2 getBallPos() {
		return ball.getOriginPosition().cpy().rotate(rotMap.get(controlledSide));
	}

	/**
	 * Gets the velocity of the ball.
	 * 
	 * @return A copy of the velocity vector of the ball.
	 */
	protected Vector2 getBallVel() {
		return ball.getVelocity().cpy().rotate(rotMap.get(controlledSide));
	}
}
