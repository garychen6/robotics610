/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author matthewtory
 */
public class RobotControl {
    
    Talon frontLeft, midLeft, rearLeft;
    Talon frontRight, midRight, rearRight;
    Encoder leftEnc, rightEnc;
    Gyro driveGyro;
    
    private RobotMain parent;
    private Timer controlTime;
    
    private int ticks = 0;
    
    public RobotControl(RobotMain parent){
        this.parent = parent;
        
        controlTime = new Timer();
        
        frontLeft = new Talon(6);
        midLeft = new Talon(5);
        rearLeft = new Talon(4);
        frontRight = new Talon(3);
        midRight = new Talon(2);
        rearRight = new Talon(1);
        
        leftEnc = new Encoder(2,1);
        rightEnc = new Encoder(4,3);
        
        driveGyro = new Gyro(1);
        driveGyro.setSensitivity(Constants.GYRO_SENSITIVITY);
    }
    
    public boolean setTurnLeft(int degrees){
        double cap = 0.35;
        double sM = 0.03;
        
        int halfDegrees = degrees/2;
        degrees += 2;
        
        double speed=0.1;
        double sG = driveGyro.getAngle();
        double angle = sG+0.1;
        
        while(driveGyro.getAngle()<sG+halfDegrees){
            angle = driveGyro.getAngle();
            
            System.out.println("Speed:"+speed+"Gyro:"+driveGyro.getAngle()+"sG"+sG);
            if(angle <= sG){
              angle = sG+2;  
            }
            if(speed<cap){
                speed = (angle-sG)* sM;
            }
            if(speed<0.2){
                speed = 0.2;
            }
            setLeft(speed);
            setRight(-speed);
        }
        while(angle<sG+degrees){
            angle = driveGyro.getAngle();
            speed = (degrees-(angle-sG))*sM;
            if (speed > cap){
                speed = cap;
            }
            System.out.println("Speed:"+speed+"Gyro:"+driveGyro.getAngle()+"sG"+sG+"------------------");
            setLeft(speed);
            setRight(-speed);
        }
        setStop(false);
        return false;
    }
    public void setTurnLeft2(int degrees){
        
        double minSpeed = 0.1;
        double maxSpeed = 0.4;
        double gap = maxSpeed-minSpeed;
        double sM = 0.05;
        
        int halfDegrees = degrees/2;
        
        double speed=0.1;
        double sG = driveGyro.getAngle();
        double angle = sG;
        
        while(angle<sG+degrees){
            angle = driveGyro.getAngle();
            
            if(angle <= sG){
              angle = sG+5;  
            }
            
            if(speed<maxSpeed){
                if(angle<sG+halfDegrees){
                    //0.1+((0-0)/(45))*(0.4-0.1)
                    speed = minSpeed+((angle-sG)/(halfDegrees)*(maxSpeed-minSpeed));
                }else{
                    speed = maxSpeed-((angle-sG)/(degrees)*(maxSpeed-minSpeed));
                }
            }
            if(speed<minSpeed){
                speed = minSpeed;
            }
            if(speed>maxSpeed){
                speed = maxSpeed;
            }
            setLeft(speed);
            setRight(-speed);
        }
        setStop(false);
    }
    public void setTurnRight2(int degrees){
        double minSpeed = 0.1;
        double maxSpeed = 0.4;
         
        int halfDegrees = degrees/2;
        double speed = 0.1;
        double sG = driveGyro.getAngle( );
        double angle = sG;
        
        
        while(angle>sG-degrees){
            angle = driveGyro.getAngle();
            if(angle>=sG){
                angle = sG-2;
            }
            
            if(speed<maxSpeed){
                if(angle>sG-halfDegrees){
                    speed = minSpeed-((angle-sG)/(halfDegrees)*(maxSpeed-minSpeed));
                }else{
                    
                    speed = maxSpeed+((angle-sG)/(halfDegrees))*(maxSpeed-minSpeed);
                }
            }
            if(speed<minSpeed){
                speed = minSpeed;
            }
            setLeft(speed);
            setRight(-speed);
        }
        setStop(false);
    }
    
    public void setTurnRight(int degrees){
        double sG = driveGyro.getAngle();
        while(driveGyro.getAngle()<sG+degrees){
            setLeft(-Constants.LOW_SPEED);
            setRight(Constants.LOW_SPEED);
        }
    }
    
