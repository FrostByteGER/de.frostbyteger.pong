package de.frostbyteger.pong.core;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import de.frostbyteger.pong.engine.Ball;
import de.frostbyteger.pong.engine.Border;
import de.frostbyteger.pong.engine.Difficulty;
import de.frostbyteger.pong.engine.GameState;
import de.frostbyteger.pong.engine.MenuState;
import de.frostbyteger.pong.engine.Pad;
import de.frostbyteger.pong.engine.PropertyHelper;


/**
 * @author Kevin
 * TODO: Actual resolution won't be displayed on option menu
 * TODO: Volume wont get really changed
 */
public class Pong extends BasicGame {

	protected Pad pad1, pad2;
	protected Ball ball;

	protected Border lastcollision;
	protected Border lastpadcollision;
	protected GameState currentgamestate = GameState.Start;
	protected MenuState currentmenustate = MenuState.Main;
	protected Difficulty cpudifficulty;
	
	private static final int ballradius = 5;
	private static final int goal = 10;
	@SuppressWarnings("unused") //TODO: Delete this
	private static final double gravity = 0.0981; // TODO: Add gravity to challenge mode
	
	private static final float easy = 2.5f;
	private static final float medium = 5.0f;
	private static final float hard = 10.0f;
	private static final float unbeatable = 15.0f;
	
	//TODO: 1600x1200 Title disappears
	//TODO: Replace 1280x1024 with 1600x1200
	private static final int[][] resArray = {{640,480},{800,600},{1024,768},{1280,960},{1280,1024}};
	
	private final String ai = "AI-Difficulty";
	
	// Options
	public static int resX = 800;
	public static int resY = 600;
	
	public static final int fps = 60;
	
	public static final String title = "Pong";
	public static final String version = "v1.0";
	
	private String[] menu = {"Player vs. CPU","Player vs. Player","LAN-Mode - Coming soon","Challenge Mode","Options","Help","Quit Game"};
	private String[] options = {"Resolution: ","Volume: ","Volume","DEBUG MODE","Save","Exit"};
	//private String[] help = {"How to Play:","Player 1 Controls:","Player 2 Controls:","How to navigate:","Menu Controls:"};
	private String[] difficultymenu = {"Easy","Medium","Hard","Unbeatable"};
	private String[] difficultyexplanation = {"1/4 Speed of Player - For N00bs","1/2 Speed of Player- For average players","Same Speed as Player - For Pr0 Gamers","Alot faster than Player - Hacks are for pussies!"};
	private Color cpuselection = Color.gray;
	private Color pvpselection = Color.gray;
	private Color lanselection = Color.gray;
	private Color challengeselection = Color.gray;
	private Color optionselection = Color.gray;
	private Color resselection = Color.gray;
	private Color volselection = Color.gray;
	private Color volstatselection = Color.gray;
	private Color debugselection = Color.red;
	private Color saveselection = Color.gray;
	private Color exitselection = Color.gray;
	private Color helpselection = Color.gray;
	private Color quitselection = Color.gray;
	private UnicodeFont smallfont;
	private UnicodeFont normalfont;
	private UnicodeFont mediumfont;
	private UnicodeFont bigfont;
	
	private int playerselection = 0;
	private int difficultyselection = 1;
	private int configselection = 0;
	private int resolutionselection = 0;
	
	private static boolean DEBUG = true;
	private static boolean DEBUG_AI = false;
	
	private boolean collision = false;
	
	private Image arrow_left;
	private Image arrow_right;
	
	private PropertyHelper prophelper;
	
	public static AppGameContainer container;
	
	protected double hip = 0;
	protected Random rndm = new Random();

