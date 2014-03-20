package gui;
/****
 * this class is for making suggestions based off of the Trie.
 * 
 */
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JComboBox;

public class SugBox extends JComboBox {
	
	public SugBox(){
		//super();
		this.setVisible(true);
		this.setSelectedIndex(-1);
	}
	
	public void addField(SugField field){
		this.addItemListener(new SelectListener(field, this));
	}
	
	private class SelectListener implements ItemListener{
		
		private SugField _field;
		private SugBox _box;
		private String _text = null;
		
		public SelectListener(SugField field, SugBox box){
			_field = field;
			_box = box;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			_text = (String)((SugBox)(e.getSource())).getSelectedItem();
			this.update();
		}
		
		private void update(){
			java.awt.EventQueue.invokeLater(new Runnable(){

				@Override
				public void run() {
					if(_text!=null && !(_field.getText().equals(_text))){
						_field.setText(_text);
					}
				}
				
			});
		}
	}
	
	public void display(List<String> list){
		this.removeAllItems();
		for(String s: list){
			this.addItem(s);
		}
		this.setSelectedIndex(-1);
	}

}
