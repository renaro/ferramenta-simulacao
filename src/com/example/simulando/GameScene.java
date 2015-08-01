package com.example.simulando;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.SensorManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.example.simulando.SceneManager.SceneType;
import com.example.simulando.Coin.CoinType;
import com.example.simulando.SceneManager.SceneType;
public class GameScene extends BaseScene implements IOnSceneTouchListener,OnClickListener {

	private HUD gameHUD;
	private Text scoreText;
	private int score = 0;
	
	
	public FixedStepPhysicsWorld simulationPhysicsWorld;
	public Sprite simulationSpriteProjectile;
	public Body simulationBodyProjectile;
	public Body simulationArrayBodyWall[];
	private Rectangle simulationRectangleArrayWalls[];
    private Point simulationProjectilePosition;
    private Point initialProjectilePosition;
	
	
	/////////////xml variables///////
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_LEVEL_INITIAL_X = "initial-x";
	private static final String TAG_LEVEL_INITIAL_Y = "initial-y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type"; 
	private static final String TAG_ENTITY_ATTRIBUTE_ANGLE = "angle"; 
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_RECTANGLE = "rectangle";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROTATING1 = "rotating-body1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROTATING2 = "rotating-body2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_FINAL_COIN = "final-coin";
	/////////////////////////////////
	
	private ButtonSprite hudButtonAngle;
	private ButtonSprite hudButtonSpeed;
	private ButtonSprite hudButtonPlay;
	private ButtonSprite hudButtonReset;
	private ButtonSprite hudNextMove;
	
	
	
	
	public boolean simulationBooleanPausedObject;
	public boolean simulationBooleanSimulationOn=false;
	public boolean simulationBooleanButtonTouched=false;
	
	public boolean hudButtonAngleActive = new Boolean(false);
	public boolean hudButtonSpeedActive= new Boolean(false);
	public boolean hudButtonPlayActive= new Boolean(false);
	public boolean hudButtonResetActive= new Boolean(false);
	public boolean hudButtonMoveActive= new Boolean(false);
	
	private Dialog dialogGetSpeed;
	private Button botaoAddSpeed;
	
	public Parametros simulationParams;
	private EditText simulationEditTextSpeed;
	
	private Point simulationPointVectorA;
	private Point simulationPointVectorC;
	private Point simulationPointVectorB;
	private Line simulationLineVector;
	private Line simulationLineVectorA;
	private Line simulationLineVectorB;
	private Line simulationLineVectorC;
	
	private float wallWidth;
	private int raioVetor=150; // =~ 140/PIXEL_TO_METER_RATIO_DEFAULT = 140/32 =~ 4,5M.
	private ConfigVariables config;
	
	private Text simulationTextSomePoint;
    private Text simulationTextPositionX;
    private Text simulationTextPositionY;
    private Text simulationTextInitialVelocity;
    private Text simulationTextVelocityY;
    private Text simulationTextVelocityX;
    private Text simulationTextAngle;
    
    private Text gameTextStageWin;
    private boolean stageWinDisplayed = false;
	
	private ArrayList<Text> simulationListTextImportantPoints;
	private ArrayList<RectanglePlatform> simulationListRectangleParabole;
	
	private ArrayList<Coin> gameListCoins;
	private ArrayList<Sprite> gameListObjects;
	
	private LevelCompletedSprite levelCompletedSprite;
	
	private int gameIntStage;
	private int gameCoinsCount;
	private int gameCoinsPicked;
	
	private RevoluteJointDef revoluteJointDef;
	private Sprite gameBoard;
	
	public GameScene(int i) {
	super(i);
	
	
	
	}

	@Override
	public void createScene() {
		
		camera.set(-100, -100, 4900, 2660);
		gameIntStage=this.level;
		simulationParams= new Parametros();
		config= new ConfigVariables(SceneType.SCENE_GAME);
		wallWidth=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		initParameters();
		
		  createBackground();
		  createHUD(); 
		  createPhysics(); 
		  createProjectile();
		  createAngleVector();
		  
		  createWalls();
		  loadLevel(gameIntStage);
		  createVariablePanel();
		  createStageWinText();
		  
		  
		  setOnSceneTouchListener(this);
		  setTouchAreaBindingOnActionDownEnabled(true);
		  DebugRenderer debug = new DebugRenderer(simulationPhysicsWorld, vbom);
		  attachChild(debug);
		   
		  
		  
	}
	
	private void newStage(){
		
		while( !gameListCoins.isEmpty() ){ //limpar parabola desenhada
			gameListCoins.get(0).setVisible(false);
			gameListCoins.get(0).dispose();
			gameListCoins.remove(0);
			}
		while( !simulationListTextImportantPoints.isEmpty() ){ //limpar parabola desenhada
			simulationListTextImportantPoints.get(0).setVisible(false);
			simulationListTextImportantPoints.get(0).dispose();
			simulationListTextImportantPoints.remove(0);
			}
		while( !simulationListRectangleParabole.isEmpty() ){ //limpar parabola desenhada
			/// getting the rectangle
			RectanglePlatform rec=simulationListRectangleParabole.get(0);
			///unregistering and destroying its body
			simulationPhysicsWorld.unregisterPhysicsConnector(simulationPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(rec));
             simulationPhysicsWorld.destroyBody(rec.body);
			//simulationPhysicsWorld.destroyBody(simulationPhysicsWorld.getPhysicsConnectorManager().findBodyByShape());
			/////destroying sprite
			rec.setVisible(false);
			rec.dispose();
			///////removing the rectangle from list
			simulationListRectangleParabole.remove(0);
			}
			
		 
		
		levelCompletedSprite.setVisible(false);
		if(!levelCompletedSprite.isDisposed())
			levelCompletedSprite.dispose();

		registerListeners();
		cleanOldStage();
		loadLevel(++gameIntStage);
		
		
	}
	
	
	