	//TODO: Add description
	public Pong(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		smallfont = newFont("data/alexis.ttf", 25, false, false);
		
		normalfont = newFont("data/alexis.ttf", 40, false, false);
		
		mediumfont = newFont("data/alexis.ttf", 50, false, false);
		
		bigfont = newFont("data/alexis.ttf", 120, false, false);
		
		arrow_left = new Image("data/arrow_left.png");
		arrow_right = new Image("data/arrow_right.png");

		lastcollision = Border.NONE;
		lastpadcollision = Border.NONE;
		currentgamestate = GameState.Play;
		
		//TODO: Warning, not safe
		prophelper = new PropertyHelper();
		prophelper.loadPropertiesFile();
		
		try{
			resX = Integer.parseInt(prophelper.loadProperty("resX"));
			resY = Integer.parseInt(prophelper.loadProperty("resY"));
			gc.setMusicVolume(Float.parseFloat(prophelper.loadProperty("volume")));
			gc.setMusicOn(Boolean.parseBoolean(prophelper.loadProperty("vol_on")));
			DEBUG = Boolean.parseBoolean(prophelper.loadProperty("debug"));
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
		}
		container.setDisplayMode(resX, resY, false);

	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if(currentmenustate == MenuState.Main){
			bigfont.drawString(resX/2 - bigfont.getWidth("Pong")/2, 20 + bigfont.getHeight("Pong"), "Pong", Color.white);	
			normalfont.drawString(resX/2 - normalfont.getWidth(menu[0])/2, resY/2, menu[0], cpuselection);		
			normalfont.drawString(resX/2 - normalfont.getWidth(menu[1])/2, resY/2 + 20, menu[1], pvpselection);
			normalfont.drawString(resX/2 - normalfont.getWidth(menu[2])/2, resY/2 + 40, menu[2], lanselection);
			normalfont.drawString(resX/2 - normalfont.getWidth(menu[3])/2, resY/2 + 60, menu[3], challengeselection);
			normalfont.drawString(resX/2 - normalfont.getWidth(menu[4])/2, resY/2 + 80, menu[4], optionselection);
			normalfont.drawString(resX/2 - normalfont.getWidth(menu[5])/2, resY/2 + 100, menu[5], helpselection);
			normalfont.drawString(resX/2 - normalfont.getWidth(menu[6])/2, resY/2 + 120, menu[6], quitselection);
			g.drawString("RELEASE " + version, resX - 115, resY - 15);
		}
		
		if(currentmenustate == MenuState.CPUSelection){
			bigfont.drawString(resX/2 - bigfont.getWidth("Pong")/2, 20 + bigfont.getHeight("Pong"), "Pong", Color.white);	
			mediumfont.drawString(resX/2 - mediumfont.getWidth(ai)/2, resY/2 - 30, ai,Color.cyan);
			normalfont.drawString(resX/2 - normalfont.getWidth(difficultymenu[difficultyselection])/2, resY/2, difficultymenu[difficultyselection],Color.white);
			smallfont.drawString(resX/2 - smallfont.getWidth(difficultyexplanation[difficultyselection])/2, resY/2 + 20, difficultyexplanation[difficultyselection],Color.lightGray);
			arrow_left.draw(resX/2 - normalfont.getWidth(difficultymenu[difficultyselection])/2 - 45, resY/2 + 2, 0.4f);
			arrow_right.draw(resX/2 + normalfont.getWidth(difficultymenu[difficultyselection])/2 + 13, resY/2 + 2, 0.4f);
		}
		
		if(currentmenustate == MenuState.Options){
			bigfont.drawString(resX/2 - bigfont.getWidth("Pong")/2, 20 + bigfont.getHeight("Pong"), "Pong", Color.white);	
			normalfont.drawString(100, resY/2, options[0],resselection);
			if(resX != resArray[resolutionselection][0]){
			normalfont.drawString(100 + normalfont.getWidth(options[0]), resY/2, resArray[resolutionselection][0] + "x" + resArray[resolutionselection][1], resselection);
			}else{
				normalfont.drawString(100 + normalfont.getWidth(options[0]), resY/2, resX + "x" + resY, resselection);
			}
			normalfont.drawString(100, resY/2 + 20, options[1],volselection);
			normalfont.drawString(100 + normalfont.getWidth(options[1]), resY/2 + 20, Integer.toString((int)gc.getMusicVolume()*100), volselection);
			normalfont.drawString(100, resY/2 + 40, options[2],volstatselection);
			if(gc.isMusicOn() == true){
				normalfont.drawString(100 + normalfont.getWidth(options[2]) + 20, resY/2 + 40, "on", volstatselection);
			}else{
				normalfont.drawString(100 + normalfont.getWidth(options[2]) + 20, resY/2 + 40, "off", volstatselection);	
			}
			normalfont.drawString(100, resY/2 + 90, options[4],saveselection);
			normalfont.drawString(100 + normalfont.getWidth(options[4]) + 40, resY/2 + 90, options[5],exitselection);
			if(DEBUG == true){
				normalfont.drawString(100, resY/2 + 60, options[3],debugselection);
				normalfont.drawString(100 + normalfont.getWidth(options[3]) + 20, resY/2 + 60, Boolean.toString(DEBUG_AI),Color.white);
			}
		}
		
		if(currentmenustate == MenuState.CPU || currentmenustate == MenuState.PvP || currentmenustate == MenuState.LAN){
			pad1.draw(g);
			pad2.draw(g);
			ball.draw(g);
			//TODO: Change this or use another font
			g.drawString(Integer.toString(pad1.getPoints()), resX / 2 - 20, resY / 2);
			g.drawString(":", resX / 2, resY / 2);
			g.drawString(Integer.toString(pad2.getPoints()), resX / 2 + 20, resY / 2);
			if (DEBUG == true) {
				g.drawString("DEBUG Monitor", 75, 25);
				g.drawString("Ballvelocity: " + Double.toString(ball.getVelocity()), 75,40);
				g.drawString("LastCollision:" + lastcollision.toString(), 75, 55);
				g.drawString("LastPadCollision:" + lastpadcollision.toString(), 75, 70);
				g.drawString("Actual Vector: " + Float.toString(ball.getVectorX()) + "|" + Float.toString(ball.getVectorY()), 75, 85);
				g.drawString("Pad1 Position: " + Float.toString(pad1.getShape().getCenterY()) + " Pad2 Position: " + Float.toString(pad2.getShape().getCenterY()), 75, 100);
				g.drawString("Pad1 Spinspeed: " + Float.toString(pad1.getSpinspeed()), 75, 115);
				gc.setShowFPS(true);
			}
			
			if(gc.isPaused() == true){
				g.drawString("GAME PAUSED, PRESS " + "P" + " TO RESUME", resX / 2 - 135, resY / 2 + 50);
			}

			if (currentgamestate == GameState.BallIsOut) {
				g.drawString("Press ENTER to spawn a new ball!", resX / 2 - 135, resY / 2 + 30);
			}

			if (currentgamestate == GameState.Player1Wins) {
				g.drawString("Player 1 wins!", resX / 2 - 50, resY / 2 + 30);
				g.drawString("Press Enter to return to the mainmenu", resX / 2 - 160, resY / 2 + 50);
			}else if(currentgamestate == GameState.Player2Wins) {
				g.drawString("Player 2 wins!", resX / 2 - 50, resY / 2 + 30);
				g.drawString("Press Enter to return to the mainmenu", resX / 2 - 160, resY / 2 + 50);
			}			
		}


	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		Input input = gc.getInput();
		if(currentmenustate == MenuState.Main){
			if(playerselection == 0){
				cpuselection = Color.white;
				pvpselection = Color.gray;
			}else if(playerselection == 1){
				cpuselection = Color.gray;
				pvpselection = Color.white;
				lanselection = Color.gray;
			}else if(playerselection == 2){
				pvpselection = Color.gray;
				lanselection = Color.white;
				challengeselection = Color.gray;
			}else if(playerselection == 3){
				lanselection = Color.gray;
				challengeselection = Color.white;
				optionselection = Color.gray;
			}else if(playerselection == 4){
				challengeselection = Color.gray;
				optionselection = Color.white;
				helpselection = Color.gray;
			}else if(playerselection == 5){
				optionselection = Color.gray;
				helpselection = Color.white;
				quitselection = Color.gray;
			}else if(playerselection == 6){
				helpselection = Color.gray;
				quitselection = Color.white;
			}
			
			if(input.isKeyPressed(Input.KEY_UP) && playerselection > 0){
				playerselection -= 1;
			}
			if(input.isKeyPressed(Input.KEY_DOWN) && playerselection < 6){
				playerselection += 1;
			}
			if(input.isKeyPressed(Input.KEY_ENTER)){
				if(playerselection == 0){
					currentmenustate = MenuState.CPUSelection;
				}
				if(playerselection == 1){
					currentmenustate = MenuState.PvP;
					newGame(hard);
					currentgamestate = GameState.Play;
				}
				if(playerselection == 2){
					/*TODO
					 *Add Lanmode to game
					 *currentmenustate = MenuState.LAN;
					 *newGame();
					 */
				}
				if(playerselection == 3){
					currentmenustate = MenuState.Challenge;
				}
				if(playerselection == 4){
					currentmenustate = MenuState.Options;
				}
				if(playerselection == 5){
					
				}
				if(playerselection == 6){
					gc.exit();
				}

				//TODO: Add Challenge Mode, Options and Help to the menu
				
			}
		}
		
		if(currentmenustate == MenuState.CPUSelection){
			if(input.isKeyPressed(Input.KEY_ESCAPE)){
				abort();	
			}
			
			if(input.isKeyPressed(Input.KEY_LEFT) && difficultyselection > 0){
				difficultyselection -= 1;
			}
			if(input.isKeyPressed(Input.KEY_RIGHT) && difficultyselection < 3){
				difficultyselection += 1;
			}
			if(input.isKeyPressed(Input.KEY_ENTER)){
				if(difficultyselection == 0){
					currentmenustate = MenuState.CPU;
					newGame(easy);
					currentgamestate = GameState.Play;
				}
				if(difficultyselection == 1){
					currentmenustate = MenuState.CPU;
					newGame(medium);
					currentgamestate = GameState.Play;
				}
				if(difficultyselection == 2){
					currentmenustate = MenuState.CPU;
					newGame(hard);
					currentgamestate = GameState.Play;
				}
				if(difficultyselection == 3){
					currentmenustate = MenuState.CPU;
					newGame(unbeatable);
					currentgamestate = GameState.Play;
				}
			}
			
		}
		
		if(currentmenustate == MenuState.Options){
			if(input.isKeyPressed(Input.KEY_UP) && configselection > 0){
				if(configselection == 4 && DEBUG == false){
					configselection -= 2;
				}else{
					configselection -= 1;
				}
			}
			if(input.isKeyPressed(Input.KEY_DOWN) && configselection < 5){
				if(configselection == 2 && DEBUG == false){
					configselection += 2;
				}else{
					configselection += 1;
				}
			}
			
			if(configselection == 0){
				resselection = Color.white;
				volselection = Color.gray;
				if(input.isKeyPressed(Input.KEY_LEFT) && resolutionselection > 0){
					resolutionselection -= 1;
				}
				if(input.isKeyPressed(Input.KEY_RIGHT) && resolutionselection < 4){
					resolutionselection += 1;
				}
			}else if(configselection == 1){
				if(input.isKeyPressed(Input.KEY_LEFT) && gc.getMusicVolume() > 0){
					gc.setMusicVolume(gc.getMusicVolume()-0.01f);
				}
				if(input.isKeyPressed(Input.KEY_RIGHT) && gc.getMusicVolume() < 100){
					gc.setMusicVolume(gc.getMusicVolume()+0.01f);
				}
				resselection = Color.gray;
				volselection = Color.white;
				volstatselection = Color.gray;
			}else if(configselection == 2){
				if(input.isKeyPressed(Input.KEY_RIGHT) && gc.isMusicOn() == true ||  gc.isMusicOn() == true &&  input.isKeyPressed(Input.KEY_ENTER)){
					gc.setMusicOn(false);
				}
				if(input.isKeyPressed(Input.KEY_LEFT) && gc.isMusicOn() == false ||  gc.isMusicOn() == false && input.isKeyPressed(Input.KEY_ENTER)){
					gc.setMusicOn(true);
				}
				if(DEBUG == true){
					debugselection = Color.red;
				}
				volselection = Color.gray;
				volstatselection = Color.white;
				saveselection = Color.gray;
			}else if(configselection == 3){
				if(input.isKeyPressed(Input.KEY_RIGHT) && DEBUG_AI == true ||  DEBUG_AI == true &&  input.isKeyPressed(Input.KEY_ENTER)){
					DEBUG_AI = false;
				}
				if(input.isKeyPressed(Input.KEY_LEFT) && DEBUG_AI == false ||  DEBUG_AI == false && input.isKeyPressed(Input.KEY_ENTER)){
					DEBUG_AI = true;
				}
				volstatselection = Color.gray;
				debugselection = Color.pink;
				saveselection = Color.gray;
			}else if(configselection == 4){
				if(DEBUG == false){
					volstatselection = Color.gray;
				}else{
					debugselection = Color.red;
				}
				if(input.isKeyPressed(Input.KEY_ENTER)){
					try{
						resX = resArray[resolutionselection][0];
						resY = resArray[resolutionselection][1];
						prophelper.saveProperty("resX", Integer.toString(resX));
						prophelper.saveProperty("resY", Integer.toString(resY));
						prophelper.saveProperty("volume", Integer.toString((int)gc.getMusicVolume()));
						prophelper.saveProperty("vol_on", Boolean.toString(gc.isMusicOn()));
						prophelper.savePropertiesFile();
					}catch(NumberFormatException nfe){
						nfe.printStackTrace();
					}
					container.setDisplayMode(resX, resY, false);
				}
				saveselection = Color.white;
				exitselection = Color.gray;
			}else if(configselection == 5){
				if(input.isKeyPressed(Input.KEY_ENTER)){
					currentmenustate = MenuState.Main;	
				}
				saveselection = Color.gray;
				exitselection = Color.white;
			}
		}
		
		if(currentmenustate == MenuState.CPU || currentmenustate == MenuState.LAN || currentmenustate == MenuState.PvP || currentmenustate == MenuState.Challenge){
			// Pause Game
			if (gc.hasFocus() == false || gc.isPaused() == false && input.isKeyPressed(Input.KEY_P) ) {
				gc.setPaused(true);
			}
			if(gc.hasFocus() == true && input.isKeyPressed(Input.KEY_P) && gc.isPaused() == true){
				gc.setPaused(false);
			}
			
			// Abort Game
			if(input.isKeyPressed(Input.KEY_ESCAPE)){
				abort();
			}
			
			if (gc.isPaused() == false) {
	
				if (currentgamestate == GameState.Play || currentgamestate == GameState.BallIsOut) {
					// For player 1
					if (input.isKeyDown(Input.KEY_UP) ^ input.isKeyDown(Input.KEY_DOWN)) {
						if (pad1.getShape().getMinY() > 0.0 && pad1.getShape().getMaxY() < resY) {
							pad1.addSpinSpeed(0.005f * delta);
						}
					}else{
						pad1.resetSpinSpeed();
					}
					if (input.isKeyDown(Input.KEY_UP)) {
						if (pad1.getShape().getMinY() > 0.0) {
							pad1.getShape().setY((float) ((pad1.getShape().getY() - 10.0)));
						}
	
					}
					if (input.isKeyDown(Input.KEY_DOWN)) {
						if (pad1.getShape().getMaxY() < resY) {
							pad1.getShape().setY((float) ((pad1.getShape().getY() + 10.0)));
						}
	
					}
					
					if(currentmenustate == MenuState.PvP || currentmenustate == MenuState.LAN ){
						// For player 2
						if (input.isKeyDown(Input.KEY_W) ^ input.isKeyDown(Input.KEY_S)) {
							if (pad2.getShape().getMinY() > 0.0 && pad2.getShape().getMaxY() < resY) {
								pad2.addSpinSpeed(0.005f * delta);
							}
						}else{
							pad2.resetSpinSpeed();
						}
						
						if (input.isKeyDown(Input.KEY_W)) {
							if (pad2.getShape().getMinY() > 0.0) {
								pad2.getShape().setY((float) ((pad2.getShape().getY() - 10.0)));
							}
	
						}
						if (input.isKeyDown(Input.KEY_S)) {
							if (pad2.getShape().getMaxY() < resY) {
								pad2.getShape().setY((float) ((pad2.getShape().getY() + 10.0)));
							}
	
						}
					}
	
				}
	
				if (currentgamestate == GameState.Play) {
					
					if(DEBUG){
						if(input.isKeyDown(Input.KEY_R)){
							ball.addDebugVelocity(0.25f, delta);
						}
					}
					
					if(currentmenustate == MenuState.CPU){
						if(DEBUG_AI || currentmenustate == MenuState.Challenge) {
							if(ball.getShape().getMinY() >= resY - pad2.getHEIGHT() / 2) {
							}else if(ball.getShape().getMaxY() <= 0 + pad2.getHEIGHT() / 2) {
							}else{
								pad2.getShape().setCenterY(ball.getShape().getCenterY());
							}
						}else{
							if(ball.getShape().getCenterX() > resX/2 + 10 && collision == false){
								ball.calcTrajectory(ball.getVector().copy(), ball.getShape().getCenterX(), ball.getShape().getCenterY());
								collision = true;
							}
								
							}if(pad2.getShape().getCenterY() != resY/2 && lastpadcollision == Border.RIGHT){
								
								//Prevents that the AI pad is glitching while floating back to middle
								if(pad2.getShape().getCenterY() == resY/2 -1 ||pad2.getShape().getCenterY() == resY/2 +1 ){
									pad2.getShape().setCenterY(resY/2);
								}else{
									if(pad2.getShape().getCenterY() > resY/2){
										for(int i = 0; i <= 2.0f;i++){
											pad2.getShape().setCenterY(pad2.getShape().getCenterY() - 1.0f);
										}
									}else if(pad2.getShape().getCenterY() < resY/2){
										for(int i = 0; i <= 2.0f;i++){
											pad2.getShape().setCenterY(pad2.getShape().getCenterY() + 1.0f);
										}
									}
								}
							}
							if(ball.getShape().getCenterX() > resX/2 + 10 && lastpadcollision != Border.RIGHT){
								if(ball.getShape().getMaxY() > resY) {
									
								}else if(ball.getShape().getMinY() < 0) {
									
								}else{
									if(ball.getRoundedEtimatedY() < pad2.getShape().getCenterY() && pad2.getShape().getMinY() >= 0.0){
										for(int i = 0; i <= pad2.getVelocity();i++){
											pad2.getShape().setCenterY(pad2.getShape().getCenterY() - 1.0f);	
										}
									}
									if(ball.getRoundedEtimatedY() > pad2.getShape().getCenterY() && pad2.getShape().getMaxY() <= resY){
										for(int i = 0; i <= pad2.getVelocity();i++){
											pad2.getShape().setCenterY(pad2.getShape().getCenterY() + 1.0f);
										}
									}
								}
						}
					}
	
	
					ball.getShape().setCenterX(ball.getShape().getCenterX() + ball.getVectorX());
					ball.getShape().setCenterY(ball.getShape().getCenterY() + ball.getVectorY());
					ball.addVelocity(0.03, delta, lastcollision);
	
					if (ball.getShape().getMinY() <= 0 && lastcollision != Border.TOP) {
						ball.setVectorXY(ball.getVectorX(), -ball.getVectorY());
						lastcollision = Border.TOP;
					}
	
					if (ball.getShape().getMaxY() >= resY && lastcollision != Border.BOTTOM) {
						ball.setVectorXY(ball.getVectorX(), -ball.getVectorY());
						lastcollision = Border.BOTTOM;
					}
	
					if (pad1.intersects(ball.getShape()) && lastcollision != Border.LEFT) {
						if(pad1.getSpinspeed() > 0.0f){
							ball.addSpin(pad1.getSpinspeed());
						}
						ball.setVectorXY(-ball.getVectorX(), ball.getVectorY());
						lastpadcollision = Border.LEFT;
						lastcollision = Border.LEFT;
					}
	
					if (pad2.intersects(ball.getShape()) && lastcollision != Border.RIGHT) {
						if(currentmenustate == MenuState.PvP || currentmenustate == MenuState.LAN ){
							if(pad2.getSpinspeed() > 0.0f){
								System.out.println(ball.getVectorX() + "|" + ball.getVectorY());
								ball.addSpin(-pad2.getSpinspeed());
								System.out.println(ball.getVectorX() + "|" + ball.getVectorY());
							}
						}
						ball.setVectorXY(-ball.getVectorX(), ball.getVectorY());
						lastpadcollision = Border.RIGHT;
						lastcollision = Border.RIGHT;
	
					}
					
					if(ball.getShape().getCenterX() < resX/2 && collision == true){
						collision = false;
					}
	
					if(ball.getShape().getMaxX() < 0) {
						pad2.addPoint();
						currentgamestate = GameState.BallIsOut;
						lastcollision = Border.NONE;
						if(pad2.getPoints() >= goal) {
							currentgamestate = GameState.Player2Wins;
						}
					}
	
					if(ball.getShape().getMinX() > resX) {
						pad1.addPoint();
						currentgamestate = GameState.BallIsOut;
						lastcollision = Border.NONE;
						if (pad1.getPoints() >= goal) {
							currentgamestate = GameState.Player1Wins;
						}
					}
				}
				
				if(input.isKeyPressed(Input.KEY_ENTER)){
					newBall();
				}
	
				}
				if(currentgamestate == GameState.BallIsOut) {
					if(input.isKeyDown(Input.KEY_ENTER)) {
						ball = new Ball(resX / 2 - ballradius / 2, resY / 2 - ballradius / 2, ballradius);
						currentgamestate = GameState.Play;
					}
				}
				if(currentgamestate == GameState.Player1Wins || currentgamestate == GameState.Player2Wins){
					if(input.isKeyDown(Input.KEY_ENTER)) {
						currentgamestate = GameState.Start;
						currentmenustate = MenuState.Main;
					}
				}
		}
	}
	
