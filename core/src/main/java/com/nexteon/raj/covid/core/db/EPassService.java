package com.nexteon.raj.covid.core.db;

import com.nexteon.raj.covid.core.entity.EPass;

public interface EPassService {

	public boolean saveEPassData(EPass epass, String table);
	public EPass getEPassData(long id, String table);
	
}
