package cubeDemo;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main extends JFrame{
	public int Window_Width=900;
	public int Window_Height=700;
	public static void main(String[]args) {
		Main block=new Main();
	}
	public Main() {
		setLayout(null);
		setSize(Window_Width,Window_Height);
		setLocation(100,100);
		setBackground(Color.black);
		blockPanel blockPanel=new blockPanel();
		add(blockPanel);
		setVisible(true);
		Container cp=this.getContentPane();
		((JComponent)cp).setOpaque(false);//设置面板透明
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
	}
}
