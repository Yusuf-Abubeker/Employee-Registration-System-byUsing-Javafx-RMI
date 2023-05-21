package Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class RJAlert extends Alert {

    public RJAlert(AlertType alertType) {
        super(alertType);
        init();
    }

    public RJAlert(AlertType alertType, String contentText, String headerText, String title, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        setTitle(title);
        setHeaderText(headerText);
        init();
    }

    public RJAlert(AlertType alertType, String contentText, String title, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        setTitle(title);
        setHeaderText(title);
        init();
    }

    public RJAlert(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        init();
    }

    private void init(){
        

        switch (getAlertType()){
            case ERROR:
                if (getTitle() == null) setTitle("Error");
                if (getHeaderText() == null) setHeaderText("Error");

                break;
            case WARNING:
                if (getTitle() == null) setTitle("Warning");
                if (getHeaderText() == null) setTitle("Warning");
                break;
            case INFORMATION:
                if (getTitle() == null) setTitle("Success");
                if (getHeaderText() == null) setTitle("Success");
                break;
            default:
                break;
        }
        
    }
}
