package gov.fac.cacom5.cetad.dronedetector.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JMenu;

public class FACJMenu extends JMenu {
	
	private Color color = Color.GRAY;
	
	public FACJMenu(String title) {
		super(title);
	}
	
	public void setColor(Color pColor){
		this.color = pColor;
	}
	
	@Override
	protected void paintComponent(Graphics pGraphics) {
		super.paintComponent(pGraphics);
		Graphics2D g2d = (Graphics2D) pGraphics;
        g2d.setColor(color);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

}
