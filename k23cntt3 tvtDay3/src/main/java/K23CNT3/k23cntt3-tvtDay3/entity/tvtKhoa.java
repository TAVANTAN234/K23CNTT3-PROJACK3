package K23CNT3.tvtDay03.service.service;

public class tvtKhoa {
    private String makh;
    private String tenkh;

    public tvtKhoa() {}
    public tvtKhoa(String makh, String tenkh) {
        this.makh = makh;
        this.tenkh = tenkh;
    }

    public String getMakh() { return makh; }
    public void setMakh(String makh) { this.makh = makh; }

    public String getTenkh() { return tenkh; }
    public void setTenkh(String tenkh) { this.tenkh = tenkh; }
}
