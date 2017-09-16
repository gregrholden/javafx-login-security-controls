/**
 * Course: UMUC, SDEV 425: Mitigating Software Vulnerabilities
 * Professor: Gary Harris
 * Semester: Fall 2017, OL1
 * 
 * Author: Greg Holden
 * Assignment #2: Apply Low-Impact Security Controls
 * Date: September 16, 2017
 * Due Date: September 17, 2017
 * 
 */
package sdev425hw2;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * NOTE: JavaMailer method call not currently functional on lines 192-198. 
 * Uncomment and insert your own Gmail credentials to fix JavaMailer call.
 * 
 * @author Greg Holden
 */
public class SDEV425HW2 extends Application {
    
    // Private Fields for Logging Event Data
    private static final File LOG = new File(
        "C:\\Users\\admin\\Documents\\NetBeansProjects\\SDEV425HW2\\log.txt"
    );
    
    // User
    private String user;
    
    // Increment counter
    private int loginAttempts = 3;
    private int authCounter = 0;
    
    // Create timer
    private boolean timerOn = false;
    private long startTime = 0;
    private long stopTime = 0;
    
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SDEV425 Login");
        // Grid Pane divides your window into grids
        GridPane grid = new GridPane();
        // Align to Center
        // Note Position is geometric object for alignment
        grid.setAlignment(Pos.CENTER);
        // Set gap between the components
        // Larger numbers mean bigger spaces
        grid.setHgap(10);
        grid.setVgap(10);

        // Create some text to place in the scene
        Text scenetitle = new Text("Welcome. Login to continue.");
        // Add text to grid 0,0 span 2 columns, 1 row
        grid.add(scenetitle, 0, 0, 2, 1);

        // Create Label
        Label userName = new Label("User Name:");
        // Add label to grid 0,1
        grid.add(userName, 0, 1);

        // Create Textfield
        TextField userTextField = new TextField();
        // Add textfield to grid 1,1
        grid.add(userTextField, 1, 1);

        // Create Label
        Label pw = new Label("Password:");
        // Add label to grid 0,2
        grid.add(pw, 0, 2);

        ///////////////////////////////////
        // IA-6: AUTHENTICATOR FEEDBACK //
        ///////////////////////////////////
        // Create Passwordfield
        PasswordField pwBox = new PasswordField();
        // Add Password field to grid 1,2
        grid.add(pwBox, 1, 2);

