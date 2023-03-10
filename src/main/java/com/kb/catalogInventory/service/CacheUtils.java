package com.kb.catalogInventory.service;

import com.kb.java.utils.AerospikeCacheClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CacheUtils {


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

    public Map<String,String> multiGet(String[] key) {
        try {
            Map<String, Object> response = aerospikeCacheClient.multiget(key);
            if (response == null) {
                return new HashMap();
            }
            Map<String, String> finalResponse = new HashMap<>();
            for (String singleKey : response.keySet()) {
                try {
                    if (response.get(singleKey) != null && !((String) response.get(singleKey)).isEmpty()) {
                        finalResponse.put(singleKey, response.get(singleKey).toString());
                    }
                } catch (Exception e) {
                    logger.error("Exception came while looking up into the singleKey for key : " + singleKey, e);
                }
            }
            return finalResponse;
        } catch (Exception e) {
            logger.error("Exception came while looking up into the cache for key : " + key, e);
            return new HashMap();
        }
    }

    public Object getAsObject(String key) {
        try {
            Object response = aerospikeCacheClient.get(key);
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
