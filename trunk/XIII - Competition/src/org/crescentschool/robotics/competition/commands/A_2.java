/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Robotics
 */
public class A_2 extends CommandGroup {
    
    public A_2() {
        System.out.println(this.toString());
        addSequential(new M_S_ShootBall());
        addSequential(new A_D_distance(10.98));
        addSequential(new A_D_turn(90));
        addParallel(new A_Fl_set(45));
        addSequential(new A_D_distance(5.03990653700226));
        addSequential(new A_Fl_set(10));
        addSequential(new A_I_timed(2));
        addSequential(new A_Fl_set(45));
        addSequential(new A_D_turn(150));
        addSequential(new M_S_ShootBall());
        //:)
        
        
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
