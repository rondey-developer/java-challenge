package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.Exception.EmployeeNotExistException;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        try {
            return employeeService.getEmployee(employeeId);
        }catch (Exception e){
            throw new EmployeeNotExistException();
        }
    }

    @PostMapping("/employees")
    public void saveEmployee(@RequestBody Employee employee){
        employeeService.saveEmployee(employee);
        logger.info("Employee Saved Successfully");
    }

    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
        logger.info("Employee Deleted Successfully");
    }

    @PutMapping("/employees/{employeeId}")
    public void updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        Employee emp = employeeService.getEmployee(employeeId);
        if(emp != null) {
            employeeService.updateEmployee(employeeId, employee);
            logger.info("Employee updated Successfully");
        }else throw new EmployeeNotExistException();
    }
}
