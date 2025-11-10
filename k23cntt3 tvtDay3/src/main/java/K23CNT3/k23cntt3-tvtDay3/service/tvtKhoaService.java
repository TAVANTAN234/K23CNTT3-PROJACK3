package K23CNT3.tvtDay03.service.service;

import K23CNT3.tvtDay3.service.tvtKhoaService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class tvtKhoaService {
    private List<tvtKhoa> khoaList = new ArrayList<>();

    public tvtKhoaService() {
        khoaList.add(new tvtKhoa("KH01", "Công nghệ thông tin"));
        khoaList.add(new tvtKhoa("KH02", "Kinh tế"));
        khoaList.add(new tvtKhoa("KH03", "Điện tử viễn thông"));
        khoaList.add(new tvtKhoa("KH04", "Xây dựng"));
        khoaList.add(new tvtKhoa("KH05", "Quản trị kinh doanh"));
    }

    public List<tvtKhoa> getAll() {
        return khoaList;
    }

    public tvtKhoa getByMakh(String makh) {
        return khoaList.stream()
                .filter(k -> k.getMakh().equals(makh))
                .findFirst()
                .orElse(null);
    }

    public tvtKhoa add(tvtKhoa k) {
        khoaList.add(k);
        return k;
    }

    public tvtKhoa update(String makh, tvtKhoa k) {
        tvtKhoa existing = getByMakh(makh);
        if (existing != null) {
            existing.setTenkh(k.getTenkh());
            return existing;
        }
        return null;
    }

    public boolean delete(String makh) {
        return khoaList.removeIf(k -> k.getMakh().equals(makh));
    }
}
