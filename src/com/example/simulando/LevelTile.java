package com.example.simulando;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class LevelTile extends Sprite{
	public Text text;
	public Sprite lock;
	private ResourcesManager res;
	private int levelID;


	public LevelTile(float pX, float pY, ITextureRegion pTextureRegion,ResourcesManager res,int levelID,boolean locked) {
		super(pX, pY, pTextureRegion, res.vbom);
		lock= new Sprite(10,10, res.lockRegion, res.vbom);
		this.levelID=levelID;
		String label = ""+levelID;
		text = new Text(this.getWidth()/2 ,this.getHeight()/2 ,res.fontLevelTile,label,res.vbom);
		this.res=res;
		
		attachChild(text);
		if(locked)
			attachChild(lock);

	}
	
	 @Override
     public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,final float pTouchAreaLocalX,
                                  final float pTouchAreaLocalY) {
		 SceneManager.getInstance().loadGameScene(res.engine,levelID);
		 return true;
     }



	
	

}
