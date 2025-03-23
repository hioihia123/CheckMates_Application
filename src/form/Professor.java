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
    private String professorID;
    
    public Professor(String professorName, String email, String professorID) {
        this.professorName = professorName;
        this.email = email;
        this.professorID = professorID;
    }
    
    public String getProfessorName() {
        return professorName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getProfessorID() {
        return professorID;
    }
}