    public void setStop(boolean brake){
        double lastTime = getTimeMSec();
        
        if(brake){
            //int mod = leftEnc.getDirection() ? 1 : -1;
            int mod = -1;
            
            while(lastTime<getTimeMSec()+Constants.BRAKE_TIME_MSEC){
                setLeft(Constants.MID_SPEED*mod);
                setRight(Constants.MID_SPEED*mod);
            }
        }
        setLeft(0);
        setRight(0);
    }
    
    public void setForward(double speed){
        setLeft(speed);
        setRight(speed);
    }
    
    public void setForward2(int inches, double speed){
        resetEncoders();
        
        double minSpeed = 0.2;
        double maxSpeed = 0.5;
        int halfInches = inches/2;
        
        while(getEncoders()<inches*Constants.TICK_PER_INCH){
            if(speed<maxSpeed){
                if(getEncoders()<halfInches*Constants.TICK_PER_INCH){
                    speed = (getEncoders()/(halfInches*Constants.TICK_PER_INCH))*maxSpeed;
                }else{
                    speed = 1-(getEncoders()/(inches*Constants.TICK_PER_INCH))*maxSpeed;
                }
            }
        }
    }
     
    public void setForward(int inches, double speed){
        resetEncoders();
        
        //Max Encoder Ticks = Wheel Circumference x Ticks Per Inch
        //360 = 18*20
        while(getEncoders() < inches*Constants.TICK_PER_INCH){
            setForward(speed);
        }
        setStop(false);
    }
     
    public void setForward(int inches, double speed,Gyro driveGyro,int lastEnc, boolean correction){
        
        double lSpeed = speed;
        double rSpeed = speed;
        
        int maxDif = 10;
        
        driveGyro.reset();
        double startAngle = driveGyro.getAngle();
        while(getEncoders()-lastEnc<inches*Constants.INCH_PER_TICK){
            setLeft(lSpeed);
            setRight(rSpeed);
            if(correction){
                double angle = driveGyro.getAngle();
                if(angle>startAngle+maxDif){
                    lSpeed--;
                }else if(angle<startAngle-maxDif){
                    rSpeed--;
                }
            }
        }
        setStop(false);
    }
    
    
    public void setForward(int inches, double speed, boolean correction){
        resetEncoders();
        
        double lSpeed = speed;
        double rSpeed = speed;
        
        int mD = 50;
        
        while(getEncoders()<inches*Constants.TICK_PER_INCH){
            int lD = leftEnc.getRaw()-rightEnc.getRaw();
            int rD = rightEnc.getRaw()-leftEnc.getRaw();
            
            setLeft(lSpeed);
            setRight(rSpeed);
            
            if(correction){
                if(lD>mD){
                    lSpeed--;
                }else{
                    lSpeed = speed;
                }
                if(rD>mD){
                    rSpeed--;
                }else{
                    rSpeed = speed;
                }
            }
        }
        setStop(false);
    }
    
     public void setLeft(double speed){
        frontLeft.set(speed);
        midLeft.set(speed);
        rearLeft.set(speed);
    }
    public void setRight(double speed){
        frontRight.set(speed);
        midRight.set(speed);
        rearRight.set(speed);
    }
    
    public void printSensors(){
        
        if(ticks >60){
            System.out.println("Left: "+leftEnc.getRaw()+
                ", Right: "+rightEnc.getRaw()+
                ", Gyro: "+driveGyro.getAngle()+", Stick"+parent.getJoystick().getRawAxis(2));
            System.out.println(frontLeft.get());
            ticks = 0;
        }
        
        ticks++;
    }
    
    public void resetEncoders(){
        leftEnc.reset();
        rightEnc.reset();
    }
    
    public void start(){
        leftEnc.start();
        rightEnc.start();
        driveGyro.reset();
        
        controlTime.start();
    }
    public void stop(){
        controlTime.stop();
        resetEncoders();
    }
    
    public int getEncoders(){
        return (Math.abs(leftEnc.getRaw())+Math.abs(rightEnc.getRaw()))/2;
    }
    public double getTime(){
        return controlTime.get();
    }
    public double getTimeMSec(){
        return getTime()/1000;
    }
    public double getTimeSec(){
        return getTime()/100000;
    }
    public double getPercentage(double num, double denom){
        return getPercentageDecimal(num,denom)*100;
    }
    public double getPercentageDecimal(double num, double denom){
        return (num/denom);
    }
}
