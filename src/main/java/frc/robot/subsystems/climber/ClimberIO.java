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

import org.littletonrobotics.junction.AutoLog;

public interface ClimberIO {
  @AutoLog
  public static class ClimberIOInputs {
    public double positionRadA = 0.0;
    public double positionRadB = 0.0;

    public double velocityRadPerSecA = 0.0;
    public double velocityRadPerSecB = 0.0;

    public double appliedVoltsA = 0.0;
    public double appliedVoltsB = 0.0;

    public double[] currentAmpsA = new double[] {};
    public double[] currentAmpsB = new double[] {};

  }

  /** Updates the set of loggable inputs. */
  public default void updateInputs(FlywheelIOInputs inputs) {}

  /** Run open loop at the specified voltage. */
  public default void setVoltage(double volts) {}



  /** Stop in open loop. */
  public default void stop() {}

  
}
