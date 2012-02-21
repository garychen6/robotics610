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
public class A_1 extends CommandGroup {

    public A_1() {
        System.out.println(this.toString());
        addSequential(new A_T_lock());
        addSequential(new A_S_shoot());
        addSequential(new A_D_distance(-18.5));
        addParallel(new A_Fl_set(90));
        addSequential(new A_Fl_set(20));
        addSequential(new A_I_timed(3));
        addSequential(new A_Fl_set(80));
        addSequential(new A_D_turn(13));
        addSequential(new A_D_distance(18.5));
        addSequential(new A_T_lock());
        //   addSequential(new A_S_shoot());


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
        // A_1 CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
