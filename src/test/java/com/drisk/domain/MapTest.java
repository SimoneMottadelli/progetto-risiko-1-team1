package com.drisk.domain;

import org.junit.Test;

public class MapTest {

	@Test
	public void mapTest() {
		Map map = Map.getInstance();
		map.createMap("hard");
	}
	
}