	private void cleanOldStage() {
		stageWinDisplayed=false;
		simulationBooleanSimulationOn=false;
		levelCompletedSprite.detachChildren();
		disableButtons(-1);
		restart_object();
		simulationBodyProjectile.setTransform(new Vector2(0, 30),0f);
		simulationBodyProjectile.setAngularVelocity(0f);
		simulationBodyProjectile.setLinearVelocity(0f,0f);
		
		setAngleVectorVisible(true);
		resetScore();
		
	}

	private void createStageWinText()
	{
		gameTextStageWin = new Text(0, 0, resourcesManager.font, "Fase Concluída!", vbom);
	}
	private void displayStageWin()
	{
		levelCompletedSprite =new LevelCompletedSprite(camera.getWidth()*0.45f, camera.getHeight()*0.4f, camera.getWidth()*0.7f, camera.getHeight()*0.6f, vbom); 
    	
    	
		gameTextStageWin.setPosition(levelCompletedSprite.getWidth()/2, camera.getHeight()/2);
		levelCompletedSprite.attachChild(gameTextStageWin);
		levelCompletedSprite.attachChild(hudNextMove);
		registerTouchArea(hudNextMove);
		 stageWinDisplayed=true;
		 unregisterListeners();
	   attachChild(levelCompletedSprite);
	   
	  
	}
	
	private void unregisterListeners() {
		gameHUD.unregisterTouchArea(hudButtonAngle);
		gameHUD.unregisterTouchArea(hudButtonPlay);
		gameHUD.unregisterTouchArea(hudButtonSpeed);
		gameHUD.unregisterTouchArea(hudButtonReset);
		
	}
	
	private void registerListeners() {
		gameHUD.registerTouchArea(hudButtonAngle);
		gameHUD.registerTouchArea(hudButtonPlay);
		gameHUD.registerTouchArea(hudButtonSpeed);
		gameHUD.registerTouchArea(hudButtonReset);
		
	} 

	private void createVariablePanel() {
		
		gameBoard = new Sprite(3100, camera.getHeight() - resourcesManager.boardRegion.getHeight() + 45  , resourcesManager.boardRegion, vbom);
		gameBoard.setWidth(2600);
		attachChild(gameBoard);
		
		simulationTextPositionX = new Text(camera.getWidth()/2 - 200,2500 ,resourcesManager.font_score, " ",100,vbom){
			
			 @Override
			 protected void onManagedUpdate(float pSecondsElapsed) {
				 super.onManagedUpdate(pSecondsElapsed);
				 simulationTextPositionX.setText("Position X: "+simulationProjectilePosition.x);
		}
	};
	
	simulationTextPositionY = new Text(camera.getWidth()/2 - 200,2340 ,resourcesManager.font_score, " ",100,vbom){
		
		 @Override
		 protected void onManagedUpdate(float pSecondsElapsed) {
			 super.onManagedUpdate(pSecondsElapsed);
			 simulationTextPositionY.setText("Position Y: "+simulationProjectilePosition.y);
	}
};

simulationTextAngle = new Text(camera.getWidth()/2 + 600,2500 ,resourcesManager.font_score, " ",100,vbom){
	
	 @Override
	 protected void onManagedUpdate(float pSecondsElapsed) {
		 super.onManagedUpdate(pSecondsElapsed);
		 simulationTextAngle.setText("Angle: "+simulationParams.getAngulo());
}
};
	
simulationTextInitialVelocity = new Text(camera.getWidth()/2 + 600,2340 ,resourcesManager.font_score, " ",100,vbom){
	
	 @Override
	 protected void onManagedUpdate(float pSecondsElapsed) {
		 super.onManagedUpdate(pSecondsElapsed);
		 simulationTextInitialVelocity.setText("Initial Velocity: "+simulationParams.getVelocidade_inicial());
}
};

simulationTextVelocityX = new Text(camera.getWidth()/2 + 1400,2500 ,resourcesManager.font_score, " ",100,vbom){
	
	 @Override
	 protected void onManagedUpdate(float pSecondsElapsed) {
		 super.onManagedUpdate(pSecondsElapsed);
		 
		 float velocityX= simulationBodyProjectile.getLinearVelocity().x;
		 BigDecimal bd = new BigDecimal(Float.toString(velocityX));
	      bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		 simulationTextVelocityX.setText("Velocity X: "+bd.toString());
}
};

simulationTextVelocityY = new Text(camera.getWidth()/2 + 1400,2340 ,resourcesManager.font_score, " ",100,vbom){
	
	 @Override
	 protected void onManagedUpdate(float pSecondsElapsed) {
		 super.onManagedUpdate(pSecondsElapsed);
		 
		 float velocityY= simulationBodyProjectile.getLinearVelocity().y;
		 BigDecimal bd = new BigDecimal(Float.toString(velocityY));
	      bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
	      simulationTextVelocityY.setText("Velocity Y: "+bd.toString());
}
};

	
	
	attachChild(simulationTextPositionX);
	attachChild(simulationTextPositionY);
	attachChild(simulationTextAngle);
	attachChild(simulationTextInitialVelocity);
	attachChild(simulationTextVelocityX);
	attachChild(simulationTextVelocityY);
	
	}
	
