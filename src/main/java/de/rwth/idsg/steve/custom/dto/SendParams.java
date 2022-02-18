package de.rwth.idsg.steve.custom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


public class SendParams {

    @Data
    public static class Request {

        @JsonProperty("MaxPower")
        private String maxPower;
        @JsonProperty("MaxCurrent")
        private String maxCurrent;
        @JsonProperty("MaxVoltage")
        private String maxVoltage;
        @JsonProperty("MaxChargeTime")
        private String maxChargeTime;
        @JsonProperty("Command")
        private Command command;
    }

    @Data
    public static class Response {

        @JsonProperty("Status")
        private Status status;
        @JsonProperty("MaxPower")
        private String maxPower;
        @JsonProperty("SessionID")
        private String sessionID;
        @JsonProperty("StartTime")
        private String startTime;
        @JsonProperty("StopTime")
        private String stopTime;
        @JsonProperty("EVCCID")
        private String EVCCID;
        @JsonProperty("SOC")
        private String SOC;
        @JsonProperty("StartSOC")
        private String startSOC;
        @JsonProperty("StopSOC")
        private String stopSOC;
        @JsonProperty("PresentCurrent")
        private String presentCurrent;
        @JsonProperty("PresentVoltage")
        private String presentVoltage;
        @JsonProperty("PresentPower")
        private String presentPower;
        @JsonProperty("SessionEnergy")
        private String sessionEnergy;
        @JsonProperty("TargetCurrent")
        private String targetCurrent;
        @JsonProperty("TargetVoltage")
        private String targetVoltage;
        @JsonProperty("SessionStatus")
        private SessionStatus sessionStatus;
        @JsonProperty("InternalError")
        private ErrorStatus internalError;
        @JsonProperty("OvercurrentError")
        private ErrorStatus overcurrentError;
        @JsonProperty("OutputIsolationError")
        private ErrorStatus outputIsolationError;
        @JsonProperty("EVError")
        private String evError;
        @JsonProperty("SessionStopReason")
        private String sessionStopReason;
    }

    public enum Command {

        Idle, Start, Reboot, Block
    }

    public enum Status {

        Wait, Charge, Fault
    }

    public enum SessionStatus {

        Disconnected, SLAC, SDP, Handshake, SessionSetup, ServiceDiscovery, ServiceDetail, PaymentServiceSelection,
        Authorization, ChargeParameterDiscovery, CableCheck, PreCharge, PowerDelivery, CurrentDemand, WeldingDetection,
        SessionStop, Stop, Error
    }

    public enum ErrorStatus {
        Error, NoError
    }
}
