package models;
import controllers.WeekController;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
public class CalendarCell extends ListCell<Calendar> {

	public CalendarCell(){

	}

	private void init(Calendar cal){
		Pane pane = new Pane();
		CheckBox checkbox = new CheckBox();
		checkbox.setSelected(true);
		Label label = new Label(cal.getName());
		label.setLayoutX(20);
		label.setTextFill(Color.web("#0076a3"));
		
		pane.getChildren().addAll(checkbox,label);
		setId("calendar-cell");
		checkbox.selectedProperty().addListener( (ob,oldVal,newVal) -> {
			if(newVal)
			{	
				PersonInfo.getPersonInfo().addCalendar(cal);
			}
			else
			{
				PersonInfo.getPersonInfo().removeCalendar(cal);
			}
		}
				);
		setGraphic(pane);
	}

	@Override
	public void updateItem(Calendar cal, boolean empty){
		super.updateItem(cal, empty);
//		if(cal!=null){
//			System.out.println("\n\n");
//			System.out.println("Kalender kommer inn "+cal.getName());
//			System.out.println("\n\n");
//		}
		if(cal!=null&&!empty){
			init(cal);			
		}
		else{
			setGraphic(null);
		}
	}



}