	private void initParameters() {
		simulationBooleanSimulationOn=false;
		hudButtonMoveActive=false;
		
		initialProjectilePosition= new Point();
		simulationProjectilePosition= new Point();
		simulationListTextImportantPoints = new ArrayList<Text>();
		simulationListRectangleParabole=new ArrayList<RectanglePlatform>();
		gameListCoins = new ArrayList<Coin>();
		gameListObjects = new ArrayList<Sprite>();
		gameCoinsCount=0;
		gameCoinsPicked=0;
	}
	
	private void createProjectile() {
		simulationSpriteProjectile= new Sprite(900,camera.getHeight()/2, resourcesManager.simulationTextureRegionProjectile, vbom){
		@Override
		 protected void onManagedUpdate(float pSecondsElapsed) {
			 super.onManagedUpdate(pSecondsElapsed);
			 
			 if(!simulationBooleanSimulationOn){
				    simulationParams.setVectorOrigin((int)simulationSpriteProjectile.getSceneCenterCoordinates()[0], (int)simulationSpriteProjectile.getSceneCenterCoordinates()[1],simulationSpriteProjectile.getWidth());
				    simulationParams.setVectorDirection(simulationParams.getOrigin().x,simulationParams.getOrigin().y,raioVetor);
				    alinhaVetor();

				   }
			
	    	 simulationProjectilePosition.y=(int) simulationBodyProjectile.getPosition().y;		     
		     simulationProjectilePosition.x=(int)simulationBodyProjectile.getPosition().x;

	    	
			    


			}
		};
			
			 final FixtureDef PLAYER_FIX= PhysicsFactory.createFixtureDef(10.0f, 0.5f, 0.1f);		
			 simulationBodyProjectile  = PhysicsFactory.createCircleBody(simulationPhysicsWorld, simulationSpriteProjectile, BodyType.DynamicBody, PLAYER_FIX);	
			 simulationBodyProjectile.setSleepingAllowed(true);
			 simulationBodyProjectile.setTransform(new Vector2(0, 30), 0f);
			 simulationBodyProjectile.setUserData("projectile");
			 attachChild(simulationSpriteProjectile);
			
			 simulationPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(simulationSpriteProjectile, simulationBodyProjectile, true, true));
//			 createParaboleHandler();
			 
			 
}
	
	private void createAngleVector() {
		//Entity vector_container = new Entity(simulationSpriteProjectile.getSceneCenterCoordinates()[0],simulationSpriteProjectile.getSceneCenterCoordinates()[1]);
		 simulationParams.setVectorOrigin((int)simulationSpriteProjectile.getSceneCenterCoordinates()[0], (int)simulationSpriteProjectile.getSceneCenterCoordinates()[1],simulationSpriteProjectile.getWidth());
		 simulationParams.setVectorDirection(simulationParams.getOrigin().x,simulationParams.getOrigin().y,raioVetor);
		   
		
		calculaPontos(simulationParams.getDirection().x, simulationParams.getDirection().y,(int)simulationParams.getInitial_position().x,(int)simulationParams.getInitial_position().y, 40.0f);
		simulationLineVector = new Line(simulationParams.getOrigin().x ,simulationParams.getOrigin().y,simulationParams.getDirection().x,simulationParams.getDirection().y,vbom);
		//Line vector = new Line(simulationSpriteProjectile.getX()+ simulationSpriteProjectile.getWidth(),simulationSpriteProjectile.getY(),200,200,vbom);
		simulationLineVectorA = new Line(simulationPointVectorA.x,simulationPointVectorA.y, simulationPointVectorC.x,simulationPointVectorC.y,vbom);
		simulationLineVectorB = new Line(simulationPointVectorA.x,simulationPointVectorA.y, simulationPointVectorB.x,simulationPointVectorB.y,vbom);
		simulationLineVectorC = new Line(simulationPointVectorB.x,simulationPointVectorB.y, simulationPointVectorC.x,simulationPointVectorC.y,vbom);
            
//		Line teste = new Line(0,0, 2560,2560,vbom);
//		teste.setColor(Color.BLACK);
//		teste.setLineWidth(3);
//		attachChild(teste);
	
		simulationLineVector.setColor(Color.WHITE);
		simulationLineVector.setLineWidth(7);
    	
		simulationLineVectorA.setColor(Color.WHITE);
		simulationLineVectorA.setLineWidth(5);
    	
		simulationLineVectorB.setColor(Color.WHITE);
		simulationLineVectorB.setLineWidth(5);
    	
		simulationLineVectorC.setColor(Color.WHITE);
		simulationLineVectorC.setLineWidth(5);
    	
    	
    	attachChild(simulationLineVector);
    	attachChild(simulationLineVectorA);
    	attachChild(simulationLineVectorB);
    	attachChild(simulationLineVectorC);

    
		
	}	
	
	private void alinhaVetor() {
		calculaPontos(simulationParams.getDirection().x, simulationParams.getDirection().y,(int)simulationSpriteProjectile.getSceneCenterCoordinates()[0],(int)simulationSpriteProjectile.getSceneCenterCoordinates()[1], 40.0f);
	  
		simulationLineVector.setPosition(simulationParams.getOrigin().x,simulationParams.getOrigin().y, simulationParams.getDirection().x,simulationParams.getDirection().y);
		simulationLineVectorA.setPosition(simulationPointVectorA.x,simulationPointVectorA.y, simulationPointVectorC.x,simulationPointVectorC.y);
		simulationLineVectorB.setPosition(simulationPointVectorA.x,simulationPointVectorA.y, simulationPointVectorB.x,simulationPointVectorB.y);
		simulationLineVectorC.setPosition(simulationPointVectorB.x,simulationPointVectorB.y, simulationPointVectorC.x,simulationPointVectorC.y);
		
	}
	
	
	private void createWalls() {
		FixtureDef WALL_FIX= PhysicsFactory.createFixtureDef(0.0f, 0.0f, 1.f);
		simulationRectangleArrayWalls= new Rectangle[4];
		simulationRectangleArrayWalls= new Walls(wallWidth, camera, vbom,SceneType.SCENE_GAME).createWalls(simulationSpriteProjectile.getWidth());
		simulationArrayBodyWall =  new Body[4];
		
		////ground
		simulationArrayBodyWall[0]=PhysicsFactory.createBoxBody(simulationPhysicsWorld, simulationRectangleArrayWalls[0], BodyType.StaticBody, WALL_FIX);
		simulationArrayBodyWall[1]=PhysicsFactory.createBoxBody(simulationPhysicsWorld, simulationRectangleArrayWalls[1], BodyType.StaticBody, WALL_FIX);
		simulationArrayBodyWall[2]=PhysicsFactory.createBoxBody(simulationPhysicsWorld, simulationRectangleArrayWalls[2], BodyType.StaticBody, WALL_FIX);
		simulationArrayBodyWall[3]=PhysicsFactory.createBoxBody(simulationPhysicsWorld, simulationRectangleArrayWalls[3], BodyType.StaticBody, WALL_FIX);
		
		
		attachChild(simulationRectangleArrayWalls[0]);
		attachChild(simulationRectangleArrayWalls[1]);
		attachChild(simulationRectangleArrayWalls[2]);
		attachChild(simulationRectangleArrayWalls[3]);
	}

	@Override
	public void onBackKeyPressed() {
		camera.set(0, 0, 1024, 600);
		SceneManager.getInstance().loadMenuScene(engine,SceneType.SCENE_GAME);
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		   camera.setHUD(null);
		
	}

	
	private void createBackground() {
		//TODO BACKGROUND

		final float cameraWidth = camera.getWidth();
		final float cameraHeight = camera.getHeight();
		
		
		final float repeatingScale = 2f;
		
		RepeatingSpriteBackground mRepeatingBackground = new RepeatingSpriteBackground(camera.getWidth(), camera.getHeight(), resourcesManager.repeating_background,repeatingScale, vbom);
		
		//Background background = new Background(Color.BLUE);
		
			setBackground(mRepeatingBackground);
			setBackgroundEnabled(true);
		
		
	}
	
	private void createHUD()
	{
	    gameHUD = new HUD();
	    camera.setHUD(gameHUD); 
	    // CREATE SCORE TEXT
	    scoreText();
	    createHudButtons();
	
	    
	   
	}
	
	private void createHudButtons() {
		createAngleButton();
		createSpeedButton();
		createPlayButton();
		createResetButton();
		createNextButton();
		
		gameHUD.registerTouchArea(hudButtonAngle);
		gameHUD.registerTouchArea(hudButtonSpeed);
		gameHUD.registerTouchArea(hudButtonPlay);
		gameHUD.registerTouchArea(hudButtonReset);
		
		
		gameHUD.attachChild(hudButtonAngle);
		gameHUD.attachChild(hudButtonSpeed);
		gameHUD.attachChild(hudButtonPlay);
		gameHUD.attachChild(hudButtonReset);

		
	}



	private void scoreText() {
	    scoreText = new Text(1300, 2450f, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom); 
	    scoreText.setAnchorCenter(0, 0);
	    scoreText.setText("Tentativas: 0");
	    gameHUD.attachChild(scoreText);
		
	}

	private void addToScore(int i)
	{
	    score += i;
	    scoreText.setText("Score: " + score);
	}
	private void resetScore()
	{
	    score =0;
	    scoreText.setText("Score: " + score);
	    
	    gameCoinsPicked=0;
	    
	}
	
	private void createPhysics()
	{
		simulationPhysicsWorld= new FixedStepPhysicsWorld(40,new Vector2(0,-SensorManager.GRAVITY_EARTH), false,5,2); //SETAR GRAVIDADE

	    registerUpdateHandler(simulationPhysicsWorld);
	}
	
	private void loadLevel(int levelID)
	{
		  final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
		    
		    final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
		    
		    levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
		    {
		        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
		        {
		            final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_LEVEL_INITIAL_X);
		            final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_LEVEL_INITIAL_Y);
		            initialProjectilePosition.x=x;
		            initialProjectilePosition.y=y;
		            simulationBodyProjectile.setTransform(x, y, 0f);
		            // TODO later we will specify camera BOUNDS and create invisible walls
		            // on the beginning and on the end of the level.

		            return GameScene.this;
		        }
		    });
	    
	    levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
	    	    {
	    			

					@Override
	    	        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
	    	        {
	    	             int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
	    	             int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
	    	            final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
	    	            
	    	             Sprite levelObject=null;
	    	             Coin  coin = null;
	    	            
	    	            if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
	    	            {
	    	             	float x_ = x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	float y_ = y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	
	    	            	levelObject = new Sprite(x_, y_, resourcesManager.platform2_region, vbom);
	    	                PhysicsFactory.createBoxBody(simulationPhysicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
	    	                //
	    	                
	    	            	gameListObjects.add(levelObject);
	    	            
	    	            	return levelObject;
	    	            }
	    	            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_RECTANGLE))
	    	            {
	    	            	 int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
		    	             int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
		    	             int angle = SAXUtils.getIntAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_ANGLE,0);
		    	             
	    	             	float x_ = x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	float y_ = y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	width*=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	height*=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	
	    	            	RectanglePlatform rectangleLevelObject = new RectanglePlatform(x_, y_, width, height, vbom,angle);
	    	            	rectangleLevelObject.body= PhysicsFactory.createBoxBody(simulationPhysicsWorld, rectangleLevelObject, BodyType.StaticBody, FIXTURE_DEF);
	    	            	rectangleLevelObject.body.setTransform(rectangleLevelObject.body.getPosition(),(float)Math.toRadians(-angle));
	    	            	rectangleLevelObject.setRotationCenter(.5f,.5f);
	    	            	rectangleLevelObject.setRotation(angle);
	    	            	
	    	                simulationListRectangleParabole.add(rectangleLevelObject);
	    	                
	    	            	return rectangleLevelObject;
	    	            }
	    	            
	    	            else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROTATING1)){
	    	            	
	    	            	 int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
		    	             int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
		    	             //int angle = SAXUtils.getIntAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_A,0);
		    	             
	    	             	float x_ = x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	float y_ = y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	width*=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	height*=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	
	    	            	Rectangle staticRect = new Rectangle(x_, y_, 40, 40,vbom);
	    	            	staticRect.setColor(Color.GREEN);
	    	            	final Body staticRectBody = PhysicsFactory.createBoxBody(simulationPhysicsWorld, staticRect, BodyType.StaticBody, PhysicsFactory.createFixtureDef(0.0f, 0.0f, 1.f));
	    	            	
	    	            	Rectangle rotatingRectangle = new Rectangle(x_+120, y_, 240, 10,vbom);
	    	            	//rotatingRectangle.setAnchorCenter(0, 0);
	    	                rotatingRectangle.setColor(Color.RED);
	    	                final Body rotatingRectangleBody = PhysicsFactory.createBoxBody(simulationPhysicsWorld, rotatingRectangle, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1, 0.5f, 1f));
	    	                simulationPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rotatingRectangle, rotatingRectangleBody, true, true));
	    	                GameScene.this.attachChild(staticRect);
	    	                
	    	                revoluteJointDef = new RevoluteJointDef();
	    	                revoluteJointDef.initialize(staticRectBody, rotatingRectangleBody, staticRectBody.getWorldCenter());
	    	                revoluteJointDef.enableMotor = true;
	    	                revoluteJointDef.motorSpeed = (float)Math.toRadians(-60);
	    	                revoluteJointDef.maxMotorTorque = 100000;
	    	                
	    	                simulationPhysicsWorld.createJoint(revoluteJointDef);
	    	               // simulationPhysicsWorld.d
	    	            	
	    	            	return rotatingRectangle;
	    	            	//TODO IMPLEMENT
	    	            }
	    	            else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROTATING2)){
	    	            	
	    	            	 int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
		    	             int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
		    	             //int angle = SAXUtils.getIntAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_A,0);
		    	             
	    	             	float x_ = x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	float y_ = y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	width*=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	height*=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	
	    	            	Rectangle staticRect = new Rectangle(x_, y_, 40, 40,vbom);
	    	            	staticRect.setColor(Color.GREEN);
	    	            	final Body staticRectBody = PhysicsFactory.createBoxBody(simulationPhysicsWorld, staticRect, BodyType.StaticBody, PhysicsFactory.createFixtureDef(0.0f, 0.0f, 1.f));
	    	            	
	    	            	Rectangle rotatingRectangle = new Rectangle(x_+120, y_, 240, 10,vbom){
	    	            		
	    	            		@Override
	    	                    protected void onManagedUpdate(float pSecondsElapsed) 
	    	                    {
	    	                        super.onManagedUpdate(pSecondsElapsed);
	    	                        if(simulationSpriteProjectile.collidesWith(this)){
	    	                        	alertFailed();
	    	                        	resetScene();
	    	                        	  
	    	                        	
	    	                        }
	    	                        
	    	                    }
	    	            		
	    	            		
	    	            		
	    	            	};
	    	            	//rotatingRectangle.setAnchorCenter(0, 0);
	    	                rotatingRectangle.setColor(Color.RED);
	    	                final Body rotatingRectangleBody = PhysicsFactory.createBoxBody(simulationPhysicsWorld, rotatingRectangle, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1, 0.5f, 1f));
	    	                simulationPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rotatingRectangle, rotatingRectangleBody, true, true));
	    	                GameScene.this.attachChild(staticRect);
	    	                
	    	                revoluteJointDef = new RevoluteJointDef();
	    	                revoluteJointDef.initialize(staticRectBody, rotatingRectangleBody, staticRectBody.getWorldCenter());
	    	                revoluteJointDef.enableMotor = true;
	    	                revoluteJointDef.motorSpeed = (float)Math.toRadians(-60);
	    	                revoluteJointDef.maxMotorTorque = 100000;
	    	                
	    	                simulationPhysicsWorld.createJoint(revoluteJointDef);
	    	               // simulationPhysicsWorld.d
	    	                
	    	                
	    	                
	    	                
	    	            	return rotatingRectangle;
	    	            	//TODO IMPLEMENT
	    	            }
