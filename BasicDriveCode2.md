# Basic Drive Code Part 2 #

This is assuming you have done all of part 1 correctly.


# Defining the Move Command #
When you got to the command package, right-click example command and select refactor -> rename. Then rename it to "Move".
Delete Example Command Base.
Remove any mention of CommandBase from the project.

Your "Move" command should then look something like this (excluding comments).

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;


public class Move extends Command {

> public Move() {


> }

> protected void initialize() {

> }


> protected void execute() {
> }


> protected boolean isFinished() {
> > return false;

> }


> protected void end() {
> }

> protected void interrupted() {
> }
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

First thing you need to do is create an instance of DriveTrain.