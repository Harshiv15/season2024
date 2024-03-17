package frc.robot.layout;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Config;
import frc.robot.RobotMap.Coordinates;
import frc.robot.RobotMap.PivotMap;
import frc.robot.RobotMap.ShamperMap;
import frc.robot.core.util.controllers.CommandMap;
import frc.robot.core.util.controllers.GameController;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Intake.IntakeDirection;
import frc.robot.util.FlywheelLookupTable;

import java.util.function.Supplier;

public abstract class OperatorMap extends CommandMap {

  public OperatorMap(GameController controller) {
    super(controller);
  }

  abstract JoystickButton getIntakeStopButton();

  abstract JoystickButton getIntakeButton();

  abstract JoystickButton getIntakeReverseButton();

  abstract JoystickButton getShootButton();

  abstract JoystickButton getShootAmpButton();

  abstract JoystickButton getAmpAlignButton();

  abstract JoystickButton getClimbSequenceButton();

  abstract double getManualPivotAxis();

  abstract JoystickButton getArcButton();

  abstract JoystickButton getTrapButton();

  abstract JoystickButton getStageAlignButton();

  abstract JoystickButton getManualShootButton();

  abstract double getManualClimberAxis();

  abstract JoystickButton getAmplifyButton();

  abstract JoystickButton getCoopButton();

  abstract JoystickButton getLEDPatternOneButton();

  abstract JoystickButton getLEDPatternTwoButton();

  abstract JoystickButton getLEDPatternThreeButton();

  abstract JoystickButton getLEDPatternFourButton();

  abstract JoystickButton getLEDPatternFiveButton();

  abstract JoystickButton getLEDPatternOffButton();

  // abstract Trigger getPivotLowerButton();

  // abstract Trigger getPivotRaiseButton();

  abstract double getLEDAxis1();

  abstract double getLEDAxis2();

  private void registerIntake() {
    if (Config.Subsystems.INTAKE_ENABLED) {
      Intake intake = Intake.getInstance();

      getIntakeStopButton().onTrue(intake.setIntakeState(IntakeDirection.STOPPED));
      getIntakeButton().whileTrue(intake.intakeUntilLoadedCommand())
              .onFalse(intake.setIntakeState(IntakeDirection.STOPPED));
      getIntakeReverseButton().onTrue(intake.setIntakeState(IntakeDirection.REVERSE));


      getShootButton().onTrue(intake.setIntakeState(IntakeDirection.FORWARD))
              .onFalse(intake.setIntakeState(IntakeDirection.STOPPED));
    }
  }

  private void registerShamper() {
    if (Config.Subsystems.SHAMPER_ENABLED) {
      Shamper shooter = Shamper.getInstance();
      Pivot pivot = Pivot.getInstance();
      FlywheelLookupTable lookupTable = FlywheelLookupTable.getInstance();
      Pose2d target = DriverStation.getAlliance().equals(DriverStation.Alliance.Blue) ? Coordinates.BLUE_SPEAKER
              : Coordinates.RED_SPEAKER;
      PoseEstimator poseEstimator = PoseEstimator.getInstance();
    }
  }


  private void registerClimber() {
    if (Config.Subsystems.CLIMBER_ENABLED) {
      Climber climber = Climber.getInstance();
      climber.setDefaultCommand(climber.run(() -> getManualClimberAxis()));
      
    }
  }

