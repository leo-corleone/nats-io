package nats.lite.util;

import io.nats.client.Connection;

import java.net.URI;
import java.util.Collection;

/**
 * @time: 2022/10/31
 * @author: .sun
 */
public class NatsServerUtil {

    public static Object getServer(Connection conn){
        Collection<URI> servers = conn.getOptions().getServers();
        return servers.toArray()[0];
    }

}
