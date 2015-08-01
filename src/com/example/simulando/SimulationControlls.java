package com.example.simulando;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.example.simulando.SceneManager.SceneType;

/**
 * @author Renaro
 *
 */
public class SimulationControlls extends HUD{
	
	public ButtonSprite hudButtonAngle;
	public ButtonSprite hudButtonSpeed;
	public ButtonSprite hudButtonPlay;
	public ButtonSprite hudButtonReset;
	public ButtonSprite hudButtonMove;
	
	private FixedStepPhysicsWorld hudPhysicsWorld;
	private Sprite hudSpriteProjectile;
	private Body hudBodyProjectile;
	
	private ResourcesManager resourcesManager;
	private VertexBufferObjectManager vbom;
	public boolean hudButtonAngleActive = new Boolean(false);
	public boolean hudButtonSpeedActive= new Boolean(false);
	public boolean hudButtonPlayActive= new Boolean(false);
	public boolean hudButtonResetActive= new Boolean(false);
	public boolean hudButtonMoveActive= new Boolean(false);
	public SimulationScene simulationScene;
	

	public SimulationControlls(ResourcesManager resourcesManager,VertexBufferObjectManager vbom,SimulationScene simulationScene){
		super();
		this.resourcesManager=resourcesManager;
		this.vbom=vbom;
		this.simulationScene=simulationScene;

		
		
		
		createAngleButton();
		createSpeedButton();
		createPlayButton();
		createResetButton();
		createMoveButton();
		
		
		registerTouchArea(hudButtonAngle);
		registerTouchArea(hudButtonSpeed);
		registerTouchArea(hudButtonPlay);
		registerTouchArea(hudButtonReset);
		registerTouchArea(hudButtonMove);
		
		attachChild(hudButtonAngle);
		attachChild(hudButtonSpeed);
		attachChild(hudButtonPlay);
		attachChild(hudButtonReset);
		attachChild(hudButtonMove);
	}

	private void createAngleButton() {
		
		hudButtonAngle= new ButtonSprite(50f,2100f, resourcesManager.simulationTextureRegionAngleButton,resourcesManager.simulationTextureRegionAngleButtonHover,resourcesManager.simulationTextureRegionAngleButtonHover, vbom){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
					if(pSceneTouchEvent.isActionDown()){
						if(hudButtonAngle.isEnabled()){
							hudButtonAngle.setEnabled(false);
							disableButtons(0);
							hudButtonAngleActive=true;
							
						}
						else{
							hudButtonAngle.setEnabled(true);
							hudButtonAngleActive=false;
							
						}
	
					}
					else if(pSceneTouchEvent.isActionMove()){
			
					return false;	
					}
				return false;
					
			}

		
		};
			
			
		
