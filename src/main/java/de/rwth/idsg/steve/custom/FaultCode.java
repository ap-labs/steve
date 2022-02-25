package de.rwth.idsg.steve.custom;

import java.util.Arrays;
import java.util.List;

public class FaultCode {

    public static final List<FaultCode> FAULT_CODES = Arrays.asList(
            new FaultCode(0, "Unknown reason"),
            new FaultCode(1, "Communication failure between TCU and charging controller"),
            new FaultCode(2, "Card reader communication fault"),
            new FaultCode(3, "Meter communication fault"),
            new FaultCode(4, "ESAM fault"),
            new FaultCode(5, "Trade record is full"),
            new FaultCode(6, "Failed to store the trade record"),
            new FaultCode(7, "Platform registration verification failed"),
            new FaultCode(8, "Program file verification failed"),
            new FaultCode(9, "Vehicle control guidance warning during charging"),
            new FaultCode(10, "Fault sent by the charge controller"),
            new FaultCode(11, "Dc bus output overvoltage alarm"),
            new FaultCode(12, "DC bus output undervoltage alarm"),
            new FaultCode(13, "Battery charging overcurrent alarm"),
            new FaultCode(14, "Battery module sampling point over temperature alarm"),
            new FaultCode(15, "Fault sent by the charge controller"),
            new FaultCode(16, "Emergency stop button action failure"),
            new FaultCode(17, "Insulation monitoring fault"),
            new FaultCode(18, "Reverse battery connection failure"),
            new FaultCode(19, "Arrester failure"),
            new FaultCode(20, "Charging connector is not at the right place"),
            new FaultCode(21, "Charging pile over temperature failure"),
            new FaultCode(22, "Smoke alarm"),
            new FaultCode(23, "Input voltage overvoltage"),
            new FaultCode(24, "Input voltage undervoltage"),
            new FaultCode(25, "Charging module failure"),
            new FaultCode(26, "Reserved"),
            new FaultCode(27, "Charging module fan failure"),
            new FaultCode(28, "Charging module over temperature alarm"),
            new FaultCode(29, "Charging module AC input alarm"),
            new FaultCode(30, "Charging module output short circuit fault"),
            new FaultCode(31, "Charging module output overcurrent alarm"),
            new FaultCode(32, "Charging module output overvoltage alarm"),
            new FaultCode(33, "Charging module output undervoltage alarm"),
            new FaultCode(34, "Charging module input overvoltage alarm"),
            new FaultCode(35, "Charging module input undervoltage alarm"),
            new FaultCode(36, "Charging module input phase loss alarm"),
            new FaultCode(37, "Charging module communication alarm"),
            new FaultCode(38, "Vehicle control guidance warning during charging"),
            new FaultCode(39, "AC circuit breaker failure"),
            new FaultCode(40, "DC bus output overcurrent alarm"),
            new FaultCode(41, "DC bus output fuse failure"),
            new FaultCode(42, "DC bus output contactor failure"),
            new FaultCode(43, "Charging connector electronic lock failure"),
            new FaultCode(44, "Charger fan failure"),
            new FaultCode(45, "Charging connector over temperature failure"),
            new FaultCode(46, "other failure of TCU"),
            new FaultCode(47, "Other failure of the charger"),
            new FaultCode(48, "Access control failure"),
            new FaultCode(49, "DC output contactor stuck fault"),
            new FaultCode(50, "Insulation monitoring alarm"),
            new FaultCode(51, "discharge circuit alarm"),
            new FaultCode(52, "Charging pile over temperature alarm"),
            new FaultCode(53, "Charge connector over temperature alarm"),
            new FaultCode(54, "Other types of failure"),
            new FaultCode(55, "AC input contactor rejection/misoperation failure"),
            new FaultCode(56, "AC input contactor stuck fault"),
            new FaultCode(57, "Auxiliary power failure"),
            new FaultCode(58, "Parallel contactor rejection/misoperation failure"),
            new FaultCode(59, "Parallel contactor stuck fault"),
            new FaultCode(200, "PE disconnection failure"),
            new FaultCode(201, "Output overcurrent protection action"),
            new FaultCode(202, "AC output contactor stuck fault"),
            new FaultCode(203, "Charging unit power failure"),
            new FaultCode(204, "Charging service has been suspended")
    );

    public final int id;
    public final String description;

    FaultCode(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
