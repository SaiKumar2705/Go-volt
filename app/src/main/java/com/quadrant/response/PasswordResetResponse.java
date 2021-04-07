package com.quadrant.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordResetResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("error")
        @Expose
        private Object error;
        @SerializedName("data")
        @Expose
        private Data data;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Object getError() {
            return error;
        }

        public void setError(Object error) {
            this.error = error;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    public class Data {

        @SerializedName("updated")
        @Expose
        private Boolean updated;

        public Boolean getUpdated() {
            return updated;
        }

        public void setUpdated(Boolean updated) {
            this.updated = updated;
        }

    }

}
