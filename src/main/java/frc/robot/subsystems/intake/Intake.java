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

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants;
import frc.robot.subsystems.intake.IntakeIO.IntakeIOInputs;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
  private final IntakeIO io;
  private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();
  private final SysIdRoutine sysId;

  public static enum IntakeDirection {
    FORWARD, REVERSE, STOPPED
}

public static enum IntakeStatus {
    EMPTY, LOADED
}

  /** Creates a new Flywheel. */
  public Intake(IntakeIO io) {
    this.io = io;

    // Switch constants based on mode (the physics simulator is treated as a
    // separate robot with different tuning)


    // Configure SysId
    sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Intake/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism((voltage) -> runVolts(voltage.in(Volts)), null, this));
  }


 

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Intake", inputs);



  //check this if statement because might be backwards, also assumes beam exists
  if (!io.getBeamBreak()) {
    io.setStatus(IntakeStatus.EMPTY);
  } else {
    io.setStatus(IntakeStatus.LOADED);
  }


if (io.getDirection() == IntakeDirection.FORWARD) {
  //setSpeed(Constants.INTAKE_FORWARD_SPEED, Constants.FEEDER_FORWARD_SPEED);
  runVolts(Constants.INTAKE_FORWARD_SPEED);
  runFeederVolts(Constants.FEEDER_FORWARD_SPEED);
} else if (io.getDirection() == IntakeDirection.REVERSE) {
  //setSpeed(Constants.INTAKE_REVERSE_SPEED, Constants.FEEDER_REVERSE_SPEED);
  runVolts(Constants.INTAKE_REVERSE_SPEED);
  runFeederVolts(Constants.FEEDER_REVERSE_SPEED);
} else {
  //setSpeed(0, 0);
  stop();
}

  }

  //set intake state
public void setIntakeDirection(IntakeDirection direction) {
 io.setDirection(direction);
}

public boolean hasNote() {
  return io.getStatus() == IntakeStatus.LOADED;
}

  /** Run open loop at the specified voltage. */
  public void runVolts(double volts) {
    io.setVoltage(volts);
  }

  public void runFeederVolts(double volts) {
    io.setFeederVoltage(volts);
  }

 
  /** Stops the flywheel. */
  public void stop() {
    io.stop();
    io.stopFeeder();
  }

  /** Returns a command to run a quasistatic test in the specified direction. */
  public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
    return sysId.quasistatic(direction);
  }

  /** Returns a command to run a dynamic test in the specified direction. */
  public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return sysId.dynamic(direction);
  }

  
 
}
