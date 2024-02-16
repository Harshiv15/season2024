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

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.intake.Intake.IntakeDirection;
import frc.robot.subsystems.intake.Intake.IntakeStatus;

public interface IntakeIO {
  @AutoLog
  public static class IntakeIOInputs {


    public double appliedVolts = 0.0;

    public double[] currentAmps = new double[] {};

    public boolean beamBreak = false;

    public double feederAppliedVolts = 0.0;

    public IntakeDirection direction = IntakeDirection.STOPPED;

    public IntakeStatus status = IntakeStatus.EMPTY;

    public double[] feederCurrentAmps = new double[] {};

  }

  /** Updates the set of loggable inputs. */
  public default void updateInputs(IntakeIOInputs inputs) {}

  /** Run open loop at the specified voltage. */
  public default void setVoltage(double volts) {}

  public default void setStatus(IntakeStatus state) {}

  public default void setDirection(IntakeDirection state) {}

  public default boolean getBeamBreak() {
    return false;
  }

  public default void setFeederVoltage(double volts) {}

  /** Stop in open loop. */
  public default void stop() {}

  public default void stopFeeder() {}

   public default IntakeDirection getDirection() {
    return null;
  }

   public default IntakeStatus getStatus() {
    return null;
  }
}
