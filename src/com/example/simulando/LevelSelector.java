package com.example.simulando;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;

import android.graphics.PointF;

public class LevelSelector extends Entity{
	
	private int levelIntlevelCount=7;
	private int levelIntMaxlevelCompleted;
	private PointF levelGridPointPosition= new PointF();
	private Scene levelSelectorScene;
	private int levelIntWorldIndex;
	ResourcesManager res;
	LevelTile  tiles[];
	
	
	public LevelSelector(final float pX, final float pY, final int maxCompletedLevel, final int pWorldIndex, ResourcesManager res, final Scene pScene) {
		this.levelGridPointPosition= new PointF(pX,pY);
		this.levelSelectorScene=pScene;
		this.levelIntMaxlevelCompleted=maxCompletedLevel;
		this.levelIntWorldIndex=pWorldIndex;
		this.res=res;
		tiles= new LevelTile[levelIntlevelCount];
		createTiles();
				
	}


	private void createTiles() {
		float pX,pY;
		for(int i = 0 ; i<levelIntlevelCount;i++)
		{
			pX= (levelGridPointPosition.x - 300) + i*75;
			pY= levelGridPointPosition.y;
			int levelID = i+1;
			tiles[i]= new LevelTile(pX, pY, res.tileTextureRegion, res,levelID,true);
			levelSelectorScene.registerTouchArea(tiles[i]);
			levelSelectorScene.attachChild(tiles[i]);
			
			
			
		}
			
		
	}
	
	

}
