package org.bluesoft.app.ws.io.dao.impl;

import org.bluesoft.app.ws.io.dao.DAO;
import org.bluesoft.app.ws.io.entity.UserEntity;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MySQLDAO implements DAO {

    Session session;

    @Override
    public void openConnection() {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        session = factory.openSession();
    }

    @Override
    public UserDTO getUserByUserName(String userName) {

        UserDTO userDTO = null;

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);

        Root<UserEntity> profileRoot = criteria.from(UserEntity.class);

        criteria.select(profileRoot);
        criteria.where(cb.equal(profileRoot.get("email"), userName));

        Query<UserEntity> query = session.createQuery(criteria);
        List<UserEntity> resultList = query.getResultList();

        if(resultList != null && resultList.size() > 0){
            UserEntity userEntity = resultList.get(0);
            userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
        }

        return userDTO;
    }

    @Override
    public UserDTO saveUser(UserDTO user) {

        UserDTO returnValue = null;
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        session.beginTransaction();
        session.save(userEntity);
        session.getTransaction().commit();

        returnValue = new UserDTO();

        BeanUtils.copyProperties(userEntity,returnValue);

        return returnValue;
    }

    @Override
    public UserDTO getUser(String id) {

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);

        Root<UserEntity> profileRoot = criteria.from(UserEntity.class);
        criteria.select(profileRoot);
        criteria.where(cb.equal(profileRoot.get("userId"), id));

        UserEntity userEntity = session.createQuery(criteria).getSingleResult();

        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(userEntity, userDTO);

        return userDTO;
    }

    @Override
    public List<UserDTO> getUsers(int start, int limit) {

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);

        Root<UserEntity> userRoot = criteria.from(UserEntity.class);

        criteria.select(userRoot);

        List<UserEntity> searchResult = session.createQuery(criteria)
                .setFirstResult(start)
                .setMaxResults(limit)
                .getResultList();

        List<UserDTO> returnValue = new ArrayList<>();

        for (UserEntity userEntity : searchResult) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
            returnValue.add(userDTO);
        }

        return returnValue;

    }

    @Override
    public void updateUser(UserDTO userProfile) {
          UserEntity userEntity = new UserEntity();
          BeanUtils.copyProperties(userProfile, userEntity);

          session.beginTransaction();
          session.update(userEntity);
          session.getTransaction().commit();
    }

    @Override
    public void deleteUser(UserDTO userProfile) {

    }

    @Override
    public UserDTO getUserByEmailToken(String token) {
        return null;
    }

    @Override
    public void closeConnection() {
       if(session != null){
           session.close();
       }
    }
}