        // Create Login Button
        Button btn = new Button("Login");
        // Add button to grid 1,4
        grid.add(btn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        // Lambda
        // Set the Action when button is clicked
        btn.setOnAction((ActionEvent e) -> {
            user = userTextField.getText();
            // Authenticate the user
            boolean isValid = authenticate(user,
                    pwBox.getText());
            // Check if timerOn and loginAttempts need to be reset
            timerResetCheck(timerOn);
            // Check if loginAttempts remain
            if (loginAttempts != 0) {
                // If valid clear the grid and Welcome the user
                if (isValid) {
                    
                    // AU-3: Log Successful Logon
                    try {
                        Log.log(LOG, user, true, 
                                "ACCESS",
                                "Non-authenticated Logon", 
                                "Main");
                    }
                    catch (IOException ioe) {
                        System.out.println("Logging error: " +
                                ioe.getMessage());
                    }
                    
                    // Reset login counter on authenticated logon
                    loginAttempts = 3;
                    
                    ///////////////////////////////////
                    // AC-8: SYSTEM USE NOTIFICATION //
                    ///////////////////////////////////
                    // Hide main grid
                    grid.setVisible(false);
                    // Write system use notification to a new GridPane
                    GridPane grid3 = new GridPane();
                    grid3.setAlignment(Pos.CENTER);
                    grid3.setHgap(10);
                    grid3.setVgap(10);
                    Text notificationTitle = new Text("Welcome!");
                    grid3.add(notificationTitle, 0, 0, 2, 1);
                    Text notification1 = new Text("Please note, you are "
                            + "currently logged into a U.S. Government "
                            + "information system.");
                    grid3.add(notification1, 0, 1, 2, 1);
                    Text notification2 = new Text("Information system usage "
                            + "may be monitored, recorded, and subject to "
                            + "audit.");
                    grid3.add(notification2, 0, 2, 2, 1);
                    Text notification3 = new Text("Unauthorized use of this "
                            + "information system is prohibited and subject "
                            + "to criminal and civil penalties.");
                    grid3.add(notification3, 0, 3, 2, 1);
                    Text notification4 = new Text("Use of this information "
                            + "system indicates consent to monitoring and "
                            + "recording.");
                    grid3.add(notification4, 0, 4, 2, 1);
                    Text notification5 = new Text("This system may only "
                            + "be used to stare at the welcome message it "
                            + "generates. Enjoy!");
                    grid3.add(notification5, 0, 5, 2, 1);
                    Text notification6 = new Text("BEFORE ALLOWED ACCESS,"
                            + "ALL USERS MUST AGREE TO THE ABOVE TERMS AND"
                            + "AUTHENTICATE THEIR ACCOUNTS:");
                    grid3.add(notification6, 0, 6, 2, 1);
                    Button agree = new Button("I Agree: AUTHENTICATE ME");
                    grid3.add(agree, 1, 8);
                    // Define parameter of new GridPane and show() it
                    Scene scene = new Scene(grid3, 650, 450);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                    
                    // Lambda
                    // Button event handler
                    agree.setOnAction((ActionEvent ae) -> {
                        
                        ///////////////////////////////////////////
                        // IA-2(1): MULTI-FACTOR AUTHENTICATION ///
                        ///////////////////////////////////////////
                        // Generate Random Auth Token
                        String token = generateRandomToken();
                        
                        // Mail Authentication Code to Registered Account
                        /* COMMENTED OUT / INSERT OWN GMAIL CREDENTIALS TO FIX
                        JavaMailer.mailAuthenticator(
                                "FROM@gmail.com",               // FROM
                                "TO@gmail.com",                 // TO
                                "password",                     // Gmail Password
                                "SDEV425: User Authentication", // Subject Line
                                token                           // Auth token
                        );                         
                        */
                        
                        // AU-3: Log Info Message
                        try {
                            Log.log(LOG, user, true, 
                                    "INFO",
                                    "Consent Granted, Auth Token Sent",
                                    "Notification");
                        }
                        catch (IOException ioe) {
                            System.out.println("Logging error: " +
                                    ioe.getMessage());
                        }
                        
                        // GET AUTHENTICATION FROM USER
                        // Make grid3 invisible
                        grid3.setVisible(false);
                        // Create New Grid with Authentication Token Input
                        GridPane grid4 = new GridPane();
                        grid4.setAlignment(Pos.CENTER);
                        grid4.setHgap(10);
                        grid4.setVgap(10);
                        // Create some text to place in the scene
                        Text scenetitle1 = new Text("An email with an "
                                + "authentication code was sent");
                        // Add text to grid 0,0 span 2 columns, 1 row
                        grid4.add(scenetitle1, 0, 0, 2, 1);
                        Label code = new Label("Enter Code: ");
                        grid4.add(code, 0, 1);
                        
                        ///////////////////////////////////
                        // IA-6: AUTHENTICATOR FEEDBACK //
                        ///////////////////////////////////
                        // Create input field with hidden feedback
                        PasswordField authToken = new PasswordField();
                        grid4.add(authToken, 1, 1);
                        
                        
                        Button authBtn = new Button("Authenticate");
                        grid4.add(authBtn, 1, 2);
                        Text actiontarget1 = new Text();
                        grid4.add(actiontarget1, 0, 4);
                        // Define parameter of new GridPane and show() it
                        Scene scene1 = new Scene(grid4, 300, 200);
                        primaryStage.setScene(scene1);
                        primaryStage.show();
                        
                        // Lambda
                        // Event Handler for Authentication Button
                        authBtn.setOnAction((ActionEvent auth) -> {
                            // Check that token and user auth code match
                            String userAuth = authToken.getText();
                            // If Authentic User:
                            if (userAuth.equals(token)) {
                                
                                // AU-3: Log Access Message
                                try {
                                    Log.log(LOG, user, true, 
                                            "ACCESS",
                                            "Authenticated Logon",
                                            "Authentication");
                                }
                                catch (IOException ioe) {
                                    System.out.println("Logging error: " +
                                            ioe.getMessage());
                                }
                                
                                authCounter = 0; // reset counter
                                grid4.setVisible(false);
                                // Create new GridPane to display welcome message
                                GridPane grid2 = new GridPane();
                                // Align to Center
                                // Note Position is geometric object for alignment
                                grid2.setAlignment(Pos.CENTER);
                                // Set gap between the components
                                // Larger numbers mean bigger spaces
                                grid2.setHgap(10);
                                grid2.setVgap(10);
                                Text scenetitle2 = new Text("Welcome " +
                                        userTextField.getText() + "!");
                                // Add text to grid 0,0 span 2 columns, 1 row
                                grid2.add(scenetitle2, 0, 0, 2, 1);
                                Scene scene2 = new Scene(grid2, 500, 400);
                                primaryStage.setScene(scene2);
                                primaryStage.show();
                                
                                // If Not Authentic User:
                            } else {
                                authCounter++;
                                if (authCounter <= 3) {
                                    
                                    // AU-3: Log Alert
                                    try {
                                        Log.log(LOG, user, false,
                                                "ALERT",
                                                "Failed Authentication Attempt",
                                                "Authentication");
                                    }
                                    catch (IOException ioe) {
                                        System.out.println("Logging error: " +
                                                ioe.getMessage());
                                    }
                                    
                                    final Text actiontarget2 = new Text();
                                    actiontarget2.setFill(Color.FIREBRICK);
                                    actiontarget2.setText("Incorrect Code");
                                    grid4.add(actiontarget2, 1, 4);
                                } else {
                                    
                                    // AU-3: Log Alert
                                    try {
                                        Log.log(LOG, user, false,
                                                "ALERT",
                                                "System Lock",
                                                "Authentication");
                                    }
                                    catch (IOException ioe) {
                                        System.out.println("Logging error: " +
                                                ioe.getMessage());
                                    }
                                    
                                    grid4.getChildren().clear();
                                    final Text actiontarget3 = new Text();
                                    actiontarget3.setFill(Color.FIREBRICK);
                                    actiontarget3.setText("System Locked. "
                                            + "Please Exit.");
                                    grid4.add(actiontarget3, 0, 1, 2, 1);
                                }
                            }
                        });
                    });
                    // If Invalid Ask user to try again
                } else {
                    // Decrement loginAttempts available on unsuccessful attempt
                    loginAttempts--;
                    
                    // AU-3: Log Alert
                    try {
                        Log.log(LOG, user, false,
                                "ALERT",
                                "Unsuccessful Logon Attempt",
                                "Main");
                    }
                    catch (IOException ioe) {
                        System.out.println("Logging error: " + 
                                ioe.getMessage());
                    }
                    
                    final Text actiontarget4 = new Text();
                    // Clear gridpane to remove actiontarget message
                    grid.getChildren().clear();
                    // Recreate gridpane with initial node objects
                    grid.add(scenetitle, 0, 0, 2, 1);
                    grid.add(userName, 0, 1);
                    grid.add(userTextField, 1, 1);
                    grid.add(pw, 0, 2);
                    grid.add(pwBox, 1, 2);
                    grid.add(btn, 1, 4);
                    grid.add(actiontarget4, 1, 6);
                    actiontarget4.setFill(Color.FIREBRICK);
                    actiontarget4.setText("Please try again.");
                }
                // If no loginAttempts remain
            } else {
                // Each time the user reaches this point, the timer is
                // activated. Each unsuccessful attempt made during the
                // timer's run will reset the timer.
                startTime = new Date().getTime(); // start of timer
                stopTime = startTime + 30000; // end of timer period
                timerOn = isTimerOn(stopTime); // turn timerOn to 'true'
                
                // AU-3: Log Alert
                try {
                    Log.log(LOG, user, false,
                            "ALERT",
                            "System Lock",
                            "Main");
                }
                catch (IOException ioe) {
                    System.out.println("Logging error: " +
                            ioe.getMessage());
                }
                
                // Recreate GridPane nodes
                final Text actiontarget5 = new Text();
                // Clear gridpane to remove actiontarget message
                grid.getChildren().clear();
                // Recreate gridpane with initial node objects
                grid.add(scenetitle, 0, 0, 2, 1);
                grid.add(userName, 0, 1);
                grid.add(userTextField, 1, 1);
                grid.add(pw, 0, 2);
                grid.add(pwBox, 1, 2);
                grid.add(btn, 1, 4);
                grid.add(actiontarget5, 1, 7);
                actiontarget5.setFill(Color.FIREBRICK);
                actiontarget5.setText("Login function temporarily locked.");
            }
        });
        
        // Lambda
        // Add to the log the data of when the app is closed
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            
            // AU-3: Log Close Event
            try {
                user = (user == null) ? "SYS" : user;
                Log.log(LOG, user, true,
                        "CLOSE",
                        "Application Closed",
                        "Default");
            }
            catch (IOException ioe) {
                System.out.println("Logging error: " + ioe.getMessage());
            }
            
        });
        
        // Set the size of Scene
        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * 
     * @return 
     */
    /////////////////////////////////////////////////////
    // USED IN IA-2(1): IDENTIFICATION AND AUTHENTICATION
    /////////////////////////////////////////////////////
    // Generates a Random String for Use as an Authentication Token
    public String generateRandomToken() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase(Locale.ROOT);
        String nums = "0123456789";
        String alphaNum = upper.concat(lower).concat(nums);
        // Random number between 5 and 10 for size of token
        Integer size = (int) (Math.random() * 6 + 5);
        // Initialize char array of that size
        char[] randChars = new char[size];
        // Transform string of all alpha numeric characters to char array
        char[] alphaNumArr = alphaNum.toCharArray();
        
        // Use loop to create random array of chars
        for (int i = 0; i < size; i++) {
            int entry = (int) (Math.random() * alphaNumArr.length);
            randChars[i] = alphaNumArr[entry];
        }
        // Convert array of chars back to string
        String token = new String(randChars);
        
        return token;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param user the username entered
     * @param pword the password entered
     * @return isValid true for authenticated
     */
    public boolean authenticate(String user, String pword) {
        boolean isValid = false;
        if (user.equalsIgnoreCase("sdevadmin")
                && pword.equals("425!pass")) {
            isValid = true;
        }

        return isValid;
    }
    
    /**
     * 
     * @param stopTime
     * @return 
     */
    ////////////////////////////////////////////
    // USED IN AC-7: UNSUCCESSFUL LOGON ATTEMPTS
    ////////////////////////////////////////////
    // Timer method to check if 30 second duration has been met.
    // Returns true if login function is currently being blocked.
    public boolean isTimerOn(long stopTime) {
        boolean timerIsOn = true;
        if (stopTime == 0) {
            timerIsOn = false;
        } else {
            long currentTime = new Date().getTime();
            if (currentTime >= stopTime) {
                timerIsOn = false;
            }
        }
        return timerIsOn;
    }
    
    /**
     * 
     * @param timerOn 
     */
    ////////////////////////////////////////////
    // USED IN AC-7: UNSUCCESSFUL LOGON ATTEMPTS
    ////////////////////////////////////////////
    // Method to reset login attempt counter and timerOn variables
    public void timerResetCheck(boolean timerOn) {
        // If the timerOn was switched to true, but now it's switched off
        // then reset variables and log the change
        if (timerOn == true && isTimerOn(stopTime) == false) {
            this.timerOn = false;
            loginAttempts = 3;
            stopTime = 0;
            startTime = 0;
            
            // AU-3: Log Alert
            try {
            Log.log(LOG, "SYS", true, 
                    "INFO", 
                    "System Unlocked", 
                    "Main");
            }
            catch (IOException ioe) {
                System.out.println("Logging error: " + ioe.getMessage());
            }
        }
    }
}
