package gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.event.MouseInputAdapter;

/**
 * @author mjhart
 * Listener for mouse events on the map
 */
public class PanListener extends MouseInputAdapter {
	
	private Point start;
	private Point end;
	private DrawingPanel _dp;
	
	public PanListener(DrawingPanel dp) {
		this._dp = dp;
	}
	
	/**
	 * Moves the screen
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		
		// get end
		end = e.getPoint();
		
		// calculate difference
		double[] delta = new double[2];
		delta[0] = (start.getX() - end.getX());
		delta[1] = (start.getY() - end.getY());
		
		// move start to current end
		start = end;
		
		// move window
		_dp.moveWindow(delta);
	}

	/**
	 * Starts a new drag
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		start = e.getPoint();
		
	}
	
	/**
	 * Performs zooming
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() > 0) {
			_dp.zoomOut();
		}
		else {
			_dp.zoomIn();
		}
	}
	
	/**
	 * Adds a new point as a start or destination
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		_dp.clickAt(e.getX(), e.getY());
	}


}
