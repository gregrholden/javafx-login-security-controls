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

        final Text actiontarget1 = new Text();
        final Text actiontarget2 = new Text();
        grid.add(actiontarget1, 1, 6);
        // Create action target for system lock message
        grid.add(actiontarget2, 1, 7);

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
                // FOR TESTING PURPOSES ONLY
                System.out.println("Timer is on: " + timerOn);
                // Check if loginAttempts remain
                if (loginAttempts != 0) {
                    // If valid clear the grid and Welcome the user
                    if (isValid) {
                        // Set log variables
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
                        grid.setVisible(false);
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
