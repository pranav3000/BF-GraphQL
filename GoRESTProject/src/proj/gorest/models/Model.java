package proj.gorest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("data")
    @Expose
    private Data data;

    @Override
    public String toString() {
        return "Model [data=" + data + "]";
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
