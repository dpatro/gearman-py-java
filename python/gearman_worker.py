from json import loads, dumps
from time import sleep
import gearman

gm_worker = gearman.GearmanWorker(['localhost'])


def task_listener_reverse(gearman_worker, gearman_job):
    # print "Calculating x^y", gearman_job.data
    # in_obj = loads(gearman_job.data)
    # x = in_obj['x']
    # y = in_obj['y']
    print gearman_job.data
    sleep(15)
    return "{\"res\": \"Done\"}"

gm_worker.set_client_id('Gearman_Test')
gm_worker.register_task('tagger', task_listener_reverse)

gm_worker.work()
