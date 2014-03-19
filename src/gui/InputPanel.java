package gui;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import newGraph.Node;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import tmp.Controller;;

public class InputPanel extends JPanel {
	
	private JTextArea _text;
	private JScrollPane _scroll;
	private JButton _astar;
	private AltDrawingPanel _dp;
	private Controller _c;
	private List<SugField> _sfields;
	private List<SugBox> _sboxes;
	
	
	public InputPanel(AltDrawingPanel dp, Controller c){
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
		
		_astar = new JButton("Find Path");
		_astar.addActionListener(new ButtonListener());
		
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
		this.addToGroups(pg,sg,_astar);
		this.addToGroups(pg,sg,_scroll);
		
		layout.setHorizontalGroup(pg);
		layout.setVerticalGroup(sg);
		for(int i = 0; i < _sfields.size(); i++){
			this.add(_sfields.get(i));
			this.add(_sboxes.get(i));
		}
		this.add(_astar);
	}
	
	private void addToGroups(ParallelGroup pg, SequentialGroup sg, Component c){
		pg.addComponent(c);
		sg.addComponent(c);
	}

	private class ButtonListener implements ActionListener{
		
		private SugField[] _sfs = new SugField[4];
		
		public ButtonListener(){
			int i = 0;
			for(SugField sf: _sfields){
				_sfs[i] = sf;
				i++;
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String s1 = _sfs[0].getText().trim();
			String cs1 = _sfs[1].getText().trim();
			String s2 = _sfs[2].getText().trim();
			String cs2 = _sfs[3].getText().trim();
			Node src = _c.getIntersection(s1, cs1);
			Node dst = _c.getIntersection(s2, cs2);
			_dp.startSearch(src, dst);
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

}