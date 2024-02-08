package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap.IntakeMap;

public class Intake extends SubsystemBase {
    private static Intake instance;

    public static Intake getInstance() {
        if(instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    private CANSparkMax motor1, motor2;
    private double MOTOR_SPEED_1 = 0.15; //TODO: fix value
    private double MOTOR_SPEED_2 = 0.69; //TODO: fix value
    

    private Intake() {
        motor1 = new CANSparkMax(IntakeMap.MOTOR_ID_1, MotorType.kBrushless);
        motor2 = new CANSparkMax(IntakeMap.MOTOR_ID_2, MotorType.kBrushless);
    }

    private void run(double speed1, double speed2){
        motor1.set(speed1);
        motor2.set(speed2);
    }

    public Command runCommand() {
        return new InstantCommand(()->{
            run(MOTOR_SPEED_1, MOTOR_SPEED_2);
        }, this);
    }

    public Command stopCommand(){
        return new InstantCommand(
            () -> run(0, 0)
        ); 
    }

    
}