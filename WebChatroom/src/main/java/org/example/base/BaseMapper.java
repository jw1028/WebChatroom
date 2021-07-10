package org.example.base;

import java.util.List;

public interface BaseMapper<T> {

    T selectByPrimaryKey(Integer id);

    T selectOne(T record);

    List<T> selectAll();

    List<T> selectByCondition(T record);

    int insert(T record);

    int insertSelective(T record);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    int deleteByPrimaryKey(Integer id);

    int deleteByIds(List<Integer> ids);
}