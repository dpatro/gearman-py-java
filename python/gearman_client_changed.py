from json import dumps, loads
import gearman

gm_client = gearman.GearmanClient(['localhost'])


def submit_job(json_to_send):
    print 'Sending job...', json_to_send
    request = gm_client.submit_job('topics', dumps(json_to_send))
    ret_obj = loads(request.result)
    print ret_obj['res']
    return


if __name__ == "__main__":

    # for i in range(1, 4):
    new_obj = {
        'db': "test",
        'articles': [
            "52b7ca4ce4b0d6faf49c67bf",
            "52b7ca53e4b0d6faf49c67c7",
            "52b7ca5ee4b0d6faf49c67d1",
            "52b7ca60e4b0d6faf49c67d3",
            "52b7ca62e4b0d6faf49c67d5",
            "52b7ca69e4b0d6faf49c67dd"
        ]
    }
    submit_job(new_obj)