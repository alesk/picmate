#!/usr/bin/python
# vim: set fileencoding=utf-8 :



import string
import sys
import itertools
import operator
from math import sqrt
from util import read_separated_data, read_data


        

# gravity
g_x, g_y, g_z = (0.0, 0.0, 0.0)
def compute_phisics(s_x, s_y, s_z, dt):
    """
    """
    # gravity
    global g_x, g_y, g_z
    g_x = ALPHA * g_x + (1 -ALPHA) * s_x
    g_y = ALPHA * g_y + (1 -ALPHA) * s_y
    g_z = ALPHA * g_z + (1 -ALPHA) * s_z

    # linear acceleration
    la_x = s_x - g_x
    la_y = s_y - g_y
    la_z = s_z - g_z

    # velocity
    v_x = la_x * dt;
    v_y = la_y * dt;
    v_z = la_z * dt;

    # diff distance
    d_x = v_x * dt + dt*dt/2
    d_y = v_y * dt + dt*dt/2
    d_z = v_z * dt + dt*dt/2

    return d_x, d_y, d_z



ALPHA=0.8
def low_pass_filter(data, alpha):
    """
    data must be in a form of (x, y, z)
    y[0] = x[0]
    y[i] = alpha * x[i] + (1-alpha) * y[i-i]
    """
    def filter(p0, p1):
        last = p0[-1]
        return p0+[[alpha * p1[0] - (1-alpha)*last[0], 
                   alpha * p1[1] - (1-alpha)*last[1], 
                   alpha * p1[2] - (1-alpha)*last[2]]]

    return reduce(filter, data[1:], [data[0]])


def v_abs(a):
    """
    absolute value of a vecor
    """
    return sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2])

def dump_to_file(file_name, data):
    fl = open(file_name, 'w')
    for d in data:
        fl.write(",".join(map(str, d)))
        fl.write('\n')
    fl.close()

def dump_to_file_simple(file_name, data):
    fl = open(file_name, 'w')
    for d in data:
        fl.write("%s\n" %d)
    fl.close()

def compute_movement(accelerometer):
    t0 = accelerometer[0][3]

    ### acc_x, acc_y,acc_z,t; t is t from begining of measurement
    acc_with_t = map(lambda a:(a[0], a[1], a[2], (a[3]-t0)/1E9), accelerometer)

    # frekvenca vzorcenja
    dt = reduce(lambda x,y: y[3]-x, acc_with_t, 0)/len(acc_with_t)

    # remove t component from acc
    acc = map(lambda a: (a[0], a[1], a[2]), acc_with_t)
    dump_to_file('acc.txt', acc)

    gravity = low_pass_filter(acc, ALPHA)
    #gravity = itertools.repeat([0.313268, 0.040861, 10.038197], len(acc))
    dump_to_file('gravity.txt', acc)

    def sub(a,g):
        return map(lambda x,y: x-y, a, g)

    def cc(l,c):
        return l + [v_add(l[-1], c)]
    
    def v_sub(a,b):
        return (a[0]-b[0], a[1]-b[1], a[2]-b[2])
    
    def v_add(a,b):
        x = (b[0]+a[0], b[1]+a[1], b[2]+a[2])
        return x

    def a_v_to_d(v, a):
        x = (
            v[0]*dt + a[0] * dt*dt / 2,
            v[1]*dt + a[1] * dt*dt / 2,
            v[2]*dt + a[2] * dt*dt / 2)
        return x

    linacc = map(sub, acc, gravity)
    dump_to_file('linacc.txt', linacc)

    delta_velocity = map(lambda a:(a[0]*dt, a[1]*dt, a[2]*dt), linacc)
    dump_to_file('delta_velocity.txt', delta_velocity)
    velocity = reduce(cc, delta_velocity, [(0,0,0)])[1:]
    dump_to_file('velocity.txt', velocity)

                
    delta_distance = list(map(a_v_to_d, velocity, linacc))    

    #delta_distance = [compute_phisics(x,y,z,dt) for x,y,z in acc]
    dump_to_file('delta_distance.txt', delta_distance)

    distance = reduce(cc, delta_distance, [(0,0,0)])
    dump_to_file('distance.txt', distance)

    dump_to_file_simple('distance_abs.txt', map(v_abs, distance))


def main(file_name):
    fl = open(file_name, 'r')
    #measurements = read_separated_data(fl.readlines())
    measurements = read_data(fl.readlines())
    fl.close()

    for accelerometer in measurements:
        compute_movement(map(lambda a:map(float, a), accelerometer))

if __name__ == "__main__":
    main(sys.argv[1])    

