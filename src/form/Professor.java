/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
public class Professor {
    private String professorName;
    private String email;
    private String department;
    
    public Professor(String professorName, String email) {
        this.professorName = professorName;
        this.email = email;
    }
    
    public String getProfessorName() {
        return professorName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getDepartment() {
        return department;
    }
}

