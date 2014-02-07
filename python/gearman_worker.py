from json import loads, dumps
import gearman

gm_worker = gearman.GearmanWorker(['localhost'])


def task_listener_reverse(gearman_worker, gearman_job):
    print "Calculating x^y", gearman_job.data
    in_obj = loads(gearman_job.data)
    x = in_obj['x']
    y = in_obj['y']

    return dumps({'res': x**y})

gm_worker.set_client_id('Gearman_Test')
gm_worker.register_task('jsonreducer', task_listener_reverse)

gm_worker.work()
