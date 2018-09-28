package jota.config;

import java.io.Serializable;
import java.util.List;

import jota.connection.Connection;
import jota.store.Store;

public abstract class IotaClientConfig implements IotaConfig {
    
    
    protected Store store;

    public IotaClientConfig(Store store) {
        this.store = store;
    }

    @Override
    public boolean canWrite() {
        return store.canWrite();
    }

    @Override
    public List<Connection> getNodes() {
        return null;
    }
    
    @Override
    public boolean hasNodes() {
        List<Connection> nodes = getNodes();
        return nodes != null && nodes.size() > 0;
    }

    protected String stringOrNull(String key) {
        Serializable ret = store.get(key);
        return ret != null ? ret .toString() : null;
    }
}
