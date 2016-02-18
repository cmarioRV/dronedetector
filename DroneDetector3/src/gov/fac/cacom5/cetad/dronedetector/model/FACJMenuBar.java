package gov.fac.cacom5.cetad.dronedetector.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenuBar;

public class FACJMenuBar extends JMenuBar {

	private Color color = Color.DARK_GRAY;
	
	public FACJMenuBar() {
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
