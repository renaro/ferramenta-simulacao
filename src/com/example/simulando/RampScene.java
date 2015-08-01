package com.example.simulando;

import java.math.BigDecimal;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import android.graphics.PointF;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.example.simulando.SceneManager.SceneType;
import com.example.simulando.SceneManager.SceneType;

public class RampScene extends BaseScene implements IOnSceneTouchListener ,IAccelerationListener {
	
	public FixedStepPhysicsWorld rampPhysicsWorld;
	private ConfigVariables config;
	private float wallWidth;
	private Sprite rampSpriteProjectile;
	public Body rampBodyProjectile;
	public Body rampBodyBase;
	public Body rampArrayBodyWall[];
	public Body rampArrayBodyRamp[];
	private Rectangle rampRectangleArrayWalls[];
	private Rectangle base;
	private Rectangle projectile;

	private PointF rampPoints[];
	
	private Text simulationTextAngle;
	private TimerHandler simulationTimeHandler;
	private TimerHandler simulationTimeHandlerRotation;
	private FixtureDef WALL_FIX;
	private VectorsDrawer vd ;
	Line gravity_line;
	public RampScene(int stage) {
	super(stage); // stage = level
	
	}
	
	@Override
	public void createScene() {
		camera.set(0, 0, 25000, 1330);
		
		 
		config= new ConfigVariables(SceneType.SCENE_RAMP);
	    WALL_FIX= PhysicsFactory.createFixtureDef(10f, 10f, 5.f);
		wallWidth=PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		rampPoints= new PointF[3];
		config= new ConfigVariables(SceneType.SCENE_RAMP);
		float camera_width=config.SIMULATION_AREA_X * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + 200;
		float camera_height=config.SIMULATION_AREA_Y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + 200;
		camera.set(-100, -100,camera_width, camera_height);
		  createBackground();
	//	  createHUD(); 
		  createPhysics(); 
		
		  
		  createBase();
		  createProjectile();
		  vd = new VectorsDrawer(new Vector2(projectile.getX(),projectile.getY()), 2* SensorManager.GRAVITY_EARTH *PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT );
		 // createRamp();
		  
		  createVariablePanel();
		  
		  setOnSceneTouchListener(this);
		  setTouchAreaBindingOnActionDownEnabled(true);
		  DebugRenderer debug = new DebugRenderer(rampPhysicsWorld, vbom);
		  attachChild(debug);
		  
		  resourcesManager.engine.enableAccelerationSensor(activity, this);
		   gravity_line = new Line(projectile.getX(),projectile.getY(), vd.gravity_force.x,vd.gravity_force.y , vbom);
		  attachChild(gravity_line);
	}

	private void createVariablePanel() {
		simulationTextAngle = new Text(camera.getWidth()/10,camera.getHeight() - 200 ,resourcesManager.font_score, "TEste",100,vbom);
		attachChild(simulationTextAngle);
		
	simulationTimeHandlerRotation=new TimerHandler(0.3f, true, 	new ITimerCallback(){
		
		@Override
	    public void onTimePassed(TimerHandler pTimerHandler) {
			AccelerationData pAccelerationData = resourcesManager.engine.getAccelerationData();
			if (pAccelerationData.getY() == 0f)
					return;
			float angle =  (float)Math.atan(pAccelerationData.getX()/pAccelerationData.getY());
			if(angle <= 0f && angle>= -45f )angle=0;
			else if(angle <= 0f && angle<= -45f )angle=90;
		    rampBodyBase.setTransform(rampBodyBase.getPosition().x,rampBodyBase.getPosition().y, angle);
		    vd.compute(angle);
		    
			BigDecimal bd = new BigDecimal(Math.toDegrees(angle));
		    bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
		  
		    simulationTextAngle.setText("Angulo: "+bd.toString());
		    gravity_line.setPosition(projectile.getX(),projectile.getY(), vd.getGravityX(),vd.getGravityY());
		   
		    
			}
		});
	
		
	simulationTimeHandler=new TimerHandler(0.2f, true, 	new ITimerCallback() {
		    

			@Override
		    public void onTimePassed(TimerHandler pTimerHandler) {
				AccelerationData pAccelerationData = resourcesManager.engine.getAccelerationData();
				//TODO ENCONTRAR EQUACAO QUE CALCULA ANGULO ENTRE DOIS EIXO UTILIZANDO ACELEROMETRO
				float x = pAccelerationData.getX();
				float y = pAccelerationData.getY();
				float z = pAccelerationData.getZ();
				float anglexy,angleyz, anglexz;
				if(y != 0f)
					anglexy =  (float)Math.toDegrees(Math.atan(pAccelerationData.getX()/pAccelerationData.getY()));
				

				BigDecimal bd = new BigDecimal(0);
			    bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
			  
			   // simulationTextAngle.setText("Angulo: ");
			    
				}
			});
		registerUpdateHandler(simulationTimeHandler);
		registerUpdateHandler(simulationTimeHandlerRotation);
		
			
	}
	
