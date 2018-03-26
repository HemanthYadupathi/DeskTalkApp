package com.desktalk.Model;

/**
 * Created by User on 3/24/2018.
 */

public class LeaveDetailsModel {

    String leave_id;
    String title;
    String reason;
    String from_date;
    String to_date;
    String class_id;
    String section_id;
    String user_id;
    String created_date;
    String status;

    public String getLeave_id() {
        return leave_id;
    }

    public void setLeave_id(String leave_id) {
        this.leave_id = leave_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
