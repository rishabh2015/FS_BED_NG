package com.kb.catalogInventory.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kb.java.utils.AerospikeCacheClientImpl;

@Component
public class LocalCache {

    @Autowired
    private AerospikeCacheClientImpl aerospikeCacheClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void put(String key, String value) {
        try {
            aerospikeCacheClient.setWithoutttl(key, value);
        } catch (Exception e) {
            logger.error("Exception came while setting data on aerospike for key " + key, e);
        }
    }
    
    public void put(String key, Object value) {
        try {
            aerospikeCacheClient.setWithoutttl(key, value);
        } catch (Exception e) {
            logger.error("Exception came while setting data on aerospike for key " + key, e);
        }
    }

    public String get(String key) {
        try {
            String response = (String) aerospikeCacheClient.get(key);
            if (response == null) {
                return "";
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception came while looking up into the cache for key : " + key, e);
            return "";
        }
    }
    
    public Object getAsObject(String key) {
        try {
            Object response =  aerospikeCacheClient.get(key);
            if (response == null) {
                return null;
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception came while looking up into the cache for key : " + key, e);
            return null;
        }
    }

    public void remove(String key) {
      try {
        aerospikeCacheClient.setWithoutttl(key, null);
      } catch (Exception e) {
        logger.error("Exception came while setting null data on aerospike for key " + key, e);
      }
    }


}
