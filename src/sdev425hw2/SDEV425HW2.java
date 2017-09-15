/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdev425hw2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
 *
 * @author Greg Holden
 */
public class SDEV425HW2 extends Application {
    
    // Private Fields for Logging Event Data
    private static final File LOG = new File(
        "C:\\Users\\admin\\Documents\\NetBeansProjects\\SDEV425HW2\\log.txt"
    );
    private String timestamp;
    private String user;
    private String event;
    private boolean eventSuccess;
    private String ruleInvoked;
    private String appState;
    // CONVERT timestamp TO STRING
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss, z");
    
    // Increment counter
    private int loginAttempts = 3;
    private int count = 0;
    
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

        // Set the Action when button is clicked
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // Set Timezone to UTC
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                // Get timestamp of login attempt
                timestamp = sdf.format(new Date());
                // Authenticate the user
                boolean isValid = authenticate(userTextField.getText(), 
                        pwBox.getText());
                // Check if timerOn and loginAttempts need to be reset
                timerResetCheck(timerOn);
                // Check if loginAttempts remain
                if (loginAttempts != 0) {
                    // If valid clear the grid and Welcome the user
                    if (isValid) {
                        // ADD TO AUDIT LOG
                        user = userTextField.getText();
                        event = "login attempt";
                        eventSuccess = true;
                        ruleInvoked = "none";
                        appState = "Secure / Active User";
                        // Log successful login
                        try {
                            log(LOG, timestamp, user, event, eventSuccess, 
                                    ruleInvoked, appState);
                        } 
                        catch (IOException ioe) {
                            System.out.println("Logging error: " + ioe.getMessage());
                        }
                        // Reset login counter on authenticated logon
                        loginAttempts = 3;
                        
                        // SYSTEM USE NOTIFICATION
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
                        
                        // Button event handler
                        agree.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent ae) {
                                
                                // MULTI-FACTOR AUTHENTICATION
                                // Generate Random Auth Token
                                String token = generateRandomToken();
                                // Mail Authentication Code to Registered Account
                                JavaMailer.mailAuthenticator("gregrholden@gmail.com",
                                        "gregrholden@gmail.com", "Lilach_83", 
                                        "SDEV425: User Authentication", token);
                                
                                // ADD TO AUDIT LOG
                                // Log user consent
                                user = userTextField.getText();
                                event = "INFO--USER_CONSENT_GRANTED: AUTHENTICATION_TOKEN_SENT";
                                eventSuccess = true;
                                ruleInvoked = "AC-8: System Use Notification";
                                appState = "Active User";
                                try {
                                    log(LOG, timestamp, user, event, eventSuccess, 
                                            ruleInvoked, appState);
                                } 
                                catch (IOException ioe) {
                                    System.out.println("Logging error: " + ioe.getMessage());
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
                                Text scenetitle = new Text("An email with an "
                                        + "authentication code was sent");
                                // Add text to grid 0,0 span 2 columns, 1 row
                                grid4.add(scenetitle, 0, 0, 2, 1);
                                Label code = new Label("Enter Code: ");
                                grid4.add(code, 0, 1);
                                // Create input field with hidden feedback
                                PasswordField authToken = new PasswordField();
                                grid4.add(authToken, 1, 1);
                                Button authBtn = new Button("Authenticate");
                                grid4.add(authBtn, 1, 2);
                                Text actiontarget = new Text();
                                grid4.add(actiontarget, 0, 4);
                                
                                // Define parameter of new GridPane and show() it
                                Scene scene = new Scene(grid4, 300, 200);
                                primaryStage.setScene(scene);
                                primaryStage.show();
                                
                                // Event Handler for Authentication Button
                                authBtn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent auth) {
                                        // Check that token and user auth code match
                                        String userAuth = authToken.getText();
                                        // If Authentic User:
                                        if (userAuth.equals(token)) {
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
                                            Text scenetitle = new Text("Welcome " + 
                                                    userTextField.getText() + "!");
                                            // Add text to grid 0,0 span 2 columns, 1 row
                                            grid2.add(scenetitle, 0, 0, 2, 1);
                                            Scene scene = new Scene(grid2, 500, 400);
                                            primaryStage.setScene(scene);
                                            primaryStage.show();
                                        // If Not Authentic User:
                                        } else {
                                            authCounter++;
                                            if (authCounter <= 3) {
                                                final Text actiontarget = new Text();
                                                actiontarget.setFill(Color.FIREBRICK);
                                                actiontarget.setText("Incorrect Code");
                                                grid4.add(actiontarget, 1, 4);
                                            } else {
                                                grid4.getChildren().clear();
                                                final Text actiontarget = new Text();
                                                actiontarget.setFill(Color.FIREBRICK);
                                                actiontarget.setText("System Locked. Please Exit.");
                                                grid4.add(actiontarget, 0, 1, 2, 1);
                                            }
                                            
                                        }
                                    }
                                });
                            }
                        });
                        
                        
                       // If Invalid Ask user to try again
                    } else {
                        // Decrement loginAttempts available on unsuccessful attempt
                        loginAttempts--;
                        // Increment count on unsuccessful logon attempt
                        count++;
                        // FOR TESTING PURPOSES ONLY
                        System.out.println("Login attempts: " + count);
                        // Set Log variables
                        user = userTextField.getText();
                        event = "login attempt";
                        eventSuccess = false;
                        // For now, ruleInvoked and appState remain generic
                        // These will change when other security rules are added
                        ruleInvoked = "Unsuccessful Logon Attempt";
                        appState = "No Active User";
                        try {
                            log(LOG, timestamp, user, event, eventSuccess, 
                                    ruleInvoked, appState);
                        } 
                        catch (IOException ioe) {
                            System.out.println("Logging error: " + ioe.getMessage());
                        }

                        final Text actiontarget = new Text();
                        // Clear gridpane to remove actiontarget message
                        grid.getChildren().clear();
                        // Recreate gridpane with initial node objects
                        grid.add(scenetitle, 0, 0, 2, 1);
                        grid.add(userName, 0, 1);
                        grid.add(userTextField, 1, 1);
                        grid.add(pw, 0, 2);
                        grid.add(pwBox, 1, 2);
                        grid.add(btn, 1, 4);
                        grid.add(actiontarget, 1, 6);
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Please try again.");
                    }
                // If no loginAttempts remain
                } else {
                    // Each time the user reaches this point, the timer is
                    // activated. Each unsuccessful attempt made during the
                    // timer's run will reset the timer.
                    count++; // increment count of unsuccessful attempts
                    startTime = new Date().getTime(); // start of timer
                    stopTime = startTime + 30000; // end of timer period
                    timerOn = isTimerOn(stopTime); // turn timerOn to 'true'
                    // FOR TESTING PURPOSES ONLY
                    System.out.println("Login attempts: " + count);
                    
                    // Log system lock event
                    user = userTextField.getText();
                    event = "WARNING--SYSTEM_LOCK_ENABLED";
                    eventSuccess = false;
                    ruleInvoked = "AC-7 - Unsuccessful Logon Attempts";
                    appState = "Account lock active";
                    // Call log() with relevant data from above
                    try {
                        log(LOG, timestamp, user, event, eventSuccess, 
                                ruleInvoked, appState);
                    } 
                    catch (IOException ioe) {
                        System.out.println("Logging error: " + ioe.getMessage());
                    }
                    
                    // Recreate GridPane nodes
                    final Text actiontarget = new Text();
                    // Clear gridpane to remove actiontarget message
                    grid.getChildren().clear();
                    // Recreate gridpane with initial node objects
                    grid.add(scenetitle, 0, 0, 2, 1);
                    grid.add(userName, 0, 1);
                    grid.add(userTextField, 1, 1);
                    grid.add(pw, 0, 2);
                    grid.add(pwBox, 1, 2);
                    grid.add(btn, 1, 4);
                    grid.add(actiontarget, 1, 7);
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Login function temporarily locked.");
                }
            }
        });
        // Add to the log the data of when the app is closed
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                // Set Timezone to UTC
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                // Set log values
                timestamp = sdf.format(new Date());
                user = userTextField.getText();
                event = "Application closed";
                eventSuccess = true;
                // The number of unsuccessful login attempts during this session
                ruleInvoked = count + " unsuccessful login attempts during this session";
                appState = "Inactive";
                try {
                    log(LOG, timestamp, user, event, eventSuccess, 
                            ruleInvoked, appState);
                } 
                catch (IOException ioe) {
                    System.out.println("Logging error: " + ioe.getMessage());
                }
            }
        });
        // Set the size of Scene
        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
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
    
    // Method to reset login attempt counter and timerOn variables
    public void timerResetCheck(boolean timerOn) {
        // If the timerOn was switched to true, but now it's switched off
        // then reset variables and log the change
        if (timerOn == true && isTimerOn(stopTime) == false) {
            this.timerOn = false;
            loginAttempts = 3;
            stopTime = 0;
            startTime = 0;
            user = "SYS";
            event = "INFO--SYSTEM_LOCK_DEACTIVATED.";
            eventSuccess = true;
            ruleInvoked = "System Lock Lifted; lock duration exceeded";
            appState = "System active, user status unknown";
            try {
                log(LOG, timestamp, user, event, eventSuccess, 
                        ruleInvoked, appState);
            } 
            catch (IOException ioe) {
                System.out.println("Logging error: " + ioe.getMessage());
            }
        }
    }
    
    /**
     * <title> LOG WRITING METHOD </title>
     * @param LOG
     * @param timestamp
     * @param user
     * @param event
     * @param eventSuccess
     * @param ruleInvoked
     * @param appState
     * @throws java.io.IOException
     */
    public final void log(File LOG, String timestamp, String user, String event, 
            boolean eventSuccess, String ruleInvoked, String appState) 
            throws IOException {
        
        Writer out = null;
        try {
            // If LOG does not yet exist, create it
            if (!LOG.exists()){
                LOG.createNewFile();
            }
            // Convert Boolean to String
            String evSuccessInStr = eventSuccess ? "true" : "false";
            
            // Specify UTF-8 encoding in OSW to avoid default encoding
            // Also use "true" as second param to FOS to allow append method
            out = new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(LOG, true), "UTF-8"));
            // Write log data to log file
            out.write("Time: " + timestamp + "\r\n");
            out.write("Username Entered: " + user + "; Event: " + event + 
                    "; Success: " + evSuccessInStr + "\r\n");
            out.write("Security Rule: " + ruleInvoked + 
                    "; Current Security State: " + appState + "\r\n");
            
        }
        catch (IOException ioe) {
            System.out.println("File writing error: "+ ioe.getMessage());
        }
        // Close the streams, ensure additional IOExceptions are handled
        finally {
            try { 
                if (out != null) {
                    out.flush();
                    out.close();
                }
            }
            catch(IOException ioe) {
                System.out.println("Problem closing output streams: " + 
                        ioe.getMessage());
                
            }
        }
    }
}
