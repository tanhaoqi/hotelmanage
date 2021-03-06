package com.hotel.service;

import com.hotel.exception.ExceptionType;
import com.hotel.exception.HotelException;
import com.hotel.model.Garage;
import com.hotel.repository.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GarageService
{
	@Autowired
	GarageRepository garageRepository;

	public List<Garage> findAll()
	{
		return garageRepository.findAll();
	}

	public Garage findById(int garageNo) throws HotelException
	{
		Garage garage=garageRepository.findById(garageNo).orElse(null);
		if(garage==null)
		{
			throw new HotelException(ExceptionType.GARAGE_FIND_BY_ID_ERROR.getCode(),
					ExceptionType.GARAGE_FIND_BY_ID_ERROR.getMsg());
		}
		else
		{
			return garage;
		}
	}

	public Garage findByBrand(String brand) throws HotelException
	{
		for (Garage garage : garageRepository.findAll())
		{
			if(garage.getBrand()==null)
			{
				continue;
			}
			if(garage.getBrand().equals(brand))
			{
				return garage;
			}
		}
		throw new HotelException(ExceptionType.GARAGE_FIND_BY_BRAND_ERROR.getCode(),
				ExceptionType.GARAGE_FIND_BY_BRAND_ERROR.getMsg());
	}

	public Garage save(Garage garage)
	{
		return garageRepository.save(garage);
	}

	public void delete(Garage garage)
	{
		garageRepository.delete(garage);
	}
}