/*
	    	            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
	    	            {
	    	                levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
	    	                final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	    	                body.setUserData("platform2");
	    	                physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	    	            }*/
	    	            else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN)){
	    	            	float x_ = x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	float y_ = y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	coin = new Coin(CoinType.COIN, gameCoinsCount++,x_, y_,x,y, resourcesManager.coin_region, vbom,resourcesManager)
	    	                {
	    	                    

								@Override
	    	                    protected void onManagedUpdate(float pSecondsElapsed) 
	    	                    {
	    	                        super.onManagedUpdate(pSecondsElapsed);
	    	                        
	    	                        if(simulationSpriteProjectile.collidesWith(this)){
	    	                        	 if(!stageWinDisplayed){
	    	                        		gameCoinsPicked++;
	    	                        		this.setVisible(false);
	    	                        		this.textPosition.setVisible(false);
	    	                        		this.setIgnoreUpdate(true);
	    	                        		 
	    	                        		 
			    	                        }
	    	                        
	    	                        }
	    	        
	    	                    }
	    	                };
	    	                coin.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	    	            	
	    	                ///add to the list of points
	    	                simulationListTextImportantPoints.add(coin.textPosition);
	    	            	GameScene.this.attachChild(coin.textPosition);
	    	            	gameListCoins.add(coin);
	    	            	coin.setCullingEnabled(true);
	    	            	return coin;
	    	            	
	    	            }
	    	            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_FINAL_COIN))
	    	            {
	    	            	float x_ = x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	float y_ = y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	    	            	coin = new Coin(CoinType.FINAL_COIN, gameCoinsCount++,x_, y_,x,y, resourcesManager.coin_region, vbom,resourcesManager)
	    	                {
	    	                    

								@Override
	    	                    protected void onManagedUpdate(float pSecondsElapsed) 
	    	                    {
	    	                        super.onManagedUpdate(pSecondsElapsed);
	    	                        
	    	                        if(simulationSpriteProjectile.collidesWith(this)){
	    	                        	 if(!stageWinDisplayed){
	    	                        	pause_object();	
	    	                        	this.setVisible(false);
	    	                        	this.setIgnoreUpdate(true);
	    	                        	displayStageWin();
	    	                        	//gameHUD.attachChild(hudNextMove);
	    	                        	//gameHUD.registerTouchArea(hudNextMove);
	    	                        }
	    	                        
	    	                        }
	    	                        /** 
	    	                         * TODO
	    	                         * we will later check if player collide with this (coin)
	    	                         * and if it does, we will increase score and hide coin
	    	                         * it will be completed in next articles (after creating player code)
	    	                         */
	    	                    }
	    	                };
	    	                coin.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1.5f, 2.5f)));
	    	               
	    	                simulationListTextImportantPoints.add(coin.textPosition);
	    	            	GameScene.this.attachChild(coin.textPosition);
	    	            	gameListCoins.add(coin);
	    	              coin.setCullingEnabled(true);
	    	                return coin;
	    	                
	    	            }            
	    	            else
	    	            {
	    	                throw new IllegalArgumentException();
	    	            }

	    	       
	    	        }
	    	    });

	    	    levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
	    	}
	

