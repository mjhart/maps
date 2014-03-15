package gui;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputAdapter;

public class PanListener extends MouseInputAdapter {
	
	public PanListener(AltDrawingPanel dp) {
		this.dp = dp;
	}
	
	private Point start;
	private Point end;
	private AltDrawingPanel dp;


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		end = e.getPoint();
		double[] delta = new double[2];
		delta[0] = (end.getX() - start.getX())*0.0001;
		delta[1] = (end.getY() - start.getY())*0.0001;
		start = end;
		dp.moveWindow(delta);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Clicked");
		start = e.getPoint();
	}


}
