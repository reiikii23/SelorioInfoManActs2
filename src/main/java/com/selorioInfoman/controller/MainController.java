package com.selorioInfoman.controller;

import com.selorioInfoman.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    private DatabaseConnection connection;

    public void initialize(){
        connection = new DatabaseConnection();
    }

    @FXML
    private void save() throws SQLException {
        String sql = "INSERT INTO students(firstName, middleName, lastName, address, phoneNumber, email, gender) VALUES (?, ?, ?,?, ?, ?, ?)";
        PreparedStatement stmt = connection.connection.prepareStatement(sql);
        stmt.setString(1,firstName.getText());
        stmt.setString(2,middleName.getText());
        stmt.setString(3,lastName.getText());
        stmt.setString(4,address.getText());
        stmt.setString(5,phoneNum.getText());
        stmt.setString(6,email.getText());

        if (male.isSelected()){
            stmt.setString(7, "Male");
        }
        else if (female.isSelected()){
            stmt.setString(7,"Female");
        }
        stmt.execute();
    }
}