private void createAngleButton() {
	
	hudButtonAngle= new ButtonSprite(50f,2350f, resourcesManager.simulationTextureRegionAngleButton,resourcesManager.simulationTextureRegionAngleButtonHover,resourcesManager.simulationTextureRegionAngleButtonHover, vbom){

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
			simulationBooleanButtonTouched=true;
				if(pSceneTouchEvent.isActionDown()){
					if(hudButtonAngle.isEnabled()){
						hudButtonAngle.setEnabled(false);
						disableButtons(0);
						hudButtonAngleActive=true;
						
					}
					else{
						hudButtonAngle.setEnabled(true);
						hudButtonAngleActive=false;
						
					}

				}
				else if(pSceneTouchEvent.isActionMove()){
		
				return false;	
				}
			return false;
				
		}

	
	};
		
		
	
	hudButtonAngle.setAnchorCenter(0, 0);
	
}

private void createSpeedButton(){
	hudButtonSpeed= new ButtonSprite(350f,2350f, resourcesManager.simulationTextureRegionSpeedButton,resourcesManager.simulationTextureRegionSpeedButtonHover,resourcesManager.simulationTextureRegionSpeedButtonHover, vbom){

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		simulationBooleanButtonTouched=true;
			if(pSceneTouchEvent.isActionDown()){
				if(hudButtonSpeed.isEnabled()){
					hudButtonSpeed.setEnabled(false);
					disableButtons(1);
					
					
					
					activity.runOnUiThread(new Runnable(){
						@Override
						public void run() {
							dialogGetSpeed = new Dialog(activity, R.style.NoTitle);
							dialogGetSpeed.setContentView(R.layout.add_parametro);
							botaoAddSpeed = (Button)dialogGetSpeed.findViewById(R.id.b_addparam);
							TextView entradaParametros=(TextView)dialogGetSpeed.findViewById(R.id.tv_entrada_parametros);
							entradaParametros.setText(R.string.velocidade_vetor);
							botaoAddSpeed.setId(2222);
							botaoAddSpeed.setOnClickListener(GameScene.this);	
							simulationEditTextSpeed= (EditText)dialogGetSpeed.findViewById(R.id.valor_parametro);
					    	dialogGetSpeed.show();
							}
			    	
						});
				}
				else{
					hudButtonSpeed.setEnabled(true);
					
				}

			}
		
		return false;
			
	}


};
	
	
	
