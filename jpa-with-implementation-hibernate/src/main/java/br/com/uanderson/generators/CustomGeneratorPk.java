package br.com.uanderson.generators;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

public class CustomGeneratorPk implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        String code  =  "udc-" + UUID.randomUUID();
        return (long) code.hashCode();//cast pois retorna um int é nosso id é long
    }
}
