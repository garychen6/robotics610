package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.IterativeRobot;
//Classes we need
import edu.wpi.first.wpilibj.Watchdog; //The robot has a built in "Watchdog" function that theoretically makes sure that the robot
                                       //doesn't rampage around killing people. The watchdog has to be 'fed' to allow the robot to
                                       //run.
import edu.wpi.first.wpilibj.Jaguar; //The Jaguars are the PWM motors on the robot - unlike the 'Spikes' they have more than
                                     //forward, off, and backwards. It can be set to any decimal value between -1 and 1 inclusive,
                                     //-1 being full backwards and 1 being full forwards.
import edu.wpi.first.wpilibj.Relay; //These are motors that run only full forward or full backwards. On the robot they are called
                                    //spikes.
import edu.wpi.first.wpilibj.Victor; //Also servo motors, used for different purposes than the Jaguars.
import edu.wpi.first.wpilibj.Joystick; //The joysticks on the driver station. This class can also be used for the gamepad attached,
                                       //but you shouldn't need that today.

public class RobotTemplate extends IterativeRobot {
    //All we are doing is declaring variables, not instantiating them. We are doing this for a reason.
    //They have special names, like 'Watchdog' or 'Relay', but they work the same way as 'int' or 'String'.
    Watchdog watchdog;
    Jaguar leftMotor;
    Jaguar rightMotor;
    Victor a;
    Victor b;
    Relay rA;
    Relay rB;
    Relay rC;
    Joystick leftStick;
    Joystick rightStick;

    
    /**
     * We will use this function to instantiate the variables we created earlier.
     */
    public void robotInit() {
        watchdog = Watchdog.getInstance(); // This is a special way of instantiating a variable, using a function.
        a = new Victor(1); //Most complex variables are instantiated with the 'new' keyword. Notice that Victor() is a function.
                           //This is called a constructor, because it constructs the variable. For all of these variables, the
                           //single parameter is the port that the component is plugged into.
        b = new Victor(2);
        rightMotor = new Jaguar(3);
        leftMotor = new Jaguar(4);
        rA = new Relay(5);
        rB = new Relay(6);
        rC = new Relay(7);
        leftStick = new Joystick(1);
        rightStick = new Joystick(2); //Erm, you may want to do some trial and error with these. I'm not sure which is 1 and which
                                      //is 2.
    }

    /**
     * Don't use autonomous. Reason being, if you hit something or break the robot while I'm not there, it's my fault.
     */
    public void autonomousPeriodic() {
        watchdog.feed(); //Leave this in - it feeds the watchdog, keeping the robot running
    }

    /**
     * This is the function that calls (in a loop) while teleoperated mode is enabled.
     * #1 rule: Do not let Josh drive.
     */
    public void teleopPeriodic() {
        watchdog.feed(); //Leave this in - it feeds the watchdog, keeping the robot running
        //These are some basic functions of the components - note their syntax.
        //leftStick.getRawButton(4); - Returns true or false, depending on whether button 4 on the robot is being pressed. You can
                                     //replace 4 with any number. The buttons on the joysticks are labelled.
        //leftStick.getTrigger(); - Returns true or false depending on if the joystick's trigger is being pressed.
        //leftStick.getY(); - Returns a decimal number from -1 to 1, depending on how far the joystick is pushed forwards or backwards.
        //leftMotor.set(leftStick.getY()); - The only function of the jaguars and victors that are important is the set() function.
                                           //It sets the motor to the speed given. Fortunately, getY() returns a number between 1 and
                                           //-1, the same as the parameter of the set function.
        //rA.set(Relay.Value.kOff);
        //rB.set(Relay.Value.kForward);
        //rC.set(Relay.Value.kReverse); //The three states for the Spikes are off, forward, and reverse.
        //What follows is a sample implementation for the robot, using nothing but the functions listed above.
        leftMotor.set(leftStick.getY());
        rightMotor.set(-rightStick.getY()); //One of the motors is reversed. I'm not sure which one. This is why
                                            //we are sending NEGATIVE of the joystick. If we are pushing forwards,
                                            //we want the motor to go backwards, which causes the wheels to spin forwards.
        if(leftStick.getTrigger()){
            rA.set(Relay.Value.kForward);
            rB.set(Relay.Value.kForward);
            rC.set(Relay.Value.kForward);
        } else {
            rA.set(Relay.Value.kOff);
            rB.set(Relay.Value.kOff);
            rC.set(Relay.Value.kOn); //Just because
        }
        if(rightStick.getTrigger()){
            a.set(1);
            b.set(-1); //I'm not actually sure one is reversed. Trial and error is your friend.
        }
    }
    
}
