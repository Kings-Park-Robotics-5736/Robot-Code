
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Solenoid;


public class Robot extends TimedRobot implements PIDOutput {
  //Create Objects
  private Timer time = new Timer();
  private DigitalInput limitswitch = new DigitalInput(9);
  private WPI_TalonSRX rightmaster = new WPI_TalonSRX(3);
  private WPI_TalonSRX leftmaster = new WPI_TalonSRX(5);
  private WPI_VictorSPX leftslave = new WPI_VictorSPX(2);
  private WPI_VictorSPX rightslave = new WPI_VictorSPX(4);
  private XboxController xboxelevator = new XboxController(1);
  private XboxController xboxdrive = new XboxController(0);
  private DifferentialDrive drive = new DifferentialDrive(leftmaster, rightmaster);
  private TalonSRX elevator =  new TalonSRX(1); 
  private Solenoid gripper = new Solenoid(4);
  private Solenoid cargopush = new Solenoid(5);
  private Solenoid pcm = new Solenoid(1, 0);
  private Solenoid hatch = new Solenoid(6);
  //private DoubleSolenoid climb = new DoubleSolenoid(2,3);
  //private DoubleSolenoid climb2 = new DoubleSolenoid(0,1);
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-lime");
  UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
//UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
 

//Toggle Variables   
boolean lockout;
boolean determine;
boolean toggle = false;
boolean state = true; 
boolean toggle1 = false;
boolean state1 = true;



void hatchpush( double position,double position2, double seconds)
{
  time.reset();
  while(time.get() < seconds)
  {
  elevator.set(ControlMode.Position, position);
  time.delay(.5);
  hatch.set(true);
  }
  hatch.set(false);
  elevator.set(ControlMode.Position, position2);
}

@Override
	public void pidWrite(double output)
	{
    rightmaster.set(ControlMode.PercentOutput, output);
    leftmaster.set(ControlMode.PercentOutput, output);
	}
  
  
  @Override
  public void robotInit() {
    //Configuration Settings for Can Use
  //CameraServer.getInstance().startAutomaticCapture();
    elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    elevator.setSensorPhase(false);
    elevator.configNominalOutputForward(0, 30);
    elevator.configNominalOutputReverse(0, 30);
    elevator.configPeakOutputForward(.6, 30);
    elevator.configPeakOutputReverse(-.4, 30);
    elevator.config_kP(0, 1);   //**** NEED TUNING*****
    elevator.configAllowableClosedloopError(0, 5, 10);
    leftmaster.setInverted(false);
    rightmaster.setNeutralMode(NeutralMode.Brake);
    leftmaster.setNeutralMode(NeutralMode.Brake);
    elevator.setNeutralMode(NeutralMode.Brake);
    rightslave.follow(leftmaster);
    leftslave.follow(rightmaster);
    elevator.configForwardSoftLimitThreshold(31500);
    elevator.configForwardSoftLimitEnable(true);
    elevator.configReverseSoftLimitThreshold(1500);
    elevator.configReverseSoftLimitEnable(true);
  }

 
  @Override
  public void robotPeriodic() {
    SmartDashboard.putString("DB/String 0"," " + elevator.getSelectedSensorPosition());
    System.out.println(elevator.getSensorCollection().getQuadraturePosition());
   
  
  
 
  
  
 

 //Elevator Command Control
 
 if(xboxelevator.getStickButton(Hand.kRight))
 {
   elevator.setSelectedSensorPosition(0);
   elevator.getSensorCollection().setQuadraturePosition(0, 10);
   elevator.set(ControlMode.PercentOutput, 0);
 }
 
 //LimitSwitch Control
 /*
 if(!limitswitch.get())
 {
   elevator.setSelectedSensorPosition(0);
   elevator.getSensorCollection().setQuadraturePosition(0, 10);
   if(-xboxelevator.getY(Hand.kLeft) < 0)
   {
     elevator.set(ControlMode.PercentOutput,0);
   }
   else
   { 
     elevator.set(ControlMode.PercentOutput, -xboxelevator.getY(Hand.kLeft));

   }
 }
 */
 //Elevator Heights
  if(xboxelevator.getAButton())
  { 
    elevator.set(ControlMode.Position, 12000);
  }  
  else if(xboxelevator.getBButton())
  {
   elevator.set(ControlMode.Position, 13000);
  }
  else if(xboxelevator.getYButton())
  { 
    elevator.set(ControlMode.Position, 25000);
  }  
  else if(xboxelevator.getXButton())
  {
   elevator.set(ControlMode.Position, 31000);
  }
  /*
  else if(xboxelevator.getBumper(Hand.kLeft))
  { 
    elevator.set(ControlMode.Position, 16000);
  }  
  else if(xboxelevator.getTriggerAxis(Hand.kLeft) > 0)
  {
   elevator.set(ControlMode.Position, 18000);
  }
  else if(xboxelevator.getTriggerAxis(Hand.kRight) >0)
  {
   elevator.set(ControlMode.Position, 20000);
  }
*/

  //Manual control Elevator
  else if(!xboxelevator.getAButton() && !xboxelevator.getBButton() && !xboxelevator.getYButton() && !xboxelevator.getXButton() && limitswitch.get() && !xboxelevator.getBumperPressed(Hand.kLeft) && xboxelevator.getTriggerAxis(Hand.kLeft) == 0 && xboxelevator.getTriggerAxis(Hand.kRight) == 0 )
  { 
     elevator.set(ControlMode.PercentOutput, -xboxelevator.getY(Hand.kLeft));
  }
  
  
//*** Drive command control*****
   drive.arcadeDrive((xboxdrive.getTriggerAxis(Hand.kLeft) * .75) - (xboxdrive.getTriggerAxis(Hand.kRight) * .75), xboxdrive.getX(Hand.kLeft));  

   //Solenoid stuff || Control
   //Gripper ****DONE*** BUT NEEDS TUNING
   
   if (toggle && xboxdrive.getYButton()) {  // Only execute once per Button push
    toggle = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
    if (state) 
    {  // Decide which way to set the motor this time through (or use this as a motor value instead)
      state= false;
      determine = true;
      gripper.set(true);
    } 
    else {
      state= true; 
      determine = false;
      gripper.set(false);
    }
  } else if(xboxdrive.getYButton() == false) { 
      toggle = true; // Button has been released, so this allows a re-press to activate the code above.
  }
  
  //PUNCh LOCKOUT **DONE** USE AS BACKUP
  /*
  if(xboxdrive.getXButton() && determine == true)
  {
    time.reset();
    time.delay(.25);
    cargopush.set(true);
    time.reset();
    time.delay(.5);
    cargopush.set(false);
    time.delay(.25);
  }
*/
//BETTER VERSION OF THAT^^ NEEDS TESTING
    if (xboxdrive.getXButton() && determine == false) {
      time.reset();
    gripper.set(true);
    time.delay(.25);
    cargopush.set(true);
    time.reset();
    time.delay(.5);
    cargopush.set(false);
    time.reset();
    time.delay(.25);
    gripper.set(false);
  
    }

    if(xboxdrive.getAButton())
    {
      hatchpush(13000, 20000, 4);
    }

  
/*
//Climb with safety net ****NEEDS TUNING****
if(xboxdrive.getStartButton() && xboxelevator.getStartButton())
 {
   lockout = true;
   climb.set(Value.kReverse);
   climb2.set(Value.kForward);
 }
 else if(xboxdrive.getBackButton() && lockout == true)
 {
   lockout = false;
  climb.set(Value.kForward);
 }
 
 else if(xboxelevator.getBackButton() && lockout == false)
 {
   climb2.set(Value.kReverse);
 }
 */
 
  
  

  
}















   
   



 


 
  
  
  
  

  @Override
  public void autonomousInit() {
    
  }

  @Override
  public void autonomousPeriodic() {

  }

 
  @Override
  public void teleopPeriodic() {
  }

 
  @Override
  public void testPeriodic() {
  }
}
