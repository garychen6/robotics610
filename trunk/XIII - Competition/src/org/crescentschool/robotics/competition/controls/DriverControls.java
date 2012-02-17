/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.controls;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.crescentschool.robotics.competition.Buttons;
import org.crescentschool.robotics.competition.OI;
import org.crescentschool.robotics.competition.commands.T_DG_Bridge;
import org.crescentschool.robotics.competition.commands.M_D_Bridge;
import org.crescentschool.robotics.competition.commands.T_Fl_Presets;
import org.crescentschool.robotics.competition.commands.M_D_Kaj;
import org.crescentschool.robotics.competition.commands.M_I_Pickup;
import org.crescentschool.robotics.competition.constants.InputConstants;
import org.crescentschool.robotics.competition.subsystems.Camera;
import org.crescentschool.robotics.competition.subsystems.DriveTrain;
import org.crescentschool.robotics.competition.subsystems.Feeder;
import org.crescentschool.robotics.competition.subsystems.Flipper;
import org.crescentschool.robotics.competition.subsystems.Intake;
import org.crescentschool.robotics.competition.subsystems.Shooter;
import org.crescentschool.robotics.competition.subsystems.Turret;

/**
 *
 * @author Warfa
 */
public class DriverControls extends Command {

    boolean kajMode = false;
    OI oi = OI.getInstance();
    Turret turret = Turret.getInstance();
    Shooter shooter = Shooter.getInstance();
    DriveTrain driveTrain = DriveTrain.getInstance();
    Flipper flipper = Flipper.getInstance();
    Intake intake = Intake.getInstance();
    Feeder feeder = Feeder.getInstance();
    Camera camera = Camera.getInstance();
    boolean M_I_Pickup = true;
    public DriverControls() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Scheduler.getInstance().add(new T_Fl_Presets());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!kajMode) {
            if (Math.abs(oi.getDriver().getRawAxis(InputConstants.kLeftYAxis)) > 0.2) {
                Scheduler.getInstance().add(new M_D_Kaj());
                kajMode = true;
            } else if (Math.abs(oi.getDriver().getRawAxis(InputConstants.kRightXAxis)) > 0.2) {
                Scheduler.getInstance().add(new M_D_Kaj());
                kajMode = true;
            }
        } else {
            if (oi.getDriver().getRawAxis(6) > 0.5) {
                Scheduler.getInstance().add(new M_D_Bridge());
                kajMode = false;
            } else if (oi.getDriver().getRawAxis(5) < -0.5) {
                Scheduler.getInstance().add(new M_D_Bridge());
                kajMode = false;
            }
            if (oi.getDriver().getRawAxis(6) < -0.5) {
                Scheduler.getInstance().add(new M_D_Bridge());
                kajMode = false;
            } else if (oi.getDriver().getRawAxis(5) > 0.5) {
                Scheduler.getInstance().add(new M_D_Bridge());
                kajMode = false;
            }
        }
        if (Buttons.isPressed(InputConstants.kXButton, oi.getDriver())) {
            Scheduler.getInstance().add(new T_DG_Bridge());
            kajMode = false;
        }
//         if (Buttons.isPressed(InputConstants.kL1Button, oi.getDriver())) {
//            if(! M_I_Pickup){
//                Scheduler.getInstance().add(new M_I_Pickup());
//                  M_I_Pickup = true;
//            }
//           
//        }
//          if (Buttons.isPressed(InputConstants.kR1Button, oi.getDriver())) {
//             if(! M_I_Pickup){
//                 Scheduler.getInstance().add(new M_I_Pickup());
//                  M_I_Pickup = true;
//             }
//        }
//           if (Buttons.isPressed(InputConstants.kR2Button, OI.getInstance().getOperator())) {
//               if(M_I_Pickup) M_I_Pickup = false;
//           }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println(this + " canceled");cancel();
    }
}
