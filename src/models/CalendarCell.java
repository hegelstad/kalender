package models;
import controllers.SidebarController;
import controllers.WeekController;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
public class CalendarCell extends ListCell<Calendar> {
	
	public Label label;

	public CalendarCell(){

	}

	private void init(Calendar cal){
		Pane pane = new Pane();
		CheckBox checkbox = new CheckBox();
		checkbox.setSelected(true);
		label = new Label(cal.getName());
		label.setLayoutX(20);
		label.setTextFill(Color.web("#0076a3"));
		Rectangle rect = new Rectangle(20,20);
		rect.setLayoutX(120);
		rect.strokeProperty().set(Color.BLACK);
		rect.strokeWidthProperty().set(1);
		switch (cal.getColorID()){
		case 0: 
			rect.fillProperty().set(Color.LIGHTSKYBLUE);
			break;
		case 1: 
			rect.fillProperty().set(Color.LIGHTSALMON);
			break;
		case 2: 
			rect.fillProperty().set(Color.LIGHTGREEN);
			break;
		case 3:
			rect.fillProperty().set(Color.LIGHTYELLOW);
			break;
		case 4: 
			rect.fillProperty().set(Color.LIGHTPINK);
			break;
		case 5: 
			rect.fillProperty().set(Color.LIGHTCORAL);
			break;
		case 6: 
			rect.fillProperty().set(Color.BEIGE);
			break;
		case 7: 
			rect.fillProperty().set(Color.SILVER);
			break;
		case 8: 
			rect.fillProperty().set(Color.AQUAMARINE);
			break;
		case 9: 
			rect.fillProperty().set(Color.CORNFLOWERBLUE);
			break;
		default: 
			rect.fillProperty().set(Color.THISTLE);
			break;
	}
	rect.opacityProperty().set(0.5);
		
		pane.getChildren().addAll(checkbox,label,rect);
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
		});
		
		label.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                PersonInfo.getPersonInfo().setSelectedCalendar(cal);
		                System.out.println("Selected calendar: " + PersonInfo.getPersonInfo().getSelectedCalendar());
		                SidebarController.getController().weekInit();
		            }
		        }
		    }
		});
		
		if (PersonInfo.getPersonInfo().getSelectedCalendar().equals(cal)){
			System.out.println("Updating font to bold for: " + cal);
			label.setFont(Font.font(null, FontWeight.BOLD, 14));	
		}else{
			System.out.println("Setting normal font for : " + cal);
			label.setFont(Font.font(null, FontWeight.NORMAL, 14));;
		}
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
