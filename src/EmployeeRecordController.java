
import Utils.RJAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class EmployeeRecordController implements Initializable {

    @FXML
    private Button clearBTN;
    @FXML
    private Button registerBTN;
    @FXML
    private TextField DepartmetnIDTF;

    @FXML
    private TextField EmployeeNOTF;

    @FXML
    private TextField PhoneNumberTF;

    @FXML
    private TextField firstNameTF;

    @FXML
    private TextField lastNameTF;

    @FXML
    private TextField positionIDTF;

    @FXML
    private TextField salaryTF;
    @FXML
    private Label errorLBL;
    
    ServerInterface server;
    ObservableList<String> departmentList = FXCollections.observableArrayList();
    ObservableList<String> positionList = FXCollections.observableArrayList();


    @FXML
    void clearBtnHanlder(ActionEvent event) {
        EmployeeNOTF.clear();
        firstNameTF.clear();
        lastNameTF.clear();
        PhoneNumberTF.clear();
        DepartmetnIDTF.clear();
        salaryTF.clear();
        positionIDTF.clear();
    }



    @FXML
    void registerBtnHandler(ActionEvent event) {

        if (firstNameTF.getText().isEmpty()  || lastNameTF.getText().isEmpty() || EmployeeNOTF.getText().isEmpty() || DepartmetnIDTF.getText().isEmpty() || positionIDTF.getText().isEmpty()
           || PhoneNumberTF.getText().isEmpty() || salaryTF.getText().isEmpty()
        ) {
            registerBTN.setDisable(true);
            errorLBL.setText("All fields are required!!!");
            errorLBL.setTextFill(Color.RED);
        } else {
            int empno = Integer.parseInt(EmployeeNOTF.getText());
            double salary = Double.parseDouble(salaryTF.getText());
            try {
                server.addEmployee(firstNameTF.getText(), lastNameTF.getText(), empno, PhoneNumberTF.getText(), DepartmetnIDTF.getText(), positionIDTF.getText(), salary);
                EmployeeNOTF.clear();
                firstNameTF.clear();
                lastNameTF.clear();
                PhoneNumberTF.clear();
                salaryTF.clear();
                DepartmetnIDTF.clear();
                positionIDTF.clear();
                new RJAlert(Alert.AlertType.CONFIRMATION,"Successfully Saved","Saved","CONFIRMATION").show();

            } catch (RemoteException e) {
                e.printStackTrace();
                new RJAlert(Alert.AlertType.ERROR,"failed to Connect to Server","Connection Error","Error").show();
            }
            

        } 
    }

    @FXML
    void keyReleasedProperty(){
        boolean isDiabled= Boolean.parseBoolean(String.valueOf((firstNameTF.getText().isEmpty())));
        registerBTN.setDisable(isDiabled);
        clearBTN.setDisable(isDiabled);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            server=(ServerInterface) Naming.lookup("rmi://127.0.0.1:2222/ServerInterface");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
             registerBTN.setDisable(true);
             clearBTN.setDisable(true);
    }
}
