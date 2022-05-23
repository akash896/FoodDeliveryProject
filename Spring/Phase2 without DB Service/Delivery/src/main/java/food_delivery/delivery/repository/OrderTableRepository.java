package food_delivery.delivery.repository;

import food_delivery.delivery.model.OrderTable;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface OrderTableRepository extends Repository<OrderTable, Integer> {
    void save(OrderTable orderTable);

    OrderTable findById(Integer agentId);

    List<OrderTable> findAll();

    public void deleteAll();

}
