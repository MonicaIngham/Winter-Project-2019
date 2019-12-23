package checkers;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Point;

/**
 *
 * @author Mitchell Ingham
 * 
 * This is a program that lets you play against a checkers AI, made by me
 * 
 */
public class Checkers {

    public static void main(String[] args) {
        
        int[][] data = initdata();  //initializes all data into a two dimensional integer array
        
        JButton[][] buttons = initgui(data);  //initializes the gui
        
        makelisteners(data, buttons);  //makes an event listener for the buttons and adds them

        printdata(data);
        
    }
    
    
    
    //makes an action listener for every button
    public static void makelisteners(int[][] data, JButton[][] buttons) {
        
        //this makes an anonymous action event for whenever a JButton is pressed, so that code runs when you press a button
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JButton) {
                    Point p = ((JButton) e.getSource()).getLocation();  //get the button location
                    
                    //convert into array location so that we can compare with the data array
                    int x = p.x / 64;
                    int y = p.y / 64;
                    
                    //process button press
                    buttonpress(data, buttons, x, y);
                }
            }
        };
        
        //add the listener to all the buttons
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                
                buttons[y][x].addActionListener(listener);  //adds the listener at the start of the function so that buttons do stuff
                
            }
        }
    }
    
    
    
    //returns the board to a deselected state
    public static int[][] deselectpieces(int[][] data) {
        
        //loop through all squares
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                
                //if a square is green, change it back to black
                if (data[y][x] == 6) {
                    data[y][x] = 1;
                }
                else if (data[y][x] == 7) {  //if a square is a selected black piece, change it back to a normal black piece
                    data[y][x] = 3;
                }
                else if (data[y][x] == 8) {  //same for selected black kings
                    data[y][x] = 5;
                }
                
            }
        }
        
        return data;
        
    }
    
    
    
    //returns the location of a selected piece as well as the type of piece
    public static int[] selectedpiece(int[][] data) {
        
        /*this stores the data to be returned
        * 0 - xpos
        * 1 - ypos
        * 2 - piece type (0 = normal black, 1 = black king)
        */
        int[] returndata = new int[3];
        
        //loop through all squares
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                
                //if the square is a selected piece
                if (data[y][x] == 7 || data[y][x] == 8) {
                    
                    //return coordinates
                    returndata[0] = y;
                    returndata[1] = x;
                    
                    //then return the type
                    if (data[y][x] == 7) {
                        returndata[2] = 0;
                    }
                    else {
                        returndata[2] = 1;
                    }
                    
                }
                
            }
        }
        
        return returndata;
        
    }
    
    
    
    //finds legal all legal moves for black pieces
    public static int[][] showblackmoves(int[][] data) {
        
        //first find the location of the selected piece
        int[] selected = selectedpiece(data);
        
        //now that we have the information of the selected piece, we can check legal moves
        //first we'll check moves for normal pieces
        //first check upper left moves
        if (selected[0] > 0 && selected[1] > 0) {  //if not on the left edge or top edge, check upper left move
            
            if (data[selected[0] - 1][selected[1] - 1] == 1) {  //if diagonally up left one is a black square
                
                data[selected[0] - 1][selected[1] - 1] = 6;  //make it a green square
                
            }
            else if (data[selected[0] - 1][selected[1] - 1] == 2 || data[selected[0] - 1][selected[1] - 1] == 4) {  //if it is an enemy piece, check if able to take it
                
                if (selected[0] > 1 && selected[1] > 1) {  //make sure it is far enough away from the top and left edges to jump over that piece
                    
                    if (data[selected[0] - 2][selected[1] - 2] == 1) {  //if diagonally up left two is a black square
                        
                        data[selected[0] - 2][selected[1] - 2] = 6;  //make it a green square
                        
                    }
                    
                }
                
            }
        
        }
        
        //next check upper right moves
        if (selected[0] < 7 && selected[1] > 0) {  //if not on the right edge or top edge, check upper right move
            
            if (data[selected[0] + 1][selected[1] - 1] == 1) {  //if diagonally up right one is a black square
                
                data[selected[0] + 1][selected[1] - 1] = 6;  //make it a green square
                
            }
            else if (data[selected[0] + 1][selected[1] - 1] == 2 || data[selected[0] + 1][selected[1] - 1] == 4) {  //if that square is an enemy, check if you can take it
                
                if (selected[0] < 6 && selected[1] > 1) {  //if far enough away from the top and right edges to take the piece
                    
                    if (data[selected[0] + 2][selected[1] - 2] == 1) {  //if diagonally up right two is a black square
                        
                        data[selected[0] + 2][selected[1] - 2] = 6;  //make it a green square
                        
                    }
                    
                }
                
            }
            
        }
        
        //if a king, check lower left and right moves
        if (selected[2] == 1) {
            
            //check lower left moves
            if (selected[0] > 0 && selected[1] < 7) {  //if far enough away from the lower and left edges
                
                if (data[selected[0] - 1][selected[1] + 1] == 1) {  //if diagonally down left is a black square
                    
                    data[selected[0] - 1][selected[1] + 1] = 6;  //make it a green square
                    
                }
                else if (data[selected[0] - 1][selected[1] + 1] == 2 || data[selected[0] - 1][selected[1] + 1] == 4) {  //if it is an enemy piece, check if able to take it
                    
                    if (selected[0] > 1 && selected[1] < 6) {  //if far enough away from the lower and left edges
                        
                        if (data[selected[0] - 2][selected[1] + 2] == 1) {  //if diagonally down left two is a black square
                            
                            data[selected[0] - 2][selected[1] + 2] = 6;  //make it green
                            
                        }
                        
                    }
                    
                }
                
            }
            
            //check lower right moves
            if (selected[0] < 7 && selected[1] < 7) {  //if far enough away from the lower and right edges
                
                if (data[selected[0] + 1][selected[1] + 1] == 1) {  //if diagonally down right one is a black square
                    
                    data[selected[0] + 1][selected[1] + 1] = 6;  //make it green
                    
                }
                else if (data[selected[0] + 1][selected[1] + 1] == 2 || data[selected[0] + 1][selected[1] + 1] == 4) {  //if it is an enemy piece, check if able to take it
                    
                    if (selected[0] < 6 && selected[1] < 6) {  //if far enough away from the lower and right edges
                        
                        if (data[selected[0] + 2][selected[1] + 2] == 1) {  //if diagonally down right two is a black square
                            
                            data[selected[0] + 2][selected[1] + 2] = 6;  //make it green
                            
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
        return data;
        
    }
    
    
    
    //decides what happens when a button is pressed
    public static void buttonpress(int[][] data, JButton[][] buttons, int xpos, int ypos) {
        
        //if the button pressed is a black piece or a black king
        if (data[xpos][ypos] == 3 || data[xpos][ypos] == 5) {
            
            //first deselect any piece
            data = deselectpieces(data);
            
            //change that piece to be selected
            if (data[xpos][ypos] == 3) {
                data[xpos][ypos] = 7;  //for normal piece
            }
            else {
                data[xpos][ypos] = 8;  //for king
            }
            
            //find all legal moves for this piece
            data = showblackmoves(data);
            
            //after all that, update the gui
            updategame(data, buttons);
        }
        else if (data[xpos][ypos] == 6) {  //if the button pressed is a green square
            
            //we have to move the selected piece to the green square, first we find the coordinates of the selected piece
            int[] selected = selectedpiece(data);
            
            //then we check how far away the piece is
            //we know that it is a passive move when both x's and y's subtracted are both -1 or 1
            if ((xpos - selected[0] == -1 || xpos - selected[0] == 1) && (ypos - selected[1] == -1 || ypos - selected[1] == 1)) {  //this is a passive move
                
                //we know that it is a passive move, so we change the selected piece to a black square, and the pressed button to a black piece or king
                data[selected[0]][selected[1]] = 1;
                if (selected[2] == 0) {
                    data[xpos][ypos] = 3;
                }
                else {
                    data[xpos][ypos] = 5;
                }
                
                //then return the board to normal
                data = deselectpieces(data);
                
            }  //we know that it takes one piece when both x's and y's subtracted are both -2 or 2
            else if ((xpos - selected[0] == -2 || xpos - selected[0] == 2) && (ypos - selected[1] == -2 || ypos - selected[1] == 2)) {
                
                //first we do the same thing as passive moves
                data[selected[0]][selected[1]] = 1;
                if (selected[2] == 0) {
                    data[xpos][ypos] = 3;
                }
                else {
                    data[xpos][ypos] = 5;
                }
                
                //then we find the piece that was jumped over by taking the average of both x's and y's, and replace it with a black square
                data[(xpos + selected[0]) / 2][(ypos + selected[1]) / 2] = 1;
                
                //then return the board to normal
                data = deselectpieces(data);
                
            }
            
            //check if the black player has made a king by moving to the end of the board
            data = checkblackking(data);
            
            //after all that, update the game
            updategame(data, buttons);
            
        }
        
    }
    
    
    
    //checks if the black player has made a king
    public static int[][] checkblackking(int[][] data) {
        
        //loop through every square in the top row
        for (int x = 0; x < 7; x++) {
            
            if (data[x][0] == 3) {  //if it's a black piece
                
                data[x][0] = 5;  //change it to a king
                
            }
            
        }
        
        return data;
        
    }
    
    
    
    //updates the button icons
    public static void updategame(int[][] data, JButton[][] buttons) {
        
        //first get all the icons
        Icon white = new ImageIcon("Images/White.png");
        Icon black = new ImageIcon("Images/Black.png");
        Icon green = new ImageIcon("Images/Green.png");
        Icon whitepiece = new ImageIcon("Images/Whitepiece.png");
        Icon blackpiece = new ImageIcon("Images/Blackpiece.png");
        Icon whiteking = new ImageIcon("Images/Whiteking.png");
        Icon blackking = new ImageIcon("Images/Blackking.png");
        Icon selectedpiece = new ImageIcon("Images/Selectedpiece.png");
        Icon selectedking = new ImageIcon("Images/Selectedking.png");
        
        //next loop through all the buttons
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                
                //next compare the button location with the database and change the icon accordingly
                if (data[x][y] == 0) {
                    buttons[x][y].setIcon(white);
                }
                else if (data[x][y] == 1) {
                    buttons[x][y].setIcon(black);
                }
                else if (data[x][y] == 2) {
                    buttons[x][y].setIcon(whitepiece);
                }
                else if (data[x][y] == 3) {
                    buttons[x][y].setIcon(blackpiece);
                }
                else if (data[x][y] == 4) {
                    buttons[x][y].setIcon(whiteking);
                }
                else if (data[x][y] == 5) {
                    buttons[x][y].setIcon(blackking);
                }
                else if (data[x][y] == 6) {
                    buttons[x][y].setIcon(green);
                }
                else if (data[x][y] == 7) {
                    buttons[x][y].setIcon(selectedpiece);
                }
                else {
                    buttons[x][y].setIcon(selectedking);
                }
                
            }
        }

    }
    
    
    
    //initializes the gui for the game
    public static JButton[][] initgui(int[][] data) {
        
        //first make the frame to put the buttons on
        JFrame frame = new JFrame("Checkers");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        //then change the properties of the frame so that it is visible and correctly sized
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //then make the array of buttons
        JButton[][] buttons = new JButton[8][8];

        //loop through every button
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                
                buttons[y][x] = new JButton();  //create the button

                panel.add(buttons[y][x]);  //add the button
                
                buttons[y][x].setBounds(y * 64, x * 64, 64, 64);  //set the location and size
                
            }
        }
       
        //add the panel and resize the frame
        frame.add(panel);
        frame.setSize(525, 548);
        frame.setLocationRelativeTo(null);
        
        //update the game before finished
        updategame(data, buttons);
        
        return buttons;
    }



    //initializes all data for runtime
    public static int[][] initdata() {
        
        /* Database data key (all pieces are on black squares
        * 0 = white square
        * 1 = black square
        * 2 = white piece
        * 3 = black piece
        * 4 = white king
        * 5 = black king
        * 6 = green square
        * 7 = selected black piece
        * 8 = selected black king
        * 4-6 won't be used for a while
        */
        
        int[][] data = new int[8][8];  //makes an empty array
        
        for (int x = 0; x < 8; x++){ //loops through every row   
            for (int y = 0; y < 8; y++){  //loops through every column
                
                if ((x + y) % 2 == 0) {  //This finds all the white squares of a checkerboard, which is when the x + y coordinates are even.
                    
                    data[x][y] = 0;  //set to white square
                    
                }
                else if (y <= 2) {  //This finds all the starting white pieces, which fill in all black squares in the top 3 rows
                    
                    data[x][y] = 2;
                    
                }
                else if (y <= 4) {  //This finds all the empty black squares in the middle, which cover two rows
                    
                    data[x][y] = 1;
                    
                }
                else {  //At this point all the rest of the squares are black pieces
                    
                    data[x][y] = 3;
                    
                }
            }   
        }
        
        return data;
    }
    
    
    
    //prints the database to the console in a readable way
    public static void printdata(int[][] data) {
        
        for (int y = 0; y < 8; y++){
            System.out.print(data[0][y]);
            System.out.print(data[1][y]);
            System.out.print(data[2][y]);
            System.out.print(data[3][y]);
            System.out.print(data[4][y]);
            System.out.print(data[5][y]);
            System.out.print(data[6][y]);
            System.out.println(data[7][y]);
        }
        
    }
}
