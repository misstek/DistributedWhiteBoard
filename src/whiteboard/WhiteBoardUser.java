package whiteboard;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

import javax.swing.*;
import client.Client;



public class WhiteBoardUser extends JFrame{

    private Client client;

    public boolean uploadFlag = false;
    public boolean downloadFlag = true;

    private JScrollPane scrollUserlist;
    public JList<String> userList;

    int whiteBoardWidth = 1130;
    int whiteBoardHeight = 620;

    private JToolBar toolBar;

    private JButton buttonPencil;
    private JButton buttonErase;

    private JButton buttonLine;
    private JButton buttonCircle;
    private JButton buttonOval;
    private JButton buttonSquare;
    private JButton buttonRectangle;
    private JButton buttonText;

    private Color foreColor = Color.BLACK;
    private Color backColor = Color.WHITE;
    private String currentState = "pencil";
    private int pixelSize = 5;

    private int whiteBoardCanvasWidth = 800;
    private int whiteBoardCanvasHeight = 600;

    public BufferedImage image = new BufferedImage(whiteBoardCanvasWidth, whiteBoardCanvasHeight, BufferedImage.TYPE_INT_BGR);

    private Graphics2D g = (Graphics2D) image.getGraphics();
    WhiteBoardCanvas canvas = new WhiteBoardCanvas();

    private int mouseX1;
    private int mouseY1;
    private int mouseX2;
    private int mouseY2;
    private int mouseX;
    private int mouseY;
    private boolean mouseDragFlag = false;





    public WhiteBoardUser(Client client) {
        this.client = client;
	initiateWhiteBoard();
	initiateCanvas();
	initiateUserlist();
	addListener();
    }


    public void initiateWhiteBoard()
    {
	setTitle("WhiteBoard");
	setBounds( 500, 500, whiteBoardWidth, whiteBoardHeight);

	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setResizable(false);

	buttonPencil = new JButton();
	buttonPencil.setText("Pencil");

	buttonErase = new JButton();
	buttonErase.setText("Eraser");

	buttonLine = new JButton();
	buttonLine.setText("Line");

	buttonCircle = new JButton();
	buttonCircle.setText("Circle");

	buttonOval = new JButton();
	buttonOval.setText("Oval");

	buttonSquare = new JButton();
	buttonSquare.setText("Square");

	buttonRectangle = new JButton();
	buttonRectangle.setText("Rectangle");

	buttonText = new JButton();
	buttonText.setText("Text");

	toolBar = new JToolBar();
	toolBar.setOrientation(SwingConstants.VERTICAL);
	getContentPane().add(toolBar, BorderLayout.WEST);
	toolBar.addSeparator();

	toolBar.add(buttonPencil);
	toolBar.add(buttonErase);
	toolBar.add(buttonLine);
	toolBar.add(buttonCircle);
	toolBar.add(buttonOval);
	toolBar.add(buttonSquare);
	toolBar.add(buttonRectangle);
	toolBar.add(buttonText);
	toolBar.addSeparator();

    }

    public void initiateUserlist(){

	scrollUserlist = new JScrollPane();
	scrollUserlist.setBounds(0, 0, 150, 150);
	getContentPane().add(scrollUserlist,BorderLayout.EAST);

	userList = new JList();
	userList.setFont(new Font("Times", Font.PLAIN, 21));
	scrollUserlist.setViewportView(userList);


    }

    public void initiateCanvas() {
	g.fillRect(0, 0, whiteBoardCanvasWidth, whiteBoardCanvasHeight);
	canvas.setImage(image);
	getContentPane().add(canvas);
    }

    public void setCanvas(BufferedImage importedImage) {
	this.image=importedImage;
	this.g=(Graphics2D)this.image.getGraphics();
	this.canvas.setImage(this.image);
	this.canvas.repaint();
    }

/*
    public static void main(String[] args) {
	new WhiteBoard().setVisible(true);
    }

 */


    private  void addListener() {
	buttonPencil.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		currentState = "pencil";
	    }
	});

	buttonErase.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		currentState = "eraser";
	    }
	});

	buttonLine.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		currentState = "line";
	    }
	});

	buttonOval.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		currentState = "oval";
	    }
	});

	buttonCircle.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		currentState = "circle";
	    }
	});

	buttonRectangle.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		currentState = "rectangle";
	    }
	});

	buttonSquare.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		currentState = "square";
	    }
	});

	buttonText.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		currentState = "text";
	    }
	});

	addWindowListener(new java.awt.event.WindowAdapter() {
	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	        try {
	            client.remoteMethods.removeUsername(client.username);
	        } catch (RemoteException e) {
		    e.printStackTrace();
		} finally {

	        }
	    }
	});



	canvas.addMouseListener(new MouseAdapter(){
	    public void mousePressed(final MouseEvent e)
	    {
		downloadFlag=false;
		mouseX1 = e.getX();
		mouseY1 = e.getY();
	    }
	});


	canvas.addMouseListener(new MouseAdapter(){
	    public void mouseReleased(final MouseEvent e){
		downloadFlag=false;
		mouseDragFlag = false;
		mouseX2 = e.getX();
		mouseY2 = e.getY();

		int positionX = Math.min(mouseX1,mouseX2);
		int positionY = Math.min(mouseY1,mouseY2);
		int width = Math.abs(mouseX1-mouseX2);
		int height = Math.abs(mouseY1-mouseY2);

		if(currentState.equals("line"))
		{
		    g.setColor(foreColor);
		    g.drawLine(mouseX1, mouseY1, mouseX2, mouseY2);
		}
		else if(currentState.equals("oval"))
		{
		    g.setColor(foreColor);
		    g.drawOval(positionX, positionY, width, height);
		}
		else if(currentState.equals("circle"))
		{
		    g.setColor(foreColor);
		    g.drawOval(positionX, positionY, width, width);
		}
		else if(currentState.equals("rectangle"))
		{
		    g.setColor(foreColor);
		    g.drawRect(positionX, positionY, width, height);
		}
		else if(currentState.equals("square"))
		{
		    g.setColor(foreColor);
		    g.drawRect(positionX, positionY, width, width);
		}
		else if(currentState.equals("text")) {
		    String inputString = JOptionPane.showInputDialog(null,null,"Input text",JOptionPane.PLAIN_MESSAGE);
		    g.setColor(foreColor);
		    g.drawString(inputString, e.getX(), e.getY());
		}

		canvas.repaint();
		uploadFlag = true;

	    }

	});


	canvas.addMouseMotionListener(new MouseMotionAdapter()
	{
	    public void mouseDragged(final MouseEvent e)
	    {
		downloadFlag = false;
		if(currentState.equals("eraser"))
		{
		    if(mouseDragFlag==true) {
			g.setColor(backColor);
			g.fillRect(mouseX, mouseY, pixelSize * 5, pixelSize * 5);
			canvas.repaint();
			uploadFlag = true;
		    }
		}
		if(currentState.equals("pencil"))
		{
		    if(mouseDragFlag==true) {
			g.setColor(foreColor);
			g.drawLine(mouseX, mouseY, e.getX(), e.getY());
			canvas.repaint();
			uploadFlag = true;
		    }
		}
		mouseX = e.getX();
		mouseY = e.getY();
		mouseDragFlag=true;
	    }
	});

    }
}
