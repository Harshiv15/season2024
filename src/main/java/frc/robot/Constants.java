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

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final Mode currentMode = Mode.REAL;


  public static final double GEAR_RATIO_CLIMB = 20*12;
  public static final double INERTIA_CLIMB = 0.004;
  public static final double MOTOR_A_CLIMB = 0;
  public static final double MOTOR_B_CLIMB = 1;

  public static final double GEAR_RATIO_INTAKE = 20*12;
  public static final double INERTIA_INTAKE = 0.004;
  public static final double GEAR_RATIO_FEEDER = 20*12;
  public static final double INERTIA_FEEDER = 0.004;
  public static final double MOTOR_INTAKE = 2;
  public static final double MOTOR_FEEDER = 6;
  public static final double BEAM_CHANNEL = 1;

  public static final double GEAR_RATIO_SHOOTER = 20*12;
  public static final double SHOOTER_INERTIA = 0.004;
  public static final double MOTOR_A_SHOOTER = 3;
  public static final double MOTOR_B_SHOOTER = 4;

  public static final double GEAR_RATIO_PIVOT = 20*12;
  public static final double PIVOT_INERTIA = 0.004;
  public static final double MOTOR_PIVOT = 5;

  public static final double INTAKE_FORWARD_SPEED = 0.5;
  public static final double INTAKE_REVERSE_SPEED = 0.5;

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }
}
