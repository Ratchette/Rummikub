//package rummikub;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//
//public class Gui extends JFrame implements ActionListener{
//	private static final long serialVersionUID = 1L;
//	public static final int WINDOW_HEIGHT = 700;
//    public static final int WINDOW_WIDTH = 500;
//    private static final int BORDER_SIZE = 7;
//    
//    private JPanel gameGrid;
//    private JButton[][] grid = new JButton[3][3];
//    private JLabel statusBar;
//    private JLabel topbar;
//	
//	private Client myClient;
//	private GameInfo myGame;
//
//	public Gui(String serverIP){
//		super("Rummikub");
//        
//        this.setAlwaysOnTop(true);
//        this.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
//        this.setResizable(false);
//        this.setLocationRelativeTo(null);
//        
//        topbar = new JLabel("testing");
//        topbar.setPreferredSize(new Dimension(500, 140));
//        this.add(topbar, BorderLayout.NORTH);
//        
//        gameGrid = new JPanel();
//        gameGrid.setPreferredSize(new Dimension(500, 500));
//
//        /*
//        for(int i=0; i<3; i++){
//            for(int j=0; j<3; j++){
//                grid[i][j] = new JButton();
//                grid[i][j].setName(Integer.toString(i*3 + j));
//                grid[i][j].setIcon(myTheme.blankSquare);
//                gameGrid.add(grid[i][j]);
//            }
//        }
//        */
//        this.add(gameGrid, BorderLayout.CENTER);
//        
//        /*
//        statusBar = myTheme.statusField;
//        statusBar.setPreferredSize(new Dimension(500, 60));
//        statusBar.setFont(new Font("Dialog", 1, 20));
//        statusBar.setText("Waiting for an opponent ...");
//        statusBar.setHorizontalTextPosition(JLabel.CENTER);
//        this.add(statusBar, BorderLayout.SOUTH);*/
//        
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
////		myClient = new Client(this, serverIP);
////		myClient.connect(serverIP);
////		myClient.execute();
//	}
//	
//	public void updateGui(String message){
//		topbar.setText(message);
//	}
//	
//	@Override
//	public void actionPerformed(ActionEvent arg0) {
//		// TODO Auto-generated method stub
//	}
//
//}
