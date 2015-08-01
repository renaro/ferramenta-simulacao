package com.example.simulando;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;




import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends BaseGameActivity {
	
	 
    static final int CAMERA_WIDTH = 4200;
    static final int CAMERA_HEIGHT = 2480;
    private FixedStepPhysicsWorld mPhysicsWorld;

    private Point pa= new Point();
    private Point pb= new Point();
    private Point pc= new Point();
    
    
    private boolean paused;
    private ResourcesManager resourcesManager;
    
    private Rectangle ground;
    private Dialog dialogGetSpeed;
    private Parametros params= new Parametros();
    private Button botaoAddParam;
    private EditText et_valor_parametro;
    private PhysicsConnector mPhysicsConnector;
    private TimerHandler time_handler;
    private boolean highest_point_found=false;
    private Point simulationProjectilePosition;
    
    BitmapTextureAtlas playerTexture;    
    BitmapTextureAtlas menuTextureButton1;
    BitmapTextureAtlas menuTextureButton2;
    BitmapTextureAtlas menuTextureButton3;
    BitmapTextureAtlas menuTextureButton4;
    BitmapTextureAtlas menuTextureButton5;
    
    

    ITextureRegion playerTextureRegion;   
    ITextureRegion mButton1TextureRegion;
    ITextureRegion mButton2TextureRegion;
    ITextureRegion mButton3TextureRegion;
    ITextureRegion mButton4TextureRegion;
    ITextureRegion mButton5TextureRegion;
	ITextureRegion mButton1TextureRegion_hover;
	ITextureRegion mButton2TextureRegion_hover;
	ITextureRegion mButton3TextureRegion_hover;
	ITextureRegion mButton4TextureRegion_hover;
	ITextureRegion repeatingBackgroundTextureRegion;
	
	
	
    private Font variablesFont;
    private boolean moving_object=false;;
    private int DISABLE=0;
    private int ENABLE=1;
    private Text texto_posY;
    private Text texto_posX;
    private Text texto_velocidade_inicial;
    private Text texto_velocidade_y;
    private Text texto_velocidade_x;
    private Text texto_angulo;

    
    
    private Text variablesText;
    private Text variablesText1;
    private Text variablesText2;
    private Text variablesText3;
    private Text variablesText4;
    private Text variablesText6;
    private Text textsomePoint;
    
    
    ButtonSprite button1;
    ButtonSprite button2;
    ButtonSprite button3;
    ButtonSprite button4;
    ButtonSprite button5;
    
    private ArrayList<Rectangle> parabola = new ArrayList<Rectangle>();
    private ArrayList<Text> textImportantPoints = new ArrayList<Text>();
    
    private Rectangle parabola_point;
    
    
    Rectangle chooseAngleMenuRectangle;
    private boolean angle_active_button=false;
    Sprite sPlayer;
    Body body;
    Body body_ground;
    Scene scene;

	private Line vector;
	private Line arrow1;
	private Line arrow2;
	private Line arrow3;
	public ConfigVariables config;
	
	private int raioVetor=150;
	BoundCamera mCamera;
	
	

	
	

	private boolean simulationOn=false;
	
    float px_menu_angulo,py_menu_angulo,width_menu_angulo,height_menu_angulo;
	private float parabola_time=0.1f;

	
    
    


    @Override
    public Engine onCreateEngine(final EngineOptions pEngineOptions) {
    return new FixedStepEngine(pEngineOptions, 60);
    }
    
    
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		 // mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		mCamera = new BoundCamera(0, 0, 1024, 600);
		mCamera.setBounds(0, 0, 8000, 4100);
		mCamera.setBoundsEnabled(true);
		 
		 EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
	            new FillResolutionPolicy()/*RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT)*/, mCamera);
		  
		// engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		 
		 engineOptions.getRenderOptions().setDithering(true);


		 
		 
		 
		 return engineOptions;
		 
	}


	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		
		 
		ResourcesManager.prepareManager(mEngine, this, mCamera, MainActivity.this.getVertexBufferObjectManager());
	    resourcesManager = ResourcesManager.getInstance();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}



	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	
	}
	


	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		
	    mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	        public void onTimePassed(final TimerHandler pTimerHandler) 
	        {
	            mEngine.unregisterUpdateHandler(pTimerHandler);
	            SceneManager.getInstance().createMenuScene();
	        }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}





	@Override
	public void onResumeGame() {
	super.onResumeGame();
	
	
	
	}





	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	        System.exit(0);	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}


}
