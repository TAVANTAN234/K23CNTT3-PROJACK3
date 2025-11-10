package tvtDay03.controller;

import K23CNT3.tvtDay03.service.entity.tvtKhoa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/khoa")
public class tvtKhoaController {
    private final K23CNT3.nhtDay03.service.tvtKhoaService khoaService;

    public tvtKhoaController(K23CNT3.nhtDay03.service.tvtKhoaService khoaService) {
        this.khoaService = khoaService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<tvtKhoa>> getAll() {
        return ResponseEntity.ok(khoaService.getAll());
    }

    @GetMapping("/{makh}")
    public ResponseEntity<tvtKhoa> getByMakh(@PathVariable String makh) {
        tvtKhoa k = khoaService.getByMakh(makh);
        return k != null ? ResponseEntity.ok(k) : ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<tvtKhoa> add(@RequestBody tvtKhoa k) {
        return ResponseEntity.ok(khoaService.add(k));
    }

    @PutMapping("/update/{makh}")
    public ResponseEntity<tvtKhoa> update(@PathVariable String makh, @RequestBody tvtKhoa k) {
        tvtKhoa updated = khoaService.update(makh, k);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{makh}")
    public ResponseEntity<Void> delete(@PathVariable String makh) {
        boolean deleted = khoaService.delete(makh);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
