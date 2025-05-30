/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ModernButton extends JButton {
    private Color normalColor = Color.BLACK;
    private Color hoverColor = new Color(30, 144, 255); // DodgerBlue
    private Color noteHoverColor = new Color(0,100,0); //Dark Green

    public ModernButton(String text, boolean noteHover) {
        super(text);
        setFont(new Font("Roboto", Font.BOLD, 14));
        setForeground(normalColor);
        setContentAreaFilled(false); // No background fill
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(noteHover){
                    setForeground(noteHoverColor);
                }
                else{
                    setForeground(hoverColor);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(normalColor);
            }
        });
    }
    
    // No custom painting is needed,  simply use the standard JButton painting
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}



