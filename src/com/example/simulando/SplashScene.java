package com.example.simulando;


import org.andengine.entity.sprite.Sprite;

import com.example.simulando.SceneManager.SceneType;


public class SplashScene extends BaseScene{
	
	private Sprite splash;
	
	 @Override
	    public void createScene()
	    {
		 splash = new Sprite(0, 0, resourcesManager.splash_region, vbom);
		         
		// splash.setScale(1.5f);
		 splash.setPosition(514, 300);
		 attachChild(splash);
	
	    }

	    @Override
	    public void onBackKeyPressed()
	    {

	    }

	    @Override
	    public SceneType getSceneType()
	    {
	    	return SceneType.SCENE_SPLASH;	    	
	    }

	    @Override
	    public void disposeScene()
	    {

	    }
	
	
}
