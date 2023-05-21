import java.rmi.Naming;
import java.sql.SQLException;
import java.util.Objects;

import Utils.RJAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnSignin;

    String role=null;
    @FXML
    public void handleButtonAction(ActionEvent event) throws SQLException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        if(username.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
        } else {
            if (event.getSource() == btnSignin) {
                try {
                    ServerInterface server=(ServerInterface) Naming.lookup("rmi://127.0.0.1:2222/ServerInterface");
                    if (server.logIn(username,password).equals("Success")) {
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.close();
                        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml"))),1100,600);
                        stage.setTitle("EMS user : "+ txtUsername.getText());
                        stage.setScene(scene);
                        stage.show();
                    }
                    else if (server.logIn(username,password).equals("notAdmin")){
                        new RJAlert(Alert.AlertType.ERROR,"THIS PROJECT IS BUILD FOR ADMIN PERSPECTIVE ONLY").show();
                    } else{
                        setLblError(Color.TOMATO, "something went wrong!");
                    }       
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    setLblError(Color.TOMATO, "Server Connection Error");
                }       
            }
        }
    }
    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
    }
}