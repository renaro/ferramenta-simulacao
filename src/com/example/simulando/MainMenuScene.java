package com.example.simulando;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

import android.graphics.LightingColorFilter;

import com.example.simulando.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_LEVEL_SELECTOR=2;
	private  Sprite light;
	
	private void createMenuChildScene()
	{
		
	    menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(0, 0);
	    
	    //SpriteMenuItem smi = new Sp
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.3f, 1f);
	    final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom),1.3f, 1f);
	    final IMenuItem levelSelectorMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LEVEL_SELECTOR, resourcesManager.simulation_region, vbom), 1.3f, 1f);
	    
	    menuChildScene.addMenuItem(playMenuItem);
	    menuChildScene.addMenuItem(levelSelectorMenuItem);
	    menuChildScene.addMenuItem(optionsMenuItem); 
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    playMenuItem.setPosition(playMenuItem.getX() -130 , playMenuItem.getY() -200);
	    optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY()-170);
	    levelSelectorMenuItem.setPosition(levelSelectorMenuItem.getX() + 125, levelSelectorMenuItem.getY() - 145);
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	   
	    setChildScene(menuChildScene);
	} 
	
	@Override
	public void createScene() {
		  createBackground();
		  createMenuChildScene();
		  menuChildScene.setZIndex(0);
		  light.setZIndex(1);
		  this.sortChildren();
		
	}

	@Override
	public void onBackKeyPressed() {
		  System.exit(0);
		
	}

	@Override
	public SceneType getSceneType() {
		 return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {

		
	}

	
	private void createBackground()
	{
	    attachChild(new Sprite(camera.getWidth()/2, camera.getHeight()/2, resourcesManager.menu_background_region, vbom));
	     light = new Sprite(camera.getWidth()/2, camera.getHeight()/2, resourcesManager.lightRegion, vbom);
	    //light.setZIndex(0);
	    attachChild(light);
	    
	
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		   switch(pMenuItem.getID())
	        {
	        case MENU_PLAY:
	            //Load Game Scene!
	            SceneManager.getInstance().loadRampScene(engine, 1);   //loadGameScene(engine,3);
	            return true;
	        case MENU_OPTIONS:
	        	SceneManager.getInstance().loadSimulationScene(engine);
	            return true;
	        case MENU_LEVEL_SELECTOR:
	        	SceneManager.getInstance().createLevelSelectorScene();
	            return true;
	        default:
	            return false;
	    }
	}
	
	
}
