package gui;

import java.util.List;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tmp.Controller;

public class SugField extends JTextField {
	
	private SugBox _box;
	private Controller _c;
	
	public SugField(SugBox box, Controller c){
		_box = box;
		_c = c;
		this.getDocument().addDocumentListener(new TypeListener(this));
	}
	
	private class TypeListener implements DocumentListener{
		private SugField _field;
		
		public TypeListener(SugField field){
			_field = field;
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			this.update();
		}

		private void update() {
			if(_field.getText().length()>0){
				StringBuilder sb = new StringBuilder();
				String[] parts = _field.getText().trim().split(" ");
				for(String s: parts){
					if(s.length()>0){
						sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1).toLowerCase());
						sb.append(" ");
					}
				}
				String query = sb.toString();
				if(query.length()>0){
					List<String> list = _c.getSuggestions(query);
					System.out.println("here");
					if(list.size()>0){
						System.out.println("SHould be here");
						_box.display(list);
					}
					else{
						_box.removeAllItems();
					}
				}
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			this.update();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

}
