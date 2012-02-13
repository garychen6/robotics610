/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Warfa
 */
public class a_1 extends CommandGroup {

    public a_1() {
        addSequential(new aT_lock());
        addSequential(new aS_shoot());
        addSequential(new aD_distance(5.03990653700226));
        addParallel(new aFl_set(45));
        addSequential(new aFl_set(10));
        addSequential(new aI_timed(2));
        addSequential(new aFl_set(65));
        addSequential(new aD_turn(180));
        addSequential(new aD_distance(5.03990653700226));
        addSequential(new aT_lock());
        addSequential(new aS_shoot());


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
        // a_1 CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
