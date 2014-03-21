package gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;

import map.Controller;

/*****
 * 
 * this class is the highest level container in our JFrame
 * @author sbreslow
 *
 */


public class MainPanel extends JFrame{
	
	private Controller controller;
	
	public MainPanel(Controller controller) {
		super();
		this.setLayout(new BorderLayout());
		this.setLocation(500, 250);
		this.setTitle("Maps");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		DrawingPanel dp = new DrawingPanel(controller);
		InputPanel ip = new InputPanel(dp, controller);
		
		dp.setIp(ip);
		this.add(dp, BorderLayout.CENTER);
		this.add(ip, BorderLayout.WEST);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		
		this.controller = controller;
	}
	
}