	private void createRamp() {
		rampArrayBodyRamp= new Body[3];
		
		float y_base= 0- rampSpriteProjectile.getWidth()/2;
		rampPoints[0] = new PointF(500 , y_base);
		rampPoints[1] = new PointF(500, 200);
		rampPoints[2] = new PointF(2000, y_base);

		
		FixtureDef RAMP_FIX= PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0);
		Line lineHeight = new Line(rampPoints[0].x, rampPoints[0].y, rampPoints[1].x, rampPoints[1].y, vbom);
		rampArrayBodyRamp[0]=PhysicsFactory.createLineBody(rampPhysicsWorld, lineHeight, RAMP_FIX);
		
		
		Line lineBase = new Line(rampPoints[0].x, rampPoints[0].y, rampPoints[2].x, rampPoints[2].y, vbom);
		rampArrayBodyRamp[1]=PhysicsFactory.createLineBody(rampPhysicsWorld, lineBase, RAMP_FIX);
		
		Line lineHypotenuse = new Line(rampPoints[1].x, rampPoints[1].y, rampPoints[2].x, rampPoints[2].y, vbom);
		rampArrayBodyRamp[2]=PhysicsFactory.createLineBody(rampPhysicsWorld, lineHypotenuse, RAMP_FIX);
		
		Line lineStart = new Line(rampPoints[1].x, rampPoints[1].y, rampPoints[1].x - 200, rampPoints[1].y, vbom);
		PhysicsFactory.createLineBody(rampPhysicsWorld, lineStart, PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.5f));
		
		attachChild(lineHeight);
		attachChild(lineBase);
		attachChild(lineHypotenuse);
		attachChild(lineStart);
		
		
	}

	private void createProjectile() {
		
		projectile = new Rectangle(base.getX(), base.getY() + base.getWidth()/6 , base.getWidth()/2, base.getWidth()/3, vbom);
	    rampBodyProjectile  = PhysicsFactory.createBoxBody(rampPhysicsWorld, projectile, BodyType.DynamicBody, WALL_FIX);
	    rampPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(projectile, rampBodyProjectile, true, true));
	    attachChild(projectile);
	   
	    WeldJointDef def = new WeldJointDef();
	    def.initialize(rampBodyBase, rampBodyProjectile, new Vector2(rampBodyBase.getPosition().x, rampBodyBase.getPosition().y));
	    rampPhysicsWorld.createJoint(def);
	    
	  
		
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
	
	private void createPhysics()
	{
		rampPhysicsWorld= new FixedStepPhysicsWorld(40,new Vector2(0,SensorManager.GRAVITY_EARTH), false,8,3); //SETAR GRAVIDADE
	    registerUpdateHandler(rampPhysicsWorld);
	}


	private void createBase() {
		
		base = new Rectangle(camera.getWidth()/2 - 100 , camera.getHeight()/2 - camera.getHeight()/8 , camera.getWidth()/4, 2f, vbom);
	    rampBodyBase  = PhysicsFactory.createBoxBody(rampPhysicsWorld, base, BodyType.KinematicBody, WALL_FIX);
	    rampPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(base, rampBodyBase, true, true));
		attachChild(base);
		


	}



	@Override
	public void onBackKeyPressed() {
		camera.set(0, 0, 1024, 600);
		resourcesManager.engine.disableAccelerationSensor(activity);
		SceneManager.getInstance().loadMenuScene(engine,SceneType.SCENE_GAME);
		
	}

	@Override
	public SceneType getSceneType() {
		
		return SceneType.SCENE_RAMP;
	}

	@Override
	public void disposeScene() {

		
	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		float gravity_y=pAccelerationData.getY();
		
		//if(pAccelerationData.getY() > 0)
			//	gravity_y=0;
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), gravity_y);
		this.rampPhysicsWorld.setGravity(gravity);		
		Vector2Pool.recycle(gravity);
		//alertSpeed();
		
	}

	
	
	private void alertSpeed() {
	 	activity.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(activity,R.string.escolha_outra_velocidade, Toast.LENGTH_SHORT).show();
			}

		
		
		});
	}
	

}
