import gearman

gm_client = gearman.GearmanClient(['localhost'])


def submit_job(string_to_reverse):
    print 'Sending job...'
    request = gm_client.submit_job('greet', string_to_reverse)
    print "Result: " + str(request.result)

if __name__ == "__main__":

    while 1:
        user_input = raw_input("Enter string to reverse: ")
        if len(user_input):
            submit_job(user_input)
        else:
            break

    print "Bye Bye"