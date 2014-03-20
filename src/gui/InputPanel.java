package gui;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import map.Controller;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import graph.Edge;
import graph.Node;
import gui.SugField;

public class InputPanel extends JPanel {
	
	private JTextArea _text;
	private JScrollPane _scroll;
	private JButton _astarText;
	private JButton _astarMap;
	private DrawingPanel _dp;
	private Controller _c;
	private List<SugField> _sfields;
	private List<SugBox> _sboxes;
	
	
	public InputPanel(DrawingPanel dp, Controller c){
		super();
		_dp = dp;
		_c = c;
		this.setLayout(new GridLayout(0,1));
		Dimension size = new Dimension(350,500);
		this.setPreferredSize(size);
		this.setSize(size);
		_sfields = new ArrayList<SugField>();
		_sboxes = new ArrayList<SugBox>();
		
		
		for(int i = 0; i < 4; i++){
			this.makeField();
		}
		
		_astarText = new JButton("Find Path from Text");
		_astarText.addActionListener(new ButtonListener(true));
		_astarMap = new JButton("Find Path from Map (Green to Red)");
		_astarMap.addActionListener(new ButtonListener(false));
		
		_text = new JTextArea();
		_scroll = new JScrollPane(_text);
		Dimension scroll = new Dimension(50,300);
		_scroll.setPreferredSize(scroll);
		_scroll.setSize(size);
		_text.setLineWrap(true);
		_text.setWrapStyleWord(true);
		_text.setEditable(false);
		_text.setOpaque(false);
		_text.setText("");
		
		this.init();
		this.setVisible(true);
	}
	
	private void init() {
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		ParallelGroup pg = layout.createParallelGroup();
		SequentialGroup sg = layout.createSequentialGroup();
		for(int i = 0; i < _sfields.size(); i++){
			this.addToGroups(pg, sg, _sfields.get(i));
			this.addToGroups(pg, sg, _sboxes.get(i));
		}
		this.addToGroups(pg,sg,_astarText);
		this.addToGroups(pg,sg, _astarMap);
		this.addToGroups(pg,sg,_scroll);
		
		layout.setHorizontalGroup(pg);
		layout.setVerticalGroup(sg);
		for(int i = 0; i < _sfields.size(); i++){
			this.add(_sfields.get(i));
			this.add(_sboxes.get(i));
		}
		this.add(_astarText);
		this.add(_astarMap);
		this.add(_scroll);
	}
	
	private void addToGroups(ParallelGroup pg, SequentialGroup sg, Component c){
		pg.addComponent(c);
		sg.addComponent(c);
	}

	private class ButtonListener implements ActionListener{
		
		private SugField[] _sfs = new SugField[4];
		private boolean _text;
		
		public ButtonListener(Boolean text){
			_text = text;
			int i = 0;
			for(SugField sf: _sfields){
				_sfs[i] = sf;
				i++;
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(_text){
				String s1 = _sfs[0].getText().trim();
				String cs1 = _sfs[1].getText().trim();
				String s2 = _sfs[2].getText().trim();
				String cs2 = _sfs[3].getText().trim();
				System.out.println(s1+", "+cs1+", "+s2+", "+cs2);
				Node src = _c.getIntersection(s1, cs1);
				Node dst = _c.getIntersection(s2, cs2);
				try{
					_dp.startSearch(src, dst);
				}
				catch(NullPointerException exc){
					if(src==null){
						System.err.println("ERROR: Source intersection couldn't be found");
					}
					if(dst==null){
						System.err.println("ERROR: Destinations intersection couldn't be found");
					}
				}
			}
			else{
				Node src = _dp.getStart();
				Node dst = _dp.getEnd();
				if(src!=null && dst!=null){
					_dp.startSearch(src, dst);
				}
			}
		}
		
	}

	private void makeField() {
		SugBox sb = new SugBox();
		SugField sf = new SugField(sb, _c);
		sf.setPreferredSize(new Dimension(100,10));
		_sfields.add(sf);
		_sboxes.add(sb);
		sb.addField(sf);
	}
	
	public void printDir(String header, List<Node> path){
		_text.setText("");
		_text.append(header+"\n");
		StringBuilder sb = new StringBuilder(header+"\n");
		if(path!=null){
			for(int i = 0; i < path.size()-1; i++){
				for(Edge e: path.get(i).getEdges()){
					if(e.getDest().equals(path.get(i+1))){
						//System.out.println("here IIIIII");
						_text.append(e.getSource()+" -> "+e.getDest()+" : "+e.getFilm()+"\n");
						sb.append(e.getSource()+" -> "+e.getDest()+" : "+e.getFilm()+"\n");
						break;
					}
				}
			}
			System.out.println(_text.getText());
			_text.setText(sb.toString());
		}
		else{
			_text.append("No Path Found");
			_text.setText(sb.toString()+"No Path Found");
		}
	}

}
