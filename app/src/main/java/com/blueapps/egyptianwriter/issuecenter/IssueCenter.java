package com.blueapps.egyptianwriter.issuecenter;

public enum IssueCenter {
    ISSUE_CENTER;

    private boolean isFatalIssue = false;

    public boolean isFatalIssue() {
        return isFatalIssue;
    }

    public void setFatalIssue(boolean fatalIssue) {
        isFatalIssue = fatalIssue;
    }
}
