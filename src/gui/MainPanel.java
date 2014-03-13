package gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;

public class MainPanel extends JFrame{
	
	public MainPanel() throws IOException{
		super();
		this.setLayout(new BorderLayout());
		this.setLocation(500, 250);
		this.setTitle("Maps");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		DrawingPanel dp = new DrawingPanel();
		//InputPanel ip = new InputPanel(dp);
		//ZoomPanel zp = new ZoomPanel(dp);
		//dp.setIPan(ip);
		//dp.setZPan(zp);
		
		this.add(dp, BorderLayout.CENTER);
		//this.add(ip, BorderLayout.WEST);
		//this.add(zp, BorderLayout.SOUTH);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException{
		new MainPanel();
	}
	
}
