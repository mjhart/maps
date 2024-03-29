package gui;

import java.util.List;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import map.Controller;

/*****
 * this class allows the user to input text into the field so that suggestions can be generated.
 * 
 * if the user does not select a suggestion, Astar will pull whatever is in the field for the search.
 * @author sbreslow
 *
 */


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
				/*StringBuilder sb = new StringBuilder();
				String[] parts = _field.getText().trim().split(" ");
				for(String s: parts){
					if(s.length()>0){
						sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1).toLowerCase());
						sb.append(" ");
					}
				}
				String query = sb.toString();*/
				String query = _field.getText();
				if(query.length()>0){
					List<String> list = _c.getSuggestions(query);
					if(list.size()>0){
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
