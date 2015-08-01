package com.example.simulando;


import org.andengine.input.sensor.acceleration.AccelerationData;

import com.badlogic.gdx.math.Vector2;

public class VectorsDrawer {
	
	public Vector2 gravity_force;
	


	public Vector2 initial_force;
	public Vector2 abnormal_force;
	public Vector2 friction_force;
	public Vector2 x_force;
	public Vector2 y_force;
	public Vector2 center;
	private float angle;
	private float gravity_value;
	
	public VectorsDrawer(Vector2 center, float gravity_value){
		this.center= center;
		this.gravity_value=gravity_value;
		initial_force=gravity_force= new Vector2(center.x, center.y  - gravity_value);
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public float getGravityX() {
		return gravity_force.x;
	}

	public float getGravityY() {
		return gravity_force.y;
	}
	
	public void compute(float angle){
	
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		float x_normal =(float)cos * gravity_value;
		float y_normal =(float)sin * gravity_value;
		
		gravity_force= new Vector2(initial_force.x + x_normal, initial_force.y  - y_normal);
		
		
		
	}
	

}
