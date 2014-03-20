package gui;

import graph.Node;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import map.Controller;


public class PathFinder extends Thread {
	
	private String _src;
	private String _dst; 
	private DrawingPanel _dp;
	Controller _c;
	private InputPanel _ip;
	
	
	public PathFinder(String src, String dst, DrawingPanel dp, Controller c, InputPanel ip) {
		_src = src;
		_dst = dst;
		_dp = dp;
		_c = c;
		_ip = ip;
	}
	
	@Override
	public void run() {
		synchronized(_dp.getPath()) {
			try {
				_dp.getPath().removeAll(_dp.getPath());
				List<Node> path = _c.getPath(_src, _dst);
				if(path!=null){
					_dp.getPath().addAll(path);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SwingUtilities.invokeLater(paint);
		SwingUtilities.invokeLater(printdir);
	}
	
	private Runnable paint = new Runnable() {
		public void run() {
			_dp.repaint();
		}
	};
	
	private Runnable printdir = new Runnable() {
		public void run(){
			_ip.printDir("Looking for Path:",_dp.getPath());
		}
	};

}
