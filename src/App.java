import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class App extends Application {

   

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root,1100,600);
        stage.setTitle("Employee Registration System: Log In");
        stage.setScene(scene);
        stage.show();
    }

//     CREATE DATABASE EMS;
// USE EMS;
// CREATE TABLE employee(
// 	   id int PRIMARY KEY not null AUTO_INCREMENT,
//       firstname varchar(70) NOT null,
//       lastname varchar(70) NOT null,
//       employee_no int NOT null,
//       phoneNumber varchar(70) NOT null,
//       department_name varchar(70) NOT null,
//       position_name varchar(70) NOT null,
//       salary double not null
// );

// CREATE TABLE users(
//     userId int PRIMARY KEY AUTO_INCREMENT,
// 	firstName VARCHAR(70),
//     lastName VARCHAR(70),
//     email VARCHAR(70),
//     password VARCHAR(70),
//     role VARCHAR(70),
//     phoneNumber VARCHAR(70)
// )
    
}