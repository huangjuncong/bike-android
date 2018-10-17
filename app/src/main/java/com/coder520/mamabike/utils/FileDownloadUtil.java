package com.coder520.mamabike.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;



public class FileDownloadUtil {

    public interface DownloadProgressLisener {
        void onProgress(long readBytes, long totalBytes, Boolean done);
    }

    public static class Progress implements Parcelable, Serializable {

        private int progress;
        private long currentFileSize;
        private long totalFileSize;

        @Override
        public int describeContents(){
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.progress);
            dest.writeLong(this.currentFileSize);
            dest.writeLong(this.totalFileSize);
        }

        public Progress(int progress, long currentFileSize, long totalFileSize) {
            this.progress = progress;
            this.currentFileSize = currentFileSize;
            this.totalFileSize = totalFileSize;
        }

        protected Progress(Parcel in){
            this.progress = in.readInt();
            this.currentFileSize = in.readLong();
            this.totalFileSize = in.readLong();
        }

        Creator<Progress> CREATOR = new Creator<Progress>(){

            @Override
            public Progress createFromParcel(Parcel source) {
                return new Progress(source);
            }

            @Override
            public Progress[] newArray(int size) {
                return new Progress[size];
            }
        };

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public long getCurrentFileSize() {
            return currentFileSize;
        }

        public void setCurrentFileSize(long currentFileSize) {
            this.currentFileSize = currentFileSize;
        }

        public long getTotalFileSize() {
            return totalFileSize;
        }

        public void setTotalFileSize(long totalFileSize) {
            this.totalFileSize = totalFileSize;
        }
    }

    public class DownloadAPI{
    }

}
