package com.jeremyfeltracco.core.controllers;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Side;

public class Controller {
	Paddle p;
	Side side;
	Paddle[] pads;
	Ball b;
	
	Vector2[] paddlePos = new Vector2[Sim.amtPad];
	float[] paddleVels = new float[Sim.amtPad];
	Vector2 ballPos = new Vector2();
	Vector2 ballVel = new Vector2();
	
	public Controller(Side s, Paddle[] pads, Ball b) {
		p = pads[s.ordinal()];
		this.side = s;
		this.pads = pads;
		this.b = b;
	}
	
	public void updatePaddle() {
		switch(side) {
		case TOP:
			for (int i = 0; i < pads.length; i++) {
				paddlePos[i] = pads[i].getOriginPosition().cpy().rotate(180);
				paddleVels[i] = pads[i].getVel();
			}
			ballPos = b.getOriginPosition().cpy().rotate(180);
			ballVel = b.getVelocity().cpy().rotate(180);
			break;
		case BOTTOM:
			for (int i = 0; i < pads.length; i++) {
				paddlePos[i] = pads[i].getOriginPosition().cpy();
				paddleVels[i] = pads[i].getVel();
			}
			ballPos = b.getOriginPosition().cpy();
			ballVel = b.getVelocity().cpy();
			break;
		case LEFT:
			for (int i = 0; i < pads.length; i++) {
				paddlePos[i] = pads[i].getOriginPosition().cpy().rotate(-90);
				paddleVels[i] = pads[i].getVel();
			}
			ballPos = b.getOriginPosition().cpy().rotate(-90).scl(-1);
			ballVel = b.getVelocity().cpy().rotate(-90);
			break;
		case RIGHT:
			for (int i = 0; i < pads.length; i++) {
				paddlePos[i] = pads[i].getOriginPosition().cpy().rotate(90).scl(-1);
				paddleVels[i] = pads[i].getVel();
			}
			ballPos = b.getOriginPosition().cpy().rotate(90);
			ballVel = b.getVelocity().cpy().rotate(90);
			break;
		}
	}
}
