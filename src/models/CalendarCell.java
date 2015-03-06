package models;
import controllers.WeekController;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
public class CalendarCell extends ListCell<Calendar> {
	
	public CalendarCell(){
		
	}
	
	private void init(Calendar cal){
		Pane pane = new Pane();
		CheckBox checkbox = new CheckBox();
		Label label = new Label(cal.getName());
		pane.getChildren().addAll(checkbox,label);
		checkbox.selectedProperty().addListener( (ob,oldVal,newVal) -> {
			if(newVal)
			{
				WeekController.getController().addCalendarEvents(cal);
			}
			else
			{
				WeekController.getController().removeCalendarEvents(cal);
			}
			}
		);
	}
	
	@Override
	public void updateItem(Calendar cal, boolean empty){
		super.updateItem(cal, empty);
		if(cal!=null&&!empty){
			init(cal);			
		}
		else{
			setGraphic(null);
		}
	}
	
	

}
