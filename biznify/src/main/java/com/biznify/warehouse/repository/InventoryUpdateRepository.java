package com.biznify.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.biznify.warehouse.dto.CurrentInventoryDTO;
import com.biznify.warehouse.dto.ProductLocationDTO;
import com.biznify.warehouse.entity.InventoryUpdate;

@Repository
public interface InventoryUpdateRepository extends JpaRepository<InventoryUpdate, Long> {
	List<InventoryUpdate> findByProductId(Long productId);

	List<InventoryUpdate> findByProductIdAndOperationType(Long productId, String operationType);

	@Query("SELECT new com.biznify.warehouse.dto.ProductLocationDTO(" + "p.name, w.name, r.rackCode, b.binCode, "
			+ "SUM(CASE WHEN iu.operationType = 'IN' THEN iu.quantity ELSE -iu.quantity END) * 1.0) "
			+ "FROM InventoryUpdate iu " + "JOIN iu.product p " + "JOIN iu.warehouse w " + "JOIN iu.rack r "
			+ "JOIN iu.bin b " + "WHERE p.id = :productId " + "GROUP BY p.name, w.name, r.rackCode, b.binCode "
			+ "ORDER BY MAX(iu.timestamp) DESC")
	List<ProductLocationDTO> findProductLocationWithNetQuantity(@Param("productId") Long productId);

	@Query("""
			    SELECT new com.biznify.warehouse.dto.CurrentInventoryDTO(
			        iu.product.id,
			        iu.bin.id,
			        iu.rack.id,
			        iu.warehouse.id,
			        SUM(CASE WHEN iu.operationType = 'IN' THEN iu.quantity ELSE -iu.quantity END)
			    )
			    FROM InventoryUpdate iu
			    GROUP BY iu.product.id, iu.bin.id, iu.rack.id, iu.warehouse.id
			    HAVING SUM(CASE WHEN iu.operationType = 'IN' THEN iu.quantity ELSE -iu.quantity END) > 0
			""")
	List<CurrentInventoryDTO> findCurrentInventory();

	@Query("SELECT SUM(i.quantity) FROM InventoryUpdate i WHERE i.product.id = :productId AND i.operationType = 'IN'")
	Double findTotalQuantityInByProductId(@Param("productId") Long productId);

}
