import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Utils.ConnectionUtil;

public class Server extends UnicastRemoteObject implements ServerInterface {

    protected Server() throws RemoteException {
        super();
    }
    private PreparedStatement selectAllEmployee;
    private PreparedStatement updateEmployee;
    private PreparedStatement deleteEmployee;

    @Override
    public int addEmployee(String firstname, String lastname, int employee_no, String phoneNumber,
        String department_name, String position_name, double salary) throws RemoteException {
        Connection connection = Utils.ConnectionUtil.conDB();
        int rowsAffected = 0;
            String insertQuery = "INSERT INTO employee (firstname, lastname, employee_no, phoneNumber, department_name, position_name, salary) VALUES (?, ?, ?, ?, ?, ?, ?);";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1,firstname);
                insertStmt.setString(2, lastname);
                insertStmt.setInt(3, employee_no);
                insertStmt.setString(4, phoneNumber);
                insertStmt.setString(5, department_name);
                insertStmt.setString(6, position_name);
                insertStmt.setDouble(7, salary); 
                rowsAffected = insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;  
    }


public List<Employee> getAllEmployees() {
    List<Employee> employeeList = new ArrayList<>();
    try {
        Connection connection = ConnectionUtil.conDB();
        selectAllEmployee = connection.prepareStatement("select * from employee");
        ResultSet resultSet = selectAllEmployee.executeQuery();
        while (resultSet.next()) {
            Employee employee = new Employee(
                    resultSet.getInt("id"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getInt("employee_no"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getString("department_name"),
                    resultSet.getString("position_name"),
                    resultSet.getDouble("salary")
            );
            employeeList.add(employee);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle SQL query error
    }
    return employeeList;
}

public boolean updateEmployee(Employee employee) {
    try {
        Connection connection = ConnectionUtil.conDB();
        updateEmployee = connection.prepareStatement("UPDATE employee " +
                "SET employee_no = ?, firstname = ?, lastname = ?, phoneNumber = ?, " +
                "department_name = ?, position_name = ?, salary = ? " +
                "WHERE id = ?");
        updateEmployee.setInt(1, employee.getEmployee_no());
        updateEmployee.setString(2, employee.getFirstname());
        updateEmployee.setString(3, employee.getLastname());
        updateEmployee.setString(4, employee.getPhoneNumber());
        updateEmployee.setString(5, employee.getDepartment_name());
        updateEmployee.setString(6, employee.getPosition_name());
        updateEmployee.setDouble(7, employee.getSalary());
        updateEmployee.setInt(8, employee.getId());

        int rowsAffected = updateEmployee.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle SQL update error
    }
    return false;
}

public boolean deleteEmployee(Employee employee) {
    try {
        Connection connection = ConnectionUtil.conDB();
        deleteEmployee = connection.prepareStatement("DELETE FROM employee WHERE id = ?");
        deleteEmployee.setInt(1, employee.getId());
        int rowsAffected = deleteEmployee.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle SQL delete error
    }
    return false;
}

@Override
public int addUsers(String firstName, String lastName, String email, String password, String role,
    String phoneNumber) throws RemoteException {
    Connection connection = Utils.ConnectionUtil.conDB();
    int rowsAffected = 0;
        String insertQuery = "INSERT INTO users "+
        "(firstName,lastName,email,password,role,phoneNumber)" +
        " VALUES (?,?,?,?,?,?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1,firstName);
            insertStmt.setString(2, lastName);
            insertStmt.setString(3, email);
            insertStmt.setString(4, password);
            insertStmt.setString(5, role);
            insertStmt.setString(6, phoneNumber);
            rowsAffected = insertStmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return rowsAffected;
}
@Override  
public String logIn(String username, String password) {
    String status = "Success";
    if (username.isEmpty() || password.isEmpty()) {
        status = "Error";
    } else {
        //query
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try {
            Connection con = ConnectionUtil.conDB();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Admin")) {
                    status = "Success";
                } else {
                    status = "notAdmin";
                }
            } else {
                status = "Error";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            status = "Exception";
        }
    }
    return status;
}

    
public static void main(String[] args) {
    try {
        Registry r=LocateRegistry.createRegistry(2222);
        Server server=new Server();
        r.rebind("ServerInterface", server);
        System.out.println("Server is running");
    } catch (Exception e) {
        System.out.println("Server is not running");
    }
}

}
