package com.example.simulando;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import android.util.Log;
public class SceneManager {
	  
    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene simulationScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;
    private BaseScene rampScene;
    private BaseScene levelSelector;
	
private static final SceneManager INSTANCE = new SceneManager();
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    
    private BaseScene currentScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    
    public enum SceneType
    {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_SIMULATION,
        SCENE_GAME,
        SCENE_RAMP,
        SCENE_LOADING,
        SCENE_LEVEL_SELECTOR
    }
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void setScene(BaseScene scene)
    {
        engine.setScene(scene);
        
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_SIMULATION:
                setScene(simulationScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            case SCENE_RAMP:
                setScene(rampScene);
                break;
                
            case SCENE_LEVEL_SELECTOR:
                setScene(levelSelector);
                break;
            default:
                break;
        }
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static SceneManager getInstance()
    {
        return INSTANCE;
    }
    
    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashScreen();
    
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }
    

    public void createLevelSelectorScene()
    {
        levelSelector = new LevelSelectorScene();
        SceneManager.getInstance().setScene(levelSelector);
        
        //disposeSplashScene();
        
    }
    
    public void loadSimulationScene(final Engine mEngine)
    {
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadSimulationResources();
                simulationScene = new SimulationScene();
                setScene(simulationScene);
            }
        }));
    }
    
    public void loadGameScene(final Engine mEngine, final int stage)
    {
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources();
                gameScene = new GameScene(stage);
                setScene(gameScene);
            }
        }));
    }
    public void loadRampScene(final Engine mEngine, final int stage)
    {
    
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources();
                rampScene = new RampScene(stage);
                setScene(rampScene);
            }
        }));
    }
    
    
    public void createMenuScene()
    {
        ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        SceneManager.getInstance().setScene(menuScene);
        disposeSplashScene();
        
    }
    
    public void loadMenuScene(final Engine mEngine, SceneType sceneGameType)
    {
    	
        setScene(loadingScene);
        
        if(gameScene!= null && sceneGameType == gameScene.getSceneType()){
	        gameScene.disposeScene();
	        ResourcesManager.getInstance().unloadGameTextures();
	        }
        
        else if(simulationScene!= null && sceneGameType == simulationScene.getSceneType()){
	        simulationScene.disposeScene();
	        ResourcesManager.getInstance().unloadSimulationTextures();
	        }
        
      
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuTextures();
               
                setScene(menuScene);
            }
        }));
    }

	
}
