package com.proofrr.proofrr.dto;

public class ShareLinkResponse {
    private String shareUuid;
    private String shareUrl;

    public ShareLinkResponse() {
    }

    public ShareLinkResponse(String shareUuid, String shareUrl) {
        this.shareUuid = shareUuid;
        this.shareUrl = shareUrl;
    }

    public String getShareUuid() {
        return shareUuid;
    }

    public void setShareUuid(String shareUuid) {
        this.shareUuid = shareUuid;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
