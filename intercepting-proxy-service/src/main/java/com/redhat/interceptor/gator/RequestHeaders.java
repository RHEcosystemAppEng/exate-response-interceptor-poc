package com.redhat.interceptor.gator;

public enum RequestHeaders {
    X_Data_Set_Type,
    X_Api_Key,
    X_Resource_Token,
    X_Request_ID,
    X_Validation_Key,
    X_Execution_Context,
    X_Dataset_On_Invalid_Manifest,
    X_Silent_Mode;

    @Override
    public String toString() {
        return this.name().replace("_", "-");
    }
}
