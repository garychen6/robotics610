/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.crescentschool.robotics.competition.subsystems.Pneumatics;
/**
 *
 * @author Ian
 */
public class SevenDisc extends CommandGroup {
//
//    Preferences preferences;
//    Pneumatics pneumatics;
//    public SevenDisc() {
//         pneumatics = Pneumatics.getInstance();
//        //addSequential(new LargeAngleTurn(180));
//
//        //Shoot 3
//
//        /*
//         addParallel(new PickUp(true, 0, false));
//         */
//        addSequential(new Shoot(1, false));
//        addSequential(new LargeAngleTurn(180));
//        addSequential(new PickUp(true, 0, false));
//        addSequential(new PositionControl(true, -5, true, -5));
//        //Keep Driving half the midD... While moving the intake up
//        addSequential(new PickUp(true, 1, false));
//        addSequential(new PickUp(true, 1, true));
//        pneumatics.setAngleUp(true);
//        addSequential(new PickUp(true, 0, false));
//        
//        addSequential(new PositionControl(true, -13, true, -13));
//        pneumatics.setAngleUp(false);
//        System.out.println("Stage 3 Complete.");
//        
//        //Drive back to the middle of the pyramid while moving the arm up
//        addParallel(new PickUp(true, 1, false));
//        addSequential(new PositionControl(true, 13, true, 13));
//        //Feed and drive the rest of the way.
//       
//        addParallel(new PickUp(true, 1, true));
//         pneumatics.setAngleUp(true);
//        addSequential(new PositionControl(true, 5, true, 5));
//        addSequential(new LargeAngleTurn(0));
//
//        //Shoot our 9 discs :D
//        addSequential(new Shoot(5, true));
//        System.out.println("If you see this then you have no idea how happy I am.");
//        /*
//         * //Corner
//         addSequential(new PositionControl(true, -3, true, -3));
//
//         addSequential(new AngleTurn(30));
//         addSequential(new PositionControl(true, -7, true, -7));
//         */
//
//
//
//        /*
//
//         //180 turn
//         addParallel(new BunnyEars(false));
//         addParallel(new PickUp(true, 0, false));
//         addSequential(new AngleTurn(180));
//         //Drive to the middle of the pyramid
//         addParallel(new PickUp(true, 0, false));
//         addSequential(new PositionControl(true, midD, true, midD));
//         //Keep Driving half the midD... While moving the intake up
//         addParallel(new PickUp(true, 1, false));
//         addSequential(new PositionControl(true, midD / 2.0, true, midD / 2.0));
//         //Drive the other half and feed discs
//         addParallel(new PickUp(true, 1, true));
//         addSequential(new PositionControl(true, midD / 2.0, true, midD / 2.0));
//         System.out.println("Stage 3 Complete.");
//
//
//         //Drive to the front of the pyramid where the discs are
//         addParallel(new PickUp(true, 0, false));
//         addSequential(new PositionControl(true, frontD, true, frontD));
//         //Drive back to the middle of the pyramid while moving the arm up
//         addParallel(new PickUp(true, 1, false));
//         addSequential(new PositionControl(true, -frontD, true, -frontD));
//         //Feed and drive the rest of the way.
//         addParallel(new PickUp(true, 1, true));
//         addParallel(new BunnyEars(true));
//         addSequential(new PositionControl(true, -midD, true, -midD));
//         //Shoot our 9 discs :D
//         addSequential(new Shoot(4));
//         System.out.println("If you see this then you have no idea how happy I am.");
//
//         */
//
//


  //  }
}
