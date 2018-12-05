package com.imust.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.imust.entity.Park;
@Mapper
public interface ParkMapper {
	

	@Select("select * from park")
	List<Park> findAllPark();
	
	@Select("select * from park where  status = #{status}")
	List<Park> findAllParkByKey(@Param("status") int status);
	
	@Select("select * from park where  name like #{key}")
	List<Park> findParkByKey(@Param("key") String key);
	
	@Select("select * from park where id=#{id}")
	Park findCarById(@Param("id") int id);
	
	//添加信息
	@Insert("insert into park(name,price,status) values(#{name},#{price},0)")
    public void insertCar(Park car);
	
	//删除信息
	@Delete("delete from car where id=#{id}")
	public void deleteCar(int id);
	
	@Update("update park set status=#{status} where id=#{id}")
	public void updateCarStatus(Park car);
	
	//修改信息
	@Update("update park set name=#{name},price=#{price} where id=#{id}")
	public void updateCar(Park car);
}
