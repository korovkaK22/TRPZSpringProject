package com.example.users;

import lombok.Setter;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;

@Setter
public class CustomTransferRatePermission extends TransferRatePermission {
    private int maxDownloadRate;
    private int maxUploadRate;

    public CustomTransferRatePermission(int maxDownloadRate, int maxUploadRate) {
        super(maxDownloadRate, maxUploadRate);
    }

}

