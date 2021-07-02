package com.opensource.msgui.commons.utils;


import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory.Builder;
import ma.glasnost.orika.metadata.Type;

import java.util.List;

/**
 * @author whj
 *
 * 对象工具类
 */
public class ObjectMappingUtils {

    private static MapperFacade mapper;

    private ObjectMappingUtils() {
    }

    static {
        MapperFactory factory = (new Builder()).build();
        mapper = factory.getMapperFacade();
    }

    public static <S, D> D map(S source, Class<D> destClass) {
        return mapper.map(source, destClass);
    }

    public static <S, D> D map(S source, Type<S> sourceType, Type<D> destType) {
        return mapper.map(source, sourceType, destType);
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destClass) {
        return mapper.mapAsList(sourceList, ma.glasnost.orika.metadata.TypeFactory.valueOf(sourceClass), ma.glasnost.orika.metadata.TypeFactory.valueOf(destClass));
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destType) {
        return mapper.mapAsList(sourceList, sourceType, destType);
    }

    public static <S, D> D[] mapArray(D[] dest, S[] source, Class<D> destClass) {
        return mapper.mapAsArray(dest, source, destClass);
    }

    public static <S, D> D[] mapArray(D[] dest, S[] source, Type<S> sourceType, Type<D> destType) {
        return mapper.mapAsArray(dest, source, sourceType, destType);
    }

    public static <E> Type<E> getType(Class<E> rawType) {
        return ma.glasnost.orika.metadata.TypeFactory.valueOf(rawType);
    }
}
