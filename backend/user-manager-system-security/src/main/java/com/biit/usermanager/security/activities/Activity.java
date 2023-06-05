package com.biit.usermanager.security.activities;

import com.biit.usermanager.security.IActivity;

import java.util.Objects;

public class Activity implements IActivity {

    private final String tag;

    public Activity(String tag) {
        this.tag = tag;
    }


    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Activity activity = (Activity) o;
        return Objects.equals(tag, activity.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    @Override
    public String toString() {
        return "Activity{"
                + "tag='" + tag + '\''
                + '}';
    }
}
