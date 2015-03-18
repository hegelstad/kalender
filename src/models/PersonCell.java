package models;

import javafx.scene.control.ListCell;

public class PersonCell extends ListCell<Person> {

	@Override
	public void updateItem(Person person, boolean empty){
		super.updateItem(person, empty);
		if(empty || person==null){
			this.setText(null);
		}
		else{
			this.setText(person.getName());
		}
	}
}
