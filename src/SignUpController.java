import java.net.URL;
import java.rmi.Naming;
import java.util.ResourceBundle;

import Utils.RJAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class SignUpController implements Initializable {

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private PasswordField password1TextFiled;

    @FXML
    private PasswordField password2TextField;

    @FXML
    private TextField phoneNumberTextFIeld;

    @FXML
    private Button registerbutton;

    @FXML
    private TextField roleTextField;

    @FXML
    private Label lblErrors;
    ServerInterface server;
    @FXML
    void handleRegisterButton(ActionEvent event) {

            if (event.getSource() == registerbutton) {

                if (signUp().equals("Success")) {
                    new RJAlert(Alert.AlertType.CONFIRMATION, "New user registered successfully").show();;
                }
            }
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            server=(ServerInterface) Naming.lookup("rmi://127.0.0.1:2222/ServerInterface");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String signUp() {
        String status = "Success";
        String password1 = password1TextFiled.getText();
        String password2 = password2TextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String role = roleTextField.getText();
        String email=emailTextField.getText();
        String phoneNumber=phoneNumberTextFIeld.getText();



        if(password1.isEmpty()
                || password2.isEmpty()||firstName.isEmpty()
                ||lastName.isEmpty()||role.isEmpty()
                ||email.isEmpty()||phoneNumber.isEmpty()
                
        ) {
            setLblError(Color.TOMATO, "All Filleds Are Required");
            status = "Error";
        } else if (!password1.equals(password2)) {
            setLblError(Color.TOMATO, "Password Mismatch");
            status = "Error";
        } else {
            //query
            try{
                int result= server.addUsers(firstName, lastName, email, password1, role, phoneNumber);
                if (result!=1) {
                    setLblError(Color.TOMATO, "Something Wrong");
                    status = "Error";
                } else {
                    setLblError(Color.GREEN, "Register Successful");
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }

        return status;
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
    }
}
