package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AltZoomPanel extends JPanel {
	
	private AltDrawingPanel _dp;
	private JSlider _zoomSlide;
	private int _curZoom;
	
	public AltZoomPanel(AltDrawingPanel dp, int maxzoom, int initzoom){
		super();
		_dp = dp;
		this.setLayout(new GridLayout(0,1));
		Dimension size = new Dimension(750,50);
		this.setPreferredSize(size);
		this.setSize(size);
		this.setVisible(true);
		
		_zoomSlide = new JSlider(JSlider.HORIZONTAL,1,maxzoom,1);
		_zoomSlide.setMinorTickSpacing(1);
		_zoomSlide.setSnapToTicks(true);
		_zoomSlide.setPaintTicks(true);
		_zoomSlide.setPaintLabels(true);
		_zoomSlide.setBackground(java.awt.Color.BLACK);
		_curZoom=initzoom;
		_zoomSlide.setValue(_curZoom);
		_zoomSlide.addChangeListener(new ZoomListener());
		this.add(_zoomSlide);
		this.repaint();
		
		// Set the zoom variable in the draw panel to be what the 
		// slider is set to now
		_dp.updateZoom(_curZoom);
	}
	
	private class ZoomListener implements ChangeListener{
		public void stateChanged(ChangeEvent e) {
			/*
			if(((JSlider) e.getSource()).getValueIsAdjusting()==false){
				System.out.println("in here");
				
				JSlider slider = (JSlider) e.getSource();
				int zoom = slider.getValue();
				if(_curZoom != zoom){
					_curZoom=zoom;
					_dp.updateZoom(zoom);
				}
			}
			*/
			//_dp.loadData();
			_dp.load = !_dp.load;
		}
	}
	
}