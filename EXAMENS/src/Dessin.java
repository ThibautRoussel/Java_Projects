import java.applet.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

@SuppressWarnings("deprecation")
public class Dessin extends Applet implements MouseMotionListener{
	private static final long serialVersionUID = 1L;
	
	
	
	int posx = 100;
	int posy = 100;
	public void init() {
	setLayout(new BorderLayout());
	setSize(200, 200);
	
	addMouseMotionListener(this);
	
	}

	public void paint(Graphics g) {

		g.setColor(Color.BLACK);
		g.fillRect(posx, posy, 10, 10);

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		posx = arg0.getX();
		posy = arg0.getY();

		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
