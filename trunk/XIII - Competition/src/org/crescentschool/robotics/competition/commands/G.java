
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.PotConstants;

/**
 *
 * @author Ian
 */
public class G extends CommandGroup {

    public G() {
        addParallel(new A_Fl_set(PotConstants.flipperBallPickup));
        addParallel(new A_I_timed(2));
        addSequential(new A_D_distance(-200));
        addParallel(new A_Fl_set(PotConstants.flipperRetract));
        //addSequential(new A_D_distance(-200));
        addSequential(new A_S_shoot(4));
        addParallel(new A_Fl_set(PotConstants.flipperBallPickup));
        addParallel(new A_I_timed(5));
        addSequential(new A_D_distance(-500));
        addParallel(new A_Fl_set(PotConstants.flipperRetract));
        addSequential(new A_D_distance(500));
        addSequential(new A_S_shoot(4));

        //addSequential(new A_S_shoot(2));
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
