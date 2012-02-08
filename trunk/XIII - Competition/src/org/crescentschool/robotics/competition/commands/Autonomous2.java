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
public class Autonomous2 extends CommandGroup {
    
    public Autonomous2() {
        addSequential(new Shoot());
        addSequential(new DriveDistance(10.98));
        addSequential(new AutoTurn(90));
        addParallel(new setFlipper(45));
        addSequential(new DriveDistance(5.03990653700226));
        addSequential(new setFlipper(10));
        addSequential(new TimedBallIntake(2));
        addSequential(new setFlipper(45));
        addSequential(new AutoTurn(150));
        addSequential(new Shoot());
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