  private void registerComplexCommands() {
    if (Config.Subsystems.SHAMPER_ENABLED && Config.Subsystems.INTAKE_ENABLED
            && Config.Subsystems.DRIVETRAIN_ENABLED) {
      Drivetrain drivetrain = Drivetrain.getInstance();
      Intake intake = Intake.getInstance();
      Shamper shooter = Shamper.getInstance();
      Pivot pivot = Pivot.getInstance();
      FlywheelLookupTable lookupTable = FlywheelLookupTable.getInstance();
      PoseEstimator poseEstimator = PoseEstimator.getInstance();

      Supplier<Pose2d> target = () -> (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue) ?
        Coordinates.BLUE_SPEAKER
        : Coordinates.RED_SPEAKER;

      target.get().transformBy(
        new Transform2d(
          drivetrain.getChassisSpeeds().vxMetersPerSecond
            * lookupTable.get(
              poseEstimator.getDistanceToPose(() -> target.get().getTranslation())
            )
            .getFlightTime(),
          drivetrain.getChassisSpeeds().vyMetersPerSecond
            * lookupTable.get(
              poseEstimator.getDistanceToPose(() -> target.get().getTranslation())
            )
            .getFlightTime(),
          target.get().getRotation()
        ).inverse() // because we're technically "closer" to the speaker
      );

      getArcButton().whileTrue((pivot.updatePosition(() -> lookupTable
                      .get(poseEstimator.getDistanceToPose(() -> target.get().getTranslation())).getAngleSetpoint())
              .alongWith(
                shooter.setShootVelocityCommand(
                  () -> lookupTable.get(
                              poseEstimator.getDistanceToPose(
                                () -> target.get().getTranslation())).getFlywheelV(),
                  // it's going to be the same value everywhere! why did we have feeder velocity in the LUT?
                  () -> 4000.0
                )
              )
        )
      );

      getArcButton().onFalse(pivot.updatePosition(() -> PivotMap.LOWER_SETPOINT_LIMIT + 1).alongWith(
        shooter.setShootVelocityCommand(() -> 0.0, () -> 0.0)).andThen(shooter.setFeederVelocityCommand(() -> 0.0)));

      getShootAmpButton().onTrue(intake.setIntakeState(IntakeDirection.FORWARD).andThen(shooter.setFeederVelocityCommand(() -> ShamperMap.AMP_SPEED_FEEDER)))
              .onFalse(intake.setIntakeState(IntakeDirection.STOPPED));

      getAmpAlignButton().onTrue(

              pivot.updatePosition(() -> PivotMap.PIVOT_AMP_ANGLE).alongWith(
                      shooter.setTopVelocityCommand(() -> ShamperMap.AMP_SPEED_TOP).andThen(shooter.setBotVelocityCommand(() -> -ShamperMap.AMP_SPEED_TOP))));

      getAmpAlignButton().onFalse(
              pivot.updatePosition(() -> PivotMap.LOWER_SETPOINT_LIMIT + 1).alongWith(
                      shooter.setFlywheelVelocityCommand(() -> 0.0).andThen(shooter.setFeederVelocityCommand(() -> 0.0))));

      getStageAlignButton().onTrue(
              pivot.updatePosition(() -> PivotMap.PIVOT_TRAP_ANGLE).alongWith(
                      shooter.setFlywheelVelocityCommand(() -> ShamperMap.TRAP_SPEED)));

      getClimbSequenceButton().onTrue(
              new SequentialCommandGroup(
                      Climber.getInstance().run(() -> 0.2),
                      new WaitCommand(1),
                      Climber.getInstance().run(() -> 0)));
      // getPivotLowerButton().onTrue(pivot.updatePosition(() -> 0.0).alongWith(new PrintCommand("hi")));
      // getPivotRaiseButton().onTrue(pivot.updatePosition(() -> 75.0).alongWith(new PrintCommand("bye")));
    }

  }

  private void registerLEDs() {
    if (Config.Subsystems.LEDS_ENABLED && Config.Subsystems.INTAKE_ENABLED) {
      AddressableLEDLights lights = AddressableLEDLights.getInstance();
      Intake intake = Intake.getInstance();

      getAmplifyButton().onTrue(
        lights.getAmplifyPatternCommand()
          .withTimeout(4.0)
          .andThen(lights.setNoteStatusCommand(intake::getNoteStatus))
      );

      getCoopButton().onTrue(
        lights.getCoOpPatternCommand()
          .withTimeout(4.0)
          .andThen(lights.setNoteStatusCommand(intake::getNoteStatus))
      );

      lights.setColorCommand(Config.IS_ALLIANCE_RED ? Color.kRed : Color.kBlue).schedule();

      // will get canceled on both triggers until the rising edge is detected
      lights.setDefaultCommand(lights.setNoteStatusCommand(intake::getNoteStatus));
    }
  }

  @Override
  public void registerCommands() {
    registerIntake();
    registerClimber();
    registerShamper();
    registerLEDs();
    registerComplexCommands();
  }
}