	private void newGame(float paddifficulty){
		pad1 = new Pad(0 + 10, resY / 2, paddifficulty, 0);
		pad1.getShape().setCenterY(resY/2);
		pad2 = new Pad(resX - 20, resY / 2, paddifficulty, 0);
		pad2.getShape().setCenterY(resY/2);
		ball = new Ball(resX / 2 - ballradius / 2, resY / 2 - ballradius / 2, ballradius);
	}
	
	private UnicodeFont newFont(String font,int fontsize, boolean bold, boolean italic) throws SlickException{
		UnicodeFont unifont = new UnicodeFont(font,fontsize , bold, italic);
		unifont.addAsciiGlyphs();
		unifont.getEffects().add(new ColorEffect());
		unifont.loadGlyphs();
		return unifont;
	}
	
	private void newBall(){
		lastcollision = Border.NONE;
		lastpadcollision = Border.NONE;
		ball = new Ball(resX / 2 - ballradius / 2, resY / 2 - ballradius / 2, ballradius);
		currentgamestate = GameState.Play;
	}
	
	private void abort(){
		playerselection = 0;
		difficultyselection = 1;
		currentgamestate = GameState.Start;
		lastcollision = Border.NONE;
		lastpadcollision = Border.NONE;
		currentmenustate = MenuState.Main;
	}

}
