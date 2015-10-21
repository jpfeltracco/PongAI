package com.jeremyfeltracco.core.controllers.mlps;

import org.neuroph.nnet.MultiLayerPerceptron;

import com.jeremyfeltracco.core.controllers.Controller;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Side;

public abstract class MLPControl extends Controller  {
	private MultiLayerPerceptron net;
	public MLPControl(Side side, Paddle[] pads, Ball ball) {
		super(side, pads, ball);
	}
	
	public MultiLayerPerceptron getNet() {
		return net;
	}
	
	public void setNet(MultiLayerPerceptron net) {
		this.net = net;
	}
}
