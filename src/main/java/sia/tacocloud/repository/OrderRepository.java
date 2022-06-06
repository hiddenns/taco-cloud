package sia.tacocloud.repository;

import sia.tacocloud.Order;

public interface OrderRepository {
    Order save(Order order);
}
