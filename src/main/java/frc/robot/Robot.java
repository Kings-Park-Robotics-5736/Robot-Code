
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
//import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Solenoid;


public class Robot extends TimedRobot {//implements PIDOutput {
  //Create Objects
  private Timer time = new Timer();
  private DigitalInput limitswitch = new DigitalInput(9);
  private WPI_VictorSPX rightmaster = new WPI_VictorSPX(3);
  private WPI_VictorSPX leftmaster = new WPI_VictorSPX(1);
  private WPI_VictorSPX leftslave = new WPI_VictorSPX(2);
  private WPI_VictorSPX rightslave = new WPI_VictorSPX(4);
  private XboxController xboxelevator = new XboxController(1);
  private XboxController xboxdrive = new XboxController(0);
  private DifferentialDrive drive = new DifferentialDrive(leftmaster, rightmaster);
 // private TalonSRX elevator =  new TalonSRX(1); 
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry ta = table.getEntry("ta");
 

/*
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
*/  
  
  @Override
  public void robotInit() {
    //Configuration Settings for Can Use
  //CameraServer.getInstance().startAutomaticCapture();
    /*elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    elevator.setSensorPhase(false);
    elevator.configNominalOutputForward(0, 30);
    elevator.configNominalOutputReverse(0, 30);
    elevator.configPeakOutputForward(.6, 30);
    elevator.configPeakOutputReverse(-.4, 30);
    elevator.config_kP(0, 1);   //**** NEED TUNING*****
    elevator.configAllowableClosedloopError(0, 5, 10);
    */
    leftmaster.setInverted(false);
    rightmaster.setNeutralMode(NeutralMode.Brake);
    leftmaster.setNeutralMode(NeutralMode.Brake);
    //elevator.setNeutralMode(NeutralMode.Brake);
    rightslave.follow(rightmaster);
    leftslave.follow(leftmaster);
    /*elevator.configForwardSoftLimitThreshold(31500);
    elevator.configForwardSoftLimitEnable(true);
    elevator.configReverseSoftLimitThreshold(1500);
    elevator.configReverseSoftLimitEnable(true);
    */
  }

 
  @Override
  public void robotPeriodic() {
   /* SmartDashboard.putString("DB/String 0"," " + elevator.getSelectedSensorPosition());
    System.out.println(elevator.getSensorCollection().getQuadraturePosition());
   
  
  */
  //read values periodically
double x = tx.getDouble(0.0);
double y = ty.getDouble(0.0);
double area = ta.getDouble(0.0);

//post to smart dashboard periodically
SmartDashboard.putNumber("LimelightX", x);
SmartDashboard.putNumber("LimelightY", y);
SmartDashboard.putNumber("LimelightArea", area);
 
  
SmartDashboard.putString("leftaxis", Double.toString((xboxdrive.getTriggerAxis(Hand.kLeft))));
SmartDashboard.putString("rightaxis", Double.toString(xboxdrive.getTriggerAxis(Hand.kRight)));
SmartDashboard.putString("left", Double.toString(xboxdrive.getX(Hand.kLeft)));
 

 //Elevator Command Control
 /*
 if(xboxelevator.getStickButton(Hand.kRight))
 {
   elevator.setSelectedSensorPosition(0);
   elevator.getSensorCollection().setQuadraturePosition(0, 10);
   elevator.set(ControlMode.PercentOutput, 0);
 }
*/


  
//*** Drive command control*****
   drive.arcadeDrive((xboxdrive.getTriggerAxis(Hand.kLeft) * .75) - (xboxdrive.getTriggerAxis(Hand.kRight) * .75), xboxdrive.getX(Hand.kLeft));  

   //Solenoid stuff || Control
   //Gripper ****DONE*** BUT NEEDS TUNING
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
