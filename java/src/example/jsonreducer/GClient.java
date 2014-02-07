package example.jsonreducer;

import org.gearman.client.*;
import org.gearman.common.Constants;
import org.gearman.common.GearmanJobServerConnection;
import org.gearman.common.GearmanNIOJobServerConnection;
import org.gearman.util.ByteUtils;
import org.json.simple.JSONObject;

/**
 * Created by prancer on 7/2/14.
 */
public class GClient {
    private org.gearman.client.GearmanClient client;

    public GClient(GearmanJobServerConnection conn) {
        client = new GearmanClientImpl();
        client.addJobServer(conn);
    }

    public GClient(String host, int port) {
        this(new GearmanNIOJobServerConnection(host, port));
    }

    public String reduce(String input) {
        String function = "jsonreducer";

        String uniqueId = null;
        byte[] data = ByteUtils.toUTF8Bytes(input);
        GearmanJobResult res = null;
        GearmanJob job = GearmanJobImpl.createJob(function, data, uniqueId);
        String value = "";

        client.submit(job);

        try {
            res = job.get();
            value = ByteUtils.fromUTF8Bytes(res.getResults());
        } catch (Exception e) {
            e.printStackTrace();                                                //NOPMD
        }
        return value;
    }

    public void shutdown() throws IllegalStateException {
        if (client == null) {
            throw new IllegalStateException("No client to shutdown");
        }
        client.shutdown();
    }

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 3) {
            usage();
            return;
        }
        String host = Constants.GEARMAN_DEFAULT_TCP_HOST;
        int port = Constants.GEARMAN_DEFAULT_TCP_PORT;
        String payload = args[args.length - 1];
        for (String arg : args) {
            if (arg.startsWith("-h")) {
                host = arg.substring(2);
            } else if (arg.startsWith("-p")) {
                port = Integer.parseInt(arg.substring(2));
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("x", new Integer(4));
        obj.put("y", new Integer(2));

        System.out.println("Input: "+ obj.toString());
        GClient rc = new GClient(host, port);
        System.out.println("Result: " + rc.reduce(obj.toString()));                                //NOPMD
        rc.shutdown();
    }

    public static void usage() {
        String[] usage = {
                "usage: org.gearman.example.GClient [-h<host>] [-p<port>] " +
                        "<string>",
                "\t-h<host> - job server host",
                "\t-p<port> - job server port",
                "\n\tExample: java org.gearman.example.GClient Foo",
                "\tExample: java org.gearman.example.GClient -h127.0.0.1 " +
                        "-p4730 Bar", //
        };

        for (String line : usage) {
            System.err.println(line);                                           //NOPMD
        }
    }
}
