package com.jeremyfeltracco.core.controllers;

import com.jeremyfeltracco.core.entities.Paddle;

public class Naive {

	public Paddle.State control(float ballX, float ballY, float padX) {
		Paddle.State out = Paddle.State.STOP;
		if (ballX > padX)
			out = Paddle.State.RIGHT;
		if (ballX < padX)
			out = Paddle.State.LEFT;
		return out;
	}
	
}
