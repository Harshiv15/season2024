// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.climber;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.SparkPIDController.ArbFFUnits;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants;

/**
 * NOTE: To use the Spark Flex / NEO Vortex, replace all instances of "CANSparkMax" with
 * "CANSparkFlex".
 */
public class ClimberIOSparkMax implements ClimberIO {
  private static final double GEAR_RATIO = Constants.GEAR_RATIO_CLIMB;

  private final CANSparkMax leader = new CANSparkMax((int) Constants.MOTOR_A_CLIMB, MotorType.kBrushless);
  private final CANSparkMax follower = new CANSparkMax((int) Constants.MOTOR_B_CLIMB, MotorType.kBrushless);
  private final RelativeEncoder encoderA = leader.getEncoder();
  private final RelativeEncoder encoderB = follower.getEncoder();
  //private final SparkPIDController pid = leader.getPIDController();

  public ClimberIOSparkMax() {
    leader.restoreFactoryDefaults();
    follower.restoreFactoryDefaults();

    leader.setCANTimeout(250);
    follower.setCANTimeout(250);

    leader.setInverted(false);
    follower.follow(leader, true);

    leader.enableVoltageCompensation(12.0);
    leader.setSmartCurrentLimit(30);

    leader.burnFlash();
    follower.burnFlash();
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    
    inputs.appliedVoltsA = leader.getAppliedOutput() * leader.getBusVoltage();
    inputs.currentAmpsA = new double[] {leader.getOutputCurrent()};

    inputs.appliedVoltsB = follower.getAppliedOutput() * follower.getBusVoltage();
    inputs.currentAmpsB = new double[] {follower.getOutputCurrent()};
  }

  @Override
  public void setVoltage(double volts) {
    leader.setVoltage(volts);
  }

 /*  @Override
  public void setVelocity(double velocityRadPerSec, double ffVolts) {
    pid.setReference(
        Units.radiansPerSecondToRotationsPerMinute(velocityRadPerSec) * GEAR_RATIO,
        ControlType.kVelocity,
        0,
        ffVolts,
        ArbFFUnits.kVoltage);
  }*/

  @Override
  public void stop() {
    leader.stopMotor();
  }

  /*@Override
  public void configurePID(double kP, double kI, double kD) {
    pid.setP(kP, 0);
    pid.setI(kI, 0);
    pid.setD(kD, 0);
    pid.setFF(0, 0);
  }*/
}
