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

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake.IntakeDirection;
import frc.robot.subsystems.intake.Intake.IntakeStatus;

public class IntakeIOSim implements IntakeIO {

  //find moment of inertia of climber

  //correct double gearing, double jKgMetersSquared
  private DCMotorSim sim = new DCMotorSim(DCMotor.getNEO(1), Constants.GEAR_RATIO_INTAKE, Constants.INERTIA_INTAKE);
  private DCMotorSim simFeeder = new DCMotorSim(DCMotor.getNEO(1), Constants.GEAR_RATIO_FEEDER, Constants.INERTIA_FEEDER);
  private DIOSim simBeamBreak = new DIOSim((int) Constants.BEAM_CHANNEL);

  private double appliedVolts = 0.0;
  private double feederAppliedVolts = 0.0;

  private IntakeStatus intake_status;
  private IntakeDirection intake_direction;

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    /*if (closedLoop) {
      appliedVolts =
          MathUtil.clamp(pid.calculate(sim.getAngularVelocityRadPerSec()) + ffVolts, -12.0, 12.0);
      sim.setInputVoltage(appliedVolts);
    }*/

    //time between updates
    sim.update(0.02);

    //inputs.positionRad = sim.getAngularPositionRad();
    //inputs.velocityRadPerSec = sim.getAngularVelocityRadPerSec();

    inputs.appliedVolts = appliedVolts;
    inputs.currentAmps = new double[] {sim.getCurrentDrawAmps()};

    inputs.beamBreak = simBeamBreak.getValue();

    inputs.feederAppliedVolts = feederAppliedVolts;
    inputs.feederCurrentAmps = new double[] {simFeeder.getCurrentDrawAmps()};

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
  public void setVoltage(double volts) {
    appliedVolts = volts;
    sim.setInputVoltage(volts);
  }

  @Override
  public void setFeederVoltage(double volts) {
    feederAppliedVolts = volts;
    simFeeder.setInputVoltage(volts);
  }

  @Override
  public boolean getBeamBreak() {
    return simBeamBreak.getValue();
  }

  @Override
  public void stop() {
    setVoltage(0.0);
  }

  @Override
  public void stopFeeder() {
    setFeederVoltage(0.0);
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
