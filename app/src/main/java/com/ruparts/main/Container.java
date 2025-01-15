package com.ruparts.main;

import com.ruparts.context.task.service.TaskApiClient;
import com.ruparts.context.task.service.TaskRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container {
    private static Map<String, Object> services = new HashMap<>();

    public static ApiClient getApiClient() {
        return (ApiClient) getService(ApiClient.class);
    }

    public static TaskApiClient getTaskApiClient() {
        return (TaskApiClient) getService(TaskApiClient.class, getApiClient());
    }

    public static TaskRepository getTaskRepository() {
        return (TaskRepository) getService(TaskRepository.class, getTaskApiClient());
    }

    public static Object getService(Class clazz, Object ...params) {
        try {
            if(services.containsKey(clazz.getName())) {
                return clazz.cast(services.get(clazz.getName()));
            } else {
                Object instance = clazz.getDeclaredConstructors()[0].newInstance(params);
                services.put(clazz.getName(),instance);

                return instance;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E> Class<E>[] unpack(E[] objects) {  //зачем дженерик у массива?
        List<Class<E>> list = new ArrayList<>();
        for (Object object : objects) {
            list.add((Class<E>) object.getClass());
        }

        return list.toArray(new Class[list.size()]);
    }
}
