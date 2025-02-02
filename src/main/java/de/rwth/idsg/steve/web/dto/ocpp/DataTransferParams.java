/*
 * SteVe - SteckdosenVerwaltung - https://github.com/RWTH-i5-IDSG/steve
 * Copyright (C) 2013-2021 RWTH Aachen University - Information Systems - Intelligent Distributed Systems Group (IDSG).
 * All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package de.rwth.idsg.steve.web.dto.ocpp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 02.01.2015
 */
@Getter
@Setter
public class DataTransferParams extends MultipleChargePointSelect {

    @NotNull(message = "Vendor ID is required")
    private String vendorId;

    private String messageId;

    private Data data;

    @lombok.Data
    public static class Data {

        private String code;
        private String eventId;
        private String timestamp;
    }

    public String getData() {
        return "";
    }

    public void setData(Data data) {
        LATEST_DATA = data;
        this.data = data;
    }

    public static Data LATEST_DATA;
}
