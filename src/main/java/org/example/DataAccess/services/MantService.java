package org.example.DataAccess.services;

import org.example.Domain.models.Car;
import org.example.Domain.models.Mantenimiento;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class MantService {

    private final SessionFactory sessionFactory;

    public MantService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Mantenimiento createMantenimiento(Date fecha, String descripcion, String tipo, Long carId) {
        try (Session session = sessionFactory.openSession()) {
            var car = session.find(Car.class, carId);
            Transaction tx = session.beginTransaction();

            Mantenimiento mantenimiento = new Mantenimiento();
            mantenimiento.setFecha(fecha);
            mantenimiento.setDescripcion(descripcion);
            mantenimiento.setTipo(tipo);
            mantenimiento.setCar(car);

            session.persist(mantenimiento);
            tx.commit();
            return mantenimiento;
        }
    }

    // -------------------------
    // READ
    // -------------------------
    public Mantenimiento getMantenimientoById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Mantenimiento.class, id);
        } catch(Exception e){
            String message = String.format("An error occurred when processing: %s. Details: %s", "getMantById", e);
            System.out.println(message);
            throw e;
        }
    }

    public List<Mantenimiento> getAllMantenimientos() {
        try (Session session = sessionFactory.openSession()) {
            List<Mantenimiento> mantenimientos = session.createQuery("FROM Mantenimiento", Mantenimiento.class).list();
            mantenimientos.forEach(mant -> {
                Hibernate.initialize(mant.getCar());

                if (mant.getCar().getOwner() != null) {
                    Hibernate.initialize(mant.getCar().getOwner());
                }
            });
            return mantenimientos;
        } catch (Exception e){
            String message = String.format("An error occurred when processing getAllMantenimientos: %s. Details: %s", "getAllCars", e);
            System.out.println(message);
            throw e;
        }
    }

    public List<Mantenimiento> getMantenimientosByCar(Car car) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Mantenimiento WHERE car = :car", Mantenimiento.class)
                    .setParameter("car", car)
                    .list();
        }
    }

    // -------------------------handleListMant
    // -------------------------
    public Mantenimiento updateMantenimiento(Long id, Date fecha,String descripcion, String tipo) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Mantenimiento mant = session.find(Mantenimiento.class, id);
            if (mant != null) {
                mant.setFecha(fecha);
                mant.setDescripcion(descripcion);
                mant.setTipo(tipo);
                session.merge(mant);

                if (mant.getCar() != null) {
                    Hibernate.initialize(mant.getCar());
                }

                if (mant.getCar().getOwner() != null) {
                    Hibernate.initialize(mant.getCar().getOwner());
                }
            }
            tx.commit();
            return mant;
        } catch (Exception e){
            String message = String.format("An error occurred when processing: %s. Details: %s", "updateCar", e);
            System.out.println(message);
            throw e;
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    public boolean deleteCar(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Mantenimiento mant = session.find(Mantenimiento.class, id);
            if (mant != null) {
                session.remove(mant);
                tx.commit();
                return true;
            }

            tx.rollback();
            return false;
        }
    }
}

