package com.jeremyfeltracco.core.controllers;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.GaussianRandomizer;

import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Paddle.State;
import com.jeremyfeltracco.core.entities.Side;

public class MLPEvolver extends Controller {
	MultiLayerPerceptron mlp;
	GaussianRandomizer r;
	
	public MLPEvolver(Side side, Paddle[] pads, Ball ball) {
		super(side, pads, ball);
		mlp = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 7, 7, 7, 1);
	}


	@Override
	public State controlPaddle() {
		float ballX = this.getBallPos().x;
		float padX = this.getPaddlePos().x;
		
		float inBallX = (ballX + Sim.maxX) / (2 * Sim.maxX);
		float inPadX = (padX + Sim.maxX) / (2 * Sim.maxX);
		
		mlp.setInput(inPadX, inBallX);
		mlp.calculate();
		double output = mlp.getOutput()[0];
		System.out.println(output);
		if (output < -.2) return State.LEFT;
		else if (output > .2) return State.RIGHT;
		
		return State.STOP;
	}

}
