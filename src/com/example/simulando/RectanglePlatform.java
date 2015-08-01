package com.example.simulando;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;

public class RectanglePlatform extends Rectangle {
	
	public Body body;
	public int angle;

	public RectanglePlatform(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager, int angle) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.setAnchorCenter(0, 0);
		this.angle=angle;
		
	
	}
	
	

}
