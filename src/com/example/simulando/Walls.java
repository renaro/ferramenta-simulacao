package com.example.simulando;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;


import com.example.simulando.SceneManager.SceneType;
public class Walls {
	private Rectangle simulationRectangleArrayWalls[] =  new Rectangle[4];
	private float wallWidth;
	
	private BoundCamera camera;
	private VertexBufferObjectManager vbom;
	private ConfigVariables config;
	
	
	/**
	 * @param wallWidth
	 * @param camera
	 * @param vbom
	 * we will create 4 walls
	 * simulationRectangleArrayWalls[0] bottom wall(ground)
	 * simulationRectangleArrayWalls[1] left wall
	 * simulationRectangleArrayWalls[2] right wall
	 * simulationRectangleArrayWalls[3] top wall(ceiling)
	 */
	public Walls(float wallWidth, BoundCamera camera, VertexBufferObjectManager vbom,SceneType currentSceneType){
		this.wallWidth= wallWidth;
		this.camera=camera;
		this.vbom=vbom;
		config= new ConfigVariables(currentSceneType);
	}
	
	public Rectangle[] createWalls(float projectileWidth){
		
		///we want the projectile center stays at the 0 position(y Axis)
		float offset =projectileWidth/2;
		float simulation_width= config.SIMULATION_AREA_X * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		float simulation_height= config.SIMULATION_AREA_Y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		offset=-wallWidth-offset;
		
		
		simulationRectangleArrayWalls[0] = new Rectangle(offset,offset, simulation_width,wallWidth,vbom);
		simulationRectangleArrayWalls[0].setAnchorCenter(0, 0);
		simulationRectangleArrayWalls[0].setColor(new Color(0.02f,0.2f,0.422f));
		
		
		simulationRectangleArrayWalls[1] = new Rectangle(offset,offset,wallWidth,simulation_height,vbom);
		simulationRectangleArrayWalls[1].setAnchorCenter(0, 0);
		simulationRectangleArrayWalls[1].setColor(new Color(0.02f,0.2f,0.422f));
		
			
		simulationRectangleArrayWalls[2] = new Rectangle(simulation_width+offset,offset, wallWidth,simulation_height+wallWidth ,vbom);
		simulationRectangleArrayWalls[2].setAnchorCenter(0, 0);
		simulationRectangleArrayWalls[2].setColor(new Color(0.02f,0.2f,0.422f));
		
		
		simulationRectangleArrayWalls[3] = new Rectangle(offset,simulation_height+offset, simulation_width,wallWidth,vbom);
		simulationRectangleArrayWalls[3].setAnchorCenter(0, 0);
		simulationRectangleArrayWalls[3].setColor(new Color(0.02f,0.2f,0.422f));
		
		
		
		
		
		
		return this.simulationRectangleArrayWalls;
	}
	
}