hudButtonSpeed.setAnchorCenter(0, 0);
	
}

private void createPlayButton() {
	hudButtonPlay= new ButtonSprite(650f,2350f, resourcesManager.simulationTextureRegionPlayButton,resourcesManager.simulationTextureRegionPlayButtonHover,resourcesManager.simulationTextureRegionPlayButtonHover, vbom){
		
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
			
			simulationBooleanButtonTouched=true;
			
				if(pSceneTouchEvent.isActionDown()){
					if(hudButtonPlay.isEnabled()){
					
						if(simulationParams.getVelocidade_inicial() != Integer.valueOf(0)){
						
						simulationBooleanSimulationOn=true;
						simulationBooleanPausedObject=false;
					
						
						restart_object();
						
						
						setAngleVectorVisible(false);
					    simulationParams.setVelocidade_inicial(simulationParams.getVelocidade_inicial()); /// remover isto depois, apenas para testar
					    simulationParams.reload();
					   
					    //drawPointText();
					    simulationBodyProjectile.setLinearVelocity(simulationParams.getLinearVelocity().x,simulationParams.getLinearVelocity().y);
					    addToScore(1); 
						}
						else alertSpeed();
						
					}
					

				}
			
			return false;
				
		}
		
		
	};
	hudButtonPlay.setAnchorCenter(0, 0);
	
}


