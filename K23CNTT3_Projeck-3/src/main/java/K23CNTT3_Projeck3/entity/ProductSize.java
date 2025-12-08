package K23CNTT3_Projeck3.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_size")
@Data
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer size;

    @Column(nullable = false)
    private Integer stock;

    public ProductSize() {}

    public ProductSize(Product product, Integer size, Integer stock) {
        this.product = product;
        this.size = size;
        this.stock = stock;
    }
}