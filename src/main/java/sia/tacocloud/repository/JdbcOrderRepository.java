package sia.tacocloud.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import sia.tacocloud.Order;
import sia.tacocloud.Taco;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private SimpleJdbcInsert orderInserter;
    private SimpleJdbcInsert orderTacoInserter;
    private ObjectMapper objectMapper;

    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbc) {
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");

        this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order_Tacos");

        this.objectMapper = new ObjectMapper();
    }
// end::core[]


    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();
        for (Taco taco : tacos) {
            saveTacoToOrder(taco, orderId);
        }

        return order;
    }

    private long saveOrderDetails(Order order) {
        @SuppressWarnings("unchecked")
        Map<String, Object> values =
                objectMapper.convertValue(order, Map.class);

//        MapSqlParameterSource values = new MapSqlParameterSource()
//                .addValue("deliverName", order.getName())
//                .addValue("deliverStreet", order.getStreet())
//                .addValue("deliverCity", order.getCity())
//                .addValue("deliverState", order.getState())
//                .addValue("deliverZip", order.getZip())
//                .addValue("ccNumber", order.getCcNumber())
//                .addValue("ccExpiration", order.getCcExpiration())
//                .addValue("ccCVV", order.getCcCVV())
//                .addValue("placedAt", order.getPlacedAt());


        values.put("placedAt", order.getPlacedAt());

        long orderId =
                orderInserter
                        .executeAndReturnKey(values)
                        .longValue();
        return orderId;
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        orderTacoInserter.execute(values);
    }

}