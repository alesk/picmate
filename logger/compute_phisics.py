#!/usr/bin/python

import string
import sys
import itertools
import operator

def compute_phisics():
    pass

def read_data(lines):
    measurements=[ map(string.strip, l.split(', ')) for l in lines]
    slices = map(lambda x:x[0],
        filter(lambda x:len(x[1])  == 1, enumerate(measurements)))
    ranges = zip(slices, slices[1:])
    data = [filter(lambda x: len(x) > 1, measurements[f:t])
            for f,t in ranges]
    return data
        

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


def compute_delta_distances(m):
    """
    @param == serie of measurements
    returns [(dx0, dy0,dz0) .. (dxn, dyn, dzn)]
    """
    ret = []
    for m1, m2 in zip(m, m[1:]):
        s1x, s1y, s1z, t1 = map(float, m1)
        s2x, s2y, s2z, t2 = map(float, m2)
        dt = (t2 - t1) / 1E9
        #print dt
        ret.append(compute_phisics(s2x, s2y, s2z, dt))
    return ret

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
                   alpha * p1[2] - (1-alpha)*last[2],
                   p1[3]]]

    return reduce(filter, data[1:], [data[0]])

def compute_movement(accelerometer):
    gravity = low_pass_filter(accelerometer, ALPHA)
    def sub(a,g):
        return map(lambda x,y: x-y, a, g)
    linear_acceleration = map(sub, accelerometer, gravity)
    print linear_acceleration
    print gravity

def main(file_name):
    fl = open(file_name, 'r')
    measurements = read_data(fl.readlines())
    fl.close()

    for accelerometer in measurements:
        compute_movement(map(lambda a:map(float, a), accelerometer))
    dst = [ compute_delta_distances(m) for m in measurements]
    flat = reduce(lambda x,y:x+y, dst)
    summed = reduce(
            lambda a, b: a+[(a[-1][0]+b[0], a[-1][1]+b[1], a[-1][2]+b[2])],
            flat, [(0,0,0), flat[0]])
    
    fl = open('data.txt', 'w')
    for f in summed:
        fl.write("%f,%f,%f\n" %f)
    fl.close()
            

if __name__ == "__main__":
    main(sys.argv[1])    

