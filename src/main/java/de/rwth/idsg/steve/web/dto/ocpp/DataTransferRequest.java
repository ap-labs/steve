package de.rwth.idsg.steve.web.dto.ocpp;

import de.rwth.idsg.ocpp.jaxb.RequestType;
import de.rwth.idsg.steve.ocpp.ws.JsonObjectMapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "DataTransferRequest",
        propOrder = {"vendorId", "messageId", "data"}
)
public class DataTransferRequest implements RequestType {
    @XmlElement(
            required = true
    )
    protected String vendorId;
    protected String messageId;
    protected Data data;

    public DataTransferRequest() {
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(String value) {
        this.vendorId = value;
    }

    public boolean isSetVendorId() {
        return this.vendorId != null;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String value) {
        this.messageId = value;
    }

    public boolean isSetMessageId() {
        return this.messageId != null;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isSetData() {
        return this.data != null;
    }

    public DataTransferRequest withVendorId(String value) {
        this.setVendorId(value);
        return this;
    }

    public DataTransferRequest withMessageId(String value) {
        this.setMessageId(value);
        return this;
    }

    public DataTransferRequest withData(Data value) {
        this.setData(value);
        return this;
    }

    public DataTransferRequest withData(String value) {
        try {
            Data request = JsonObjectMapper.INSTANCE.getMapper().readValue(value, Data.class);
            this.setData(request);
        } catch (Exception e) {
            // stub
        }
        return this;
    }

    @lombok.Data
    public static class Data {

        private String code;
        private String eventId;
        private String timestamp;
    }

    public String toString() {
        return "DataTransferRequest(vendorId=" + this.getVendorId() + ", messageId=" + this.getMessageId() + ", data=" + this.getData() + ")";
    }
}
