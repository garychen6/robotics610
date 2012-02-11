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
public class Autonomous extends CommandGroup {

    public Autonomous() {
        addSequential(new ManualShooter());
        addSequential(new AutoTurn(180));
        addSequential(new DriveDistance(5.03990653700226));
        addParallel(new setFlipper(45));
        addSequential(new setFlipper(10));
        addSequential(new TimedBallIntake(2));
        addSequential(new setFlipper(65));
        addSequential(new AutoTurn(180));
        addSequential(new DriveDistance(5.03990653700226));
        addSequential(new ManualShooter());
        
        //addParallel(new setFlipper(44.9));
        

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
