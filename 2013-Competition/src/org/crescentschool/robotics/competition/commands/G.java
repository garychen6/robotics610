/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crescentschool.robotics.competition.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Ian
 */
public class G extends CommandGroup {

    public G() {
        /*
         preferences = Preferences.getInstance();
         double centreD = preferences.getDouble("centreD", 0);
         double midD = preferences.getDouble("midD", 0);
         double frontD = preferences.getDouble("frontD", 0);
         if (centreD == 0) {
         System.out.println("centreD is 0!");
         }
         if (midD == 0) {
         System.out.println("midD is 0!");
         }
         if (frontD == 0) {
         System.out.println("frontD is 0!");
         }
         */
        //addSequential(new LargeAngleTurn(180));

        //Shoot 3


        // addParallel(new PickUp(true, 0, false));

        addSequential(new Shoot(4, false));
        addSequential(new PositionControl(true, -7, true, -7));
        addSequential(new LargeAngleTurn(73.5));
        addSequential(new PositionControl(true, -11, true, -11));

        /*
         System.out.println("Stage 1 Complete.");

         //Drive Back to centre of the field
         addParallel(new PickUp(true, 0, false));

         addSequential(new PositionControl(true, -14.5, true, -14.5));
         //Go halfway to the pyramid while moving the intake up
         addParallel(new PickUp(true, 1, false));
         addSequential(new PositionControl(true, 14.5 / 2.0, true, 14.5 / 2.0));
         //Feed discs while moving the other half of the way
         addParallel(new PickUp(true, 1, true));
         addSequential(new PositionControl(true, 15.5 / 2.0, true, 15.5 / 2.0));

         //Shoot 2 while moving the intake down
         //addParallel(new PickUp(true, 0, false));
         addSequential(new Shoot(7, true));
         System.out.println("Stage 2 Complete.");
         */

        /*
         * //Corner
         addSequential(new PositionControl(true, -3, true, -3));

         addSequential(new AngleTurn(30));
         addSequential(new PositionControl(true, -7, true, -7));
         */


        /*

         //180 turn
         addParallel(new BunnyEars(false));
         addParallel(new PickUp(true, 0, false));
         addSequential(new AngleTurn(180));
         //Drive to the middle of the pyramid
         addParallel(new PickUp(true, 0, false));
         addSequential(new PositionControl(true, midD, true, midD));
         //Keep Driving half the midD... While moving the intake up
         addParallel(new PickUp(true, 1, false));
         addSequential(new PositionControl(true, midD / 2.0, true, midD / 2.0));
         //Drive the other half and feed discs
         addParallel(new PickUp(true, 1, true));
         addSequential(new PositionControl(true, midD / 2.0, true, midD / 2.0));
         System.out.println("Stage 3 Complete.");


         //Drive to the front of the pyramid where the discs are
         addParallel(new PickUp(true, 0, false));
         addSequential(new PositionControl(true, frontD, true, frontD));
         //Drive back to the middle of the pyramid while moving the arm up
         addParallel(new PickUp(true, 1, false));
         addSequential(new PositionControl(true, -frontD, true, -frontD));
         //Feed and drive the rest of the way.
         addParallel(new PickUp(true, 1, true));
         addParallel(new BunnyEars(true));
         addSequential(new PositionControl(true, -midD, true, -midD));
         //Shoot our 9 discs :D
         addSequential(new Shoot(4));
         System.out.println("If you see this then you have no idea how happy I am.");

         */




    }
}
