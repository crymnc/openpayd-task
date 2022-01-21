package com.openpayd.task.service.base;

import com.openpayd.task.entity.base.BaseEntity;
import com.openpayd.task.repository.base.EntityRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Service
public class EntityService {

    protected EntityRepository entityRepository;

    public EntityService(EntityRepository entityRepository){
        this.entityRepository = entityRepository;
    }

    public <T extends BaseEntity> T save(BaseEntity baseEntity) {
        if(baseEntity != null)
            return (T)entityRepository.save(baseEntity);
        return null;
    }

    public <T extends BaseEntity> Optional<T> find(Long id, Class<T> c) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        T instance = createInstance(c);
        instance.setId(id);
        if(id != null)
            return entityRepository.findOne(Example.of(instance, matcher));
        return null;
    }

    public <T extends BaseEntity> List<T> findAll(Class<T> c) {
        T instance = createInstance(c);
        return entityRepository.findAll(Example.of(instance));
    }

    public <T extends BaseEntity> void delete(Long id, Class<T> c){
        T instance = createInstance(c);
        instance.setId(id);
        entityRepository.delete(instance);
    }

    protected  <T> T createInstance(Class<T> c){
        try {
            return c.getDeclaredConstructor(c).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
