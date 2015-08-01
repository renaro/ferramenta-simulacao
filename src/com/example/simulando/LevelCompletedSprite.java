package com.example.simulando;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class LevelCompletedSprite extends Rectangle {

	public LevelCompletedSprite(float pX, float pY, float pWidth,
			float pHeight,
			VertexBufferObjectManager pRectangleVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pRectangleVertexBufferObject);
		this.setColor(Color.GREEN);
		this.setAlpha(0.28f);
			
	}



}
