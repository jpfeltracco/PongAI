package com.jeremyfeltracco.core;


import java.text.DecimalFormat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.controllers.Controller;
import com.jeremyfeltracco.core.controllers.mlps.MLPControl;
import com.jeremyfeltracco.core.controllers.mlps.XPositions;
import com.jeremyfeltracco.core.elvolver.GA;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Corner;
import com.jeremyfeltracco.core.entities.Entity;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Side;

public class Sim extends ApplicationAdapter {
	
	public static int maxX;
	public static int maxY;
	
	public static int amtPad = 4;
	public static Paddle[] pads;
	public static Ball ball;
	public static Vector2[] ballStartPos;
	public static final int BALLOFFSET = 50;
	public static float cornerSize;
	public static double systemTime = 0;
	public static boolean enable = true;
	public static String errorMessage;
	public static FileHandle fh;
	private static Side loser;
	private static int simulationRuns = 0;
	private static double totalSystemTime = 0; 
	private static double systemTiming = 0; 
	private static int numErrors = 0;
	private static int numTimouts = 0;
	private static long nanoStartTime = System.nanoTime();
	private static double averageTimeTaken = 0;
	SpriteBatch batch;
	public static OrthographicCamera cam;
	Controller[] controls;
	
	public static boolean goal = false;
	public static boolean threads = true;
	public static int simHold = 0;
	
	GA algorithm;
	
	boolean value = false;
	@Override
	public void create () {
		fh = Gdx.files.internal("output.txt");
		log("----------Simulation Start----------\n");
		maxX = Gdx.graphics.getWidth() / 2;
		maxY = Gdx.graphics.getHeight() / 2;
		
		ballStartPos = new Vector2[] {new Vector2(0, maxY - BALLOFFSET),new Vector2(0, -maxY + BALLOFFSET),new Vector2(maxX - BALLOFFSET , 0),new Vector2(-maxY + BALLOFFSET, 0)};
		
		pads = new Paddle[amtPad];
		for (int i = 0; i < amtPad; i++) {
			pads[i] = new Paddle(Side.values()[i]);
		}
		ball = new Ball();//new Vector2(-300f,680f));
		
		cornerSize = Textures.corner.getTexture().getHeight()/2;
		new Corner(-maxX+cornerSize,-maxY+cornerSize);
		new Corner(maxX-cornerSize,-maxY+cornerSize);
		new Corner(-maxX+cornerSize,maxY-cornerSize);
		new Corner(maxX-cornerSize,maxY-cornerSize);
		new Corner(130,130);
		new Corner(50,50);
		new Corner(-49f,-50);
		new Corner(-55,0);
		
		log("Added " + Entity.objCount + " objects to the simulation.\n");
		
		MLPControl[] mlpcontrols = new MLPControl[pads.length];
		for (int i = 0; i < mlpcontrols.length; i++)
			mlpcontrols[i] = new XPositions(Side.values()[i], pads, ball);
		
		algorithm = new GA(mlpcontrols, ball);
		
		controls = algorithm.getControllers();

		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.x = 0;
		cam.position.y = 0;
		
		value = true;
		while(value && numTimouts < 20){//simulationRuns < 10000){
			
			update(1/60f, algorithm.getCurrentGeneration() % 10 == 0 && algorithm.getGameGroup() == 36);
			
		}
		
	}
	
	int secToSec;
	public void update(float delta, boolean print){
		if(!enable)
			reset(print);
		
		if(systemTime > 60){
			numTimouts++;
			algorithm.update(loser); // No loser, so won't subtract fitnesses
			reset(print);
			
		}
		//float delta = 1/60f;//0.01666f;
		systemTime += delta;
		totalSystemTime += delta;
		systemTiming += delta;
		
		
		
		//System.out.println("UPDATE");
		for (int i = 0; i < 4; i++){
			//System.out.println(controls[i]);
			controls[i].update();
		}

		
		for(Controller c : controls){
			c.update();
		}
		
		//System.out.println(System.nanoTime() - t);
		
		for(Entity e : Entity.entities)
			e.update(delta);
		
		if(loser != null){
			algorithm.update(loser);
			reset(print);
			loser = null;
		}
		//System.out.println("ENDLOOP");
		//System.out.println(ball.getVelocity().len());
	}
	
	DecimalFormat df = new DecimalFormat("#.00"); 
	private void reset(boolean print){
		averageTimeTaken = avgTime(systemTime);
		for(Entity e : Entity.entities){
			e.reset();
		}
		enable = true;
		systemTime = 0;
		
		if(print){
			secToSec = (int)(systemTiming / ((double)(System.nanoTime() - nanoStartTime) * 1e-9));
			System.out.println("Sim Runs: " + simulationRuns + "\tGen: " + algorithm.getCurrentGeneration() + "\tGrp: " 
					+ algorithm.getGameGroup() + "\tTime: (/m) " + ((int)Math.ceil(totalSystemTime/60)) + "\tTimePerSim: " 
					+ df.format(averageTimeTaken) + "\tSim secs per Sec: " + secToSec +"\tErrors: " + numErrors + "\tTimouts: " 
					+ numTimouts);
			nanoStartTime = System.nanoTime();
			systemTiming = 0;
			//System.out.println(simHold);
		}
		
		simulationRuns++;
	}

	@Override
	public void render () {
		//System.out.println("DELTA: " + delta);
		update(Gdx.graphics.getDeltaTime(),true);
		batch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0.8431372549f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for(Entity e : Entity.entities)
			e.draw(batch);
		batch.end();
		
	}
	
	public static void setSideHit(Side s){
		loser = s;
	}
	
	public static void reportError(String in){
		numErrors++;
		//System.out.println(in);
		log(in + "\nElements:\n");
		for(Entity e : Entity.entities){
			log("\t" + e.getClass() + "\t POS: " + e.getInitPos() + "\t ROT: " + e.getInitRotation() + "\t VEL: " + e.getInitVelocity() + "\n");
		}
		log("\nConditions: " + "\tSimulation Number: " + simulationRuns + "\tAI Version: " + "<AI VERSION INFO>" + "\tTotal Sim Time: " + totalSystemTime + "\n");
	}
	
	public static void log(String s){
//		try {
//		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fh.file().getAbsolutePath(), true)));
//		    out.print(s);
//		    out.close();
//		} catch (IOException e) {
//			System.out.println(e);
//		}
	}
	
	public void updateProgress(double progressPercentage, long timeAtStart) {
        final int width = 50; // progress bar width in chars
        long elapsedTime = System.nanoTime() - timeAtStart;

        double secRemaining = (double)elapsedTime / progressPercentage / 1000000000.0 - (double)elapsedTime / 1000000000.0;

        System.out.print("\r");
        for(int i = 0; i < width + 20; i++){
            System.out.print(" ");
        }

        System.out.print("\r[");
        int i = 0;
        for (; i <= (int)(progressPercentage*width); i++) {
            System.out.print(".");
        }
        for (; i < width; i++) {
            System.out.print(" ");
        }
        System.out.print("] " + (int)(progressPercentage*100) + "%, est " + (int)secRemaining + "s");
    }
	
	private static final int sampleNum = 75;
	private double times[] = new double[sampleNum];
	private int timeLoc = 0;
	public double avgTime(double systemTime){
		times[timeLoc] = systemTime;
		timeLoc++;
		if(timeLoc >= sampleNum){
			timeLoc = 0;
		}
		
		double sum = 0;
		for(double d : times){
			sum += d;
		}
		
		return (double)sum / (double)sampleNum;
	}
}


