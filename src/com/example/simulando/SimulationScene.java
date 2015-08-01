package com.example.simulando;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
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
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.simulando.SceneManager.SceneType;
import com.example.simulando.SceneManager.SceneType;
public class SimulationScene extends BaseScene implements IOnSceneTouchListener,OnClickListener {
	//private SimulationControlls simulationHUD;
	private HUD simulationHUD_;

	public FixedStepPhysicsWorld simulationPhysicsWorld;
	public Sprite simulationSpriteProjectile;
	public Sprite simulationBoard;
	public Body simulationBodyProjectile;
	public Body simulationArrayBodyWall[];
	private Rectangle simulationRectangleArrayWalls[];
    private Point simulationProjectilePosition;
	
	public boolean simulationBooleanMovingObject;
	public boolean simulationBooleanPausedObject;
	public boolean simulationBooleanSimulationOn=false;
	public boolean simulationBooleanHighestPointFound;
	public boolean simulationBooleanButtonTouched=false;
	
	private Point simulationPointVectorA;
	private Point simulationPointVectorC;
	private Point simulationPointVectorB;
	
	public Parametros simulationParams;
	private EditText simulationEditTextSpeed;
	
	
	private int raioVetor=150;
	private Line simulationLineVector;
	private Line simulationLineVectorA;
	private Line simulationLineVectorB;
	private Line simulationLineVectorC;
	
	public boolean hudButtonAngleActive = new Boolean(false);
	public boolean hudButtonSpeedActive= new Boolean(false);
	public boolean hudButtonPlayActive= new Boolean(false);
	public boolean hudButtonResetActive= new Boolean(false);
	public boolean hudButtonMoveActive= new Boolean(false);
	
	private ButtonSprite hudButtonAngle;
	private ButtonSprite hudButtonSpeed;
	private ButtonSprite hudButtonPlay;
	private ButtonSprite hudButtonReset;
	private ButtonSprite hudButtonMove;
	
    private ArrayList<Rectangle> simulationListRectangleParabole = new ArrayList<Rectangle>();
    private ArrayList<Text> simulationListTextImportantPoints = new ArrayList<Text>();
    
    private Rectangle simulationRectangleParabolePoint;
	
	private Button botaoAddSpeed;
	
	private Dialog dialogGetSpeed;
	
	private float wallWidth;
	
    private Text simulationTextPositionX;
    private Text simulationTextPositionY;
    private Text simulationTextInitialVelocity;
    private Text simulationTextVelocityY;
    private Text simulationTextVelocityX;
    private Text simulationTextAngle;
    private Text simulationTextSomePoint;

	private TimerHandler simulationTimeHandler;
	
	private float simulationFloatParaboleInterval=0.2f;

	private ConfigVariables config;
	

	@Override
	public void createScene() {
		
		camera.set(-100, -100, 4900, 2660);
		initParameters();
		simulationParams= new Parametros();
		raioVetor=150;
		config= new ConfigVariables(SceneType.SCENE_SIMULATION);
		wallWidth=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	
		  createBackground();
		  createHUD();
		  
		  createPhysics();
		  createProjectile();
		  createAngleVector();
		 
		  createWalls();
		  createVariablePanel();
		  
		  
		  
		  setOnSceneTouchListener(this);
		  setTouchAreaBindingOnActionDownEnabled(true);

		  DebugRenderer debug = new DebugRenderer(simulationPhysicsWorld, vbom);
		  attachChild(debug);
	}
	

