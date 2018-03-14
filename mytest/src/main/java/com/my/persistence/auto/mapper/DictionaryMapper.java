package com.my.persistence.auto.mapper;

import com.my.persistence.auto.model.Dictionary;
import com.my.persistence.auto.model.DictionaryQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface DictionaryMapper {
    long countByExample(DictionaryQuery example);

    int deleteByExample(DictionaryQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Dictionary record);

    int insertSelective(Dictionary record);

    List<Dictionary> selectByExampleWithRowbounds(DictionaryQuery example, RowBounds rowBounds);

    List<Dictionary> selectByExample(DictionaryQuery example);

    Dictionary selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Dictionary record, @Param("example") DictionaryQuery example);

    int updateByExample(@Param("record") Dictionary record, @Param("example") DictionaryQuery example);

    int updateByPrimaryKeySelective(Dictionary record);

    int updateByPrimaryKey(Dictionary record);
}