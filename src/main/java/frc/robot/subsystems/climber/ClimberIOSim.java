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

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class ClimberIOSim implements ClimberIO {

  //find moment of inertia of climber

  //correct double gearing, double jKgMetersSquared
  private DCMotorSim simA = new sim(DCMotor.getNEO(1), Constants.GEAR_RATIO_CLIMB, Constants.INERTIA_CLIMB);
  private DCMotorSim simB = new sim(DCMotor.getNEO(1), Constants.GEAR_RATIO_CLIMB, Constants.INERTIA_CLIMB);

  //private boolean closedLoop = false;
  //private double ffVolts = 0.0;
  private double appliedVolts = 0.0;

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    /*if (closedLoop) {
      appliedVolts =
          MathUtil.clamp(pid.calculate(sim.getAngularVelocityRadPerSec()) + ffVolts, -12.0, 12.0);
      sim.setInputVoltage(appliedVolts);
    }*/

    //time between updates
    simA.update(0.02);
    simB.update(0.02);

    inputs.positionRadA = simA.getAngularPositionRad();
    inputs.velocityRadPerSecA = simA.getAngularVelocityRadPerSec();
    inputs.positionRadB = simB.getAngularPositionRad();
    inputs.velocityRadPerSecB = simB.getAngularVelocityRadPerSec();
    inputs.appliedVoltsA = appliedVolts;
    inputs.appliedVoltsB = -appliedVolts;
    inputs.currentAmpsA = new double[] {simA.getCurrentDrawAmps()};
    inputs.currentAmpsB = new double[] {simB.getCurrentDrawAmps()};
  }

  @Override
  public void setVoltage(double volts) {
    //closedLoop = false
    appliedVolts = volts;
    simA.setInputVoltage(volts);
    simB.setInputVoltage(-volts);
  }

 

  @Override
  public void stop() {
    setVoltage(0.0);
  }


}
