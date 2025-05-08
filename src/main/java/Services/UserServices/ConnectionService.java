package Services.UserServices;

import Domain.Connection;
import Domain.Status;
import Domain.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class ConnectionService {

    @PersistenceContext
    private EntityManager em;

    public void sendFriendRequest(Long senderId, Long receiverId) {
        Connection connection = new Connection();
        connection.setSender(em.find(User.class, senderId));
        connection.setReceiver(em.find(User.class, receiverId));
        connection.setStatus(Status.valueOf("PENDING"));
        em.persist(connection);
    }

    public void respondToRequest(Long requestId, boolean accept) {
        Connection connection = em.find(Connection.class, requestId);
        if (connection != null && connection.getStatus().equals("PENDING")) {
            connection.setStatus(Status.valueOf(accept ? "ACCEPTED" : "REJECTED"));
            em.merge(connection);
        }
    }

    public List<Connection> getPendingRequests(Long userId) {
        return em.createQuery("SELECT c FROM Connection c WHERE c.receiver.id = :uid AND c.status = 'PENDING'", Connection.class)
                .setParameter("uid", userId)
                .getResultList();
    }

    public List<User> getFriends(Long userId) {
        return em.createQuery("""
            SELECT CASE
                     WHEN c.sender.id = :uid THEN c.receiver
                     ELSE c.sender
                   END
            FROM Connection c
            WHERE (c.sender.id = :uid OR c.receiver.id = :uid) AND c.status = 'ACCEPTED'
            """, User.class)
                .setParameter("uid", userId)
                .getResultList();
    }
}

