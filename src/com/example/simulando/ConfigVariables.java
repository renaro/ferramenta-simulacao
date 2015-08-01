package com.example.simulando;
import com.example.simulando.SceneManager.SceneType;
public class ConfigVariables {
	
	public int SIMULATION_AREA_X=150;
	public int SIMULATION_AREA_Y=70;
	 
	public ConfigVariables(SceneType currentSceneType){
		if(currentSceneType == SceneType.SCENE_RAMP){
			SIMULATION_AREA_X=50;
			SIMULATION_AREA_X=70;
			
		}
		
		
		
	}
	
}
