package com.example.simulando;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class Parametros {
	
	private int velocidade_inicial=0;
	private int angulo;
	private Point vector_direction;
	private Point vector_origin;
	private PointF initial_position;
	private PointF linearVelocity; // x means velocity in X axis, y means velocity in Y axis
	
	public Parametros(){
		angulo=45;
		vector_direction=new Point();
		vector_origin=new Point();
		initial_position=new PointF(30,20);
		
		linearVelocity=new PointF(0f,0f);
	}
	
	public int getVelocidade_inicial() {
		return velocidade_inicial;
	}
	
	public void setLinearVelocity(PointF _linearVelocity) {
		this.linearVelocity=_linearVelocity;
	}
	
	public void setVelocidade_inicial(int velocidade_inicial) {
		double rad = Math.toRadians((double)angulo);       
		
		this.velocidade_inicial = velocidade_inicial;
		BigDecimal vel_x= new BigDecimal((Math.cos(rad)*velocidade_inicial));
		vel_x= vel_x.setScale(1,RoundingMode.HALF_EVEN);
		
		BigDecimal vel_y= new BigDecimal((Math.sin(rad)*velocidade_inicial));
		vel_y= vel_y.setScale(1,RoundingMode.HALF_EVEN);
		
		
		this.linearVelocity.x=vel_x.floatValue();
		this.linearVelocity.y=vel_y.floatValue();
	}
	
	public float getAngulo() {
		return angulo;
	}
	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public void setVectorDirection(int _x, int _y, int raioVetor) {
	
		double rad = Math.toRadians((double)angulo);
		
		vector_direction.x=_x+ (int)(Math.cos(rad)*raioVetor);
		vector_direction.y=_y+ (int)(Math.sin(rad)*raioVetor);
		

	}
	public Point getDirection() {
		return vector_direction;	
	}
	public Point getOrigin() {
		return vector_origin;	
	}

	public void setVectorOrigin(int _x, int _y, float space) {
	double rad = Math.toRadians((double)angulo);

	this.vector_origin.x=_x +(int)(Math.cos(rad)*space);
	this.vector_origin.y=_y +(int)(Math.sin(rad)*space);
		
	}

	public PointF getLinearVelocity() {

		return linearVelocity;
	}

	public void reload() {
		setVelocidade_inicial(getVelocidade_inicial());
		
		
	}

	public PointF getInitial_position() {
		return initial_position;
	}

	public void setInitial_position(PointF initial_position) {
		this.initial_position = initial_position;
	}


}
