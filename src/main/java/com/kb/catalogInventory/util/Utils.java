package com.kb.catalogInventory.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class Utils {
	
	public static double roundSetPrecison(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return Double.valueOf(String.valueOf(bd));
	}

	public static Double calculateRetailMargin(Double productMrp, Double finalPricePerUnit, Integer setPeicesCount) {
		log.info("****************  productMrp:{}  **********************************", productMrp,"****************  finalPrice:{}  **********************************", finalPricePerUnit);
		double retailMargin = 0;
		setPeicesCount =  setPeicesCount == null ? 0: setPeicesCount;

		if(setPeicesCount > 1) {
			retailMargin = (100 - (((finalPricePerUnit) / (productMrp / setPeicesCount)) * 100))	;
		} else {
			retailMargin = (100 - ((finalPricePerUnit / productMrp) * 100));
		}
		//retailMargin = ((productMrp - finalPrice) / productMrp) * 100;
		log.info("****************  retailMargin:{}  **********************************", retailMargin);
		double roundOffRetailMargin = roundSetPrecison(retailMargin, 2);
		log.info("****************  roundOffRetailMargin:{}  **********************************", roundOffRetailMargin);
		return roundOffRetailMargin;
	}

}
