package com.selorioInfoman.controller;

import com.selorioInfoman.DatabaseConnection;
import com.selorioInfoman.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController {
    public ToggleGroup gender;
    @FXML
    private TextField firstName;
    @FXML
    private TextField middleName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField address;
    @FXML
    private TextField phoneNum;
    @FXML
    private TextField email;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;
    @FXML
    private TableView<Student> table;
    @FXML
    private TableColumn<Student, String> colFirstName;
    @FXML
    private TableColumn<Student, String> colMidName;
    @FXML
    private TableColumn<Student, String> colLastName;
    @FXML
    private TableColumn<Student, String> colAddress;
    @FXML
    private TableColumn<Student, String> colPhoneNum;
    @FXML
    private TableColumn<Student, String> colEmail;
    @FXML
    private TableColumn<Student, String> colGender;

    private boolean isEditing = false;
    private int studentId = 0;
    private DatabaseConnection connection;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void initialize() throws SQLException{
        connection = new DatabaseConnection();

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colMidName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        loadStudents();
    }

    public void loadStudents() throws SQLException{
        studentList.clear();
        String sql = "SELECT * FROM students";

        Statement stmt = connection.getConnection().createStatement();
        ResultSet result = stmt.executeQuery(sql);

        while(result.next()){
            Student student = new Student(result.getInt("id"),result.getString("firstName"),
                    result.getString("middleName"),result.getString("lastName"),
                    result.getString("address"),result.getString("phoneNumber"),
                    result.getString("email"),result.getString("gender"));
            studentList.add(student);
        }

        table.setItems(studentList);
    }

    @FXML
    private void save() throws SQLException {
        if(!isEditing) {
            String sql = "INSERT INTO students(firstName, middleName, lastName, address, phoneNumber, email, gender) VALUES (?, ?, ?,?, ?, ?, ?)";
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
            stmt.setString(1, firstName.getText());
            stmt.setString(2, middleName.getText());
            stmt.setString(3, lastName.getText());
            stmt.setString(4, address.getText());
            stmt.setString(5, phoneNum.getText());
            stmt.setString(6, email.getText());

            if (male.isSelected()) {
                stmt.setString(7, "Male");
            } else if (female.isSelected()) {
                stmt.setString(7, "Female");
            }

            if (stmt.executeUpdate() == 1) {
                firstName.clear();
                middleName.clear();
                lastName.clear();
                address.clear();
                phoneNum.clear();
                email.clear();
                gender.getToggles().clear();
                loadStudents();
            }
        }else{
            String sql = "UPDATE students SET firstName = ?, middleName = ?, lastName = ?, address = ?, phoneNumber = ?, email = ?, gender = ? WHERE id = ?";
            try{
                PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
                stmt.setString(1,firstName.getText());
                stmt.setString(2,middleName.getText());
                stmt.setString(3,lastName.getText());
                stmt.setString(4,address.getText());
                stmt.setString(5,phoneNum.getText());
                stmt.setString(6,email.getText());
                if(male.isSelected()){
                    stmt.setString(7,"Male");
                }
                else if (female.isSelected()){
                    stmt.setString(7, "Female");
                }else{
                    stmt.setString(7, "");
                }
                stmt.setInt(8,studentId);

                if (stmt.executeUpdate() == 1) {
                    firstName.clear();
                    middleName.clear();
                    lastName.clear();
                    address.clear();
                    phoneNum.clear();
                    email.clear();
                    loadStudents();
                    isEditing = false;
                    studentId = 0;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void delete(){
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if(selectedStudent != null){
            String sql = "DELETE from students WHERE id = ?";
            try{
                PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
                stmt.setInt(1, selectedStudent.getId());
                stmt.executeUpdate();

                studentList.remove(selectedStudent);
            }catch(SQLException e){
                e.printStackTrace();
            }

        }
    }
    @FXML
    private void edit(){
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if(selectedStudent != null){
                firstName.setText(selectedStudent.getFirstName());
            middleName.setText(selectedStudent.getMiddleName());
            lastName.setText(selectedStudent.getLastName());
            address.setText(selectedStudent.getAddress());
            phoneNum.setText(selectedStudent.getPhoneNumber());
            email.setText(selectedStudent.getEmail());
            male.setText(selectedStudent.getGender());

            isEditing = true;
            studentId = selectedStudent.getId();
        }
    }

}