private void createResetButton() {
	hudButtonReset= new ButtonSprite(950f,2350f, resourcesManager.simulationTextureRegionResetButton,resourcesManager.simulationTextureRegionResetButtonHover,resourcesManager.simulationTextureRegionResetButtonHover, vbom){
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
			
			simulationBooleanButtonTouched=true;
				if(pSceneTouchEvent.isActionDown()){
					
						hudButtonReset.setEnabled(false);
						disableButtons(3);
						
						resetScene();
						
		

						
			
						
			

				}
		
			return false;
				
		}


		
		
		
	};
	
	
	hudButtonReset.setAnchorCenter(0, 0);
	
}

private void resetScene() {
	simulationBooleanSimulationOn=false;
	restart_object();

	simulationBodyProjectile.setTransform(initialProjectilePosition.x,initialProjectilePosition.y,0f);
	simulationBodyProjectile.setAngularVelocity(0f);
	simulationBodyProjectile.setLinearVelocity(0f,0f);
	
	//alinhaVetor();
	setAngleVectorVisible(true);
	resetScore();
	for(int i=0;i<gameListCoins.size();i++ ){ //limpar parabola desenhada
		gameListCoins.get(i).setVisible(true);
		gameListCoins.get(i).setIgnoreUpdate(false);
		gameListCoins.get(i).textPosition.setVisible(true);
			}
	
}

private void createNextButton() {
	hudNextMove= new ButtonSprite(camera.getCenterX() + 200,camera.getCenterY() - 400, resourcesManager.simulationTextureRegionMoveButton,resourcesManager.simulationTextureRegionMoveButtonHover,resourcesManager.simulationTextureRegionMoveButtonHover, vbom){
		
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
			simulationBooleanButtonTouched=true;
				if(pSceneTouchEvent.isActionDown()){
					newStage();
				
				}
		
			return false;
				
		}
		
		
		
	};
	hudNextMove.setAnchorCenter(0, 0);
	
}



