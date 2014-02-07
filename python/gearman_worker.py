import gearman

gm_worker = gearman.GearmanWorker(['localhost'])


#  define method to handled 'reverse' work
def task_listener_reverse(gearman_worker, gearman_job):
    print gearman_worker
    print 'Reversing :', gearman_job.data
    to_send = gearman_job.data[::-1]
    print to_send
    return to_send


gm_worker.set_client_id('Gearman_Test')
gm_worker.register_task('reverse', task_listener_reverse)

gm_worker.work()
