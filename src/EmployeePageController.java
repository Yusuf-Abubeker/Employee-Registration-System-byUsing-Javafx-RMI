import Utils.RJAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeePageController implements Initializable {

    final ObservableList<Employee> data= FXCollections.observableArrayList();
    @FXML
    private TableColumn<Employee, Void> deleteCol;

    @FXML
    private TableColumn<Employee, String> departmentCol;

    @FXML
    private TableColumn<Employee, Integer> eNo;

    @FXML
    private TableColumn<Employee, String> firstnamecol;
    @FXML
    private TableColumn<Employee, String> lastnameCol;

    @FXML
    private TableColumn<Employee, String> phoneNumberCol;

    @FXML
    private TableColumn<Employee, String> positionCol;

    @FXML
    private TableColumn<Employee, Double> salaryCol;
    @FXML
    private TableView<Employee> table;
    @FXML
    private GridPane root;

    ServerInterface serverHandler;

    @FXML
    void addEmployeeHandler(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("EmployeeRecord.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.setTitle("New Record");
        stage.sizeToScene();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.root.getScene().getWindow());
        stage.centerOnScreen();
        stage.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            serverHandler=(ServerInterface) Naming.lookup("rmi://127.0.0.1:2222/ServerInterface");
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }


        eNo.setCellValueFactory(new PropertyValueFactory<>("employee_no"));
        firstnamecol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department_name"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position_name"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));

        // Enable editing of the relevant columns
        enableEditing();

        // Load and display employee data from the server
        loadEmployeeData();
    }

    private void enableEditing() {
        table.setEditable(true);
        // Enable editing of employee number column
        eNo.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        eNo.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setEmployee_no(event.getNewValue());
            updateEmployee(employee);
        });

        // Enable editing of firstname column
        firstnamecol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstnamecol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setFirstname(event.getNewValue());
            updateEmployee(employee);
        });

        // Enable editing of lastname column
        lastnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastnameCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setLastname(event.getNewValue());
            updateEmployee(employee);
        });

        // Enable editing of phoneNumber column
        phoneNumberCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumberCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setPhoneNumber(event.getNewValue());
            updateEmployee(employee);
        });

        // Enable editing of department column
        departmentCol.setCellFactory(TextFieldTableCell.forTableColumn());
        departmentCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setDepartment_name(event.getNewValue());
            updateEmployee(employee);
        });

        // Enable editing of position column
        positionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        positionCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setPosition_name(event.getNewValue());
            updateEmployee(employee);
        });

        // Enable editing of salary column
        salaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        salaryCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setSalary(event.getNewValue());
            updateEmployee(employee);
        });
        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setTextFill(Color.RED);
                deleteButton.setOnAction(event -> {
                    deleteSelectedEmployee();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    private void loadEmployeeData() {
        List<Employee> employeeList;
        try {
            employeeList = serverHandler.getAllEmployees();
            table.getItems().addAll(employeeList);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }

    private void updateEmployee(Employee employee) {
        try {
            if (serverHandler.updateEmployee(employee)) {
                // Update successful
                System.out.println("Employee updated: " + employee.getId());
            } else {
                // Update failed
                System.out.println("Failed to update employee: " + employee.getId());
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSelectedEmployee() {
        Employee selectedEmployee = table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                if (serverHandler.deleteEmployee(selectedEmployee)) {
                    // Deletion successful
                    table.getItems().remove(selectedEmployee);
                    new RJAlert(Alert.AlertType.CONFIRMATION,"Employee id: "+selectedEmployee.getId()+" Successfully Deleted",selectedEmployee.getFirstname()+" is Deleted","CONFIRMATION").show();
                } else {
                    // Deletion failed
                    System.out.println("Failed to delete employee: " + selectedEmployee.getId());
                    new RJAlert(Alert.AlertType.CONFIRMATION,"Failed to delete employee: " + selectedEmployee.getId(),"Error ","CONFIRMATION").show();

                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

    
