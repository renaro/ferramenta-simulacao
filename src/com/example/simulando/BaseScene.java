package com.example.simulando;


import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.simulando.SceneManager.SceneType;

import android.app.Activity;


public abstract class BaseScene extends Scene{
		protected Engine engine;
	    protected Activity activity;
	    protected ResourcesManager resourcesManager;
	    protected VertexBufferObjectManager vbom;
	    protected BoundCamera camera;
	    protected int level; 
	    
	    
	    public BaseScene()
	    {
	        this.resourcesManager = ResourcesManager.getInstance();
	        this.engine = resourcesManager.engine;
	        this.activity = resourcesManager.activity;
	        this.vbom = resourcesManager.vbom;
	        this.camera = resourcesManager.camera;
	        createScene();
	        
	        
	    }
	    
	    public BaseScene(int level)
	    {
	        this.resourcesManager = ResourcesManager.getInstance();
	        this.engine = resourcesManager.engine;
	        this.activity = resourcesManager.activity;
	        this.vbom = resourcesManager.vbom;
	        this.camera = resourcesManager.camera;
	        this.level=level;
	        createScene();
	        
	    }
	    
	    //---------------------------------------------
	    // ABSTRACTION
	    //---------------------------------------------
	    
	    public abstract void createScene();
	    
	    public abstract void onBackKeyPressed();
	    
	    public abstract SceneType getSceneType();
	    
	    public abstract void disposeScene();
	    

}
