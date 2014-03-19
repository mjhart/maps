package gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;

import tmp.Controller;

public class AltMainPanel extends JFrame{
	
	private Controller controller;
	
	public AltMainPanel(Controller controller) {
		super();
		this.setLayout(new BorderLayout());
		this.setLocation(500, 250);
		this.setTitle("Maps");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		AltDrawingPanel dp = new AltDrawingPanel(controller);
		//InputPanel ip = new InputPanel(dp);
		AltZoomPanel zp = new AltZoomPanel(dp, 10, 10);
		//dp.setIPan(ip);
		//dp.setZPan(zp);
		InputPanel ip = new InputPanel(dp, controller);
		
		
		this.add(dp, BorderLayout.CENTER);
		//this.add(ip, BorderLayout.WEST);
		this.add(zp, BorderLayout.SOUTH);
		this.add(ip, BorderLayout.WEST);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		
		this.controller = controller;
	}
	
	public static void main(String[] args) throws IOException{
		//new MainPanel();
	}
	
}