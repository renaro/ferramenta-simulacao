package com.example.simulando;

import org.andengine.entity.scene.background.Background;

import com.example.simulando.SceneManager.SceneType;

public class LevelSelectorScene  extends BaseScene{

	LevelSelector levelSelector ;
	
	@Override
	public void createScene() {
		  createBackground();
		  createMenuChildScene();
		  levelSelector= new LevelSelector(camera.getCenterX(), camera.getCenterY(), 5, 1, resourcesManager, this);
		
	}

	private void createBackground() {
		Background bg = new Background(0.6f, 0.6f, 0.6f);
		setBackground(bg);
		
		
	}

	private void createMenuChildScene() {
		
		
	}

	@Override
	public void onBackKeyPressed() {
		
		SceneManager.getInstance().loadMenuScene(engine,SceneType.SCENE_LEVEL_SELECTOR);
		
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