/**
 * @param i is the button that WILL NOT be disabled
 * 0=angle
 * 1=speed
 * 2=play
 * 3=reset
 * 4=move
 */
private void disableButtons(int i) {
if(i != 0){hudButtonAngle.setEnabled(true);hudButtonAngleActive=false;}
if(i != 1){hudButtonSpeed.setEnabled(true);hudButtonSpeedActive=false;}
if(i != 2){hudButtonPlay.setEnabled(true);hudButtonPlayActive=false;}
if(i != 3){hudButtonReset.setEnabled(true);hudButtonResetActive=false;}

	
}


public void pause_object() {
	simulationPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(simulationSpriteProjectile).getBody().setActive(false);
	//unregisterUpdateHandler(simulationTimeHandler);
	simulationBooleanPausedObject=true;
}
public void restart_object() {
	if(stageWinDisplayed) return;
	simulationPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(simulationSpriteProjectile).getBody().setActive(true);
	simulationBooleanPausedObject=false;
//	if(getUpdateHandlerCount() == 1){
//		registerUpdateHandler(simulationTimeHandler);
//
//	}
	
}

private void setAngleVectorVisible(boolean b) {
    simulationLineVector.setVisible(b);
    simulationLineVectorA.setVisible(b);
    simulationLineVectorB.setVisible(b);
    simulationLineVectorC.setVisible(b);
	
}


private void calculaPontos(int posX, int posY, int posX2,
		int posY2,float size) {

float r = (float) Math.sqrt(Math.pow(posX - posX2, 2) + Math.pow(posY - posY2, 2));
float cos = (posX - posX2) / (r); 
float sen = (posY - posY2) / (r);
int transX = (int) ((posX)); 
int transY = (int) ((posY) ); 
/*
*Atribuir novos valores para pontos 'a', 'b' e 'c' 
*/
simulationPointVectorA = new Point(Math.round(-sen * size) + (transX), Math.round(cos * size) + (transY));   
simulationPointVectorB = new Point(Math.round(-sen * -size) + (transX), Math.round(cos * -size) + (transY)); 
simulationPointVectorC = new Point(Math.round(cos * size) + (transX), Math.round(sen * size) + (transY));    

}

@Override
public void onClick(View v) {
	if(v.getId() == 2222){
		dialogGetSpeed.dismiss();
		
		if(simulationEditTextSpeed.getText().length() >0){
			int velocidade_inicial=Integer.valueOf(simulationEditTextSpeed.getText().toString());
			if(velocidade_inicial!= Integer.valueOf(0))
				simulationParams.setVelocidade_inicial(velocidade_inicial);
			else alertSpeed();
		}
		else alertSpeed();
		
		
	}
	
}


private void drawPointText() {
	int px=(int)simulationSpriteProjectile.getSceneCenterCoordinates()[0];
	int py=(int)simulationSpriteProjectile.getSceneCenterCoordinates()[1];
	
	simulationTextSomePoint = new Text(px ,py+80 ,resourcesManager.font,""+simulationProjectilePosition.x+" , "+simulationProjectilePosition.y+"",vbom);
	attachChild(simulationTextSomePoint);
	simulationListTextImportantPoints.add(simulationTextSomePoint);
	
}


private void alertSpeed() {
 	activity.runOnUiThread(new Runnable(){
		@Override
		public void run() {
			Toast.makeText(activity,R.string.escolha_outra_velocidade, Toast.LENGTH_SHORT).show();
		}

	
	
	});
}

private void alertFailed() {
 	activity.runOnUiThread(new Runnable(){
		@Override
		public void run() {
			Toast.makeText(activity,R.string.nao_toque_este_objeto, Toast.LENGTH_SHORT).show();
		}

	
	
	});
}


@Override
public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
	
if(!simulationBooleanButtonTouched)	{
	if(pSceneTouchEvent.isActionDown()){
		if(simulationBooleanSimulationOn){
			if(!simulationBooleanPausedObject)
				pause_object();
			else restart_object();
				
		}
	}	
	else if(hudButtonAngleActive && (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove())  ){
			
				///get touch event positions
			 	int pointx=Math.round(pSceneTouchEvent.getX());
				int pointy=Math.round( pSceneTouchEvent.getY());
				// get the projectile positions
				int px=(int)simulationSpriteProjectile.getSceneCenterCoordinates()[0];
				int py=(int)simulationSpriteProjectile.getSceneCenterCoordinates()[1];
				
				
				// x higher than reference point and y lower than reference point
				//Is this a valid point? (first quadrant)
				if(  pointx >=px   && pointy >= py ){
					
					int xDiff = pointx -px;  // 10 is the padding
					int yDiff = pointy - py ;  // 10 is the padding
					
					BigDecimal relative_angle = new BigDecimal(Math.atan2(yDiff, xDiff));
					relative_angle= relative_angle.multiply(new BigDecimal((180 / Math.PI)));
					relative_angle = relative_angle.setScale(0, BigDecimal.ROUND_HALF_UP);
				    simulationParams.setAngulo(Integer.valueOf(""+relative_angle.toString()));
			 	}
				
		 }
		
		 
		
}

//to asure that just 1 touch is being proccessed
simulationBooleanButtonTouched=false;
return false;
		
}

}
