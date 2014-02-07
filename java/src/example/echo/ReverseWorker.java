package example.echo;

import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.gearman.common.GearmanJobServerConnection;
import org.gearman.common.GearmanNIOJobServerConnection;
import org.gearman.worker.AbstractGearmanFunction;
import org.gearman.worker.GearmanWorker;
import org.gearman.worker.GearmanWorkerImpl;

import java.io.IOException;

/**
 * Created by prancer on 7/2/14.
 */
public class ReverseWorker {

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
            return "greet";
        }

        @Override
        public GearmanJobResult executeFunction() {

            final byte[] hello = "hello ".getBytes();
            final byte[] name = (byte[]) this.data;

            final byte[] resp = new byte[hello.length + name.length];

            System.arraycopy(hello, 0, resp, 0, hello.length);
            System.arraycopy(name, 0, resp, hello.length, name.length);

            return new GearmanJobResultImpl(this.jobHandle, true, resp, EMPTY_BYTES, EMPTY_BYTES, 0, 0);
        }
    }
}
