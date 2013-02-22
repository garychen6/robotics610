package edu.wpi.first.wpilibj.templates;
// Jordan Grant
// Mr. Lim
// FRC Programming Code 2013 - Edgewalker

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class FRC_Programming_Grant_Jordan extends IterativeRobot {
    
    /*/
     * 
     *  7 FEET, 4 INCHES AWAY AT 70% SPEED

    1 - left button
    2 - bottom
    3 - right
    4 - top
`
    5- LB
    7 - LT

    6- RB
    8 -RT

    9 select
    10 start
    */
    
    Joystick driverJoystick = new Joystick(1);
    Joystick operatorJoystick = new Joystick(2);
    
    CANJaguar frontLeftMotor;
    CANJaguar rearLeftMotor;
    CANJaguar frontRightMotor;
    CANJaguar rearRightMotor;
    CANJaguar turret;
    CANJaguar flywheel;
    Victor feeder;
    Victor feeder2;
    Victor intake;
    double shooterSpeed = 0;
    
    public void robotInit() {
        try {
            frontLeftMotor = new CANJaguar(3);
            rearLeftMotor = new CANJaguar(2);
            frontRightMotor = new CANJaguar(5);
            rearRightMotor = new CANJaguar(4); 
            flywheel = new CANJaguar(8);
            feeder = new Victor(1);
            feeder2 = new Victor(2);
            intake = new Victor(3);
            turret = new CANJaguar(9);
            turret.configPotentiometerTurns(10);
            turret.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            flywheel.configEncoderCodesPerRev(256);
            
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            System.out.println("Problemo");
        }
    }

    public void autonomousPeriodic() {
        
    }
    
    public void teleopPeriodic() {     
            
    try {  
            double leftSpeed;
            double rightSpeed;
            
        
           // INTAKE CODE AND FEEDER CODE - LB to suck in, LT to spit out //
            if (driverJoystick.getRawButton(5) == true){
            intake.set(-0.8);
            feeder.set(0.8);
            feeder2.set(0.8);
            }
            else if (driverJoystick.getRawButton(7) == true){
            intake.set(0.8);
            feeder.set(-0.8);
            feeder2.set(-0.8); 
            }
            else {
            intake.set(0);
            feeder.set(0);
            feeder2.set(0);
            }
            
           // TURRET CODE - left on left joystick to turn left, right to turn right
           // Limits on the potentiometer. 45 degrees to either side.
            if (turret.getPosition() > 7) {
            turret.setX(Math.max(0, driverJoystick.getRawAxis(1)));
            }
            else if (turret.getPosition() < 3) {
            turret.setX(Math.min(0, driverJoystick.getRawAxis(1)));
            }
            else
            {
            turret.setX(driverJoystick.getRawAxis(1));
            }
            
           // SHOOTER CODE - Click right joystick to run shooter at preset speed, click left to reduce speed to zero //
            if (driverJoystick.getRawButton(12) == true){
            shooterSpeed = 0.7;
            flywheel.setX(shooterSpeed);
            }
            else if (driverJoystick.getRawButton(11) == true){
            flywheel.setX(0);
            } 
            
              // SHOOTER CODE - RB to increment speed of shooter, RT to decrement//
            if (driverJoystick.getRawButton(6) == true){
            shooterSpeed += 0.05;
            flywheel.setX(shooterSpeed);
            }
            if (driverJoystick.getRawButton(8) == true){
            shooterSpeed -= 0.05;
            flywheel.setX(shooterSpeed);
            }
            System.out.println(shooterSpeed);
            
            
           // DRIVETRAIN CODE - Left Joystick forward/back, right joystick left/right //
            leftSpeed = (driverJoystick.getRawAxis(2) - driverJoystick.getRawAxis(3));
            rightSpeed = driverJoystick.getRawAxis(2) + driverJoystick.getRawAxis(3);
            
            leftSpeed = leftSpeed * leftSpeed * leftSpeed;
            rightSpeed = rightSpeed * rightSpeed * rightSpeed;

            // Kaj Drive for the four CIMs on 610
            frontLeftMotor.setX(leftSpeed);
            rearLeftMotor.setX(leftSpeed);
            frontRightMotor.setX(rightSpeed);
            rearRightMotor.setX(rightSpeed);
            
           
            
            
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
    }
     
    public void testPeriodic() {
    
    }
    
}
