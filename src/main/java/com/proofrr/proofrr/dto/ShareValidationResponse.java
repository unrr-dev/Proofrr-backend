package com.proofrr.proofrr.dto;

public class ShareValidationResponse {
    private boolean valid;
    private String shareUuid;

    public ShareValidationResponse() {
    }

    public ShareValidationResponse(boolean valid, String shareUuid) {
        this.valid = valid;
        this.shareUuid = shareUuid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getShareUuid() {
        return shareUuid;
    }

    public void setShareUuid(String shareUuid) {
        this.shareUuid = shareUuid;
    }
}
