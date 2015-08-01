package com.example.simulando;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



import android.graphics.PointF;

public class Coin extends Sprite {
	
	  public PointF pointTextPosition= new PointF();
	  public Text 	 textPosition;
	  public ResourcesManager res;
	  public int coinOrder;
	  public enum CoinType  {
	        FINAL_COIN,
	        COIN,
	        SPECIAL_COIN
	    }
	  private CoinType coinType;
	  

	  
	/**
	 * @param pX position in meters
	 * @param pY position in meters
	 * @param pX_ position in pixels
	 * @param pY_ position in pixels
	 * @param pTextureRegion
	 * @param pVertexBufferObjectManager
	 * @param res
	 */
	public Coin(CoinType type, int i,float pX, float pY,int pX_, int pY_, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, ResourcesManager res) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	 this.pointTextPosition= new PointF(pX, pY + 2*this.getHeight());
	 this.textPosition= new Text(pointTextPosition.x,pointTextPosition.y, res.simulationvariablesFont, pX_+","+pY_, pVertexBufferObjectManager);
 	 this.coinOrder=i;
 	 this.coinType=type;


		
		
	}

	  
	
	

	  
	  
	
	
}
