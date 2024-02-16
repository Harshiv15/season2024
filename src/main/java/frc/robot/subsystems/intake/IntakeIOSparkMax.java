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

package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.SparkPIDController.ArbFFUnits;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake.IntakeDirection;
import frc.robot.subsystems.intake.Intake.IntakeStatus;


/**
 * NOTE: To use the Spark Flex / NEO Vortex, replace all instances of "CANSparkMax" with
 * "CANSparkFlex".
 */
public class IntakeIOSparkMax implements IntakeIO {
  private static final double GEAR_RATIO = Constants.GEAR_RATIO_INTAKE;

  private final CANSparkMax main_intake = new CANSparkMax((int) Constants.MOTOR_INTAKE, MotorType.kBrushless);
  private final CANSparkMax main_feeder = new CANSparkMax((int) Constants.MOTOR_FEEDER, MotorType.kBrushless);
  private final DigitalInput beam_break = new DigitalInput((int) Constants.BEAM_CHANNEL);
  private IntakeStatus intake_status;
  private IntakeDirection intake_direction;

  //private final SparkPIDController pid = main_intake.getPIDController();

  public IntakeIOSparkMax() {
    main_intake.restoreFactoryDefaults();

    main_intake.setCANTimeout(250);

    main_intake.setInverted(false);

    main_intake.enableVoltageCompensation(12.0);
    main_intake.setSmartCurrentLimit(30);

    main_feeder.restoreFactoryDefaults();

    main_feeder.setCANTimeout(250);

    main_feeder.setInverted(false);

    main_feeder.enableVoltageCompensation(12.0);
    main_feeder.setSmartCurrentLimit(30);

    main_feeder.burnFlash();

    main_intake.burnFlash();
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
  
    inputs.appliedVolts = main_intake.getAppliedOutput() * main_intake.getBusVoltage();
    inputs.currentAmps = new double[] {main_intake.getOutputCurrent()};
    inputs.beamBreak = getBeamBreak();
    inputs.feederCurrentAmps = new double[] {main_feeder.getOutputCurrent()};
    inputs.feederAppliedVolts = main_feeder.getAppliedOutput() * main_feeder.getBusVoltage();
    inputs.status = intake_status;
    inputs.direction = intake_direction;

    
  }

  @Override
  public void setStatus(IntakeStatus state) {
    intake_status = state;
  }

  @Override
  public void setDirection(IntakeDirection state) {
    intake_direction = state;
  }

  @Override
  public boolean getBeamBreak() {
    return beam_break.get();
  }

  @Override
  public void setVoltage(double volts) {
    main_intake.setVoltage(volts);
  }

  @Override
  public void setFeederVoltage(double volts) {
    main_feeder.setVoltage(volts);
  }

  @Override
  public void stop() {
    main_intake.stopMotor();
  }

  @Override
  public void stopFeeder() {
    main_feeder.stopMotor();
  }


  @Override
  public IntakeDirection getDirection() {
    return intake_direction;
  }

  @Override
   public IntakeStatus getStatus() {
    return intake_status;
  }


}
