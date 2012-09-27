/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.constants.PIDConstants;
import org.crescentschool.robotics.competition.constants.PotConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.CoyoBotUltrasonic;
import org.crescentschool.robotics.competition.subsystems.Turret;

/**
 *
 * @author Warfa
 */
public class AMT_T_turn extends Command {

    Turret turret = Turret.getInstance();
    OI oi = OI.getInstance();
    Camera camera = Camera.getInstance();
    CoyoBotUltrasonic ultrasonic = CoyoBotUltrasonic.getInstance();
    double tPos = 0;
    double n = 2;
    double tVbus = 0;
    boolean dPadR = false;
    boolean dPadL = false;
    boolean isLocked = false;

    public AMT_T_turn() {
        System.out.println(this.toString());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // OI.printToDS(0, "Turret SetPoint: " + turret.getPosSet());
        // OI.printToDS(1, "Turret Position: " + turret.getPos());

//        if (Buttons.isPressed(InputConstants.kYButton, oi.getOperator())) {
//            turret.incTurretP(1);
//        } else if (Buttons.isPressed(InputConstants.kXButton, oi.getOperator())) {
//            turret.decTurretP(1);
//        } else if (Buttons.isPressed(InputConstants.kBButton, oi.getOperator())) {
//            turret.incTurretI(0.001);
//        } else if (Buttons.isPressed(InputConstants.kAButton, oi.getOperator())) {
//            turret.decTurretI(0.001);
//        }

        //System.out.println("Turret Set: " + turret.getPosSet() + " Pos: " + turret.getPos());

        // Throw away any old images when we first track
        if (Buttons.isPressed(InputConstants.kL2Button, 2)) {
            camera.newImage();
        }

        // Track with camera
        if (Buttons.isHeld(InputConstants.kL2Button, oi.getOperator()) && !Buttons.isHeld(InputConstants.kR2Button, oi.getOperator())) {
            if (camera.newImage()) {
                
                //TODO: Do nice tangent math here
                double offset = 1.129363476 * MathUtils.atan(camera.getX() * 0.434812375);
                System.out.println("Pot Diff: " + offset);
                if (offset != 0.0) {
                    turret.setPosition(camera.getTurretPot() - offset);
                    isLocked = true;
                }
            } 
        } else if (!Buttons.isHeld(InputConstants.kL2Button, oi.getOperator()) && !Buttons.isHeld(InputConstants.kR2Button, oi.getOperator())) {
            double axis = oi.getOperator().getRawAxis(InputConstants.kRightXAxis);
            if (axis < -0.1) {
                turret.setVBus(MathUtils.pow(axis + 0.1, n) * (-0.85 / MathUtils.pow(0.9, n)) - 0.15);
                isLocked = false;
            } else if (axis > 0.1) {
                turret.setVBus(MathUtils.pow(axis - 0.1, n) * (0.85 / MathUtils.pow(0.9, n)) + 0.15);
                isLocked = false;
            } else if (!isLocked) {
                turret.resetPosition();
                turret.incPosition(0);
                isLocked = true;
            }
        }

        if (OI.getInstance().getOperator().getRawAxis(5) > 0.5) {

            turret.incPosition(-0.005);

        } else if (OI.getInstance().getOperator().getRawAxis(5) < -0.5) {

            turret.incPosition(0.005);
            
        }
        if (Buttons.isHeld(InputConstants.kYButton, OI.getInstance().getOperator())) {
              turret.setPosition(PotConstants.turretCentre);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println(this + " canceled");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println(this + " canceled");
        cancel();
    }
}
