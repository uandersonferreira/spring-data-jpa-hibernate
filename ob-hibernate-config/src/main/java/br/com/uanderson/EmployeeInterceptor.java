package br.com.uanderson;

import br.com.uanderson.entities.Employee;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

public class EmployeeInterceptor implements Interceptor {//EmptyInterceptor está deprecated hibernate 6+

    @Override
    public boolean onPersist(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {

        if (entity instanceof Employee){
            System.out.println("Salvando um empregado!!");
            ((Employee) entity).setLastName("Editado dentro do Interceptor");

            for (int i = 0; i < propertyNames.length; i++) {
                System.out.println(propertyNames[i]);
            }
        }

        return Interceptor.super.onPersist(entity, id, state, propertyNames, types);
        /*
            Esse intercptor é global, ou seja é executado para toda operação de persistência, independente da class onde é
            executada. Para captar operações de uma entidade especifica deve fazer a verificação e trabalhar dentro da condição
            de verificação.
         */
    }

}//class
