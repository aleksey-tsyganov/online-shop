package dao;

import connection.ConnectionManager;
import entity.Order;
import entity.Product;
import entity.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс для получения заказов
 * */
@Slf4j
public class OrderDAO {

    private ResultSet resultSet;
    ConnectionManager connectionManager = new ConnectionManager();

    /**
     * Метод добавляет данные о заказе и пользователе в БД order_data.
     * Метод вызывается из BuyServlet
     * @param order объект заказ
     * */
    public void insert(Order order) {

        try (PreparedStatement statement = connectionManager.getPreparedStatement(OrderQueries.INSERT_ORDER_DATA)){
            statement.setInt(1, order.getOrderId());
            statement.setInt(2, order.getUser().getId());
            statement.setInt(3, order.getProduct().getId());
            statement.setInt(4, order.getQuantity());
            statement.setTimestamp(5, order.getOrderTime());
            statement.executeUpdate();
        }
        catch (Exception err) {
            log.error("Ошибка при добавлении данных в БД (метод OrderDAO.insert) " + err);
        }
        finally {
            connectionManager.closeConnection();
        }
    }

    /**
     * Метод для генерации номера заказа.
     * Происходит поиск максимального order_id в БД orders_data и увеличивается на один.
     * Метод вызывается из BuyServlet
     * @return номер последнего заказа увеличенный на 1; 1 если заказов не было
     * */
    public int generateOrderId () {

        try (PreparedStatement statement = connectionManager.getPreparedStatement(OrderQueries.SELECT_MAX_ORDER_ID)){
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("MAX(order_id)") + 1;
            }
        }
        catch (Exception err){
            log.error("Ошибка при генерации номера заказа (метод OrderDAO.generateOrderId) " + err);
        }
        finally {
            connectionManager.closeConnection();
        }
        return 1;
    }

    /**
     * Метод для получения id и времени всех заказов пользователя.
     * Запрос происходит из PersonalOrdersServlet
     * Для отображения в personal-orders.jsp
     * @param id идентификатор клиента
     * @return список заказов
     * */
    public List <Order> getUserOrdersId(int id) {

        List<Order> orderIdList = new LinkedList<>();
        int userOrderId;
        Timestamp timestamp;
        Order order;

        try (PreparedStatement statement = connectionManager.getPreparedStatement(OrderQueries.SELECT_ORDER_ID_AND_TIME)){
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userOrderId = resultSet.getInt("order_id");
                timestamp = resultSet.getTimestamp("order_time");
                order = new Order(userOrderId, timestamp);
                orderIdList.add(order);
            }
        }
        catch (Exception err){
            log.error("Ошибка при получении id заказаов (метод OrderDAO.getUserOrdersId) " + err);
        }
        finally {
            connectionManager.closeConnection();
        }
        return orderIdList;
    }

    /**
     * Метод для получения детализированной информации о заказае.
     * Происходит получение всех товаров по определенному id заказа
     * @param orderId идентификатор заказа
     * @return Список заказов
     * */
    public List<Order> getUserOrderDetails(int orderId){

        List<Order> orderDetailList = new LinkedList <>();
        Product product;
        ProductDAO productDAO = new ProductDAO();
        User user;
        Order order;
        int quantity;
        Timestamp timestamp;

        try (PreparedStatement statement = connectionManager.getPreparedStatement(OrderQueries.SELECT_ORDER_DETAILS)){
            statement.setInt(1, orderId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("client_id"));
                timestamp = resultSet.getTimestamp("order_time");
                product = productDAO.getProductById(resultSet.getInt("product_id"));
                quantity = resultSet.getInt("product_quantity");
                order = new Order(orderId, timestamp, user, product, quantity);
                orderDetailList.add(order);
            }
        }
        catch (Exception err){
            log.error("Ошибка при получении всех товаров из заказа (метод OrderDAO.getUserOrderDetails) " + err);
        }
        finally {
            connectionManager.closeConnection();
        }
        return orderDetailList;
    }
}
