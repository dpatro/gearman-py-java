package example.jsonreducer;

import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.gearman.common.GearmanJobServerConnection;
import org.gearman.common.GearmanNIOJobServerConnection;
import org.gearman.worker.AbstractGearmanFunction;
import org.gearman.worker.GearmanWorker;
import org.gearman.worker.GearmanWorkerImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by prancer on 7/2/14.
 */
public class GWorker {

    public static void main(String[] args) {
        final GearmanJobServerConnection connection = new
                GearmanNIOJobServerConnection("localhost", 4730);

        GearmanWorker worker = new GearmanWorkerImpl();
        worker.addServer(connection);

        worker.registerFunction(SampleFunction.class);

        worker.work();
    }

    private static final byte[] EMPTY_BYTES = new byte[]{};

    public static class SampleFunction extends AbstractGearmanFunction {

        @Override
        public String getName() {
            return "jsonreducer";
        }

        @Override
        public GearmanJobResult executeFunction() {

            JSONParser parser = new JSONParser();
            Object obj;
            double cal_res = 1.0;
            try {

                obj = parser.parse( new String((byte[]) this.data));
                JSONObject jsonObject = (JSONObject) obj;

                Long x = (Long) jsonObject.get("x");
                System.out.println(x);

                Long y = (Long) jsonObject.get("y");
                System.out.println(y);


                for (int i=0; i<y; i++) cal_res *= x;

            } catch (ParseException e) {
                e.printStackTrace();
            }

            JSONObject res_obj = new JSONObject();
            res_obj.put("res", new Long((long) cal_res));

            final byte[] resp_bytes = res_obj.toString().getBytes();

            final byte[] resp = new byte[resp_bytes.length];

            System.arraycopy(resp_bytes, 0, resp, 0, resp_bytes.length);
            return new GearmanJobResultImpl(this.jobHandle, true, resp, EMPTY_BYTES, EMPTY_BYTES, 0, 0);
        }
    }
}
