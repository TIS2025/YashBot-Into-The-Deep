package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Globals.MotorConst;
import org.firstinspires.ftc.teamcode.Hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Sequences.AutoSeq;
import org.firstinspires.ftc.teamcode.Sequences.FinalAutoSeq;
import org.firstinspires.ftc.teamcode.Subsystems.Arm;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;
import org.firstinspires.ftc.teamcode.Subsystems.Slider;

import java.util.Arrays;

@Autonomous(group = "Auto", name = "Auto Sample 1 + 3")
public class Auto_Sample_71 extends LinearOpMode {
    MecanumDrive drive;

    VelConstraint baseConst = new MinVelConstraint(Arrays.asList(new TranslationalVelConstraint(80)));

    public static double wait1 = 0.5;
    public static double wait2 = 0.3;
    public static double wait3 = 0.8;
    public static double wait4 = 0.7;
    public static double wait5 = 1.2;
    public static double wait6 = 0.3;

    public static Vector2d sample1pick = new Vector2d(-49,-44);
    public static Vector2d sample1drop = new Vector2d(-59,-52);
    public static Vector2d sample2pick = new Vector2d(-59,-44);
    public static Vector2d sample2drop = new Vector2d(-57.5,-52);
    public static Vector2d sample3pick = new Vector2d(-66,-42);
    public static Vector2d sample3drop = new Vector2d(-57.5,-52);
    public static Vector2d park = new Vector2d(-30,0);



    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware robot = new RobotHardware(hardwareMap);
        drive = new MecanumDrive(hardwareMap,new Pose2d(new Vector2d(-6,-64),-Math.PI/2));
        robot.init_encoders();
        Slider slider = new Slider(robot);
        Hanger hanger = new Hanger(robot);
        Arm arm = new Arm(robot);
        robot.reset_encoders();

        Action autoSequence = drive.actionBuilder(new Pose2d(new Vector2d(-6,-64),-Math.PI/2))
                //////////////// PRELOAD ////////////////////
                .afterTime(0.01,AutoSeq.SpecimenDropPos(arm,slider))
                .strafeToConstantHeading(new Vector2d(-6,-32))
                .stopAndAdd(AutoSeq.SpecimenDrop(arm,slider))
                //////////////// FIRST SAMPLE ////////////////
                .waitSeconds(wait2)
                .afterTime(0.01,FinalAutoSeq.SamplePickPosNoExtNoYaw(arm,slider,MotorConst.extSpecimenDrop))
                //TODO WAIT ADJUST
                .waitSeconds(wait3)
                .strafeToLinearHeading(sample1pick,Math.PI/2 + Math.toRadians(4))
                .stopAndAdd(FinalAutoSeq.SamplePick(arm))
                //TODO WAIT ADJUST
                .waitSeconds(wait4)
                .afterTime(0.01,FinalAutoSeq.SampleDropPosYawLeft(arm,slider,MotorConst.extMin,MotorConst.turretDown))
                .strafeToConstantHeading(sample1drop)
                //TODO WAIT ADJUST
                .waitSeconds(wait5)
                .stopAndAdd(FinalAutoSeq.SampleSideDrop(arm))
                //TODO WAIT ADJUST
                .waitSeconds(wait2)
                .afterTime(0.01,FinalAutoSeq.SamplePickPosNoExtNoYaw(arm,slider,MotorConst.extHighBucketDrop))
//                //TODO WAIT ADJUST
                .waitSeconds(wait3)
                .strafeToLinearHeading(sample2pick,Math.PI/2 + Math.toRadians(4))
////                //////////////////////// SECOND ELEMENT /////////////////////
                .stopAndAdd(FinalAutoSeq.SamplePick(arm))
                //TODO WAIT ADJUST
                .waitSeconds(wait4)
                .afterTime(0.01,FinalAutoSeq.SampleDropPosYawLeft(arm,slider,MotorConst.extMin,MotorConst.turretDown))
                .strafeToConstantHeading(sample2drop)
                //TODO WAIT ADJUST
                .waitSeconds(wait5)
                .stopAndAdd(FinalAutoSeq.SampleSideDrop(arm))
                //TODO WAIT ADJUST
                .waitSeconds(wait6)
                .afterTime(0.01,FinalAutoSeq.SamplePickPosNoExtLeftYaw(arm,slider,MotorConst.extHighBucketDrop))
//                //TODO WAIT ADJUST
                .waitSeconds(wait3)
                .strafeToLinearHeading(sample3pick,Math.PI/2 + Math.toRadians(3))
////                //////////////////////// THIRD ELEMENT //////////////////////
                .stopAndAdd(FinalAutoSeq.SamplePick(arm))
//                //TODO WAIT ADJUST
                .waitSeconds(wait4)
                .afterTime(0.01,FinalAutoSeq.SampleDropPosYawLeft(arm,slider,MotorConst.extMin,MotorConst.turretDown))
                .strafeToConstantHeading(sample3drop)
//                //TODO WAIT ADJUST
                .waitSeconds(wait5)
                .stopAndAdd(FinalAutoSeq.SampleSideDrop(arm))
//                //TODO WAIT ADJUST
                .waitSeconds(wait6)
                .afterTime(0.01,FinalAutoSeq.TeleOpInit(arm,slider,MotorConst.extHighBucketDrop))
                .strafeToConstantHeading(park,baseConst)
                .build();

        if(opModeInInit()) {
            Actions.runBlocking(AutoSeq.SpecimenInit(arm, slider));
        }

        waitForStart();
        if(opModeIsActive()){
            Actions.runBlocking(autoSequence);
        }

    }
}
