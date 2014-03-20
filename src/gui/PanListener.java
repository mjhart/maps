package gui;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

import javax.swing.event.MouseInputAdapter;

public class PanListener extends MouseInputAdapter {
	
	public PanListener(DrawingPanel dp) {
		this._dp = dp;
	}
	
	private Point start;
	private Point end;
	private DrawingPanel _dp;
	private long _clicked;


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		end = e.getPoint();
		double[] delta = new double[2];
		delta[0] = (start.getX() - end.getX());
		delta[1] = (start.getY() - end.getY());
		//System.out.println(Arrays.toString(delta));
		start = end;
		_dp.moveWindow(delta);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println("Clicked");
		start = e.getPoint();
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() > 0) {
			_dp.zoomOut();
		}
		else {
			_dp.zoomIn();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		_dp.clickAt(e.getX(), e.getY());
	}


}