		hudButtonAngle.setAnchorCenter(0, 0);
		
	}
	
	private void createSpeedButton(){
		hudButtonSpeed= new ButtonSprite(350f,2100f, resourcesManager.simulationTextureRegionSpeedButton,resourcesManager.simulationTextureRegionSpeedButtonHover,resourcesManager.simulationTextureRegionSpeedButtonHover, vbom){

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
			
				if(pSceneTouchEvent.isActionDown()){
					if(hudButtonSpeed.isEnabled()){
						hudButtonSpeed.setEnabled(false);
						disableButtons(1);
						
					}
					else{
						hudButtonSpeed.setEnabled(true);
						
					}

				}
				else if(pSceneTouchEvent.isActionMove()){
		
				return false;	
				}
			return false;
				
		}

	
	};
		
		
		
	hudButtonSpeed.setAnchorCenter(0, 0);
		
	}
	
	private void createPlayButton() {
		hudButtonPlay= new ButtonSprite(650f,2100f, resourcesManager.simulationTextureRegionPlayButton,resourcesManager.simulationTextureRegionPlayButtonHover,resourcesManager.simulationTextureRegionPlayButtonHover, vbom){
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
					if(pSceneTouchEvent.isActionDown()){
						if(hudButtonPlay.isEnabled()){
							hudButtonPlay.setEnabled(false);
							disableButtons(2);
							
						}
						else{
							hudButtonPlay.setEnabled(true);
						}

					}
					else if(pSceneTouchEvent.isActionMove()){
			
					return false;	
					}
				return false;
					
			}
			
			
		};
		hudButtonPlay.setAnchorCenter(0, 0);
		
	}
  
 
	private void createResetButton() {
		hudButtonReset= new ButtonSprite(950f,2100f, resourcesManager.simulationTextureRegionResetButton,resourcesManager.simulationTextureRegionResetButtonHover,resourcesManager.simulationTextureRegionResetButtonHover, vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
					if(pSceneTouchEvent.isActionDown()){
						if(hudButtonReset.isEnabled()){
							hudButtonReset.setEnabled(false);
							disableButtons(0);
							
						}
						else{
							hudButtonReset.setEnabled(true);
						}

					}
					else if(pSceneTouchEvent.isActionMove()){
			
					return false;	
					}
				return false;
					
			}
			
			
			
		};
		
		
		hudButtonReset.setAnchorCenter(0, 0);
		
	}
	
	private void createMoveButton() {
		hudButtonMove= new ButtonSprite(1250f,2100f, resourcesManager.simulationTextureRegionMoveButton,resourcesManager.simulationTextureRegionMoveButtonHover,resourcesManager.simulationTextureRegionMoveButtonHover, vbom){
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
					if(pSceneTouchEvent.isActionDown()){
						if(hudButtonMove.isEnabled()){
							hudButtonMove.setEnabled(false);
							hudButtonMoveActive=true;
							disableButtons(4);
							

							if(simulationScene.simulationBooleanSimulationOn){
						
								if(!simulationScene.simulationBooleanPausedObject)
									simulationScene.simulationBooleanPausedObject=pause_object();
								else simulationScene.simulationBooleanPausedObject=restart_object();
									
							}else{
								simulationScene.simulationBooleanPausedObject=pause_object(); // pause the object
								simulationScene.simulationBooleanMovingObject=true; // the object now can be moved

								
							}
							
						}
						else{
							hudButtonMove.setEnabled(true);
							hudButtonMoveActive=false;
						}

					}
					else if(pSceneTouchEvent.isActionMove()){
			
					return false;	
					}
				return false;
					
			}
			
			
			
		};
		hudButtonMove.setAnchorCenter(0, 0);
		
	}
	
	
	
	/**
	 * @param i is the button that WILL NOT be disabled
	 * 0=angle
	 * 1=speed
	 * 2=play
	 * 3=reset
	 * 4=move
	 */
	private void disableButtons(int i) {
	if(i != 0){hudButtonAngle.setEnabled(true);hudButtonAngleActive=false;}
	if(i != 1){hudButtonSpeed.setEnabled(true);hudButtonSpeedActive=false;}
	if(i != 2){hudButtonPlay.setEnabled(true);hudButtonPlayActive=false;}
	if(i != 3){hudButtonReset.setEnabled(true);hudButtonResetActive=false;}
	if(i != 4){hudButtonMove.setEnabled(true);hudButtonMoveActive=false;}
		
	}
	
	public boolean restart_object() {
		hudPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(hudSpriteProjectile).getBody().setActive(true);
	
		if(simulationScene.getUpdateHandlerCount() == 1){
			//simulationScene.registerUpdateHandler(simulationScene.time_handler);
	
		}
		return false;
	}
	
	public boolean pause_object() {
		hudPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(hudSpriteProjectile).getBody().setActive(false);
		//scene.unregisterUpdateHandler(time_handler);
		return true;
		
		
	}

	public void setScene(SimulationScene simulationScene) {
		this.simulationScene=simulationScene;
		
	}

	

}
