/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.constants.InputConstants;
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
    double xOffset = 0;
    boolean dPadR = false;
    boolean dPadL = false;

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
        if (Buttons.isPressed(InputConstants.kL2Button, oi.getOperator())) {
            turret.resetPosition();
            camera.setLight(true);
        }
        if (Buttons.isHeld(InputConstants.kL2Button, oi.getOperator())) {
            if (turret.isLocked()) {
                ultrasonic.setUSonic(true);
            }
        }
        if (Buttons.isReleased(InputConstants.kL2Button, oi.getOperator())) {
            camera.setLight(false);
            ultrasonic.setUSonic(false);

        }

        if (Buttons.isHeld(InputConstants.kL2Button, oi.getOperator()) && !Buttons.isHeld(InputConstants.kR2Button, oi.getOperator())) {
            double offset = -0.65 * camera.getX();
            if (offset > 0.05) {
                offset = 0.05;
            }
            if (offset < -0.05) {
                offset = -0.05;
            }
            turret.incPosition(offset);
        } else if (!Buttons.isHeld(InputConstants.kR2Button, oi.getOperator())) {
            double axis = oi.getOperator().getRawAxis(InputConstants.kRightXAxis);
            if (axis < -0.1) {
                turret.setVBus(axis * 0.85 / 0.9 - 0.05 / 0.9);
            } else if (axis > 0.1) {
                turret.setVBus(axis * 0.85 / 0.9 + 0.05 / 0.9);
            } else {
                turret.setVBus(0);
            }

        }

        SmartDashboard.putString("offsets", "x: " + xOffset);
        if (OI.getInstance().getOperator().getRawAxis(5) > 0.5 && !dPadR) {
            xOffset -= 0.05;
            turret.xOffset(xOffset);
            camera.setTurretOffset(xOffset);
            dPadR = true;
        } else if (OI.getInstance().getOperator().getRawAxis(5) < -0.5 && !dPadL) {
            xOffset += 0.05;
            turret.xOffset(xOffset);
            camera.setTurretOffset(xOffset);
            dPadL = true;
        } else if (Math.abs(OI.getInstance().getOperator().getRawAxis(5)) < 0.5) {
            dPadL = false;
            dPadR = false;
            turret.xOffset(xOffset);
            camera.setTurretOffset(xOffset);
        }
        if (Buttons.isPressed(InputConstants.kSelectButton, OI.getInstance().getOperator())) {
            xOffset = 0;
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
        System.out.println(this + " canceled");cancel();
    }
}
