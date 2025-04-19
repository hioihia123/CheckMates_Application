/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ModernScrollBarUI extends BasicScrollBarUI {
  @Override
  protected void configureScrollBarColors() {
    thumbColor = new Color(0x888888AA, true);
    trackColor = new Color(0xEEEEEE);
  }

  @Override
  protected Dimension getMinimumThumbSize() {
    return new Dimension(8, 30);
  }

  @Override
  protected JButton createDecreaseButton(int orientation) {
    return zeroButton();
  }

  @Override
  protected JButton createIncreaseButton(int orientation) {
    return zeroButton();
  }

  private JButton zeroButton() {
    JButton btn = new JButton();
    btn.setPreferredSize(new Dimension(0, 0));
    btn.setMinimumSize(new Dimension(0, 0));
    btn.setMaximumSize(new Dimension(0, 0));
    return btn;
  }

  @Override
  protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    g.setColor(trackColor);
    g.fillRoundRect(trackBounds.x, trackBounds.y,
                    trackBounds.width, trackBounds.height,
                    8, 8);
  }

  @Override
  protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    g.setColor(thumbColor);
    g.fillRoundRect(thumbBounds.x, thumbBounds.y,
                    thumbBounds.width, thumbBounds.height,
                    8, 8);
  }
}

