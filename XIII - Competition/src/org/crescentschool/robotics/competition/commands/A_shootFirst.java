/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.constants.PotConstants;

/**
 *
 * @author Warfa
 */
public class A_shootFirst extends CommandGroup {

    public A_shootFirst() {
        System.out.println(this.toString());
        addSequential(new A_T_lock());
        addSequential(new A_S_shoot());
        addSequential(new A_D_distance(15));
        addParallel(new A_Fl_set(PotConstants.flipperBridgeAssist));
        addSequential(new A_D_distance(3));
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
