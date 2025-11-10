package K23CNT3.tvtDay03.service.entity;

public class tvtMonHoc {
    private String mamh;
    private String tenmh;
    private int sotiet;

    public tvtMonHoc() {}
    public tvtMonHoc(String mamh, String tenmh, int sotiet) {
        this.mamh = mamh;
        this.tenmh = tenmh;
        this.sotiet = sotiet;
    }

    public String getMamh() { return mamh; }
    public void setMamh(String mamh) { this.mamh = mamh; }

    public String getTenmh() { return tenmh; }
    public void setTenmh(String tenmh) { this.tenmh = tenmh; }

    public int getSotiet() { return sotiet; }
    public void setSotiet(int sotiet) { this.sotiet = sotiet; }
}
