from json import dumps, loads
import gearman

gm_client = gearman.GearmanClient(['localhost'])


def submit_job(json_to_send):
    print 'Sending job...', json_to_send
    request = gm_client.submit_job('jsonreducer', dumps(json_to_send))
    ret_obj = loads(request.result)
    print ret_obj['res']
    return


if __name__ == "__main__":

    for i in range(1, 4):
        new_obj = {
            'x': i,
            'y': i-1
        }
        submit_job(new_obj)