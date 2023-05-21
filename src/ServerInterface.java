import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote, Serializable {
    int addEmployee(String firstname, String lastname, int employee_no, String phoneNumber, String department_name, String position_name, double salary) throws RemoteException;
    List<Employee> getAllEmployees() throws RemoteException;
    boolean updateEmployee(Employee employee) throws RemoteException;
    boolean deleteEmployee(Employee employee) throws RemoteException;
    int addUsers(String firstName,String lastName,String email,String password,String role,String phoneNumber) throws RemoteException;
    String logIn(String username, String password) throws RemoteException;
    }
