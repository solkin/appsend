package com.tomclaw.appsend.main.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.tomclaw.appsend.util.Unobfuscatable;

/**
 * Created by solkin on 28/11/2017.
 */
public class AbuseResult implements Parcelable, Unobfuscatable {

    public AbuseResult() {
    }

    protected AbuseResult(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AbuseResult> CREATOR = new Creator<AbuseResult>() {
        @Override
        public AbuseResult createFromParcel(Parcel in) {
            return new AbuseResult(in);
        }

        @Override
        public AbuseResult[] newArray(int size) {
            return new AbuseResult[size];
        }
    };

}