	private void createVariablePanel() {
		simulationBoard = new Sprite(3100, camera.getHeight() - resourcesManager.boardRegion.getHeight() + 45  , resourcesManager.boardRegion, vbom);
		simulationBoard.setWidth(2600);
		attachChild(simulationBoard);
		
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
	      bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
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
		simulationProjectilePosition= new Point();
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
		simulationRectangleArrayWalls= new Walls(wallWidth, camera, vbom,SceneType.SCENE_SIMULATION).createWalls(simulationSpriteProjectile.getWidth());
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
	//	 BigDecimal bd = new BigDecimal(simulationProjectilePosition.y);
	 //    bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
	  //   simulationProjectilePosition.y=bd.intValue();
	     
	     simulationProjectilePosition.x=(int)simulationBodyProjectile.getPosition().x;
	    // bd = new BigDecimal(simulationProjectilePosition.x);
	    // bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
	    // simulationProjectilePosition.x=bd.intValue();
    	
		    


		}
	};
		//c simulationSpriteProjectile.setAnchorCenter(0, 0);
		 final FixtureDef PLAYER_FIX= PhysicsFactory.createFixtureDef(10.0f, 0.5f, 0.1f);		
		 simulationBodyProjectile  = PhysicsFactory.createCircleBody(simulationPhysicsWorld, simulationSpriteProjectile, BodyType.DynamicBody, PLAYER_FIX);	
		 simulationBodyProjectile.setSleepingAllowed(true);
		 simulationBodyProjectile.setTransform(new Vector2(0, 30), 0f);
		 simulationBodyProjectile.setUserData("projectile");
		 attachChild(simulationSpriteProjectile);
		
		 simulationPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(simulationSpriteProjectile, simulationBodyProjectile, true, true));
		 createParaboleHandler();
		 
		 
}

	

	
	


	@Override
	public void onBackKeyPressed() {
		camera.set(0, 0, 1024, 600);
		SceneManager.getInstance().loadMenuScene(engine,SceneType.SCENE_SIMULATION);
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SIMULATION;
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
		//simulationHUD = new SimulationControlls(resourcesManager, vbom,this);	
		simulationHUD_= new HUD();
	    camera.setHUD(simulationHUD_);
	    createHudButtons();
	    
	   
	}
	
	private void createHudButtons() {
		createAngleButton();
		createSpeedButton();
		createPlayButton();
		createResetButton();
		createMoveButton();
		
		simulationHUD_.registerTouchArea(hudButtonAngle);
		simulationHUD_.registerTouchArea(hudButtonSpeed);
		simulationHUD_.registerTouchArea(hudButtonPlay);
		simulationHUD_.registerTouchArea(hudButtonReset);
		simulationHUD_.registerTouchArea(hudButtonMove);
		
		simulationHUD_.attachChild(hudButtonAngle);
		simulationHUD_.attachChild(hudButtonSpeed);
		simulationHUD_.attachChild(hudButtonPlay);
		simulationHUD_.attachChild(hudButtonReset);
		simulationHUD_.attachChild(hudButtonMove);
		
	}








	
	private void createPhysics()
	{
		simulationPhysicsWorld= new FixedStepPhysicsWorld(40,new Vector2(0,-SensorManager.GRAVITY_EARTH), false,5,2); //SETAR GRAVIDADE
		registerUpdateHandler(simulationPhysicsWorld);
		
		simulationPhysicsWorld.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beginContact(Contact contact) {
			  	if(simulationBooleanSimulationOn)
		    	   {
		    	   
		    		  //	body.setLinearVelocity(new Vector2(0,0));
			  			simulationBooleanSimulationOn=false;
				    	setAngleVectorVisible(true);
				    	simulationBooleanHighestPointFound=false;
				    	
				    	drawPointText();
				    	
		    	   
		    	}	
				
			}
		});
				

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
			simulationBooleanMovingObject=false;
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
								botaoAddSpeed.setOnClickListener(SimulationScene.this);	
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
							simulationBooleanMovingObject=false;
							simulationBooleanSimulationOn=true;
							simulationBooleanPausedObject=false;
						
							
							restart_object();
							
							simulationBooleanHighestPointFound=false;
							setAngleVectorVisible(false);
						    simulationParams.setVelocidade_inicial(simulationParams.getVelocidade_inicial()); /// remover isto depois, apenas para testar
						    simulationParams.reload();
						   
						    drawPointText();
						    simulationBodyProjectile.setLinearVelocity(simulationParams.getLinearVelocity().x,simulationParams.getLinearVelocity().y);
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
				simulationBooleanMovingObject=false;
				simulationBooleanButtonTouched=true;
					if(pSceneTouchEvent.isActionDown()){
						
							hudButtonReset.setEnabled(false);
							disableButtons(3);
							
							simulationBooleanSimulationOn=false;
							simulationBooleanHighestPointFound=false;
							simulationBooleanMovingObject=false;
							
							restart_object();
							
							simulationBodyProjectile.setTransform(new Vector2(0, 30),0f);
							simulationBodyProjectile.setAngularVelocity(0f);
							simulationBodyProjectile.setLinearVelocity(0f,0f);
							alinhaVetor();
							setAngleVectorVisible(true);
							while( !simulationListRectangleParabole.isEmpty() ){ //limpar parabola desenhada
									simulationListRectangleParabole.get(0).setVisible(false);
									simulationListRectangleParabole.remove(0);
									}
							while( !simulationListTextImportantPoints.isEmpty() ){ //limpar parabola desenhada
								
								simulationListTextImportantPoints.get(0).setVisible(false);
								simulationListTextImportantPoints.remove(0);
							}
							
				
							
				

					}
			
				return false;
					
			}
			
			
			
		};
		
		
		hudButtonReset.setAnchorCenter(0, 0);
		
	}
	
	private void createMoveButton() {
		hudButtonMove= new ButtonSprite(1250f,2350f, resourcesManager.simulationTextureRegionMoveButton,resourcesManager.simulationTextureRegionMoveButtonHover,resourcesManager.simulationTextureRegionMoveButtonHover, vbom){
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				simulationBooleanButtonTouched=true;
					if(pSceneTouchEvent.isActionDown()){
						
						if(hudButtonMove.isEnabled() && !simulationBooleanMovingObject){
							hudButtonMove.setEnabled(false);
							hudButtonMoveActive=true;
							disableButtons(4);
							simulationBooleanMovingObject=true; // the object now can be moved
							pause_object(); // pause the object	
							simulationBodyProjectile.setLinearVelocity(0, 0);
							return true;
						}else{
									///only for matter of effect, enabled is setted to true(hover)
								restart_object();
								hudButtonMove.setEnabled(true);
								simulationBooleanMovingObject=false;
								hudButtonMoveActive=false;
								return true;
						}
					

					}
			
				return false;
					
			}
			
			
			
		};
		hudButtonMove.setAnchorCenter(0, 0);
		
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
	if(i != 4){hudButtonMove.setEnabled(true);hudButtonMoveActive=false;}
		
	}
	
	
	public void pause_object() {
		simulationPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(simulationSpriteProjectile).getBody().setActive(false);
		unregisterUpdateHandler(simulationTimeHandler);
		simulationBooleanPausedObject=true;
	}
	public void restart_object() {
		simulationPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(simulationSpriteProjectile).getBody().setActive(true);
		simulationBooleanPausedObject=false;
		if(getUpdateHandlerCount() == 1){
			registerUpdateHandler(simulationTimeHandler);
	
		}
		
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

private void alertSpeed() {
 	activity.runOnUiThread(new Runnable(){
		@Override
		public void run() {
			Toast.makeText(activity,R.string.escolha_outra_velocidade, Toast.LENGTH_SHORT).show();
		}

	
	
	});
}

	private void setAngleVectorVisible(boolean b) {
	     simulationLineVector.setVisible(b);
	     simulationLineVectorA.setVisible(b);
	     simulationLineVectorB.setVisible(b);
	     simulationLineVectorC.setVisible(b);
		
	}
	
	private void drawPointText() {
		int px=(int)simulationSpriteProjectile.getSceneCenterCoordinates()[0];
		int py=(int)simulationSpriteProjectile.getSceneCenterCoordinates()[1];
		simulationTextSomePoint = new Text(px ,py+80 ,resourcesManager.font,""+simulationProjectilePosition.x+" , "+simulationProjectilePosition.y+"",vbom);
		attachChild(simulationTextSomePoint);
		simulationListTextImportantPoints.add(simulationTextSomePoint);
		
	}

	
private void createParaboleHandler() {
		
		simulationTimeHandler=new TimerHandler(0.1f, true, 	new ITimerCallback() {
		    

			@Override
		    public void onTimePassed(TimerHandler pTimerHandler) {

		    	if(simulationBooleanSimulationOn && !simulationBooleanPausedObject){ // every 0.1 second if the simulation is on
		    		int px=(int)simulationSpriteProjectile.getSceneCenterCoordinates()[0];
		    		int py=(int)simulationSpriteProjectile.getSceneCenterCoordinates()[1];
		    					/// 			round down (   the abolute value ( of the linear velocity.y)        )	
		    	
		    	
		    		simulationRectangleParabolePoint=new Rectangle(px, py,4, 4,vbom);
		    		simulationRectangleParabolePoint.setColor(Color.WHITE);
		    		if(!simulationBooleanHighestPointFound){
			    		float difY =  (float) Math.floor(Math.abs(simulationBodyProjectile.getLinearVelocity().y)) ;
			    		/////////// if it is 0, then paint it red
			    		Log.d("velocidade eixo y="+Math.abs(simulationBodyProjectile.getLinearVelocity().y),"ENTROU1");
						if(Float.compare(0f, difY) == 0){
							Log.d("PONTO ENCONTRADO","ENTROU1");
							simulationBooleanHighestPointFound=true;
						    drawPointText();
						    simulationRectangleParabolePoint=new Rectangle(px, py,16, 16,vbom);
						    simulationRectangleParabolePoint.setColor(Color.RED);
							
						
						}
					}
				
					
					simulationListRectangleParabole.add(simulationRectangleParabolePoint);
					attachChild(simulationRectangleParabolePoint);
		    	}
					
			
		                 
		         //  pTimerHandler.reset();

		    }
		});
				registerUpdateHandler(simulationTimeHandler);
	
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
			else if(simulationBooleanMovingObject && (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove())){
					
					//float pointx=pSceneTouchEvent.getX()/Ph
					
					float pointx=pSceneTouchEvent.getX()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
					
					float p[]=convertLocalCoordinatesToSceneCoordinates(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					float spriteOffsiteX = simulationSpriteProjectile.getWidth()/(2*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
					float spriteOffsiteY = simulationSpriteProjectile.getHeight()/(2*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
					//actual position = x/PixelRatio + sprite (width/2)/PixelRatio
					p[0]=p[0]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
					p[1]=p[1]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
					
					float offset=wallWidth/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT ;//+ simulationSpriteProjectile.getWidth()/(2*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
					
					//p[0] and p[1] reffers to projectile center, so we have do add and subtract its height and width
					if (p[0] - spriteOffsiteX <= 0.f)p[0]=0;
					if (p[1] - spriteOffsiteY <= 0.f)p[1]=0;
					
					if (p[0] + spriteOffsiteX >= config.SIMULATION_AREA_X-offset-spriteOffsiteX )p[0]=config.SIMULATION_AREA_X -offset-spriteOffsiteX;
					if (p[1] + spriteOffsiteY>= config.SIMULATION_AREA_Y -offset-spriteOffsiteY)p[1]=config.SIMULATION_AREA_Y - offset-spriteOffsiteY;
					
					simulationBodyProjectile.setTransform(p[0], p[1], 0f);
					
				}
			 
			
}

//to asure that just 1 touch is being proccessed
simulationBooleanButtonTouched=false;
return false;
			
}


}
