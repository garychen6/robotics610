# Basic Drive Code with Command Based Robot Template #

In this wiki entry we will be instructing you in how to create a command based project that will drive our 2011-2012 Rebound Rumble directly forward for 10 seconds. In this tutorial we are assuming that you have the current FRC netbeans plugins.


# Step 0: Creating the Project #

Create a new project in netbeans. In the new project menu under FRC Java select CommandBasedRobotTemplateProject.


# Step 1: Defining Electrical Constants #

Next thing we like to do when creating any robot code project is define all our electrical constants a class.

For this projects sake we will be using these constants:
  * public static final int DriveLeftMaster = 2;
  * public static final int DriveLeftSlave = 3;
  * public static final int DriveRightMaster = 5;
  * public static final int DriveRightSlave = 4;

These constants refer to the CAN ID of each drivetrain jaguar on our robot.
Please place them within the Robot Map Class.

# Step 2: Defining DriveTrain Subsystem #

First thing you should do is rename the default subsystem class to DriveTrain. From now on we will refer to this class as DriveTrain.

Inside this class you will define and initialize the 4 jaguars on the robot.

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class DriveTrain extends Subsystem {
> CANJaguar rightMaster;

> CANJaguar rightSlave;

> CANJaguar leftMaster;

> CANJaguar leftSlave;

> private DriveTrain(){

> rightMaster = new CANJaguar(RobotMap.DriveRightMaster);

> rightSlave = new CANJaguar(RobotMap.DriveRightSlave);

> leftMaster = new CANJaguar(RobotMap.DriveLeftMaster);

> leftSlave = new CANJaguar(RobotMap.DriveLeftSlave);

> }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


Netbeans is probably yelling at you for implementing the previous code. What your missing is a CANTimeoutException.


try{
> //Code Here
}
catch (CANTimeoutException ex) {
> ex.printStackTrace();
}


You may need to import CANTimeoutException. To do this click alt-enter on the underlined(CANTimeoutException) and select add Import.

Now that you have done that you must add the following to the constructor above:

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


leftMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

rightMaster.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

leftSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

rightSlave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

leftMaster.enableControl(0);

rightMaster.enableControl(0);

leftSlave.enableControl(0);

rightSlave.enableControl(0);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

This will just confirm that the jaguars are in "percent power" mode, which takes a value from 0-1 for the power. It will then enable all four jaguars.

That's it for the constructor.

Now we need to define methods in the subsystem to allow the command classes to interact with the DriveTrain.

Here is a basic method that will take a power value(0-1) and set the jaguars.

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

> public void setVBus(double power){
> > try {
> > > leftSlave.setX(-power);
> > > leftMaster.setX(-power);
> > > rightMaster.setX(power);
> > > rightSlave.setX(power);

> > } catch (CANTimeoutException ex) {
> > > ex.printStackTrace();

> > }

> }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Once you have something like that you can start defining the command to interact with the driveTrain.
Go on to Part 2.